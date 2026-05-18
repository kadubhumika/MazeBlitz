from pydantic import BaseModel


class CreatePlayer(BaseModel):

    username: str
    city: str


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

class SubmitScore(BaseModel):

    player_id: int

    score: int

    difficulty: str

    time_taken: float

    steps: int

    level: int