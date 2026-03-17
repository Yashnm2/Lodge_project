from dataclasses import dataclass


@dataclass(frozen=True)
class GenerationPreset:
    key: str
    title: str
    description: str
    icon: str
    category: str  # "combine" or "effects"
    instruction: str
    style_hint: str


PRESETS: dict[str, GenerationPreset] = {
    # ---- COMBINE (pure face merge) ----
    "merge": GenerationPreset(
        key="merge",
        title="Future Kid",
        icon="\U0001F476",
        category="combine",
        description="Generate what their child would look like.",
        instruction=(
            "These two photos are the parents. Create a photorealistic portrait of their "
            "adorable young child (around 4-6 years old). The child's face must clearly "
            "inherit features from BOTH parents — blend their eye shapes, nose structure, "
            "jawline, skin tone, and hair characteristics into a believable child's face. "
            "The child should have a natural, cute expression with bright curious eyes "
            "and a gentle smile."
        ),
        style_hint=(
            "Warm soft portrait lighting, cozy neutral background, shallow depth of field, "
            "professional family photography, natural skin texture, sharp focus on the child's face."
        ),
    ),

    # ---- EFFECTS ----
    "trump": GenerationPreset(
        key="trump",
        title="Trump",
        icon="\U0001F454",
        category="effects",
        description="Presidential business mogul style.",
        instruction=(
            "Keep the original person's face as the primary base — their eyes, nose, "
            "bone structure, and skin must remain clearly recognizable. Then apply a "
            "Trump-inspired transformation on top: give them a swept-back blonde "
            "combover hairstyle, a slightly more tan complexion, a confident squinting "
            "expression, and dress them in an expensive navy suit with a long red tie "
            "and American flag pin. The person should look like THEMSELVES cosplaying "
            "as Trump, not like Trump himself."
        ),
        style_hint=(
            "Presidential podium or rally setting, dramatic stage lighting, "
            "news magazine cover quality, ultra-sharp editorial photography."
        ),
    ),
    "obama": GenerationPreset(
        key="obama",
        title="Obama",
        icon="\U0001F3DB\uFE0F",
        category="effects",
        description="Charismatic world leader aura.",
        instruction=(
            "Keep the original person's face as the primary base — their eyes, nose, "
            "bone structure, and skin must remain clearly recognizable. Then apply an "
            "Obama-inspired transformation on top: give them a close-cropped dark "
            "hairstyle with subtle grey at the temples, a warm wide smile, a calm "
            "confident presidential expression, and dress them in a well-fitted dark "
            "charcoal suit with American flag pin. The person should look like "
            "THEMSELVES styled as Obama, not like Obama himself."
        ),
        style_hint=(
            "Oval office or White House setting, warm professional lighting, "
            "official portrait photography, hope-poster inspired color grading."
        ),
    ),
    "hulk": GenerationPreset(
        key="hulk",
        title="Hulk",
        icon="\U0001F4AA",
        category="effects",
        description="Giant green superhero powerhouse.",
        instruction=(
            "Merge the facial features of both people into a massively muscular "
            "fantasy giant with green-tinted skin. The face must clearly show blended "
            "traits from both reference photos. Powerful intense expression, "
            "huge arms, broad shoulders, strong imposing physique."
        ),
        style_hint=(
            "Dramatic low-angle shot, harsh directional lighting with green ambient fill, "
            "volumetric dust, rocky outdoor environment, cinematic fantasy quality."
        ),
    ),
    "rock": GenerationPreset(
        key="rock",
        title="The Rock",
        icon="\U0001F4AA",
        category="effects",
        description="Action hero movie star physique.",
        instruction=(
            "Blend the facial features of both people into an extremely muscular, "
            "confident action movie star. The face should clearly combine traits from "
            "both reference photos. Bald head, raised eyebrow expression, massive arms, "
            "wearing a fitted black T-shirt that shows off huge biceps."
        ),
        style_hint=(
            "Dramatic Hollywood lighting, gym or movie set backdrop, "
            "cinematic portrait, professional entertainment photography."
        ),
    ),
    "anime": GenerationPreset(
        key="anime",
        title="Anime",
        icon="\U0001F338",
        category="effects",
        description="Japanese anime character style.",
        instruction=(
            "Transform the facial features of both people into a stylized anime "
            "character portrait. Blend recognizable traits from both reference photos "
            "into the anime face — their eye color, hair style hints, face shape. "
            "Large expressive anime eyes, colorful vibrant hair, clean linework."
        ),
        style_hint=(
            "Anime art style, vibrant colors, cherry blossom or school background, "
            "cel-shaded lighting, Studio Ghibli meets modern anime aesthetic."
        ),
    ),
    "cyberpunk": GenerationPreset(
        key="cyberpunk",
        title="Cyberpunk",
        icon="\U0001F916",
        category="effects",
        description="Neon-lit futuristic augmentation.",
        instruction=(
            "Blend the facial features of both people into a cyberpunk-enhanced "
            "portrait. The face should clearly combine traits from both reference photos "
            "but with futuristic cybernetic implants, glowing circuit-line tattoos, "
            "one eye replaced with a glowing mechanical lens, metallic jaw augmentation."
        ),
        style_hint=(
            "Neon-lit rain-soaked city at night, cyan and magenta lighting, "
            "holographic UI elements floating nearby, Blade Runner atmosphere."
        ),
    ),
    "angel": GenerationPreset(
        key="angel",
        title="Angel",
        icon="\U0001F607",
        category="effects",
        description="Heavenly celestial being with wings.",
        instruction=(
            "Blend the facial features of both people into a serene celestial angel "
            "with magnificent white feathered wings spread wide. The face should "
            "clearly combine traits from both reference photos. Flowing ethereal "
            "white-and-gold robes, gentle warm halo of light, peaceful expression."
        ),
        style_hint=(
            "Heavenly golden sunrise cloudscape, soft dreamy focus, luminous glowing skin, "
            "renaissance painting meets modern photography."
        ),
    ),
    "devil": GenerationPreset(
        key="devil",
        title="Devil",
        icon="\U0001F608",
        category="effects",
        description="Elegant dark lord with horns and embers.",
        instruction=(
            "Merge the facial features of both people into an elegantly sinister "
            "figure with subtle curved horns and piercing amber eyes. The face must "
            "clearly blend recognizable traits from both reference photos. "
            "Tailored obsidian-black suit with crimson accents, confident smirk."
        ),
        style_hint=(
            "Luxurious dark chamber with floating embers, chiaroscuro firelight, "
            "smoke wisps curling elegantly, dark fantasy fashion photography."
        ),
    ),
    "executive": GenerationPreset(
        key="executive",
        title="CEO",
        icon="\U0001F4BC",
        category="effects",
        description="Powerful corporate executive.",
        instruction=(
            "Blend the facial features of both people into a powerful Fortune 500 CEO. "
            "The face should clearly combine traits from both reference photos. "
            "Impeccably tailored charcoal suit, silver cufflinks, confident posture, "
            "commanding presence, salt-and-pepper distinguished styling."
        ),
        style_hint=(
            "Corner office with floor-to-ceiling windows overlooking a city skyline, "
            "professional corporate portrait lighting, sharp editorial photography."
        ),
    ),
    "emperor": GenerationPreset(
        key="emperor",
        title="Space Emperor",
        icon="\U0001FA90",
        category="effects",
        description="Cosmic ruler in ornate armor.",
        instruction=(
            "Fuse the facial features of both people into a powerful cosmic emperor "
            "in ornate golden-and-purple ceremonial armor with glowing gemstones. "
            "The face must clearly combine recognizable traits from both reference "
            "photos. Imposing jawline, contemplative expression, massive build."
        ),
        style_hint=(
            "Alien throne room with vibrant nebula visible through windows, "
            "dramatic wide-angle composition, volumetric cosmic dust, sci-fi lighting."
        ),
    ),
}
