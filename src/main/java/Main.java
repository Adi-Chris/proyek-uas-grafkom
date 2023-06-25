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
    private Window window = new Window(1000, 1000, "Window");
    ArrayList<Object> objects = new ArrayList<>();
    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());
    SkyboxRenderer skyboxRenderer;
    Camera thirdPersonCamera = new Camera();
    Camera firstPersonCamera = new Camera();
    int pilihanKamera = 1;
    int count = 0;
    public void init() {
        window.init();
        GL.createCapabilities();
        thirdPersonCamera.addRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(90.0f));
        firstPersonCamera.addRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(90.0f));
        this.skyboxRenderer = new SkyboxRenderer(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/skyboxVertexShader.vert"
                                , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/skyboxFragmentShader.frag"
                                , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.0f, 1.0f, 0.0f, 1.0f)
        );
        camera.setPosition(0.04f, 0.45f, 0.055f);
        camera.setRotation((float) Math.toRadians(90.0f), (float) Math.toRadians(0.0f));

        // environment
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
                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),
                0.0f,
                0.0f,
                0.0f,
                0.125f,
                0.125f,
                0.125f,
                1
        ));
        objects.get(0).scaleObject(0.1f, 0.1f, 0.1f);

        // mobil merah
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
                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),
                0.0f,
                0.0f,
                0.0f,
                0.125f,
                0.125f,
                0.125f,
                2
        ));
        objects.get(1).scaleObject(0.0005f, 0.0005f, 0.0005f);
        objects.get(1).rotateObject((float) (Math.toRadians(90f)), 0f, 1f, 0f);
        objects.get(1).translateObject(0.0f, 0.001f, -0.001f);

        // mobil biru
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
                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),
                0.0f,
                0.0f,
                0.0f,
                0.125f,
                0.125f,
                0.125f,
                3
        ));
        objects.get(2).scaleObject(0.0005f, 0.0005f, 0.0005f);
        objects.get(2).rotateObject((float) (Math.toRadians(90f)), 0f, 1f, 0f);
        objects.get(2).translateObject(0.015f, 0.001f, -0.001f);

        objects.get(0).translateObject(0f, 0.4f, 0f);
        objects.get(1).translateObject(0f, 0.4f, 0f);
        objects.get(2).translateObject(0f, 0.4f, 0f);
