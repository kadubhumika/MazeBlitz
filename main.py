from fastapi import FastAPI

from database import engine
from models import Base
from fastapi.middleware.cors import CORSMiddleware
from routes import router

app = FastAPI()

Base.metadata.create_all(bind=engine)

app.include_router(router)
app.add_middleware(

    CORSMiddleware,

    allow_origins=["*"],

    allow_credentials=True,

    allow_methods=["*"],

    allow_headers=["*"],
)


@app.get("/")
def home():

    return {
        "message": "MazeBlitz Backend Running"
    }


