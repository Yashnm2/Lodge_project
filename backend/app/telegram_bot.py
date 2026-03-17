"""Background Telegram bot poller.

Listens for /start <image_id> deep-link commands and automatically
sends the corresponding generated image back to the user.
"""

from __future__ import annotations

import threading
import time
from pathlib import Path

import requests


class TelegramBotPoller:
    def __init__(self, token: str, output_dir: Path) -> None:
        self.token = token
        self.base_url = f"https://api.telegram.org/bot{token}"
        self.output_dir = output_dir
        self.bot_username: str | None = None
        self._offset = 0
        self._running = False
        self._thread: threading.Thread | None = None

    def start(self) -> None:
        """Start the bot in a background thread (non-blocking)."""
        self._running = True
        self._thread = threading.Thread(target=self._run, daemon=True)
        self._thread.start()

    def _run(self) -> None:
        try:
            resp = requests.get(f"{self.base_url}/getMe", timeout=10)
            data = resp.json()
            if not data.get("ok"):
                print(f"[TelegramBot] Failed to connect: {data}")
                return

            self.bot_username = data["result"]["username"]
            print(f"[TelegramBot] Connected as @{self.bot_username}")

            # Clear any existing webhook so getUpdates polling works
            requests.post(f"{self.base_url}/deleteWebhook", timeout=10)
        except Exception as exc:
            print(f"[TelegramBot] Startup error: {exc}")
            return

        self._poll_loop()

    def stop(self) -> None:
        self._running = False

    def _poll_loop(self) -> None:
        while self._running:
            try:
                resp = requests.get(
                    f"{self.base_url}/getUpdates",
                    params={"offset": self._offset, "timeout": 30},
                    timeout=35,
                )
                for update in resp.json().get("result", []):
                    self._offset = update["update_id"] + 1
                    self._handle_update(update)
            except Exception as exc:
                print(f"[TelegramBot] Poll error: {exc}")
                time.sleep(5)

    def _handle_update(self, update: dict) -> None:
        message = update.get("message")
        if not message or "text" not in message:
            return

        text: str = message["text"]
        chat_id: int = message["chat"]["id"]

        if text.startswith("/start"):
            parts = text.split(maxsplit=1)
            if len(parts) == 2:
                self._send_image(chat_id, parts[1].strip())
            else:
                self._send_welcome(chat_id)

    def _send_welcome(self, chat_id: int) -> None:
        requests.post(
            f"{self.base_url}/sendMessage",
            json={
                "chat_id": chat_id,
                "text": (
                    "Welcome to Cute Fusion Lab!\n\n"
                    "Generate an image on the website, then press the QR button "
                    "and scan the code to receive your image here."
                ),
            },
            timeout=10,
        )

    def _send_image(self, chat_id: int, image_id: str) -> None:
        filename = f"{image_id}.png"
        image_path = self.output_dir / filename

        if not image_path.is_file():
            requests.post(
                f"{self.base_url}/sendMessage",
                json={
                    "chat_id": chat_id,
                    "text": "Sorry, that image was not found or has expired.",
                },
                timeout=10,
            )
            return

        with open(image_path, "rb") as photo:
            requests.post(
                f"{self.base_url}/sendPhoto",
                data={
                    "chat_id": chat_id,
                    "caption": "Here's your Cute Fusion Lab result!",
                },
                files={"photo": (filename, photo, "image/png")},
                timeout=30,
            )
