package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.GameInputProcessor;
import com.programmers.ui_elements.CardContainer;
import com.programmers.ui_elements.YesNoDialog;
import com.programmers.ui_elements.MyButton;

public abstract class GameScreen extends Stage implements Screen, InputProcessor {

    protected final int size;
    protected final int playersCount;

    protected final ScreenLoader screenLoader;
    protected final Difficulty difficulty;
    protected final Array<ModelInstance> instances;
    protected final AssetManager assetManager;

    protected final PerspectiveCamera camera;
    protected final Environment environment;
    protected final ModelBatch modelBatch;
    protected final GameInputProcessor gameInputProcessor;
    protected final InputMultiplexer multiplexer;

    private Dialog pauseMenu;
    private boolean isPauseMenuHidden = true;
    protected Field field;

    protected GameScreen(final ScreenLoader screenLoader, final Difficulty difficulty, final int playersCount) {
        this.screenLoader = screenLoader;
        this.difficulty = difficulty;
        this.playersCount = playersCount;

        size = difficulty == Difficulty.Hard ? 9 : 6;
        instances = new Array<>();
        assetManager = screenLoader.getAssetManager();
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0.8f, 0.2f));

        camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 100f;
        camera.update();

        gameInputProcessor = new GameInputProcessor(camera, this);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(new GestureDetector(gameInputProcessor));
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void addUI() {
        final Skin skin = ScreenLoader.getDefaultGdxSkin();

        final YesNoDialog yesNoDialog = new YesNoDialog
                ("Are you sure you want to return to main menu?", skin) {
            @Override
            public void call() {
                dispose();
                screenLoader.setScreen(screenLoader.getMainMenu());
            }
        };

        pauseMenu = new Dialog("PAUSE MENU", skin);
        pauseMenu.getContentTable().setFillParent(true);
        pauseMenu.setMovable(false);

        ImageTextButton returnButton =
                new MyButton("CONTINUE", screenLoader.getButtonStyle()) {
                    @Override
                    public void call() {
                        pauseMenu.hide();
                        isPauseMenuHidden = true;
                        gameInputProcessor.unlockCamera();
                    }
                };
        ImageTextButton settingsButton =
                new MyButton("SETTINGS", screenLoader.getButtonStyle()) {
                    @Override
                    public void call() {
                        screenLoader.setScreen(new SettingsScreen(screenLoader, GameScreen.this));
                    }
                };
        ImageTextButton mainMenuButton =
                new MyButton("QUIT ROOM", screenLoader.getButtonStyle()) {
                    @Override
                    public void call() {
                        yesNoDialog.show(GameScreen.this);
                    }
                };

        pauseMenu.getContentTable().pad(
                0.05f * Gdx.graphics.getHeight(), 0.05f * Gdx.graphics.getHeight(),
                0.025f * Gdx.graphics.getHeight(), 0.05f * Gdx.graphics.getHeight());

        pauseMenu.getContentTable().add(returnButton).space(0.05f * Gdx.graphics.getHeight()).row();
        pauseMenu.getContentTable().add(settingsButton).space(0.05f * Gdx.graphics.getHeight()).row();
        pauseMenu.getContentTable().add(mainMenuButton).space(0.05f * Gdx.graphics.getHeight());

        ImageTextButton toDialogButton = new MyButton("PAUSE MENU", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                if (isPauseMenuHidden) {
                    gameInputProcessor.lockCamera();
                    pauseMenu.show(GameScreen.this);
                    isPauseMenuHidden = false;
                }
            }
        };
        addActor(toDialogButton);
        toDialogButton.setPosition(
                Gdx.graphics.getWidth() - (Gdx.graphics.getHeight() * 0.02f),
                Gdx.graphics.getHeight() * 0.98f,
                Align.topRight);

        addCardWindows();
    }

    private void addAxises() {
        Vector3 start = new Vector3(0f, size / 2f, 0f), end = new Vector3();

        ModelBuilder modelBuilder1 = new ModelBuilder();
        modelBuilder1.begin();
        MeshPartBuilder builder1 = modelBuilder1.part("Y", 1, 3, new Material());
        builder1.setColor(Color.GREEN);
        end.set(start).add(new Vector3(Vector3.Y).scl(size / 3f));
        builder1.line(start.x, start.y, start.z, end.x, end.y, end.z);
        instances.add(new ModelInstance(modelBuilder1.end()));

        ModelBuilder modelBuilder2 = new ModelBuilder();
        modelBuilder2.begin();
        MeshPartBuilder builder2 = modelBuilder2.part("X", 1, 3, new Material());
        builder2.setColor(Color.RED);
        end.set(start).add(new Vector3(Vector3.X).scl(size / 3f));
        builder2.line(start.x, start.y, start.z, end.x, end.y, end.z);
        instances.add(new ModelInstance(modelBuilder2.end()));

        ModelBuilder modelBuilder3 = new ModelBuilder();
        modelBuilder3.begin();
        MeshPartBuilder builder3 = modelBuilder3.part("Z", 1, 3, new Material());
        builder3.setColor(Color.BLUE);
        end.set(start).add(new Vector3(Vector3.Z).scl(size / 3f));
        builder3.line(start.x, start.y, start.z, end.x, end.y, end.z);
        instances.add(new ModelInstance(modelBuilder3.end()));
    }

    protected void constructorEnd() {
        loadModels();
        gameInputProcessor.unlockCamera();
        addUI();
        addAxises();
        setCameraPosition();
    }

    public int getSize() {
        return size;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Array<ModelInstance> getInstances() {
        return instances;
    }

    protected abstract void setCameraPosition();

    protected abstract void addCardWindows();

    protected abstract void loadModels();

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        gameInputProcessor.cameraPosChange();
        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();

        act(Gdx.graphics.getDeltaTime());
        getBatch().setProjectionMatrix(camera.combined);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
        modelBatch.dispose();
        instances.clear();
        CardContainer.cardContainers.clear();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK && isPauseMenuHidden) {
            pauseMenu.show(this);
            isPauseMenuHidden = false;
            return true;
        }
        return false;
    }
}
