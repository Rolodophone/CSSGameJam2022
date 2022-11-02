package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class TextureAssets {
    private val textureAtlas = TextureAtlas(Gdx.files.internal("textures.atlas"))

    val background = loadAtlasRegion("background")
    val barrier = loadAtlasRegion("barrier")
    val door = loadAtlasRegion("door")
    val platform = loadAtlasRegion("platform")
    val player = loadAtlasRegion("player")
    val saw = loadAtlasRegion("saw")

    private fun loadAtlasRegion(name: String): TextureAtlas.AtlasRegion {
        val atlasRegion = textureAtlas.findRegion(name)
        requireNotNull(atlasRegion) { "Region '$name' not found in texture atlas." }
        return atlasRegion
    }
}
