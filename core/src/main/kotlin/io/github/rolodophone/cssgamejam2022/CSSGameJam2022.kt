package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use

class CSSGameJam2022 : KtxGame<KtxScreen>() {
    lateinit var textureAssets: TextureAssets

    override fun create() {
        textureAssets = TextureAssets()

        addScreen(FirstScreen(this))
        setScreen<FirstScreen>()
    }
}

class FirstScreen(private val game: CSSGameJam2022) : KtxScreen {
    private val image = game.textureAssets.ktxLogo
    private val batch = SpriteBatch()

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {
            it.draw(image, 100f, 160f)
        }
    }

    override fun dispose() {
        batch.disposeSafely()
    }
}
