import Engine.*;
import Engine.Object;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class Main {
    private Window window = new Window(1000, 1000, "Window");
    ArrayList<Sphere> objects = new ArrayList<>();
    ArrayList<Lampu> lampu = new ArrayList<>();
    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());
    SkyboxRenderer skyboxRenderer;
    Camera thirdPersonCamera = new Camera();
    Camera firstPersonCamera = new Camera();
    int pilihanKamera = 1;
    int count = 0;
    ArrayList<AABB> aabbList = hardcodeAABB();

    public void init() {
        window.init();
        GL.createCapabilities();

        // Cull
        GL30.glCullFace(GL30.GL_CULL_FACE);
        GL30.glCullFace(GL_BACK);

        thirdPersonCamera.addRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(90.0f));
        firstPersonCamera.addRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(90.0f));
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
                new Vector4f(0.0f, 1.0f, 0.0f, 1.0f)
        );
        camera.setPosition(0.09f, 1.0f, 0.15f);
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
                "/models/sirkuitfinalrilcuy2.obj", "C:\\Users\\ADI CHRISTIAN\\OneDrive\\Documents\\Tugas-Tugas Kuliah\\Grafika Komputer\\ProyekUASGrafkom\\src\\main\\resources\\textures\\texture.png"

        ));
        objects.get(0).scaleObject(0.25f, 0.25f, 0.25f);

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
                "/models/supra2.obj", "C:\\Users\\ADI CHRISTIAN\\OneDrive\\Documents\\Tugas-Tugas Kuliah\\Grafika Komputer\\ProyekUASGrafkom\\src\\main\\resources\\textures\\texture2.png"
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
                "/models/supra3.obj","C:\\Users\\ADI CHRISTIAN\\OneDrive\\Documents\\Tugas-Tugas Kuliah\\Grafika Komputer\\ProyekUASGrafkom\\src\\main\\resources\\textures\\texture3.png"
        ));
        objects.get(2).scaleObject(0.0005f, 0.0005f, 0.0005f);
        objects.get(2).rotateObject((float) (Math.toRadians(90f)), 0f, 1f, 0f);
        objects.get(2).translateObject(0.015f, 0.001f, -0.001f);

        lampu.add(new Lampu(
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
                "/models/tiangonly.obj","C:\\Users\\ADI CHRISTIAN\\OneDrive\\Documents\\Tugas-Tugas Kuliah\\Grafika Komputer\\ProyekUASGrafkom\\src\\main\\resources\\textures\\texture3.png"
        ));
        lampu.get(0).scaleObject(0.25f,0.25f,0.25f);
        lampu.get(0).translateObject(0.125f,0.0f,-0.01f);
    }

    // AABB Collision Hardcode
    private ArrayList<AABB> hardcodeAABB() {
        ArrayList<AABB> walls = new ArrayList<>();

        // Pagar Luar
        // Atas ke kanan
        walls.add(new AABB(new Vector3f(-3.790E-1f, 1.000E-3f, -3.000E-3f),
                new Vector3f(-3.490E-1f, 2.500E-2f, 1.500E-2f)));
        walls.add(new AABB(new Vector3f(-3.450E-1f, 1.000E-3f, -2.700E-2f),
                new Vector3f(-3.160E-1f, 2.500E-2f, -1.000E-3f)));
        walls.add(new AABB(new Vector3f(-3.160E-1f, 1.000E-3f, -2.700E-2f),
                new Vector3f(-2.870E-1f, 2.500E-2f, -1.000E-2f)));

        walls.add(new AABB(new Vector3f(-2.982E-1f, 1.000E-3f, -4.871E-2f),
                new Vector3f(-7.424E-2f, 2.500E-2f, -1.571E-2f)));
        walls.add(new AABB(new Vector3f(-7.424E-2f, 1.000E-3f, -4.600E-2f),
                new Vector3f(3.800E-2f, 2.500E-2f, -2.500E-2f)));
        walls.add(new AABB(new Vector3f(3.800E-2f, 1.000E-3f, -9.500E-2f),
                new Vector3f(1.010E-1f, 2.500E-2f, -4.300E-2f)));
        walls.add(new AABB(new Vector3f(1.010E-1f, 1.000E-3f, -1.320E-1f),
                new Vector3f(1.800E-1f, 2.500E-2f, -5.700E-2f)));
        walls.add(new AABB(new Vector3f(1.010E-1f, 1.000E-3f, -1.320E-1f),
                new Vector3f(2.340E-1f, 2.500E-2f, -7.700E-2f)));
        walls.add(new AABB(new Vector3f(2.340E-1f, 1.000E-3f, -1.760E-1f),
                new Vector3f( 2.834E-1f, 2.500E-2f, -1.187E-1f)));
        walls.add(new AABB(new Vector3f(2.834E-1f, 1.000E-3f, -1.587E-1f),
                new Vector3f( 3.150E-1f, 2.500E-2f, -1.310E-1f)));
        walls.add(new AABB(new Vector3f(3.150E-1f, 1.000E-3f, -1.730E-1f),
                new Vector3f( 3.195E-1f, 2.500E-2f, -1.390E-1f)));
        walls.add(new AABB(new Vector3f(3.195E-1f, 1.000E-3f, -1.730E-1f),
                new Vector3f( 3.505E-1f, 2.500E-2f, -1.430E-1f)));
        walls.add(new AABB(new Vector3f(3.505E-1f, 1.000E-3f, -1.730E-1f),
                new Vector3f( 4.815E-1f, 2.500E-2f, -1.560E-1f)));

        // Kanan ke bawah
        walls.add(new AABB(new Vector3f(4.740E-1f, 1.000E-3f, -1.730E-1f),
                new Vector3f( 4.980E-1f, 2.500E-2f, -1.532E-1f)));
        walls.add(new AABB(new Vector3f(4.980E-1f, 1.000E-3f, -1.730E-1f),
                new Vector3f( 5.660E-1f, 2.500E-2f, -1.532E-1f)));

        walls.add(new AABB(new Vector3f(5.660E-1f, 1.000E-3f, -1.730E-1f),
                new Vector3f( 6.650E-1f, 2.500E-2f, -8.400E-2f)));
        walls.add(new AABB(new Vector3f(5.940E-1f, 1.000E-3f, -8.400E-2f),
                new Vector3f( 6.650E-1f, 2.500E-2f, 1.070E-1f)));
        walls.add(new AABB(new Vector3f(6.130E-1f, 1.000E-3f, 1.070E-1f),
                new Vector3f( 6.650E-1f, 2.500E-2f, 2.430E-1f)));

        // Bawah ke kiri, sampai tengah
        walls.add(new AABB(new Vector3f(4.170E-1f, 1.000E-3f, 2.430E-1f),
                new Vector3f( 6.090E-1f, 2.500E-2f, 3.070E-1f)));

        // Tengah bawah ke bawah
        walls.add(new AABB(new Vector3f(4.170E-1f, 1.000E-3f, 3.070E-1f),
                new Vector3f( 4.210E-1f, 2.500E-2f, 6.100E-1f)));

        // Paling Bawah ke kiri
        walls.add(new AABB(new Vector3f(4.400E-2f, 1.000E-3f, 6.100E-1f),
                new Vector3f( 4.210E-1f, 2.500E-2f, 6.920E-1f)));

        // Paling Bawah ke atas
        walls.add(new AABB(new Vector3f(-3.000E-3f, 1.000E-3f, 5.190E-1f),
                new Vector3f( 4.400E-2f, 2.500E-2f, 6.920E-1f)));

        // Paling Bawah ke atas 2
        walls.add(new AABB(new Vector3f(-4.900E-2f, 1.000E-3f, 2.530E-1f),
                new Vector3f( -4.000E-3f, 2.500E-2f, 5.190E-1f)));

        // Bawah ke kiri
        walls.add(new AABB(new Vector3f(-4.060E-1f, 1.000E-3f, 2.530E-1f),
                new Vector3f( -4.000E-3f, 2.500E-2f, 3.490E-1f)));

        // Bawah ke atas
        walls.add(new AABB(new Vector3f(-4.360E-1f, 1.000E-3f, 1.400E-2f),
                new Vector3f( -3.830E-1f, 2.500E-2f, 2.530E-1f)));

        return walls;
    }

    public void input() {
////        Debug Position
//        if (window.isKeyPressed(GLFW_KEY_P)) {
//            System.out.println("Coord: " + objects.get(2).updateCenterPoint());
//        }

        float move = 0.001f;

        if (window.isKeyPressed(GLFW_KEY_1) && pilihanKamera != 1) {
            pilihanKamera = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_2) && pilihanKamera != 2) {
            pilihanKamera = 2;
            float x2 = objects.get(2).updateCenterPoint().x;
            float y2 = objects.get(2).updateCenterPoint().y;
            float z2 = objects.get(2).updateCenterPoint().z;
            thirdPersonCamera.setPosition(x2, y2, z2);
            thirdPersonCamera.moveBackwards(0.02f);
            thirdPersonCamera.moveUp(0.0056f);
        }
        if (window.isKeyPressed(GLFW_KEY_3) && pilihanKamera != 3) {
            pilihanKamera = 3;
            float x2 = objects.get(2).updateCenterPoint().x;
            float y2 = objects.get(2).updateCenterPoint().y;
            float z2 = objects.get(2).updateCenterPoint().z;
            firstPersonCamera.setPosition(x2, y2, z2);
            firstPersonCamera.moveUp(0.0056f);
        }

        if (pilihanKamera == 1) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                objects.get(1).translateObject(0.0f, 0.0f, -move);
