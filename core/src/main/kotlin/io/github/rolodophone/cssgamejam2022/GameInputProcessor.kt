package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor
import io.github.rolodophone.cssgamejam2022.sys.PlayerSys

class GameInputProcessor(
	private val playerSys: PlayerSys,
	private val gameScreen: GameScreen
): InputProcessor {
	override fun keyDown(keycode: Int): Boolean {
		//fullscreen control
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && keycode == Keys.F || keycode == Keys.F11) {
			if (Gdx.graphics.isFullscreen) {
				Gdx.graphics.setWindowedMode(1280, 720)
			}
			else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
			}
			return true
		}

		//quit shortcut
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && keycode == Keys.Q) {
			Gdx.app.exit()
			return true
		}

		//game
		if (keycode in setOf(Keys.A, Keys.D, Keys.W, Keys.R, Keys.SPACE, Keys.NUMPAD_0)) {
			when (keycode) {
				Keys.A -> playerSys.moveLeft()
				Keys.D -> playerSys.moveRight()
				Keys.W -> playerSys.jump()
				Keys.R -> gameScreen.restartLevel()
				Keys.SPACE -> gameScreen.continueDialog()
				Keys.NUMPAD_0 -> gameScreen.completeLevel()
			}

			return true
		}

		return false
	}

	override fun keyUp(keycode: Int): Boolean {
		//game
		if (keycode == Keys.A && !Gdx.input.isKeyPressed(Keys.D) ||
				keycode == Keys.D && !Gdx.input.isKeyPressed(Keys.A)) {
			playerSys.stop()
		}
		return false
	}

	override fun keyTyped(character: Char): Boolean {
		return false
	}

	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		return false
	}

	override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		return false
	}

	override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
		return false
	}

	override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
		return false
	}

	override fun scrolled(amountX: Float, amountY: Float): Boolean {
		return false
	}
}