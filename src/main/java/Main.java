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
    private SkyboxRenderer skyboxRenderer;
    Camera thirdPersonCamera = new Camera();
    Camera firstPersonCamera = new Camera();
    int pilihanKamera = 1;

    public void init() {

        window.init();
        GL.createCapabilities();
//        camera.setPosition(0.0f,  0.0f, 0.5f);
        camera.setPosition(0.0f, 0.1f, 0.006f);
        camera.setRotation((float) Math.toRadians(90.0f), (float) Math.toRadians(0.0f));
        camera.setPosition(0.0f, 0.1f, 0.006f);
        camera.setRotation((float) Math.toRadians(90.0f), (float) Math.toRadians(0.0f));
        skyboxRenderer = new SkyboxRenderer(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/skyboxVertexShader.vert"
                                , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData(
                                "resources/shaders/skyboxFragmentShader.frag"
                                , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f()
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
    }

    public void input() {
//        float move = 0.00075f;
        float move = 0.002f;
//        float move = 0.01f;
        float moveThirdPerson = 0.001f;

        if (window.isKeyPressed(GLFW_KEY_1)) {
            pilihanKamera = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_2)) {
            pilihanKamera = 2;
            thirdPersonCamera.setPosition(objects.get(1).updateCenterPoint().x - 0.02f, objects.get(1).updateCenterPoint().y + 0.005f, objects.get(1).updateCenterPoint().z);
            thirdPersonCamera.addRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(90.0f));
        }
        if (window.isKeyPressed(GLFW_KEY_3)) {
            pilihanKamera = 3;
            firstPersonCamera.setPosition(objects.get(1).updateCenterPoint().x - 0.006f, objects.get(1).updateCenterPoint().y + 0.005f, objects.get(1).updateCenterPoint().z);
            firstPersonCamera.addRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(90.0f));
        }

        if (pilihanKamera == 1) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                camera.moveDown(move);
                System.out.println(camera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                camera.moveRight(move);
                System.out.println(camera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                camera.moveUp(move);
                System.out.println(camera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                camera.moveLeft(move);
                System.out.println(camera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_T)) {
                camera.moveForward(move);
                System.out.println(camera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_G)) {
                camera.moveBackwards(move);
                System.out.println(camera.getPosition());
            }
            if (window.getMouseInput().isLeftButtonPressed()) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                camera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));
                System.out.println(camera.getPosition());
            }
            if (window.getMouseInput().getScroll().y != 0) {
                projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
                window.getMouseInput().setScroll(new Vector2f());
                System.out.println(camera.getPosition());
            }
        }
        if (pilihanKamera == 1) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                objects.get(1).translateObject(0.0f, 0.0f, -moveThirdPerson / 2);
                objects.get(2).translateObject(0.0f, 0.0f, -moveThirdPerson / 2);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                objects.get(1).translateObject(-moveThirdPerson, 0.0f, 0.0f);
                objects.get(2).translateObject(-moveThirdPerson, 0.0f, 0.0f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                objects.get(1).translateObject(0.0f, 0.0f, moveThirdPerson / 2);
                objects.get(2).translateObject(0.0f, 0.0f, moveThirdPerson / 2);
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                objects.get(1).translateObject(moveThirdPerson, 0.0f, 0.0f);
                objects.get(2).translateObject(moveThirdPerson, 0.0f, 0.0f);
            }
            if (window.isKeyPressed(GLFW_KEY_T)) {
                camera.moveForward(move);
                System.out.println(camera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_G)) {
                camera.moveBackwards(move);
                System.out.println(camera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_UP)) {
                camera.moveUp(moveThirdPerson / 2);
            }
            if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                camera.moveDown(moveThirdPerson / 2);
            }
            if (window.isKeyPressed(GLFW_KEY_LEFT)) {
                camera.moveLeft(moveThirdPerson);
            }
            if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
                camera.moveRight(moveThirdPerson);
            }
            if (window.getMouseInput().isLeftButtonPressed()) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                camera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));
                System.out.println(camera.getPosition());
            }
            if (window.getMouseInput().getScroll().y != 0) {
                projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
                window.getMouseInput().setScroll(new Vector2f());
                System.out.println(camera.getPosition());
            }
        }

        if (pilihanKamera == 2) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                thirdPersonCamera.moveDown(move);
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                thirdPersonCamera.moveRight(move);
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                thirdPersonCamera.moveUp(move);
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                thirdPersonCamera.moveLeft(move);
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_T)) {
                thirdPersonCamera.moveForward(move);
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_G)) {
                thirdPersonCamera.moveBackwards(move);
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.getMouseInput().isLeftButtonPressed()) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                thirdPersonCamera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.getMouseInput().getScroll().y != 0) {
                projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
                window.getMouseInput().setScroll(new Vector2f());
                System.out.println(thirdPersonCamera.getPosition());
            }
        }
        if (pilihanKamera == 2) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                objects.get(1).translateObject(moveThirdPerson, 0.0f, 0.0f);
                objects.get(2).translateObject(moveThirdPerson, 0.0f, 0.0f);
                thirdPersonCamera.moveForward(moveThirdPerson);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                objects.get(1).translateObject(0.0f, 0.0f, -moveThirdPerson);
                objects.get(2).translateObject(0.0f, 0.0f, -moveThirdPerson);
                thirdPersonCamera.moveLeft(moveThirdPerson);
