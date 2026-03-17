from pathlib import Path

from pydantic_settings import BaseSettings, SettingsConfigDict


BACKEND_ROOT = Path(__file__).resolve().parents[1]


class Settings(BaseSettings):
    app_name: str = "Cute Fusion Lab API"
    output_dir: Path = BACKEND_ROOT / "generated"

    # API provider (apiyi.com)
    aimlapi_key: str = ""
    aimlapi_base_url: str = "https://api.apiyi.com"
    imagen_model: str = "gpt-4o-image"

    target_image_size: int = 384

    # Telegram
    telegram_bot_token: str = ""

    cors_origins: str = (
        "http://127.0.0.1:5173,"
        "http://localhost:5173,"
        "http://127.0.0.1:5500,"
        "http://localhost:5500,"
        "http://127.0.0.1:8001,"
        "http://localhost:8001,"
        "http://127.0.0.1:8080,"
        "http://localhost:8080,"
        "http://127.0.0.1:3000,"
        "http://localhost:3000"
    )

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    @property
    def cors_origin_list(self) -> list[str]:
        return [item.strip() for item in self.cors_origins.split(",") if item.strip()]


settings = Settings()
