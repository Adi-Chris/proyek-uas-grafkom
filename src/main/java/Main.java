import Engine.*;
import Engine.Object;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class Main {
    private Window window = new Window(1000,1000, "Hello World");
    ArrayList<Object> objects = new ArrayList<>();

    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(),window.getHeight());
    public void init(){
        window.init();
        GL.createCapabilities();
        camera.setPosition(0,0,0.5f);
        camera.setRotation((float)Math.toRadians(0.0f),(float)Math.toRadians(30.0f));

        objects.add(new Sphere(
            Arrays.asList(
                new ShaderProgram.ShaderModuleData(
                        "resources/shaders/scene.vert"
                        , GL_VERTEX_SHADER),
                new ShaderProgram.ShaderModuleData(
                        "resources/shaders/scene.frag"
                        , GL_FRAGMENT_SHADER)
            ),
            new ArrayList<>(),
            new Vector4f(0.0f,1.0f,1.0f,1.0f),
                0.0f,
                0.0f,
                0.0f,
                0.125f,
                0.125f,
                0.125f
        ));
//        objects.get(0).translateObject(0.5f,0.0f,0.0f);
        objects.get(0).scaleObject(0.01f,0.01f,0.01f);
    }
    public void input(){
        float move = 0.01f;
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveDown(move);
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveUp(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveLeft(move);
        }
        if (window.isKeyPressed(GLFW_KEY_T)) {
            camera.moveForward(move);
        }
        if (window.isKeyPressed(GLFW_KEY_G)) {
            camera.moveBackwards(move);
        }
        if(window.getMouseInput().isLeftButtonPressed()){
            Vector2f displayVector = window.getMouseInput().getDisplVec();
            camera.addRotation((float)Math.toRadians(displayVector.x * 0.1f),(float)Math.toRadians(displayVector.y * 0.1f));
        }
        if(window.getMouseInput().getScroll().y != 0){
            projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
            window.getMouseInput().setScroll(new Vector2f());
        }
    }

    public void loop(){
        while (window.isOpen()) {
            window.update();
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL.createCapabilities();
            input();

            for(Object object:objects){
                object.draw(camera,projection);
            }

            glDisableVertexAttribArray(0);
            glfwPollEvents();
        }
    }
    public void run() {
        init();
        loop();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public static void main(String[] args) {
        new Main().run();
    }
}