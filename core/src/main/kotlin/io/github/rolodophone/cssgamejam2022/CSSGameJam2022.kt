package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.box2d.createWorld
import ktx.graphics.use
import kotlin.math.min

class CSSGameJam2022 : KtxGame<KtxScreen>() {
	lateinit var textureAssets: TextureAssets

	lateinit var camera: OrthographicCamera
	lateinit var viewport: ScreenViewport

	lateinit var world: World
	lateinit var box2DDebugRenderer: Box2DDebugRenderer

	lateinit var engine: Engine

	lateinit var spriteBatch: SpriteBatch

	var dt = Float.POSITIVE_INFINITY

	override fun create() {
		textureAssets = TextureAssets()

		camera = OrthographicCamera(16f, 9f)
		viewport = ScreenViewport(camera)
		viewport.unitsPerPixel = 1/120f

		Box2D.init()
		world = createWorld(gravity = Vector2(0f, -10f))
		box2DDebugRenderer = Box2DDebugRenderer()

		engine = Engine()

		spriteBatch = SpriteBatch()

		addScreen(GameScreen(this))
		setScreen<GameScreen>()
	}

	override fun render() {
		clearScreen(0f ,0f, 0f)

		dt = min(Gdx.graphics.deltaTime, 0.1f)

		spriteBatch.use {
			it.draw(textureAssets.ktxLogo, 100f, 160f)
		}

		engine.update(dt)

		box2DDebugRenderer.render(world, camera.combined)
		world.step(dt, 6, 2)
	}
}

