import random
from collections import deque


def select_difficulty(mode, level):
    if mode == "easy":
        rows = 5 + (level // 20) * 2
        cols = rows
        timer = 60 + level
    elif mode == "medium":
        rows = 10 + (level // 20) * 2
        cols = rows
        timer = 120 + level
    elif mode == "hard":
        rows = 15 + (level // 20) * 3
        cols = rows
        timer = 180 + level
    else:
        return None

    # Ensure odd dimensions for proper maze generation
    if rows % 2 == 0:
        rows += 1
        cols += 1

    return {
        "rows": rows,
        "cols": cols,
        "timer": timer
    }


def generate_maze(rows, cols):
    # Initialize grid with walls (1)
    maze = [[1 for _ in range(cols)] for _ in range(rows)]
    start = (0, 0)
    end = (rows - 1, cols - 1)

    def carve_path(r, c):
        maze[r][c] = 0
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        random.shuffle(directions)

        for dr, dc in directions:
            nr = r + dr * 2
            nc = c + dc * 2
            if 0 <= nr < rows and 0 <= nc < cols:
                if maze[nr][nc] == 1:
                    maze[r + dr][c + dc] = 0
                    carve_path(nr, nc)

    carve_path(0, 0)

    # Set start and end as integers to prevent solver crash
    maze[start[0]][start[1]] = "S"
    maze[end[0]][end[1]] = "E"

    return {
        "maze": maze,
        "start": start,
        "end": end
    }


def solve_maze(maze, start, end):
    rows = len(maze)
    cols = len(maze[0])
    queue = deque([(start, [start])])
    visited = set()
    visited.add(start)

    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

    while queue:
        (r, c), path = queue.popleft()
        if (r, c) == end:
            return path

        for dr, dc in directions:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < rows and 0 <= nc < cols:
                # Allow the solver to pass through "E" (the end node)
                cell_val = maze[nr][nc]
                if cell_val != 1 and (nr, nc) not in visited:
                    visited.add((nr, nc))
                    queue.append(((nr, nc), path + [(nr, nc)]))

    return None

