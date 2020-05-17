package com.programmers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class SkyBox implements Disposable {

    private Matrix4 transformation;
    private ShaderProgram program;
    private int u_projTrans;
    private int u_worldTrans;

    private Texture[] textures;

    private Mesh quad;
    private boolean invert = false;

    public SkyBox(Pixmap positiveX, Pixmap negativeX, Pixmap positiveY,
                  Pixmap negativeY, Pixmap positiveZ, Pixmap negativeZ) {
        this(new Texture(positiveZ), new Texture(negativeZ), new Texture(negativeX),
                new Texture(positiveX), new Texture(positiveY), new Texture(negativeY));
    }

    public SkyBox(Texture positiveX, Texture negativeX, Texture positiveY,
                  Texture negativeY, Texture positiveZ, Texture negativeZ) {
        textures = new Texture[] {
                positiveZ, negativeZ, negativeX,
                positiveX, positiveY, negativeY
        };
        init();
    }

    public SkyBox(FileHandle positiveX, FileHandle negativeX, FileHandle positiveY,
                  FileHandle negativeY, FileHandle positiveZ, FileHandle negativeZ) {
        this(new Pixmap(positiveX), new Pixmap(negativeX), new Pixmap(positiveY),
                new Pixmap(negativeY), new Pixmap(positiveZ), new Pixmap(negativeZ));
    }

    public SkyBox(Pixmap cubeMap) {
        int w = cubeMap.getWidth();
        int h = cubeMap.getHeight();

        Pixmap[] data = new Pixmap[6];
        for(int i = 0; i < 6; i++) {
            data[i] = new Pixmap(w / 4, h / 3, Pixmap.Format.RGB888);
        }
        for(int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (x <= w / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[1].drawPixel(x, y - h / 3, cubeMap.getPixel(x, y));
                if (x >= w / 4 && x <= w / 2 + 1 && y <= h / 3)
                    data[2].drawPixel(x - w / 4, y, cubeMap.getPixel(x, y));
                if (x >= w / 4 && x <= w / 2 && y >= h / 3 && y <= h * 2 / 3)
                    data[4].drawPixel(x - w / 4, y - h / 3, cubeMap.getPixel(x, y));
                if (x >= w / 4 && x <= w / 2 && y >= h * 2 / 3)
                    data[3].drawPixel(x - w / 4, y - h * 2 / 3, cubeMap.getPixel(x, y));
                if (x >= w / 2 && x <= w * 3 / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[0].drawPixel(x - w / 2, y - h / 3, cubeMap.getPixel(x, y));
                if (x >= w * 3 / 4 && y >= h / 3 && y <= h * 2 / 3)
                    data[5].drawPixel(x - w * 3 / 4, y - h / 3, cubeMap.getPixel(x, y));
            }
        }

        textures = new Texture[6];

        textures[0] = new Texture(data[4]);
        textures[1] = new Texture(data[5]);

        textures[2] = new Texture(data[1]);
        textures[3] = new Texture(data[0]);

        textures[4] = new Texture(data[2]);
        textures[5] = new Texture(data[3]);

        for(int i = 0; i < 6; i++) {
            data[i].dispose();
            data[i] = null;
        }

        init();
    }

    public SkyBox(FileHandle cubeMap) {
        this(new Pixmap(cubeMap));
    }

    private Mesh createTexturedQuad() {
        Mesh quad = new Mesh(true, 4,
                6, VertexAttribute.Position(),
                new VertexAttribute(
                        VertexAttributes.Usage.TextureCoordinates,
                        2, "a_texCoord0"
                )
        );
        quad.setVertices(new float[] {-1f, -1f, 0, 0, 1, 1f, -1f, 0, 1, 1,
                1f, 1f, 0, 1, 0, -1f, 1f, 0, 0, 0});
        quad.setIndices(new short[] {0, 1, 2, 2, 3, 0});
        return quad;
    }

    public void setInvert(boolean enable) {
        invert = enable;
    }

    private void init() {
        String vertexShader = " attribute vec4 a_position; " +
                " attribute vec2 a_texCoord0; " +
                " varying vec2 v_texCoord; " +
                " uniform mat4 u_worldTrans; " +
                " uniform mat4 u_projTrans; " +
                " void main() " +
                " {  " +
                "   gl_Position = u_projTrans * u_worldTrans * vec4(a_position);     " +
                "   v_texCoord = a_texCoord0;    " +
                " } ";
        String fragmentShader = " #ifdef GL_ES \n" +
                " precision mediump float; \n" +
                " #endif \n" +
                " uniform sampler2D s_diffuse; " +
                " varying vec2 v_texCoord; " +
                " void main() " +
                " { " +
                "   gl_FragColor = texture2D( s_diffuse, v_texCoord );   " +
                " } ";
        program = new ShaderProgram(vertexShader, fragmentShader);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        else
            Gdx.app.log("shader", "shader compiled successfully!");
        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");

        transformation = new Matrix4();
        quad = createTexturedQuad();
    }


    public void render(Camera camera) {
        Gdx.graphics.getGL20().glCullFace(GL20.GL_BACK);

        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);

        //front
        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.translate(0, 0, -1);
        if (invert)
            transformation.rotate(Vector3.Y, 180);
        program.setUniformMatrix(u_worldTrans, transformation);
        textures[0].bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        //left
        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.Y, 90);
        transformation.translate(0, 0, -1);
        if (invert)
            transformation.rotate(Vector3.Y, 180);
        program.setUniformMatrix(u_worldTrans, transformation);
        textures[ invert ? 3 : 2].bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        //right
        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.Y, -90);
        transformation.translate(0, 0, -1);
        if (invert)
            transformation.rotate(Vector3.Y, 180);
        program.setUniformMatrix(u_worldTrans, transformation);
        textures[invert ? 2 : 3].bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        //bottom
        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.X, -90);
        transformation.translate(0, 0, -1);
        if (invert)
            transformation.rotate(Vector3.Y, 180);
        program.setUniformMatrix(u_worldTrans, transformation);
        textures[5].bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        //top
        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.X, 90);
        transformation.translate(0, 0, -1);
        if (invert)
            transformation.rotate(Vector3.Y, 180);
        program.setUniformMatrix(u_worldTrans, transformation);
        textures[4].bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        //back
        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.Y, 180);
        transformation.translate(0, 0, -1);
        if (invert)
            transformation.rotate(Vector3.Y, 180);
        program.setUniformMatrix(u_worldTrans, transformation);
        textures[1].bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        program.end();
    }

    @Override
    public void dispose() {
        program.dispose();
        quad.dispose();
        for(int i=0; i<6; i++) {
            textures[i].dispose();
            textures[i]=null;
        }
    }
}
