package com.programmers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class SkyBox implements Disposable {

    private final Matrix4 transformation;
    private final ShaderProgram program;
    private final Mesh quad;

    private final int u_projTrans;
    private final int u_worldTrans;

    private final Texture bottomTexture;
    private final Texture leftTexture;
    private final Texture rightTexture;
    private final Texture frontTexture;
    private final Texture backTexture;

    public SkyBox(Texture rightTexture, Texture leftTexture, Texture bottomTexture,
                  Texture frontTexture, Texture backTexture) {
        this.backTexture = backTexture;
        this.bottomTexture = bottomTexture;
        this.frontTexture = frontTexture;
        this.leftTexture = leftTexture;
        this.rightTexture = rightTexture;
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
        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");

        transformation = new Matrix4();
        quad = createTexturedQuad();
    }

    private Mesh createTexturedQuad() {
        Mesh quad = new Mesh(true, 4,
                6, VertexAttribute.Position(),
                new VertexAttribute(
                        VertexAttributes.Usage.TextureCoordinates,
                        2, "a_texCoord0")
        );
        quad.setVertices(new float[] {-1f, -1f, 0, 0, 1, 1f, -1f, 0, 1, 1,
                1f, 1f, 0, 1, 0, -1f, 1f, 0, 0, 0});
        quad.setIndices(new short[] {0, 1, 2, 2, 3, 0});
        return quad;
    }

    public void render(Camera camera) {
        Gdx.graphics.getGL20().glCullFace(GL20.GL_BACK);

        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);

        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.translate(0, 0, -1);
        program.setUniformMatrix(u_worldTrans, transformation);
        frontTexture.bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.Y, 90);
        transformation.translate(0, 0, -1);
        program.setUniformMatrix(u_worldTrans, transformation);
        leftTexture.bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.Y, -90);
        transformation.translate(0, 0, -1);
        program.setUniformMatrix(u_worldTrans, transformation);
        rightTexture.bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.X, -90);
        transformation.translate(0, 0, -1);
        program.setUniformMatrix(u_worldTrans, transformation);
        bottomTexture.bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        transformation.idt();
        transformation.translate(camera.position.x, camera.position.y, camera.position.z);
        transformation.rotate(Vector3.Y, 180);
        transformation.translate(0, 0, -1);
        program.setUniformMatrix(u_worldTrans, transformation);
        backTexture.bind(0);
        program.setUniformi("s_diffuse", 0);
        quad.render(program, GL20.GL_TRIANGLES);

        program.end();
    }

    @Override
    public void dispose() {
        program.dispose();
        quad.dispose();
        backTexture.dispose();
        bottomTexture.dispose();
        frontTexture.dispose();
        leftTexture.dispose();
        rightTexture.dispose();
    }
}
