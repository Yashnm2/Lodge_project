# Cute Fusion Lab

A two-folder full-stack app:

- `frontend` - aesthetic upload UI and mode selection.
- `backend` - Python FastAPI service that runs PhotoMaker V2 generation.

Main feature:
- Upload two people and generate a "future kid" blend.

Extra modes:
- Spider Hero, Gamma Titan (Hulk-style), Angel Aura, Devil Charm, Titan Fusion (Thanos-style), Political Satire (Trump-inspired).

## Structure

```text
frontend/
  index.html
  styles.css
  app.js
backend/
  app/
    main.py
    config.py
    presets.py
    prompt_builder.py
    photomaker_service.py
    image_utils.py
  requirements.txt
  .env.example
```

## Quick Start

### Frontend Only (Dummy Mode, No Backend)

Use this first if you just want the UI flow with fake generated results.

```powershell
cd frontend
python -m http.server 5173
```

Open: `http://127.0.0.1:5173`

Notes:
- This mode does not call FastAPI.
- Output image is a local canvas simulation for demo/testing.

### Full Stack

1. Backend setup (recommended Python 3.12 or 3.11 for Torch compatibility):

```powershell
cd backend
py -3.12 -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
copy .env.example .env
```

2. Hugging Face CLI login (needed in many environments):

```powershell
pip install "huggingface_hub[cli]"
hf auth login
```

3. Start backend:

```powershell
cd backend
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

4. Start frontend (from project root):

```powershell
cd frontend
python -m http.server 5173
```

Then open: `http://127.0.0.1:5173`

## Troubleshooting

If you see:

`uvicorn : The term 'uvicorn' is not recognized...`

Use the module form (recommended):

```powershell
cd backend
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

If that still fails, make sure dependencies were installed in your venv:

```powershell
cd backend
.venv\Scripts\activate
pip install -r requirements.txt
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

If your current `.venv` was created with Python 3.14, recreate it with 3.12:

```powershell
cd backend
deactivate 2>$null
Remove-Item -Recurse -Force .venv
py -3.12 -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

## Prompt Engineering Included

The backend composes prompts from:
- identity anchor (`img person`) required by PhotoMaker adapter flow,
- mode-specific cinematic style blocks,
- quality boosters,
- mode-specific negative prompt boosts,
- optional user custom prompt.

This gives more consistent lighting, facial detail, and cleaner outputs versus a single short prompt.

## Notes

- This is a creative generator, not a scientific predictor of real children.
- Local inference with PhotoMaker V2 is GPU-heavy.
- If generation fails, check CUDA/Torch install, model download access, and free VRAM.
