package io.github.rolodophone.cssgamejam2022.sys

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import io.github.rolodophone.cssgamejam2022.comp.BoxBodyComp
import io.github.rolodophone.cssgamejam2022.comp.TextureComp
import io.github.rolodophone.cssgamejam2022.getComp
import ktx.ashley.allOf
import ktx.graphics.use

class RenderSys(
		private val camera: OrthographicCamera,
		private val spriteBatch: SpriteBatch
): SortedIteratingSystem(
	allOf(BoxBodyComp::class, TextureComp::class).get(),
	compareBy { it.getComp(TextureComp.mapper).z },
	0
) {
	private val sprite = Sprite()

	override fun update(deltaTime: Float) {
		camera.update()
		spriteBatch.use(camera.combined) {
			super.update(deltaTime)
		}
	}

	override fun processEntity(entity: Entity, deltaTime: Float) {
		val boxBodyComp = entity.getComp(BoxBodyComp.mapper)
		val textureComp = entity.getComp(TextureComp.mapper)

		sprite.setBounds(boxBodyComp.x, boxBodyComp.y, boxBodyComp.width, boxBodyComp.height)
		sprite.setRegion(textureComp.texture)
		sprite.rotation = MathUtils.radDeg * boxBodyComp.body.angle
		sprite.draw(spriteBatch)
	}
}