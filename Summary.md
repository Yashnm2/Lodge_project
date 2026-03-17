# Cute Fusion Lab — Complete Project Summary

## What Is This App?

Cute Fusion Lab is an AI-powered face fusion web app with two main features:

1. **Future Kid** — Upload two parent photos and generate what their child would look like as a 4-6 year old, with control over gender, hair, skin tone, eye size, nose size, ear size, cheekbone definition, and jawline definition.

2. **Apply Effects** — Upload a single photo and transform the person into different characters: Trump, Obama, Hulk, The Rock, Anime, Cyberpunk, Angel, Devil, CEO, or Space Emperor.

Results can be downloaded as PNG or sent directly to a Telegram user.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | Vanilla HTML/CSS/JS (no framework) |
| Backend | Python FastAPI |
| AI Model | `gpt-4o-image` via apiyi.com |
| API Protocol | OpenAI-compatible chat completions (`/v1/chat/completions`) |
| Image Processing | Pillow (PIL) |
| Sharing | Telegram Bot API |

---

## How It Works

### Architecture

```
Browser (port 5173)  -->  FastAPI Backend (port 8001)  -->  apiyi.com API
   index.html                main.py                        gpt-4o-image
   styles.css                photomaker_service.py
   app.js                    prompt_builder.py
                             presets.py
```

### Image Generation Flow

1. User uploads photo(s) via drag-drop, file picker, or camera
2. Frontend sends photos + options as multipart form data to `POST /api/generate`
3. Backend preprocesses images: EXIF transpose, RGB convert, center crop to square, resize to 512x512
4. Images are base64-encoded as JPEG (quality 80) and sent as data URLs in a multimodal chat completions request to `gpt-4o-image`
5. The prompt is constructed from the preset instruction + user-selected options (gender, hair, skin, facial features)
6. The API generates an image; backend extracts it from the response (handles multiple formats: content parts, markdown URLs, bare URLs, base64 inline)
7. Image is saved as PNG to `backend/generated/` and URL returned to frontend
8. Frontend displays the result with download and Telegram send options

### Key Design Decision: Why Chat Completions?

The app originally used a text-to-image endpoint (`/v1/images/generations`) which **completely ignored the uploaded photos** — output had zero resemblance to inputs. The fix was switching to multimodal chat completions (`/v1/chat/completions`) with `gpt-4o-image`, which actually sees the uploaded photos and blends facial features from them.

---

## API Configuration

```env
# backend/.env
AIMLAPI_KEY=sk-l137IVRQGgiUI6abBc60299dF33742A6BcA6Aa1cE4884b20
AIMLAPI_BASE_URL=https://api.apiyi.com
IMAGEN_MODEL=gpt-4o-image
TELEGRAM_BOT_TOKEN=        # Optional: create via @BotFather on Telegram
```

- **API Provider**: apiyi.com (NOT aimlapi.com — the key is from apiyi)
- **Model**: `gpt-4o-image` — the only model on apiyi.com that accepts image inputs AND generates images
- **Endpoint**: `/v1/chat/completions` (OpenAI-compatible format)
- **Payload format**: Images sent as base64 data URLs in the `messages` array with `"modalities": ["text", "image"]`

---

## Backend Files

### `backend/app/main.py`
FastAPI application with endpoints:
- `GET /api/health` — Health check
- `GET /api/presets` — List available presets
- `POST /api/generate` — Generate image (accepts: person_a, person_b (optional), mode, gender, hair, skin, eyes, nose, ears, cheekbones, jawline, seed)
- `POST /api/send-telegram` — Send generated image to a Telegram user
- Static file serving for generated images at `/api/images/`

### `backend/app/photomaker_service.py`
Core image generation service:
- Encodes input images as JPEG base64 data URLs (optimized from PNG for ~50x smaller payload)
- Sends multimodal chat completion request to apiyi.com
- Retry logic (up to 3 attempts) for content policy violations
- Detects both English and Chinese error messages (`policy`, `violat`, `违反`, `政策`, `invalid_request`)
- Extracts generated image from multiple response formats (content parts list, markdown image URLs, bare URLs, base64 inline)

### `backend/app/presets.py`
11 generation presets:

