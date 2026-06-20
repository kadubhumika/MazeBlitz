from fastapi import APIRouter
from sqlalchemy.orm import Session
from fastapi import HTTPException, status

from database import SessionLocal
from models import Score, Player
from maze_logic import solve_maze
from schemas import (
    CreatePlayer,
    DifficultyRequest, SubmitScore, LoginRequest
)

from maze_logic import (
    select_difficulty,
    generate_maze
)

router = APIRouter()


@router.post("/login")
def login(data: LoginRequest):
    db = SessionLocal()
    try:
        user = db.query(Player).filter(Player.username == data.username).first()

        if not user or user.password != data.password:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid credentials"
            )

        return {
            "player_id": user.id,
            "username": user.username,
            "city": user.city
        }
    finally:
        db.close()


@router.post("/create-player")
def create_player(player: CreatePlayer):
    db: Session = SessionLocal()
    try:
        existing_player = db.query(Player).filter(
            Player.username == player.username
        ).first()

        if existing_player:
            return {
                "message": "Player already exists",
                "player_id": existing_player.id,
                "username": existing_player.username,
                "city": existing_player.city,
                "easy_level_completed": existing_player.easy_level_completed,
                "medium_level_completed": existing_player.medium_level_completed,
                "hard_level_completed": existing_player.hard_level_completed,
                "total_score": existing_player.total_score
            }

        new_player = Player(
            username=player.username,
            city=player.city,
            password=player.password
        )

        db.add(new_player)
        db.commit()
        db.refresh(new_player)

        return {
            "message": "New Player Created",
            "player_id": new_player.id,
            "username": new_player.username,
            "city": new_player.city,
            "easy_level_completed": 0,
            "medium_level_completed": 0,
            "hard_level_completed": 0,
            "total_score": 0
        }
    finally:
        db.close()


@router.post("/generate-maze")
def create_maze(data: DifficultyRequest):

    settings = select_difficulty(
        data.difficulty,
        data.level
    )

    if settings is None:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invalid difficulty - must be easy, medium, or hard"
        )

    maze_data = generate_maze(
        settings["rows"],
        settings["cols"],
        settings["traps"],
        settings["loop_chance"],
        settings["dead_end_factor"],
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
        player = db.query(Player).filter(
            Player.id == data.player_id
        ).first()

        if not player:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Player not found"
            )

        difficulty_multiplier = {
            "easy": 100,
            "medium": 250,
            "hard": 500
        }

        if data.difficulty not in difficulty_multiplier:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Invalid difficulty - must be easy, medium, or hard"
            )

        final_score = (
            data.level * difficulty_multiplier[data.difficulty]
        ) + int(data.time_taken)

        new_score = Score(
            player_id=data.player_id,
            score=final_score,
            difficulty=data.difficulty,
            time_taken=data.time_taken,
            steps=data.steps,
            level=data.level
        )

        db.add(new_score)

        player.total_score = (player.total_score or 0) + final_score

        if data.difficulty == "easy":
            player.easy_level_completed = max(player.easy_level_completed or 0, data.level)
        elif data.difficulty == "medium":
            player.medium_level_completed = max(player.medium_level_completed or 0, data.level)
        elif data.difficulty == "hard":
            player.hard_level_completed = max(player.hard_level_completed or 0, data.level)

        db.commit()
        db.refresh(player)
        db.refresh(new_score)

        return {"message": "Score Saved"}
    finally:
        db.close()


@router.get("/leaderboard")
def leaderboard():
    db: Session = SessionLocal()
    try:
        players = (
            db.query(Player)
            .order_by(Player.total_score.desc())
            .limit(10)
            .all()
        )

        return [
            {
                "username": player.username,
                "score": player.total_score
            }
            for player in players
        ]
    finally:
        db.close()


@router.get("/player-progress/{player_id}")
def player_progress(player_id: int):
    db: Session = SessionLocal()
    try:
        player = db.query(Player).filter(
            Player.id == player_id
        ).first()

        if not player:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="Player not found"
            )

        return {
            "easy": player.easy_level_completed,
            "medium": player.medium_level_completed,
            "hard": player.hard_level_completed
        }
    finally:
        db.close()


@router.get("/player-history/{player_id}")
def player_history(player_id: int):
    db: Session = SessionLocal()
    try:
        scores = db.query(Score).filter(
            Score.player_id == player_id
        ).order_by(
            Score.created_at.desc()
        ).all()

        return [
            {
                "difficulty": score.difficulty,
                "level": score.level,
                "score": score.score,
                "time_taken": score.time_taken,
                "date": score.created_at.strftime("%d %b %Y")
            }
            for score in scores
        ]
    finally:
        db.close()


@router.delete("/player-history/{player_id}")
def delete_history(player_id: int):
    db: Session = SessionLocal()
    try:
        db.query(Score).filter(
            Score.player_id == player_id
        ).delete()

        db.commit()

        return {"message": "History deleted successfully"}
    finally:
        db.close()