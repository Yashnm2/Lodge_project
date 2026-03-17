# Cute Fusion Lab — Overhaul Changelog

## Overview

Complete overhaul of UI, prompt engineering, and API integration.
The previous version had multiple critical bugs: wrong API provider,
images that looked nothing like the inputs, and a basic UI with
unnecessary text input fields. This update fixes all root causes
and delivers a premium visual-only experience.

---

## Architecture (Final)

- **Backend:** FastAPI on port 8001
- **Frontend:** Vanilla HTML/CSS/JS on port 5173
- **API Provider:** apiyi.com (`https://api.apiyi.com`)
- **Model:** `gpt-4o-image` via `/v1/chat/completions` (multimodal)
- **Key Feature:** Both uploaded photos are sent as base64 images to the
  model, which actually sees and blends the faces in its output

---

## Critical Bugs Fixed

### 1. Wrong API provider + architecture (MAJOR)

**Before:** Backend pointed to `api.aimlapi.com` but the API key was
from `apiyi.com`. Additionally, the old text-to-image approach
(`/v1/images/generations`) completely ignored uploaded photos — output
had zero resemblance to inputs.

**After:** Base URL changed to `https://api.apiyi.com`. Model changed to
`gpt-4o-image` using `/v1/chat/completions` with multimodal input. Both
photos are sent as base64 data URLs and the model genuinely blends them.

### 2. Text input fields exposed to users

**Before:** Users could type custom prompts and seeds. This was confusing
and the text input had no meaningful effect on output quality.

**After:** All text inputs removed. Users interact only through visual
selections: upload photos, pick an effect card, click generate.

### 3. Content policy blocks unhandled

**Before:** Character-themed prompts (Hulk, etc.) sometimes triggered
content policy violations. Errors were in Chinese and not caught by
retry logic.

**After:** Retry logic (up to 3 attempts) detects both English and
Chinese policy error keywords (`policy`, `violat`, `违反`, `政策`,
`invalid_request`). Preset descriptions avoid copyrighted names.

### 4. Assorted API issues

- Prompts exceeded Imagen 4.0's 400-char limit (moot with gpt-4o-image)
- `negative_prompt` sent but unsupported by any model
- Wrong field name `number_of_images` instead of `n`
- PhotoMaker trigger tokens (`img person`) in prompts
- SD-style quality keywords (`masterpiece, ultra-detailed...`)
- Seed never passed to API
- Frontend/backend preset key mismatch
- Reference images encoded but never sent in payload
- Frontend ran in dummy mode only (never called backend)

All fixed.

---

## UI Redesign (v3)

### Two-Section Layout

1. **Combine Faces** — Upload two portraits, click "Combine Faces" to
   merge them into one blended person
2. **Apply Effects** — Pick one of 10 character transformation cards,
   click "Generate Image" to apply the effect

### 10 Effect Cards

| Effect | Icon | Description |
|--------|------|-------------|
| Trump | Tie | Presidential business mogul |
| Obama | Columns | Charismatic world leader |
| Hulk | Muscle | Giant green powerhouse |
| The Rock | Muscle | Action hero movie star |
| Anime | Cherry blossom | Japanese anime character |
| Cyberpunk | Robot | Neon-lit cyber augmentation |
| Angel | Halo | Celestial being with wings |
| Devil | Horns | Dark lord with horns |
| CEO | Briefcase | Fortune 500 executive |
| Space Emperor | Saturn | Cosmic ruler in armor |

### Design

- Dark navy background with animated gradient mesh and floating orbs
- Glassmorphism panels with `backdrop-filter: blur(16px)`
- Gradient buttons (green for combine, purple-pink for generate)
- Shimmer animation on generate button
- Drag-and-drop photo upload with preview and clear button
- Effect cards with active checkmark and hover animations
- Loading spinner states on all action buttons
- Result section with download PNG and regenerate buttons
- Responsive layout (stacks at 640px breakpoint)
- No text input fields anywhere

---

## Backend Architecture

### `photomaker_service.py`
- Uses `/v1/chat/completions` with `gpt-4o-image`
- Both reference photos sent as base64 data URLs in messages array
- Extracts generated image from: content parts list, markdown image URLs,
  bare URLs, or base64 inline data
- Retry logic for content policy errors (3 attempts)
- Specific error for invalid/expired API keys

### `presets.py`
- 11 presets: 1 combine (`merge`) + 10 effects
- Each preset has: key, title, description, icon, category, instruction, style_hint
- Instructions explicitly reference "both reference photos" for face blending
- Character descriptions are original (avoid copyrighted names in prompts)

### `prompt_builder.py`
- Builds instruction: "Look carefully at these two portrait photos. [instruction] [style_hint]"
- No PhotoMaker tokens, no SD keywords, no quality boosters
- Returns (instruction, info_display) tuple

### `config.py`
- Base URL: `https://api.apiyi.com`
- Model: `gpt-4o-image`
- CORS origins for ports 5173, 5500, 8001, 8080, 3000

### `main.py`
- Endpoints: GET `/api/health`, GET `/api/presets`, POST `/api/generate`
- Image preprocessing: EXIF transpose, RGB convert, center crop, resize to 1024x1024

---

## Tested Modes (All Working)

| Mode | Status | Notes |
|------|--------|-------|
| merge | OK | Clean face blend |
| trump | OK | Presidential style |
| obama | OK | Political leader |
| hulk | OK | Green powerhouse (retry may be needed) |
| rock | OK | Action star |
| anime | OK | Anime style |
| cyberpunk | OK | Neon augmentation |
| angel | OK | Celestial wings |
| devil | OK | Dark lord |
| executive | OK | Corporate CEO |
| emperor | OK | Space ruler |

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

---

## File Changes Summary

| File | Change |
|------|--------|
| `frontend/index.html` | Complete rewrite — two-section layout, dark theme, no text inputs |
| `frontend/styles.css` | Complete rewrite — premium dark UI, glassmorphism, animations |
| `frontend/app.js` | Complete rewrite — visual-only, 10 effects, drag-drop, backend integration |
| `backend/app/presets.py` | Complete rewrite — 11 presets with chat-style instructions |
| `backend/app/prompt_builder.py` | Simplified for chat instructions |
| `backend/app/photomaker_service.py` | Complete rewrite — multimodal chat completions |
| `backend/app/config.py` | apiyi.com base URL, gpt-4o-image model |
| `backend/app/main.py` | Fixed seed handling, cleaner errors |
| `backend/.env` | Updated base URL and model |
| `changes.md` | This file |