| Key | Title | Category | Description |
|-----|-------|----------|-------------|
| merge | Future Kid | combine | Generates a child from two parent photos |
| trump | Trump | effects | Trump-inspired styling (keeps original face) |
| obama | Obama | effects | Obama-inspired styling (keeps original face) |
| hulk | Hulk | effects | Green muscular fantasy giant |
| rock | The Rock | effects | Muscular action movie star |
| anime | Anime | effects | Japanese anime character style |
| cyberpunk | Cyberpunk | effects | Neon-lit cyber augmentation |
| angel | Angel | effects | Celestial being with wings |
| devil | Devil | effects | Dark lord with horns and embers |
| executive | CEO | effects | Fortune 500 corporate executive |
| emperor | Space Emperor | effects | Cosmic ruler in ornate armor |

Each preset has: `key`, `title`, `description`, `icon`, `category` (combine/effects), `instruction` (the AI prompt), `style_hint` (lighting/setting description).

### `backend/app/prompt_builder.py`
Constructs the final prompt from preset + user options:
- Prepends "Look carefully at these two portrait photos" (or singular for effects)
- Appends gender hint (boy/girl for kid mode, male/female for effects)
- Appends hair length hint
- Appends facial feature hints (skin tone, eye size, nose size, ear size, cheekbones, jawline)
- For single-photo mode (effects tab), rewrites "both people" references to singular
- Feature hints are constructed as natural language: "The child should have light skin tone, large eyes, a small nose, and a strong, defined jawline."

### `backend/app/config.py`
Pydantic settings loaded from `.env`:
- API key, base URL, model name
- Target image size: 512px (reduced from 1024 for faster upload)
- Telegram bot token
- CORS origins for dev servers (ports 5173, 5500, 8001, 8080, 3000)

### `backend/app/image_utils.py`
Image preprocessing:
- Loads image from bytes with error handling
- EXIF transpose (fixes rotated phone photos)
- RGB conversion
- Center crop to square
- Resize to 512x512 with LANCZOS resampling

---

## Frontend Files

### `frontend/index.html`
Two-tab layout:
- **Future Kid tab**: Two photo dropzones (Person A / Person B), 8 facial feature option groups in a 2-column grid, "Generate Kid" button
- **Effects tab**: Single photo dropzone, gender + hair options, 10 effect cards grid, "Generate Image" button
- **Result section**: Generated image display, download PNG link, regenerate button, Telegram send field
- **Camera modal**: Fullscreen camera overlay with capture/cancel buttons
- Each dropzone has a camera button (bottom-right) for taking photos

### `frontend/styles.css`
Dark theme with green accents:
- Background: `#080808` (pure black)
- Accent: `#22c55e` (green), secondary: `#10b981` (emerald)
- Glassmorphism panels with `backdrop-filter: blur(16px)`
- Animated background orbs and gradient mesh
- Responsive at 640px breakpoint
- Shimmer animation on generate button
- Effect cards with active checkmark overlay
- Camera modal with fullscreen video preview

### `frontend/app.js`
Client-side logic:
- Tab switching between Future Kid and Effects
- Toggle button groups for all options (gender, hair, skin, eyes, nose, ears, cheekbones, jawline)
- Drag-and-drop file upload with DataTransfer API
- Camera capture via `getUserMedia` with square crop
- Backend health probe on page load
- API calls with loading spinners and error handling
- Telegram send integration
- Regenerate with last used mode/tab

---

## Features

### Photo Input
- **File upload**: Click dropzone or drag-and-drop
- **Camera capture**: Camera button opens fullscreen modal with live video, captures square-cropped frame
- **Clear button**: Remove uploaded image (X button on top-right of preview)

### Future Kid Tab
- Upload two parent photos
- Customize 8 facial features:
  - Gender (Any / Boy / Girl)
  - Hair length (Any / Short / Medium / Long)
  - Skin tone (Blend / Light / Medium / Dark)
  - Eye size (Natural / Small / Large)
  - Nose size (Natural / Small / Large)
  - Ear size (Natural / Small / Large)
  - Cheekbones (Natural / Subtle / Defined)
  - Jawline (Natural / Soft / Defined)
