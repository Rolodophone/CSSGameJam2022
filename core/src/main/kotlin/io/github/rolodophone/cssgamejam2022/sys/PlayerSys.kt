package io.github.rolodophone.cssgamejam2022.sys

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import io.github.rolodophone.cssgamejam2022.GameScreen
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.PlayerComp
import io.github.rolodophone.cssgamejam2022.getComp

const val PLAYER_MAX_SPEED_X = 7f

class PlayerSys(private val gameScreen: GameScreen, player: Entity): EntitySystem(20) {
	private val playerBody = player.getComp(BoxBodyComp.mapper).body
	private val playerComp = player.getComp(PlayerComp.mapper)

	override fun update(deltaTime: Float) {
		if (playerComp.movingLeft && playerComp.onGround) {
			playerBody.applyLinearImpulse(-0.2f, 0f, 0f, 0f, true)
		}
		if (playerComp.movingRight && playerComp.onGround) {
			playerBody.applyLinearImpulse(0.2f, 0f, 0f, 0f, true)
		}

		if (playerBody.linearVelocity.x < -PLAYER_MAX_SPEED_X) {
			playerBody.setLinearVelocity(-PLAYER_MAX_SPEED_X, playerBody.linearVelocity.y)
		}
		if (playerBody.linearVelocity.x > PLAYER_MAX_SPEED_X) {
			playerBody.setLinearVelocity(PLAYER_MAX_SPEED_X, playerBody.linearVelocity.y)
		}
	}

	fun moveLeft() {
		playerComp.movingRight = false
		playerComp.movingLeft = true
	}

	fun moveRight() {
		playerComp.movingLeft = false
		playerComp.movingRight = true
	}

	fun stop() {
		playerComp.movingLeft = false
		playerComp.movingRight = false
	}

	fun jump() {
		if (playerComp.onGround) {
			playerBody.applyLinearImpulse(0f, 10f, 0f, 0f, true)

			//extra push in direction of travel
			if (playerComp.movingLeft) playerBody.applyLinearImpulse(-1f, 0f, 0f, 0f, true)
			if (playerComp.movingRight) playerBody.applyLinearImpulse(1f, 0f, 0f, 0f, true)
		}
	}
}