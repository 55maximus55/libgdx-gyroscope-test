package ru.maximus.gyrofly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.RayHandler;

public class GameScreen implements Screen {

    Viewport viewport = new ScreenViewport();
    Stage stage = new Stage(viewport);
    SpriteBatch batch = new SpriteBatch();

    Texture texture = new Texture("test.png");
    float rotation = 0f;
    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();
    Vector2 calibration = new Vector2();

    Label.LabelStyle style;

    Label labelGyroX;
    Label labelGyroY;
    Label labelGyroZ;

    World world = new World(new Vector2(0, 0), true);
    RayHandler rayHandler = new RayHandler(world);

    Body body;

    @Override
    public void show() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        style = new LabelStyle();
        style.font = font;
        style.fontColor = Color.RED;


        labelGyroX = new Label("Test", style);
        labelGyroY = new Label("", style);
        labelGyroZ = new Label("", style);


        Table tableGyro = new Table();
        tableGyro.setFillParent(true);
        tableGyro.left();
        tableGyro.add(labelGyroX).pad(20f).row();
        tableGyro.add(labelGyroY).pad(20f).row();
        tableGyro.add(labelGyroZ).pad(20f).row();


        stage.addActor(tableGyro);

        createBody();
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(texture, body.getPosition().x, body.getPosition().y, 0, 0, texture.getWidth(), texture.getHeight(), 1,
                1, body.getAngle(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        batch.end();

        stage.act();
        stage.draw();

        labelGyroX.setText(Float.toString(Gdx.input.getGyroscopeX()));
        labelGyroY.setText(Float.toString(Gdx.input.getGyroscopeY()));
        labelGyroZ.setText(Float.toString(Gdx.input.getGyroscopeZ()));


        rotation -= Gdx.input.getGyroscopeZ();
        body.setTransform(body.getPosition(), rotation);


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }
}
