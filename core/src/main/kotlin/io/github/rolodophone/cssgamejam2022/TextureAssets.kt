package io.github.rolodophone.cssgamejam2022

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class TextureAssets {
    private val textureAtlas = TextureAtlas(Gdx.files.internal("textures.atlas"))

    val ktxLogo = loadAtlasRegion("logo")

    private fun loadAtlasRegion(name: String): TextureAtlas.AtlasRegion {
        val atlasRegion = textureAtlas.findRegion(name)
        requireNotNull(atlasRegion) { "Region '$name' not found in texture atlas." }
        return atlasRegion
    }
}
