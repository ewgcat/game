package com.lishuaihua.game.controller
sealed class GameState(open val score: Long) {

    object Start : GameState(0)

    data class Over(override val score: Long) : GameState(score)

    data class Score(override val score: Long) : GameState(score)

}