from __future__ import annotations

import base64
import io
import re
from dataclasses import dataclass

import requests
from PIL import Image

from app.config import settings


@dataclass
class GenerationSettings:
    prompt: str
    negative_prompt: str  # kept for UI display only
    seed: int | None = None


class PhotoMakerService:
    """Generates images via apiyi.com using gpt-4o-image (chat completions).

    Unlike the old text-to-image approach, this sends the actual reference
    photos to the model so the output genuinely blends the input faces.
    """

    def __init__(self) -> None:
        self._session = requests.Session()
        self._session.headers.update(
            {
                "Authorization": f"Bearer {settings.aimlapi_key}",
                "Content-Type": "application/json",
            }
        )

    @staticmethod
    def _image_to_data_url(img: Image.Image) -> str:
        buf = io.BytesIO()
        img.save(buf, format="JPEG", quality=60)
        b64 = base64.b64encode(buf.getvalue()).decode("utf-8")
        return f"data:image/jpeg;base64,{b64}"

    def generate(
        self,
        id_images: list[Image.Image],
        options: GenerationSettings,
    ) -> Image.Image:
        if not settings.aimlapi_key:
            raise RuntimeError("API key not configured. Set AIMLAPI_KEY in .env")

        # Encode reference images as data URLs
        content_parts: list[dict] = []
        for i, img in enumerate(id_images):
            content_parts.append(
                {
                    "type": "image_url",
                    "image_url": {"url": self._image_to_data_url(img)},
                }
            )

        # Add the text instruction
        content_parts.append({"type": "text", "text": options.prompt})

        payload = {
            "model": settings.imagen_model,
            "messages": [
                {
                    "role": "user",
                    "content": content_parts,
                }
            ],
            "modalities": ["text", "image"],
            "max_tokens": 1024,
        }

        # Retry up to 2 times for content policy errors (sometimes probabilistic)
        last_error = ""
        for attempt in range(3):
            response = self._session.post(
                f"{settings.aimlapi_base_url}/v1/chat/completions",
                json=payload,
                timeout=180,
            )

            if response.status_code == 200:
                break

            body = response.text[:500]
            if "API key" in body or response.status_code == 401:
                raise RuntimeError(
                    "API key is invalid or expired. "
                    "Get a new key at https://apiyi.com and update backend/.env"
                )

            # Content policy violations can be probabilistic — retry
            # Error messages may be in Chinese (违反/政策) or English (policy/violat)
            policy_keywords = ["policy", "violat", "\u8fdd\u53cd", "\u653f\u7b56", "invalid_request"]
            if response.status_code == 500 and any(k in body.lower() for k in policy_keywords):
                last_error = body
                continue

            raise RuntimeError(f"API returned {response.status_code}: {body}")
        else:
            raise RuntimeError(
                f"Content policy blocked after {attempt + 1} attempts. "
                "Try a different mode or different photos."
            )

        result = response.json()

        if "choices" not in result or not result["choices"]:
            raise RuntimeError("API returned no choices.")

        message = result["choices"][0].get("message", {})
        content = message.get("content", "")

        # Parse image from response — can be markdown, list of parts, or direct URL
        return self._extract_image(content)

    def _extract_image(self, content) -> Image.Image:
        """Extract the generated image from various response formats."""

        # Format 1: content is a list of parts (OpenAI native format)
        if isinstance(content, list):
            for part in content:
                if isinstance(part, dict):
                    # Image URL part
                    if part.get("type") == "image_url":
                        url = part.get("image_url", {}).get("url", "")
                        return self._download_image(url)
                    # Base64 inline image
                    if part.get("type") == "image":
                        b64 = part.get("data") or part.get("b64_json", "")
                        if b64:
                            return Image.open(
                                io.BytesIO(base64.b64decode(b64))
                            ).convert("RGB")
            raise RuntimeError("No image found in response parts.")

        # Format 2: content is a string with markdown image(s)
        if isinstance(content, str):
            # Match ![...](url) or just raw URLs
            urls = re.findall(r'!\[.*?\]\((https?://[^\s)]+)\)', content)
            if not urls:
                # Try bare URL pattern
                urls = re.findall(r'(https?://\S+\.(?:png|jpg|jpeg|webp))', content)
            if urls:
                return self._download_image(urls[0])

        raise RuntimeError(
            "Could not extract image from API response. "
            f"Content type: {type(content).__name__}"
        )

    def _download_image(self, url: str) -> Image.Image:
        """Download image from URL, handling data URLs and http URLs."""
        if url.startswith("data:"):
            # data:image/png;base64,...
            _, encoded = url.split(",", 1)
            return Image.open(io.BytesIO(base64.b64decode(encoded))).convert("RGB")

        resp = self._session.get(url, timeout=60)
        resp.raise_for_status()
        return Image.open(io.BytesIO(resp.content)).convert("RGB")