- Generates a 4-6 year old child portrait blending features from both parents

### Effects Tab
- Upload single photo
- Choose gender and hair length
- Pick from 10 character transformation cards
- Effects keep the original person's face as the base (especially Trump/Obama)

### Result
- Download as PNG
- Regenerate with same settings
- Send to Telegram user (requires bot token setup)

### Telegram Integration
- Enter @username under the generated image and click Send
- Requires `TELEGRAM_BOT_TOKEN` in `.env` (create via @BotFather)
- Recipient must have messaged the bot first (Telegram API requirement)

---

## Running the App

```bash
# Backend (port 8001)
cd backend
.venv/Scripts/python.exe -m uvicorn app.main:app --host 127.0.0.1 --port 8001

# Frontend (port 5173)
cd frontend
python -m http.server 5173

# Open http://127.0.0.1:5173
```

### Prerequisites
- Python 3.11+ with virtual environment
- Python packages: `fastapi`, `uvicorn`, `requests`, `Pillow`, `pydantic-settings`, `python-multipart`
- Valid apiyi.com API key with access to `gpt-4o-image`

---

## Performance Optimizations

- **JPEG encoding** instead of PNG for base64 payloads (~50x smaller)
- **512x512** reference images instead of 1024x1024 (model doesn't need full resolution for face recognition)
- Total payload reduced from ~7.4 MB to ~0.1 MB for two images
- API generation time is still ~15-30 seconds (server-side, can't be reduced)

---

## Content Policy Handling

Some character-themed prompts (Hulk, etc.) can trigger content policy violations from the API. The backend handles this with:
- Retry logic: up to 3 attempts per request
- Error detection in both English and Chinese: `policy`, `violat`, `违反`, `政策`, `invalid_request`
- Preset descriptions avoid copyrighted character names where possible
- If all retries fail, user gets a clear error message suggesting different photos or mode

---

## Development History

### Major Iterations

1. **v1 (Original)**: Light theme, text-to-image via aimlapi.com, dummy mode only (frontend never called backend), output had zero resemblance to input photos

2. **v2 (API Fix)**: Switched to apiyi.com (correct provider for the API key), changed model to gpt-4o-image, switched from text-to-image to multimodal chat completions so uploaded photos are actually used

3. **v3 (UI Redesign)**: Dark premium theme, removed all text input fields, split into Combine/Effects sections, added 10 effect presets, drag-and-drop uploads, loading states

4. **v4 (Tabs + Features)**: Two-tab layout (Future Kid / Effects), Effects tab uses single photo, added gender and hair length toggles, added camera capture

5. **v5 (Current)**: Black and green theme, added 6 facial feature controls (skin tone, eye size, nose size, ear size, cheekbones, jawline), merge mode generates a kid, Trump/Obama effects keep original face, Telegram sharing, performance optimizations (JPEG encoding, 512px images)

### Key Bugs Fixed
- Wrong API provider (aimlapi.com vs apiyi.com)
- Text-to-image endpoint ignoring uploaded photos (switched to multimodal chat)
- Content policy blocks unhandled (added retry with Chinese error detection)
- Prompts exceeding model limits (rewritten as natural language)
- PhotoMaker trigger tokens in prompts (removed)
- Frontend never connected to backend (was dummy mode only)
- Trump/Obama effects generating the celebrity instead of the user's face (rewritten prompts to keep original face)
- Browser caching old stylesheets (added cache-bust query params)
- Purple color remnants in "black and red" theme (fixed all rgba values and text colors)

---

## File Structure

```
LODGE_PROJECT/
  backend/
    .env                          # API keys and config
    app/
      __init__.py
      config.py                   # Pydantic settings
      image_utils.py              # Image preprocessing
      main.py                     # FastAPI app + endpoints
      photomaker_service.py       # AI image generation service
      presets.py                  # 11 generation presets
      prompt_builder.py           # Prompt construction with feature hints
    generated/                    # Output images (gitignored)
    .venv/                        # Python virtual environment
  frontend/
    index.html                    # Two-tab UI layout
    styles.css                    # Dark green theme
    app.js                        # Client-side logic
  Summary.md                      # This file
  changes.md                      # Changelog
```
