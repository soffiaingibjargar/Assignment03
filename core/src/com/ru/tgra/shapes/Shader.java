package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

/**
 * Created by andri on 10/13/15.
 */
public class Shader {
    private int renderingProgramID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int positionLoc;
    private int normalLoc;
    private int globalAmbientLoc;
    private int eyePosLoc;

    private int lightPosLoc[];
    private int lightDirLoc[];
    private int lightFocLoc[];
    private int lightDifLoc[];
    private int constantAttLoc[];
    private int linearAttLoc[];
    private int quadraticAttLoc[];

    private int materialDifLoc;
    private int materialShineLoc;
    private int materialSpecularLoc;
    private int materialEmiLoc;

    private int modelMatrixLoc;
    private int viewMatrixLoc;
    private int projectionMatrixLoc;


    /*
     * Initializes the vert and fragment shader.  We are mainly using the fragment shader for the light though.
     */
    Shader(){
        //Arrays to have multiple lights without needing to add functions
        lightPosLoc = new int[2];
        lightDirLoc = new int[2];
        lightFocLoc = new int[2];
        lightDifLoc = new int[2];
        constantAttLoc = new int[2];
        linearAttLoc = new int[2];
        quadraticAttLoc = new int[2];
        String vertexShaderString;
        String fragmentShaderString;

        vertexShaderString = Gdx.files.internal("shaders/fragmentLighting3D.vert").readString();
        fragmentShaderString = Gdx.files.internal("shaders/fragmentLighting3D.frag").readString();

        vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
        Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

        Gdx.gl.glCompileShader(vertexShaderID);
        Gdx.gl.glCompileShader(fragmentShaderID);

        Gdx.gl.glGetError();	// Use glGetShadeGetInfoLoc for more detailed errors.

        renderingProgramID = Gdx.gl.glCreateProgram();

        Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
        Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);

        Gdx.gl.glLinkProgram(renderingProgramID);

        positionLoc = Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
        Gdx.gl.glEnableVertexAttribArray(positionLoc);

        normalLoc = Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
        Gdx.gl.glEnableVertexAttribArray(normalLoc);

        modelMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
        viewMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
        projectionMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

        eyePosLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");
        materialDifLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
        materialShineLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");
        materialSpecularLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
        globalAmbientLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");
        materialEmiLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");

        lightPosLoc[0] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition");
        lightDifLoc[0] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDiffuse");
        lightDirLoc[0] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDirection");
        lightFocLoc[0] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightFocus");
        constantAttLoc[0] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_constantAttenuation");
        linearAttLoc[0] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_linearAttenuation");
        quadraticAttLoc[0] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_quadraticAttenuation");

        lightPosLoc[1] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition2");
        lightDifLoc[1] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDiffuse2");
        lightDirLoc[1] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDirection2");
        lightFocLoc[1] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightFocus2");
        constantAttLoc[1] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_constantAttenuation2");
        linearAttLoc[1] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_linearAttenuation2");
        quadraticAttLoc[1] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_quadraticAttenuation2");

        Gdx.gl.glUseProgram(renderingProgramID);
    }

    public void setEyePosition(float x, float y, float z, float w){
        Gdx.gl.glUniform4f(eyePosLoc, x, y, z, w);
    }

    public void setShininess(float shine){
        Gdx.gl.glUniform1f(materialShineLoc, shine);
    }

    public void setMaterialSpecular(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialSpecularLoc, r, g, b, a);
    }

    public void setGlobalAmbient(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(globalAmbientLoc, r, g, b, a);
    }

    public void setMaterialDiffuse(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialDifLoc, r, g, b, a);
    }

    public void setMaterialEmission(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialEmiLoc, r, g, b, a);
    }

    public void setLightPosition(float x, float y, float z, float w, int num){
        Gdx.gl.glUniform4f(lightPosLoc[num], x, y, z, w);
    }

    public void setLightDirection(float x, float y, float z, float w, int num){
        Gdx.gl.glUniform4f(lightDirLoc[num], x, y, z, w);
    }

    public void setFocus(float focus, int num){
        Gdx.gl.glUniform1f(lightFocLoc[num], focus);
    }

    public void setConstantAtt(float att, int num){
        Gdx.gl.glUniform1f(constantAttLoc[num], att);
    }

    public void setLinearAtt(float att, int num){
        Gdx.gl.glUniform1f(linearAttLoc[num], att);
    }

    public void setQuadraticAtt(float att, int num){
        Gdx.gl.glUniform1f(quadraticAttLoc[num], att);
    }

    public void setLightDiffuse(float r, float g, float b, float a, int num){
        Gdx.gl.glUniform4f(lightDifLoc[num], r, g, b, a);
    }

    public int getVertexPointer(){
        return positionLoc;
    }

    public int getNormalPointer(){
        return normalLoc;
    }

    public void setModelMatrix(FloatBuffer matrix){
        Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
    }


    public void setViewMatrix(FloatBuffer matrix){
        Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
    }


    public void setProjectionMatrix(FloatBuffer matrix){
        Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
    }
}
