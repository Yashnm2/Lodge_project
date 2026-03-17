from io import BytesIO

from PIL import Image, ImageOps, UnidentifiedImageError

from app.config import settings


def load_image_from_bytes(raw: bytes) -> Image.Image:
    try:
        image = Image.open(BytesIO(raw))
    except UnidentifiedImageError as exc:
        raise ValueError("Uploaded file is not a valid image.") from exc
    return image


def preprocess_reference_image(image: Image.Image) -> Image.Image:
    image = ImageOps.exif_transpose(image).convert("RGB")
    width, height = image.size
    side = min(width, height)

    left = (width - side) // 2
    top = (height - side) // 2
    cropped = image.crop((left, top, left + side, top + side))
    return cropped.resize(
        (settings.target_image_size, settings.target_image_size),
        Image.Resampling.LANCZOS,
    )

