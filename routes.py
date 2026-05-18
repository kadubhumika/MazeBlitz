from fastapi import APIRouter
from sqlalchemy.orm import Session

from database import SessionLocal
from models import Score, Player
from maze_logic import solve_maze
from schemas import (
    CreatePlayer,
    DifficultyRequest, SubmitScore
)

from maze_logic import (
    select_difficulty,
    generate_maze
)

router = APIRouter()


@router.post("/create-player")
def create_player(player: CreatePlayer):

    db: Session = SessionLocal()

    try:

        # check existing user
        existing_player = db.query(Player).filter(
            Player.username == player.username
        ).first()

        # if exists -> return existing user
        if existing_player:

            return {
                "message": "Player already exists",
                "player_id": existing_player.id,
                "username": existing_player.username,
                "city": existing_player.city
            }

        # else create new user
        new_player = Player(
            username=player.username,
            city=player.city
        )

        db.add(new_player)

        db.commit()

        db.refresh(new_player)

        return {
            "message": "New Player Created",
            "player_id": new_player.id,
            "username": new_player.username,
            "city": new_player.city
        }

    finally:
        db.close()



@router.post("/generate-maze")
def create_maze(data: DifficultyRequest):

    settings = select_difficulty(
        data.difficulty,
        data.level
    )

    maze_data = generate_maze(
        settings["rows"],
        settings["cols"]
    )
    shortest_path = solve_maze(

        maze_data["maze"],

        tuple(maze_data["start"]),

        tuple(maze_data["end"])
    )

    return {

        "difficulty": data.difficulty,

        "level": data.level,

        "timer": settings["timer"],

        "maze_size": f"{settings['rows']}x{settings['cols']}",

        "maze_data": maze_data
    }

@router.post("/submit-score")
def submit_score(data: SubmitScore):

    db: Session = SessionLocal()

    try:

        new_score = Score(

            player_id=data.player_id,
            score=data.score,
            difficulty=data.difficulty,
            time_taken=data.time_taken,
            steps=data.steps,
            level=data.level
        )

        db.add(new_score)

        db.commit()

        db.refresh(new_score)

        return {
            "message": "Score Saved"
        }

    finally:

        db.close()

@router.get("/leaderboard")
def leaderboard():

    db: Session = SessionLocal()

    try:

        scores = db.query(
            Player.username,
            Score.score
        ).join(
            Score,
            Player.id == Score.player_id
        ).order_by(
            Score.score.desc()
        ).limit(10).all()

        return [
            {
                "username": item[0],
                "score": item[1]
            }
            for item in scores
        ]

    finally:
        db.close()

