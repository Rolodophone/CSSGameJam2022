package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable

class TextureAssets: Disposable {
    private val textureAtlas = TextureAtlas(Gdx.files.internal("textures.atlas"))

    val background1 = loadAtlasRegion("background1")
    val background2 = loadAtlasRegion("background2")
    val background3 = loadAtlasRegion("background3")
    val background4 = loadAtlasRegion("background4")
    val background5 = loadAtlasRegion("background5")
    val barrier = loadAtlasRegion("barrier")
    val dialog_init = loadAtlasRegion("dialog_init")
    val dialog_fail = loadAtlasRegion("dialog_fail")
    val dialog_error = loadAtlasRegion("dialog_error")
    val dialog_crash = loadAtlasRegion("dialog_crash")
    val processor = loadAtlasRegion("processor")
    val platform = loadAtlasRegion("platform")
    val player1 = loadAtlasRegion("player1")
    val player2 = loadAtlasRegion("player2")
    val saw = loadAtlasRegion("saw")
    val led = loadAtlasRegion("led")

    private fun loadAtlasRegion(name: String): TextureAtlas.AtlasRegion {
        val atlasRegion = textureAtlas.findRegion(name)
        requireNotNull(atlasRegion) { "Region '$name' not found in texture atlas." }
        return atlasRegion
    }

    override fun dispose() {
        textureAtlas.dispose()
    }
}