//        camera.moveUp();
    }

    public void input() {
//        float move = 0.00075f;
//        float move = 0.01f;
        float move = 0.0005f;
        float moveThirdPerson = 0.00025f;

        if (window.isKeyPressed(GLFW_KEY_1) && pilihanKamera != 1) {
            pilihanKamera = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_2) && pilihanKamera != 2) {
            pilihanKamera = 2;
            thirdPersonCamera.setPosition(objects.get(2).updateCenterPoint().x - 0.02f, objects.get(2).updateCenterPoint().y + 0.005f, objects.get(2).updateCenterPoint().z);
        }
        if (window.isKeyPressed(GLFW_KEY_3) && pilihanKamera != 3) {
            pilihanKamera = 3;
            firstPersonCamera.setPosition(objects.get(2).updateCenterPoint().x - 0.006f, objects.get(2).updateCenterPoint().y + 0.005f, objects.get(2).updateCenterPoint().z);
        }

        if (pilihanKamera == 1) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                objects.get(1).translateObject(0.0f, 0.0f, -moveThirdPerson);
                objects.get(2).translateObject(0.0f, 0.0f, -moveThirdPerson);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                objects.get(1).translateObject(-moveThirdPerson, 0.0f, 0.0f);
                objects.get(2).translateObject(-moveThirdPerson, 0.0f, 0.0f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                objects.get(1).translateObject(0.0f, 0.0f, moveThirdPerson);
                objects.get(2).translateObject(0.0f, 0.0f, moveThirdPerson);
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                objects.get(1).translateObject(moveThirdPerson, 0.0f, 0.0f);
                objects.get(2).translateObject(moveThirdPerson, 0.0f, 0.0f);
            }
            if (window.isKeyPressed(GLFW_KEY_T)) {
                camera.moveForward(move);
            }
            if (window.isKeyPressed(GLFW_KEY_G)) {
                camera.moveBackwards(move);
            }
            if (window.isKeyPressed(GLFW_KEY_I)) {
                camera.moveUp(moveThirdPerson / 2);
            }
            if (window.isKeyPressed(GLFW_KEY_K)) {
                camera.moveDown(moveThirdPerson / 2);
            }
            if (window.isKeyPressed(GLFW_KEY_J)) {
                camera.moveLeft(moveThirdPerson);
            }
            if (window.isKeyPressed(GLFW_KEY_L)) {
                camera.moveRight(moveThirdPerson);
            }
            if (window.isKeyPressed(GLFW_KEY_LEFT)) {
                float x = objects.get(1).updateCenterPoint().x;
                float y = objects.get(1).updateCenterPoint().y;
                float z = objects.get(1).updateCenterPoint().z;
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).translateObject(-x, -y, -z);
                objects.get(1).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
                objects.get(1).translateObject(x, y, z);
                objects.get(2).translateObject(-x2, -y2, -z2);
                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
                objects.get(2).translateObject(x2, y2, z2);
                count--;
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.005f);
            }
            if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
                float x = objects.get(1).updateCenterPoint().x;
                float y = objects.get(1).updateCenterPoint().y;
                float z = objects.get(1).updateCenterPoint().z;
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).translateObject(-x, -y, -z);
                objects.get(1).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
                objects.get(1).translateObject(x, y, z);
                objects.get(2).translateObject(-x2, -y2, -z2);
                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
                objects.get(2).translateObject(x2, y2, z2);
                count++;
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.005f);
            }
            if (window.getMouseInput().isLeftButtonPressed()) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                camera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));
            }
            if (window.getMouseInput().getScroll().y != 0) {
                projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
                window.getMouseInput().setScroll(new Vector2f());
            }
        }

        if (pilihanKamera == 2) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveForward(1f);
                objects.get(2).moveForward(1f);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.005f);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                float x = objects.get(1).updateCenterPoint().x;
                float y = objects.get(1).updateCenterPoint().y;
                float z = objects.get(1).updateCenterPoint().z;
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).translateObject(-x, -y, -z);
                objects.get(1).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
                objects.get(1).translateObject(x, y, z);
                objects.get(2).translateObject(-x2, -y2, -z2);
                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
                objects.get(2).translateObject(x2, y2, z2);
                count--;
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.005f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveBackward(1f);
                objects.get(2).moveBackward(1f);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.005f);
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                float x = objects.get(1).updateCenterPoint().x;
                float y = objects.get(1).updateCenterPoint().y;
                float z = objects.get(1).updateCenterPoint().z;
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).translateObject(-x, -y, -z);
                objects.get(1).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
                objects.get(1).translateObject(x, y, z);
                objects.get(2).translateObject(-x2, -y2, -z2);
                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
                objects.get(2).translateObject(x2, y2, z2);
                count++;
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.005f);
            }
        }

        if (pilihanKamera == 3) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveForward(1f);
                objects.get(2).moveForward(1f);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.005f);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                float x = objects.get(1).updateCenterPoint().x;
                float y = objects.get(1).updateCenterPoint().y;
                float z = objects.get(1).updateCenterPoint().z;
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).translateObject(-x, -y, -z);
                objects.get(1).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
                objects.get(1).translateObject(x, y, z);
                objects.get(2).translateObject(-x2, -y2, -z2);
                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
                objects.get(2).translateObject(x2, y2, z2);
                count--;
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.005f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveBackward(1f);
                objects.get(2).moveBackward(1f);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.005f);
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                float x = objects.get(1).updateCenterPoint().x;
                float y = objects.get(1).updateCenterPoint().y;
                float z = objects.get(1).updateCenterPoint().z;
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).translateObject(-x, -y, -z);
                objects.get(1).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
                objects.get(1).translateObject(x, y, z);
                objects.get(2).translateObject(-x2, -y2, -z2);
                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
                objects.get(2).translateObject(x2, y2, z2);
                count++;
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.005f);
            }
        }
    }

    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL.createCapabilities();
            input();

            skyboxRenderer.draw(camera, projection);

            if (pilihanKamera == 1) {
                for (Object object : objects) {
                    object.draw(camera, projection);
                }
            } else if (pilihanKamera == 2) {
                for (Object object : objects) {
                    object.draw(thirdPersonCamera, projection);
                }
            } else if (pilihanKamera == 3) {
                for (Object object : objects) {
                    object.draw(firstPersonCamera, projection);
                }
            }

            glDisableVertexAttribArray(0);
            glfwPollEvents();
        }
    }

    public void run() {
        init();
        loop();

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}