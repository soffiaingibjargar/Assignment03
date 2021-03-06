package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {
    public Camera camera;
    private boolean alive;
    private boolean flashlight;
    private float fov;
    public Score score;
    

    Player(float fov){
        camera = new Camera();
        camera.look(new Point3D(-1.0f, 0.08f, -1.0f), new Point3D(0,0.0f,0), new Vector3D(0,0.8f,0));
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        this.fov = fov;
        flashlight = false;
        alive = true;
        score = new Score(camera);
    }


    public boolean isAlive(){
        return alive;
    }

    public void flipAlive(){
        alive = !alive;
    }

    private void changeFov(float fov, float deltaTime){
    	if(fov < 0 && this.fov > 10.0f){ this.fov += fov * deltaTime; }
    	if(fov > 0 && this.fov < 80.0f){ this.fov += fov * deltaTime; }
    }

    public float getFov(){
        return fov;
    }

    public void update(float deltaTime){
        //do all updates to the game

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.rotateY(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.rotateY(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.pitch(-90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.pitch(90.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.slide(-2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.slide(2.0f * deltaTime, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.walk(2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.walk(-2.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            changeFov(-20.0f, deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.G)) {
            changeFov(20.0f, deltaTime);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            flashlight = !flashlight;
        }

    }

    public void display(Shader shader){
        if (flashlight){
            shader.setLightDiffuse(1.0f, 1.0f,1.0f,1.0f, 1);
        } else {
            shader.setLightDiffuse(0.0f, 0.0f,0.0f,1.0f, 1);
        }
        flashlight(shader);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.setEyePosition(camera.eye.x, camera.eye.y, camera.eye.z, 1.0f);
        camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
        shader.setViewMatrix(camera.getViewMatrix());
        shader.setProjectionMatrix(camera.getProjectionMatrix());
    }

    public void flashlight(Shader shader){
        shader.setLightPosition(camera.eye.x, camera.eye.y, camera.eye.z, 1.0f, 1);
        shader.setLightDirection(-camera.n.x, -camera.n.y, -camera.n.z, 1, 1);
    }

    public void displayMap(Shader shader){
        shader.setMaterialDiffuse(1.0f, 0.3f, 0.1f, 1.0f);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(camera.eye.x, camera.eye.y, camera.eye.z);
        ModelMatrix.main.addScale(0.25f, 0.25f, 0.25f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere();
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(-camera.u.x, 0, -camera.u.z);
        ModelMatrix.main.addScale(0.55f, 0.55f, 0.55f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere();
        ModelMatrix.main.popMatrix();
        ModelMatrix.main.addTranslation(camera.u.x, 0, camera.u.z);
        ModelMatrix.main.addScale(0.55f, 0.55f, 0.55f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        SphereGraphic.drawSolidSphere();
        ModelMatrix.main.popMatrix();

    }
}