//                objects.get(2).translateObject(0.0f, 0.0f, -move);

//                AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                ArrayList<AABB> aabbList = new ArrayList<>();
//                aabbList.add(aabb);
                objects.get(2).translateObject2(0.0f, 0.0f, -move, aabbList);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                objects.get(1).translateObject(-move, 0.0f, 0.0f);
//                objects.get(2).translateObject(-move, 0.0f, 0.0f);
//                AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                ArrayList<AABB> aabbList = new ArrayList<>();
//                aabbList.add(aabb);
                objects.get(2).translateObject2(-move, 0.0f, 0.0f, aabbList);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                objects.get(1).translateObject(0.0f, 0.0f, move);
//                objects.get(2).translateObject(0.0f, 0.0f, move);
//                AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                ArrayList<AABB> aabbList = new ArrayList<>();
//                aabbList.add(aabb);
                objects.get(2).translateObject2(0.0f, 0.0f, move, aabbList);
            }
            if (window.isKeyPressed(GLFW_KEY_D)) {
                objects.get(1).translateObject(move, 0.0f, 0.0f);
//                objects.get(2).translateObject(move, 0.0f, 0.0f);
//                AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                ArrayList<AABB> aabbList = new ArrayList<>();
//                aabbList.add(aabb);
                objects.get(2).translateObject2(move, 0.0f, 0.0f, aabbList);
            }
            if (window.isKeyPressed(GLFW_KEY_T)) {
                camera.moveForward(move);
            }
            if (window.isKeyPressed(GLFW_KEY_G)) {
                camera.moveBackwards(move);
            }
            if (window.isKeyPressed(GLFW_KEY_I)) {
                camera.moveUp(move);
            }
            if (window.isKeyPressed(GLFW_KEY_K)) {
                camera.moveDown(move);
            }
            if (window.isKeyPressed(GLFW_KEY_J)) {
                camera.moveLeft(move);
            }
            if (window.isKeyPressed(GLFW_KEY_L)) {
                camera.moveRight(move);
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
//                objects.get(2).translateObject(-x2, -y2, -z2);
//                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
//                objects.get(2).translateObject(x2, y2, z2);
                    boolean colliding;
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                colliding = objects.get(2).rotateObjectCheckCollision((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f, aabbList);

                if (!colliding) {
                    count--;
                }
//                count--;
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
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
//                objects.get(2).translateObject(-x2, -y2, -z2);
//                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
//                objects.get(2).translateObject(x2, y2, z2);
//                count++;
                    boolean colliding;
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                    colliding = objects.get(2).rotateObjectCheckCollision((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f, aabbList);

                    if (!colliding) {
                        count++;
                    }
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
            }
//            if (window.getMouseInput().isLeftButtonPressed()) {
//                Vector2f displayVector = window.getMouseInput().getDisplVec();
//                camera.addRotation((float) Math.toRadians(displayVector.x * 0.1f), (float) Math.toRadians(displayVector.y * 0.1f));
//            }
//            if (window.getMouseInput().getScroll().y != 0) {
//                projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
//                window.getMouseInput().setScroll(new Vector2f());
//            }
        }

        if (pilihanKamera == 2) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveForward(1f);
//                objects.get(2).moveForward(1f);
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                    objects.get(2).moveForwardCheckCollision(1, aabbList);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
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
//                objects.get(2).translateObject(-x2, -y2, -z2);
                    boolean colliding;
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
//                    objects.get(2).rotateObject2((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f, aabbList);
                    colliding = objects.get(2).rotateObjectCheckCollision((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f, aabbList);
//                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
//                objects.get(2).translateObject(x2, y2, z2);
                if (!colliding) {
                    count--;
                }
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveBackward(1f);
//                objects.get(2).moveBackward(1f);
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                    objects.get(2).moveBackwardCheckCollision(1, aabbList);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
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
//                objects.get(2).translateObject(-x2, -y2, -z2);
                    boolean colliding;
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
//                    objects.get(2).rotateObject2((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f, aabbList);
                    colliding = objects.get(2).rotateObjectCheckCollision((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f, aabbList);
//                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
//                objects.get(2).translateObject(x2, y2, z2);
                    if (!colliding) {
                        count++;
                    }

                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
            }
            if (window.isKeyPressed(GLFW_KEY_UP)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveUp(0.8f);
                objects.get(2).moveUp(0.8f);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
            }
            if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveDown(0.8f);
                objects.get(2).moveDown(0.8f);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
            }
        }

        if (pilihanKamera == 3) {
            if (window.isKeyPressed(GLFW_KEY_W)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveForward(1f);
//                objects.get(2).moveForward(1f);
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                    objects.get(2).moveForwardCheckCollision(1, aabbList);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
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
//                objects.get(2).translateObject(-x2, -y2, -z2);
//                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f);
//                objects.get(2).translateObject(x2, y2, z2);
//                count--;
                    boolean colliding;
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                    colliding = objects.get(2).rotateObjectCheckCollision((float) Math.toRadians(1.5), 0.0f, 1f, 0.0f, aabbList);
                    if (!colliding) {
                        count--;
                    }
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
            }
            if (window.isKeyPressed(GLFW_KEY_S)) {
                float x2 = objects.get(2).updateCenterPoint().x;
                float y2 = objects.get(2).updateCenterPoint().y;
                float z2 = objects.get(2).updateCenterPoint().z;
                objects.get(1).moveBackward(1f);
//                objects.get(2).moveBackward(1f);
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                    objects.get(2).moveBackwardCheckCollision(1, aabbList);
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
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
//                objects.get(2).translateObject(-x2, -y2, -z2);
//                objects.get(2).rotateObject((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f);
//                objects.get(2).translateObject(x2, y2, z2);
//                count++;
                    boolean colliding;
//                    AABB aabb = new AABB(objects.get(1).boundingBox.getMin(), objects.get(1).boundingBox.getMax());
//                    ArrayList<AABB> aabbList = new ArrayList<>();
//                    aabbList.add(aabb);
                    colliding = objects.get(2).rotateObjectCheckCollision((float) Math.toRadians(1.5), 0.0f, -1f, 0.0f, aabbList);
                    if (!colliding) {
                        count++;
                    }
                firstPersonCamera.setPosition(x2, y2, z2);
                firstPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                firstPersonCamera.moveUp(0.0056f);
                thirdPersonCamera.setPosition(x2, y2, z2);
                thirdPersonCamera.setRotation(0f, (float) ((Math.toRadians(1.5f) * count) + Math.toRadians(90)));
                thirdPersonCamera.moveBackwards(0.02f);
                thirdPersonCamera.moveUp(0.0056f);
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
                for (Lampu lampu : lampu){
                    lampu.draw(camera,projection);
                }
                skyboxRenderer.draw(camera, projection);
            } else if (pilihanKamera == 2) {
                for (Object object : objects) {
                    object.draw(thirdPersonCamera, projection);
                }
                for (Lampu lampu : lampu){
                    lampu.draw(thirdPersonCamera,projection);
                }
                skyboxRenderer.draw(thirdPersonCamera, projection);
            } else if (pilihanKamera == 3) {
                for (Object object : objects) {
                    object.draw(firstPersonCamera, projection);
                }
                for (Lampu lampu : lampu){
                    lampu.draw(firstPersonCamera,projection);
                }
                skyboxRenderer.draw(firstPersonCamera, projection);
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