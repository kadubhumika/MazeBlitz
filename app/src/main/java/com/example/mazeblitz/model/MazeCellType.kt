package com.example.mazeblitz.model

import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive

sealed class MazeCell {
    object Path : MazeCell()
    object Wall : MazeCell()
    object Trap : MazeCell()
    object Start : MazeCell()
    object End : MazeCell()

    companion object {
        fun fromRaw(value: Any?): MazeCell = when (value) {
            is String -> when (value) {
                "S" -> Start
                "E" -> End
                else -> Path
            }
            is Number -> when (value.toInt()) {
                1 -> Wall
                2 -> Trap
                else -> Path
            }
            else -> Path
        }
    }
}

val mazeCellDeserializer = JsonDeserializer<MazeCell> { json, _, _ ->
    val primitive = json.asJsonPrimitive
    when {
        primitive.isString -> MazeCell.fromRaw(primitive.asString)
        primitive.isNumber -> MazeCell.fromRaw(primitive.asDouble)
        else -> MazeCell.Path
    }
}