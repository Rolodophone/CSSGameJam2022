package io.github.rolodophone.cssgamejam2022

import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(private val game: CSSGameJam2022) : KtxScreen {
	override fun show() {
		game.engine.entity {
			with<BoxBodyComp> {
				init(game.world, 8f, 4.5f, .5f, .5f)
			}
		}
	}
}