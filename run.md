# How to Run Lodge Project (Cute Fusion Lab)

## Prerequisites

- Python 3.10+
- All backend Python dependencies installed (see `backend/requirements.txt`)

### Install dependencies

```bash
cd backend
pip install -r requirements.txt
```

## Running the App

You need to start **two servers**: the backend API and the frontend file server.

### Step 1: Start the Backend

Open a terminal and run:

```bash
cd backend
python -m uvicorn app.main:app --host 127.0.0.1 --port 8001 --reload
```

The FastAPI backend will be available at `http://localhost:8001`.

### Step 2: Start the Frontend

Open a **second terminal** and run:

```bash
cd frontend
python -m http.server 8080
```

The frontend will be available at `http://localhost:8080`.

### Step 3: Open the App

Go to **http://localhost:8080** in your browser.

## Configuration

The backend reads settings from a `.env` file in the `backend/` directory. Available options:

| Variable             | Description                        | Default                    |
|----------------------|------------------------------------|----------------------------|
| `AIMLAPI_KEY`        | API key for image generation       | (empty)                    |
| `AIMLAPI_BASE_URL`   | Base URL for the image API         | `https://api.apiyi.com`    |
| `TELEGRAM_BOT_TOKEN` | Telegram bot token (optional)      | (empty)                    |
| `CORS_ORIGINS`       | Comma-separated allowed origins    | localhost on common ports   |

## Stopping the App

Press `Ctrl+C` in each terminal to stop the backend and frontend servers.
