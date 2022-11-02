package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.sys.Box2DSys
import io.github.rolodophone.cssgamejam2022.sys.PlayerSys
import ktx.app.KtxScreen

class GameScreen(val game: CSSGameJam2022): KtxScreen {
	val entityPresets = EntityPresets(game)

	lateinit var player: Entity
	lateinit var door: Entity
	lateinit var background: Entity
	lateinit var barriers: List<Entity>
	lateinit var movingBarriers: List<Entity>
	lateinit var movingPlatforms: List<Entity>
	lateinit var platforms: List<Entity>
	lateinit var saws: List<Entity>

	lateinit var playerSys: PlayerSys
	lateinit var box2DSys: Box2DSys

	var currentLevel = 1
	var timeScheduled = 0L
	var funScheduled: (GameScreen.() -> Unit)? = null

	override fun show() {
		box2DSys = Box2DSys(game.world)
		game.engine.addSystem(box2DSys)

		levels[1].initOneOff(this)
		levels[1].init(this)
		resumeGame()
	}

	override fun render(delta: Float) {
		if (funScheduled != null && TimeUtils.millis() >= timeScheduled) {
			funScheduled?.invoke(this)
			funScheduled = null
		}

		if (player.getComp(BoxBodyComp.mapper).y < -0.7f) scheduleRestartLevel()
	}

	fun scheduleRestartLevel() {
		if (funScheduled == null) {
			timeScheduled = TimeUtils.millis() + 500L
			funScheduled = { restartLevel() }
			pauseGame()
		}
	}

	fun scheduleNextLevel() {
		if (funScheduled == null) {
			timeScheduled = TimeUtils.millis() + 500L
			funScheduled = { nextLevel() }
			pauseGame()
		}
	}

	fun restartLevel() {
		funScheduled = null
		for (i in 1..currentLevel) levels[i].init(this)
		resumeGame()
	}

	fun nextLevel() {
		funScheduled = null
		currentLevel++
		for (i in 1 until currentLevel) levels[i].init(this)
		levels[currentLevel].initOneOff(this)
		levels[currentLevel].init(this)
		resumeGame()
	}

	fun pauseGame() {
		game.engine.removeSystem(box2DSys)
	}

	fun resumeGame() {
		game.engine.addSystem(box2DSys)
	}
}