//                objects.get(1).rotateObject((float)Math.toRadians(90.0), 0.0f, -moveThirdPerson, 0.0f);
//                objects.get(2).rotateObject((float)Math.toRadians(90.0), 0.0f, -moveThirdPerson, 0.0f);
//                thirdPersonCamera.addRotation(-moveThirdPerson, 0);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                objects.get(1).translateObject(-moveThirdPerson, 0.0f, 0.0f);
                objects.get(2).translateObject(-moveThirdPerson, 0.0f, 0.0f);
                thirdPersonCamera.moveBackwards(moveThirdPerson);
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                objects.get(1).translateObject(0.0f, 0.0f, moveThirdPerson);
                objects.get(2).translateObject(0.0f, 0.0f, moveThirdPerson);
                thirdPersonCamera.moveRight(moveThirdPerson);
            }

            if (window.getMouseInput().isLeftButtonPressed()) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                thirdPersonCamera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));
                System.out.println(thirdPersonCamera.getPosition());
            }
            if (window.getMouseInput().getScroll().y != 0) {
                projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
                window.getMouseInput().setScroll(new Vector2f());
                System.out.println(thirdPersonCamera.getPosition());
            }
        }

        if (pilihanKamera == 3) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                firstPersonCamera.moveDown(move);
                System.out.println(firstPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                firstPersonCamera.moveRight(move);
                System.out.println(firstPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                firstPersonCamera.moveUp(move);
                System.out.println(firstPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                firstPersonCamera.moveLeft(move);
                System.out.println(firstPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_T)) {
                firstPersonCamera.moveForward(move);
                System.out.println(firstPersonCamera.getPosition());
            }
            if (window.isKeyPressed(GLFW_KEY_G)) {
                firstPersonCamera.moveBackwards(move);
                System.out.println(firstPersonCamera.getPosition());
            }
            if (window.getMouseInput().isLeftButtonPressed()) {
                Vector2f displayVector = window.getMouseInput().getDisplVec();
                firstPersonCamera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));
                System.out.println(firstPersonCamera.getPosition());
            }
            if (window.getMouseInput().getScroll().y != 0) {
                projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
                window.getMouseInput().setScroll(new Vector2f());
                System.out.println(firstPersonCamera.getPosition());
            }
        }
    }

    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL.createCapabilities();
            input();

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

            skyboxRenderer.draw(camera, projection);

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