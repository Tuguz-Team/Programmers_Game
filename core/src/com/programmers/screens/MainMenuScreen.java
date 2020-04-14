package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.programmers.enums.Difficulty;

public class MainMenuScreen extends Stage implements Screen {

    private ScreenLoader screenLoader;
    //private VerticalGroup mainButtons;

    private Vector3 temp = new Vector3(); // временный вектор для "захвата" входных координат
    private SpriteBatch batch; // объект для отрисовки спрайтов нашей игры
    private OrthographicCamera camera; // область просмотра нашей игры

    private Texture startButtonTexture;
    private Texture exitButtonTexture;
    private Texture backGroundTexture;
    private Sprite startButtonSprite;
    private Sprite exitButtonSprite;
    private Sprite backGroundSprite;
    //Skin skin;

    private static float BUTTON_RESIZE_FACTOR = 800f; // задаём относительный размер
    private static float START_VERT_POSITION_FACTOR = 3f; // задаём позицию конпки start
    private static float EXIT_VERT_POSITION_FACTOR = 8f; // задаём позицию кнопки exit

    public MainMenuScreen(ScreenLoader screenLoader) {
        this.screenLoader = screenLoader;
        /*
        skin = new Skin();

        Gdx.input.setInputProcessor(this);
        mainButtons = new VerticalGroup();
        mainButtons.setFillParent(true);
        addActor(mainButtons);
        mainButtons.setDebug(true);
        mainButtons.addActor(new TextButton("START", skin));
        mainButtons.addActor(new TextButton("EXIT", skin));
        */

        // получаем размеры экрана устройства пользователя и записываем их в переменнные высоты и ширины
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
        // устанавливаем переменные высоты и ширины в качестве области просмотра нашей игры
        camera = new OrthographicCamera(width,height);
        // этим методом мы центруем камеру на половину высоты и половину ширины
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        // инициализируем текстуры и спрайты
        startButtonTexture = new Texture(Gdx.files.internal("start_button.jpg"));
        exitButtonTexture = new Texture(Gdx.files.internal("exit_button.jpg"));
        backGroundTexture = new Texture(Gdx.files.internal("menu_background.jpg"));
        startButtonSprite = new Sprite(startButtonTexture);
        exitButtonSprite = new Sprite(exitButtonTexture);
        backGroundSprite = new Sprite(backGroundTexture);
        // устанавливаем размер и позиции
        startButtonSprite.setSize(startButtonSprite.getWidth() *(width/BUTTON_RESIZE_FACTOR), startButtonSprite.getHeight()*(width/BUTTON_RESIZE_FACTOR));
        exitButtonSprite.setSize(exitButtonSprite.getWidth() *(width/BUTTON_RESIZE_FACTOR), exitButtonSprite.getHeight()*(width/BUTTON_RESIZE_FACTOR));
        backGroundSprite.setSize(width,height);
        startButtonSprite.setPosition((width/2f -startButtonSprite.getWidth()/2) , width/START_VERT_POSITION_FACTOR);
        exitButtonSprite.setPosition((width/2f -exitButtonSprite.getWidth()/2) , width/EXIT_VERT_POSITION_FACTOR);
    }

    void handleTouch(){
        // Проверяем были ли касание по экрану?
        if(Gdx.input.justTouched()) {
            // Получаем координаты касания и устанавливаем эти значения в временный вектор
            temp.set(Gdx.input.getX(),Gdx.input.getY(), 0);
            // получаем координаты касания относительно области просмотра нашей камеры
            camera.unproject(temp);
            float touchX = temp.x;
            float touchY= temp.y;
            // обработка касания по кнопке Stare
            if((touchX>=startButtonSprite.getX()) && touchX<= (startButtonSprite.getX()+startButtonSprite.getWidth()) && (touchY>=startButtonSprite.getY()) && touchY<=(startButtonSprite.getY()+startButtonSprite.getHeight()) ){
                screenLoader.setScreen(new GameScreen(screenLoader, Difficulty.Hard, 4));
            }
            // обработка касания по кнопке Exit
            else if((touchX>=exitButtonSprite.getX()) && touchX<= (exitButtonSprite.getX()+exitButtonSprite.getWidth()) && (touchY>=exitButtonSprite.getY()) && touchY<=(exitButtonSprite.getY()+exitButtonSprite.getHeight()) ){
                screenLoader.setScreen(new GameScreen(screenLoader, Difficulty.Easy, 4));
                //Gdx.app.exit(); /*выход из приложения*/
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Очищаем экран
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //act(Gdx.graphics.getDeltaTime());
        //draw();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined); // устанавливаем в экземпляр spritebatch вид с камеры (области просмотра)

        //отрисовка игровых объектов
        batch.begin();
        backGroundSprite.draw(batch);
        startButtonSprite.draw(batch);
        exitButtonSprite.draw(batch);
        handleTouch();
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
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
        //super.dispose();
        startButtonTexture.dispose();
        exitButtonTexture.dispose();
        backGroundTexture.dispose();
        batch.dispose();
    }
}
