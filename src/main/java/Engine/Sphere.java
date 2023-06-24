package Engine;

import Texture .*;
import Texture.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.*;
import org.lwjgl.assimp.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.io.FileInputStream;
import java.lang.Math;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Sphere extends Object {
    float centerX;
    float centerY;
    float centerZ;
    float radiusX;
    float radiusY;
    float radiusZ;

    List<Integer> index;
    int ibo;
    int textures;
    List<Vector3f> normal;
    int nbo;

    public Sphere(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ) {
        super(shaderModuleDataList, vertices, color);
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
//        createBox();
        loadObjModel("/models/racingtrack.obj");
        setIbo();
        setupVAOVBO();
    }

    public void setIbo() {
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.listoInt(index), GL_STATIC_DRAW);
    }

    @Override
    public void draw(Camera camera, Projection projection) {
        drawSetup(camera, projection);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, index.size(), GL_UNSIGNED_INT, 0);
    }

    public void createBox() {
        vertices.clear();
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();
        //Titik 1 kiri atas belakang
        temp.x = centerX - radiusX / 2;
        temp.y = centerY + radiusY / 2;
        temp.z = centerZ - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 2 kiri bawah belakang
        temp.x = centerX - radiusX / 2;
        temp.y = centerY - radiusY / 2;
        temp.z = centerZ - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 3 kanan bawah belakang
        temp.x = centerX + radiusX / 2;
        temp.y = centerY - radiusY / 2;
        temp.z = centerZ - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 4 kanan atas belakang
        temp.x = centerX + radiusX / 2;
        temp.y = centerY + radiusY / 2;
        temp.z = centerZ - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 5 kiri atas depan
        temp.x = centerX - radiusX / 2;
        temp.y = centerY + radiusY / 2;
        temp.z = centerZ + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 6 kiri bawah depan
        temp.x = centerX - radiusX / 2;
        temp.y = centerY - radiusY / 2;
        temp.z = centerZ + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 7 kanan bawah depan
        temp.x = centerX + radiusX / 2;
        temp.y = centerY - radiusY / 2;
        temp.z = centerZ + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 8 kanan atas depan
        temp.x = centerX + radiusX / 2;
        temp.y = centerY + radiusY / 2;
        temp.z = centerZ + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //kotak belakang
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(2));

        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(0));
        //kotak depan
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(4));
        //kotak samping kiri
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(4));

        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(4));
        //kotak samping kanan
        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(2));

        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(7));
        //kotak bawah
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(1));
        //kotak atas
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(7));

        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(3));

        normal = new ArrayList<>(Arrays.asList(
                //belakang
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                //depan
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                //kiri
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                //kanan
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                //bawah
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                //atas
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f)
        ));
    }

    public void setupVAOVBO() {
        super.setupVAOVBO();

        //nbo
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
                GL_STATIC_DRAW);
//        uniformsMap.createUniform("lightColor");
//        uniformsMap.createUniform("lightPos");
    }

    public void drawSetup(Camera camera, Projection projection) {
        super.drawSetup(camera, projection);

        // Bind NBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // directional light
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.05f, 0.05f, 0.05f));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));

        Vector3f[] _pointLightPositions = {
                new Vector3f(0.7f, 0.2f, 2.0f),
                new Vector3f(2.3f, -3.3f, -4.0f),
                new Vector3f(-4.0f, 2.0f, -12.0f),
                new Vector3f(0.0f, 0.0f, -3.0f)
        };

        for (int i = 0; i < _pointLightPositions.length; i++) {
            uniformsMap.setUniform("pointLights[" + i + "].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(1.0f, 1.0f, 1.0f));
            uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
            uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
        }

        // spotlight
        uniformsMap.setUniform("spotLight.position", camera.getPosition());
        uniformsMap.setUniform("spotLight.direction", camera.getDirection());
        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.constant", 1.0f);
        uniformsMap.setUniform("spotLight.linear", 0.09f);
        uniformsMap.setUniform("spotLight.quadratic", 0.032f);
        uniformsMap.setUniform("spotLight.cutOff", (float) Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("viewPos", camera.getPosition());
    }

    public void loadObjModel(String fileName) {
        List<String> lines = Utils.readAllLines(fileName);
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        vertices.clear();
        normal = new ArrayList<>();
        index = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    // vertices
                    Vector3f verticesVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVec);
                    break;
                case "vt":
                    Vector2f textureVec = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(textureVec);
                    break;
                case "vn":
                    Vector3f normalsVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normal.add(normalsVec);
                    break;
                case "f":
                    processFace(tokens[1], faces);
                    processFace(tokens[2], faces);
                    processFace(tokens[3], faces);
                    break;
                default:
                    break;
            }
        }

        List<Integer> indices = new ArrayList<>();
        float[] verticesArr = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f pos : vertices) {
            verticesArr[i * 3] = pos.x;
            verticesArr[i * 3 + 1] = pos.y;
            verticesArr[i * 3 + 2] = pos.z;
            i++;
        }
        float[] texCoordArr = new float[vertices.size() * 2];
        float[] normalArr = new float[vertices.size() * 3];

        for (Vector3i face : faces) {
            processVertex(face.x, face.y, face.z, textures, normal, indices, texCoordArr, normalArr);
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        this.vertices = Utils.floatToList(verticesArr);
        this.normal = Utils.floatToList(normalArr);
        this.index = Utils.intToList(indicesArr);
    }
    public int loadCubeMap(String[]textureFiles){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.glGenTextures());
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i=0;1<textureFiles.length;i++){
            TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 1,0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0,GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        textures = texID;
        return texID;
    }
    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
    }

    private static void processVertex(int pos, int texCoord, int normal, List<Vector2f> texCoordList, List<Vector3f> normalList,
                                      List<Integer> indicesList, float[] texCoordArr, float[] normalArr) {
        indicesList.add(pos);
        if (texCoord >= 0) {
            Vector2f texCoordVec = texCoordList.get(texCoord);
            texCoordArr[pos * 2] = texCoordVec.x;
            texCoordArr[pos * 2 + 1] = texCoordVec.y;
        }

        if (normal >= 0) {
            Vector3f normalVec = normalList.get(normal);
            normalArr[pos * 3] = normalVec.x;
            normalArr[pos * 3 + 1] = normalVec.y;
            normalArr[pos * 3 + 2] = normalVec.z;
        }
    }

    private static void processFace(String token, List<Vector3i> faces) {
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int pos = -1, coords = -1, normal = -1;
        pos = Integer.parseInt(lineToken[0]) - 1;
        if (length > 1) {
            String textCoord = lineToken[1];
            coords = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
            if (length > 2) {
                normal = Integer.parseInt(lineToken[2]) - 1;
            }
        }
        Vector3i facesVec = new Vector3i(pos, coords, normal);
        faces.add(facesVec);
    }
}