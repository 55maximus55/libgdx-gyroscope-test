package ru.maximus.gyrofly

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxGame
import ktx.assets.toInternalFile
import ktx.async.enableKtxCoroutines
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin
import ktx.style.*
import ru.maximus.gyrofly.screens.GameScreen

/**
 * [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.
 */
class App : KtxGame<Screen>() {

    companion object {
        val context = Context()
    }

    override fun create() {
        enableKtxCoroutines(asynchronousExecutorConcurrencyLevel = 1)
        context.register {
            bindSingleton(TextureAtlas("ui/skin.atlas"))
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton<Viewport>(ScreenViewport())
            bindSingleton(Stage(inject(), inject()))
            bindSingleton(createSkin(inject()))
            bindSingleton(this@App)
            Scene2DSkin.defaultSkin = inject()

            bindSingleton(GameScreen())
        }
        Gdx.input.inputProcessor = context.inject<Stage>()

        addScreen(context.inject<GameScreen>())
        setScreen<GameScreen>()
    }

    override fun render() {
        super.render()
        context.inject<Stage>().apply {
            act(Gdx.graphics.deltaTime)
            draw()
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        context.inject<Stage>().viewport.update(width, height, true)
    }

    fun createSkin(atlas: TextureAtlas): Skin = skin(atlas) { skin ->
        add(defaultStyle, FreeTypeFontGenerator("Roboto-Regular.ttf".toInternalFile()).generateFont(
                FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                    size = 24
                }
        ))
        label {
            font = skin[defaultStyle]
        }
        textField {
            font = skin[defaultStyle]
            fontColor = Color.WHITE
            disabledFontColor = Color.DARK_GRAY
            cursor = skin["dot"]
        }
        textButton {
            font = skin[defaultStyle]
            fontColor = Color.GREEN
            downFontColor = Color.BLUE
        }
        scrollPane {

        }
        slider {
            knob = skin["knob-v"]
            background = skin["line-h"]
        }
    }
}