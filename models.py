from sqlalchemy import Column, Integer, String, Float, ForeignKey
from database import Base
from sqlalchemy import DateTime
from sqlalchemy.sql import func


class Player(Base):

    __tablename__ = "players"

    id = Column(Integer, primary_key=True, index=True)


    username = Column(String, unique=True)

    city = Column(String)
    password = Column(String, nullable=False)

    easy_level_completed = Column(
        Integer,
        default=0
    )

    medium_level_completed = Column(
        Integer,
        default=0
    )

    hard_level_completed = Column(
        Integer,
        default=0
    )

    total_score = Column(
        Integer,
        default=0
    )
    created_at = Column(
        DateTime(timezone=True),
        server_default=func.now()
    )



class Score(Base):

    __tablename__ = "scores"

    id = Column(Integer, primary_key=True, index=True)

    player_id = Column(Integer, ForeignKey("players.id"))

    score = Column(Integer)

    difficulty = Column(String)

    time_taken = Column(Float)

    steps = Column(Integer)

    level = Column(Integer)
    created_at = Column(
        DateTime(timezone=True),
        server_default=func.now()
    )