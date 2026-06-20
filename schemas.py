from pydantic import BaseModel


class LoginRequest(BaseModel):
    username: str
    password: str


class CreatePlayer(BaseModel):
    username: str
    city: str
    password: str


class DifficultyRequest(BaseModel):
    difficulty: str
    level: int


class SubmitScore(BaseModel):
    player_id: int
    score: int
    difficulty: str
    time_taken: float
    steps: int
    level: int