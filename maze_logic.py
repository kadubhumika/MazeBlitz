import random
from collections import deque

TRAP = 2


def select_difficulty(mode, level):
    if mode == "easy":
        rows = min(5 + (level - 1) // 3, 15)
        cols = rows
        timer = max(40, 60 - (level // 3))
        loop_chance = max(0.10, 0.22 - (level * 0.004))
        dead_end_factor = min(0.15 + (level * 0.01), 0.45)

    elif mode == "medium":
        rows = min(9 + (level - 1) // 2, 21)
        cols = rows
        timer = max(70, 120 - (level // 2))
        loop_chance = max(0.05, 0.14 - (level * 0.003))
        dead_end_factor = min(0.25 + (level * 0.012), 0.6)

    elif mode == "hard":
        rows = min(13 + (level - 1) // 2, 31)
        cols = rows
        timer = max(90, 180 - (level // 2))
        loop_chance = max(0.0, 0.08 - (level * 0.0025))
        dead_end_factor = min(0.35 + (level * 0.015), 0.75)

    else:
        return None

    if rows % 2 == 0:
        rows += 1
        cols += 1

    area = rows * cols
    density = min(0.015 + (level * 0.0006), 0.05)
    traps = max(2, int(area * density))

    return {
        "rows": rows,
        "cols": cols,
        "timer": timer,
        "traps": traps,
        "loop_chance": loop_chance,
        "dead_end_factor": dead_end_factor,
    }


def _carve_perfect_maze(rows, cols):
    maze = [[1 for _ in range(cols)] for _ in range(rows)]

    def carve(r, c):
        maze[r][c] = 0
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        random.shuffle(directions)
        for dr, dc in directions:
            nr, nc = r + dr * 2, c + dc * 2
            if 0 <= nr < rows and 0 <= nc < cols and maze[nr][nc] == 1:
                maze[r + dr][c + dc] = 0
                carve(nr, nc)

    carve(0, 0)
    return maze


def _add_loops(maze, start, end, loop_chance):
    rows, cols = len(maze), len(maze[0])
    sr, sc = start
    er, ec = end

    for r in range(1, rows - 1):
        for c in range(1, cols - 1):
            if maze[r][c] != 1:
                continue
            if random.random() >= loop_chance:
                continue
            if abs(r - sr) + abs(c - sc) <= 2:
                continue
            if abs(r - er) + abs(c - ec) <= 2:
                continue
            horizontal_open = maze[r][c - 1] == 0 and maze[r][c + 1] == 0
            vertical_open = maze[r - 1][c] == 0 and maze[r + 1][c] == 0
            if horizontal_open or vertical_open:
                maze[r][c] = 0


def _find_dead_ends(maze, exclude):
    rows, cols = len(maze), len(maze[0])
    dead_ends = []
    for r in range(rows):
        for c in range(cols):
            if maze[r][c] != 0 or (r, c) in exclude:
                continue
            open_neighbors = 0
            for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and maze[nr][nc] != 1:
                    open_neighbors += 1
            if open_neighbors == 1:
                dead_ends.append((r, c))
    return dead_ends


def _extend_dead_ends(maze, start, end, dead_end_factor):
    rows, cols = len(maze), len(maze[0])
    dead_ends = _find_dead_ends(maze, exclude={start, end})
    random.shuffle(dead_ends)

    extend_count = int(len(dead_ends) * dead_end_factor)

    for (r, c) in dead_ends[:extend_count]:
        branch_len = random.randint(2, 4)
        cr, cc = r, c
        for _ in range(branch_len):
            directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
            random.shuffle(directions)
            extended = False
            for dr, dc in directions:
                nr, nc = cr + dr, cc + dc
                if not (1 <= nr < rows - 1 and 1 <= nc < cols - 1):
                    continue
                if maze[nr][nc] != 1:
                    continue
                open_neighbors = 0
                for ddr, ddc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                    ar, ac = nr + ddr, nc + ddc
                    if 0 <= ar < rows and 0 <= ac < cols and maze[ar][ac] == 0:
                        open_neighbors += 1
                if open_neighbors != 1:
                    continue
                maze[nr][nc] = 0
                cr, cc = nr, nc
                extended = True
                break
            if not extended:
                break


def place_traps(maze, count, solution_path):
    rows, cols = len(maze), len(maze[0])
    solution_set = set(solution_path)

    candidates = [
        (r, c)
        for r in range(1, rows - 1)
        for c in range(1, cols - 1)
        if maze[r][c] == 0 and (r, c) not in solution_set
    ]
    random.shuffle(candidates)

    placed = 0
    for (r, c) in candidates:
        if placed >= count:
            break
        maze[r][c] = TRAP
        placed += 1

    return placed


def generate_maze(rows, cols, traps, loop_chance=0.08, dead_end_factor=0.3):
    start = (0, 0)
    end = (rows - 1, cols - 1)

    maze = _carve_perfect_maze(rows, cols)
    solution_path = solve_maze(maze, start, end)

    _add_loops(maze, start, end, loop_chance)
    _extend_dead_ends(maze, start, end, dead_end_factor)

    actual_traps = place_traps(maze, traps, solution_path or [])

    maze[start[0]][start[1]] = "S"
    maze[end[0]][end[1]] = "E"

    return {
        "maze": maze,
        "start": start,
        "end": end,
        "traps_placed": actual_traps,
    }


def solve_maze(maze, start, end):
    rows, cols = len(maze), len(maze[0])
    queue = deque([(start, [start])])
    visited = {start}
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

    while queue:
        (r, c), path = queue.popleft()
        if (r, c) == end:
            return path

        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols:
                cell_val = maze[nr][nc]
                if cell_val != 1 and (nr, nc) not in visited:
                    visited.add((nr, nc))
                    queue.append(((nr, nc), path + [(nr, nc)]))

    return None