package io.github.rolodophone.cssgamejam2022

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.rolodophone.cssgamejam2022.sys.RenderSys
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.box2d.createWorld
import kotlin.math.min

const val WORLD_WIDTH = 16f
const val WORLD_HEIGHT = 9f
const val WORLD_WIDTH_HALF = 8f
const val WORLD_HEIGHT_HALF = 4.5f

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
		Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)

		textureAssets = TextureAssets()

		camera = OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT)
		camera.translate(WORLD_WIDTH_HALF, WORLD_HEIGHT_HALF)
		camera.update()
		viewport = ScreenViewport(camera)
		viewport.unitsPerPixel = 1/120f

		spriteBatch = SpriteBatch()

		Box2D.init()
		world = createWorld(gravity = Vector2(0f, -20f))
		box2DDebugRenderer = Box2DDebugRenderer()

		engine = Engine()

		val renderSys = RenderSys(camera, spriteBatch)
		engine.addSystem(renderSys)

		val gameScreen = GameScreen(this)
		addScreen(gameScreen)
		setScreen<GameScreen>()
		gameScreen.restartLevel()
	}

	override fun render() {
		clearScreen(0f ,0f, 0f)

		dt = min(Gdx.graphics.deltaTime, 0.1f)

		engine.update(dt)

		box2DDebugRenderer.render(world, camera.combined)
		world.step(dt, 6, 2)
	}
}

