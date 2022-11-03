package io.github.rolodophone.cssgamejam2022

import box2dLight.RayHandler
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.github.rolodophone.cssgamejam2022.sys.DebugSys
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
	lateinit var musicManager: MusicManager

	lateinit var camera: OrthographicCamera
	lateinit var viewport: ScreenViewport

	lateinit var world: World
	lateinit var box2DDebugRenderer: Box2DDebugRenderer

	lateinit var rayHandler: RayHandler

	lateinit var engine: Engine
	lateinit var debugSys: DebugSys
	lateinit var renderSys: RenderSys

	lateinit var spriteBatch: SpriteBatch

	var dt = Float.POSITIVE_INFINITY

	override fun create() {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)

		textureAssets = TextureAssets()
		musicManager = MusicManager()

		camera = OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT)
		camera.translate(WORLD_WIDTH_HALF, WORLD_HEIGHT_HALF)
		camera.update()
		viewport = ScreenViewport(camera)
		viewport.unitsPerPixel = 1/120f

		spriteBatch = SpriteBatch()

		Box2D.init()
		world = createWorld(gravity = Vector2(0f, -20f))
		box2DDebugRenderer = Box2DDebugRenderer()

		rayHandler = RayHandler(world)
		rayHandler.setAmbientLight(0.3f)
		rayHandler.setBlurNum(3)

		engine = Engine()
		debugSys = DebugSys(this)
		renderSys = RenderSys(camera, spriteBatch, rayHandler)
//		engine.addSystem(debugSys)
		engine.addSystem(renderSys)

		musicManager.play()

		val gameScreen = GameScreen(this)
		addScreen(gameScreen)
		setScreen<GameScreen>()
	}

	override fun render() {
		clearScreen(0f ,0f, 0f)

		dt = min(Gdx.graphics.deltaTime, 0.1f)

		engine.update(dt)
		currentScreen.render(dt)
	}

	override fun dispose() {
		textureAssets.dispose()
		musicManager.dispose()
		world.dispose()
		box2DDebugRenderer.dispose()
		spriteBatch.dispose()
	}
}

