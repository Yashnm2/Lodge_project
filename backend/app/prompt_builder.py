import re

from app.presets import GenerationPreset


def build_prompts(
    preset: GenerationPreset,
    custom_prompt: str | None = None,
    gender: str | None = None,
    hair: str | None = None,
    single: bool = False,
    facial_features: dict | None = None,
) -> tuple[str, str]:
    """Build the chat instruction for gpt-4o-image.

    Returns (instruction, info_display) where info_display is shown in the UI.
    """
    instruction_text = preset.instruction

    # For single-photo mode, rewrite "both people" references to singular
    if single:
        instruction_text = _to_single(instruction_text)

    # Append gender hint
    if gender in ("male", "female"):
        if preset.category == "combine":
            gender_word = "boy" if gender == "male" else "girl"
            instruction_text += f" The child should be a {gender_word}."
        else:
            instruction_text += f" The resulting person should be {gender}."

    # Append hair length hint
    if hair in ("short", "medium", "long"):
        instruction_text += f" The child should have {hair} hair."

    # Append facial feature hints (primarily for combine/kid mode)
    if facial_features:
        feature_hints = _build_feature_hints(facial_features)
        if feature_hints:
            instruction_text += " " + feature_hints

    preamble = (
        "Look carefully at this portrait photo."
        if single
        else "Look carefully at these two portrait photos."
    )

    parts = [preamble, instruction_text, preset.style_hint]

    instruction = " ".join(parts)
    info = f"[{preset.category.upper()}] {preset.title}\n{preset.instruction}"

    return instruction, info


def _build_feature_hints(features: dict) -> str:
    """Build natural language hints from facial feature selections."""
    hints = []

    skin = features.get("skin")
    if skin in ("light", "medium", "dark"):
        hints.append(f"a {skin} skin tone")

    eyes = features.get("eyes")
    if eyes in ("small", "large"):
        hints.append(f"{eyes} eyes" if eyes == "large" else "smaller, narrower eyes")

    nose = features.get("nose")
    if nose in ("small", "large"):
        hints.append(f"a {nose} nose" if nose == "small" else "a broader, larger nose")

    ears = features.get("ears")
    if ears in ("small", "large"):
        hints.append(f"{ears} ears")

    cheekbones = features.get("cheekbones")
    if cheekbones == "subtle":
        hints.append("soft, subtle cheekbones")
    elif cheekbones == "defined":
        hints.append("prominent, well-defined cheekbones")

    jawline = features.get("jawline")
    if jawline == "soft":
        hints.append("a soft, rounded jawline")
    elif jawline == "defined":
        hints.append("a strong, defined jawline")

    if not hints:
        return ""

    return "The child should have " + ", ".join(hints) + "."


def _to_single(text: str) -> str:
    """Rewrite two-person instructions to single-person instructions."""
    replacements = [
        ("both people's faces", "this person's face"),
        ("both people", "this person"),
        ("both reference photos", "the reference photo"),
        ("these two people's faces", "this person's face"),
        ("these two people", "this person"),
        ("of both people", "of this person"),
        ("two people", "this person"),
        ("from both", "from the"),
        ("from BOTH parents", "from the reference photo"),
        ("BOTH parents", "the person in the photo"),
        ("both parents", "the person in the photo"),
    ]
    for old, new in replacements:
        text = text.replace(old, new)
    return text
