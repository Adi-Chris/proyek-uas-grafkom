package Engine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Lampu extends Object {
    float centerX;
    float centerY;
    float centerZ;
    float radiusX;
    float radiusY;
    float radiusZ;
    List<Integer> index;
    int ibo;
    List<Vector3f> normal;
    int nbo;
    int tex_tbo;
    int tbo;
    String element;
    String elemen2;
    List<Vector2f> textureCoordinates;

    public Lampu(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, float centerX, float centerY, float centerZ, float radiusX, float radiusY, float radiusZ, String element, String elemen2) {
        super(shaderModuleDataList, vertices, color);
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
        this.element = element;
        this.elemen2 = elemen2;
//        createBox();
        loadObjModel(element);
        setIbo();
        setupVAOVBO();
        try {
            loadTexture(elemen2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex_tbo);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        glDrawElements(GL_TRIANGLES, index.size(), GL_UNSIGNED_INT, 0);
    }

    public void setupVAOVBO() {
        super.setupVAOVBO();

        //nbo
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(normal), GL_STATIC_DRAW);

        //tbo
        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat2(textureCoordinates), GL_STATIC_DRAW);
    }

    public void drawSetup(Camera camera, Projection projection) {
        super.drawSetup(camera, projection);

        // Bind NBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // directional light
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.0f,-0.0f,-0.0f));
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.0f,0.0f,0.0f));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.0f,0.0f,0.0f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(0.0f,0.0f,0.0f));

        Vector3f[] _pointLightPositions = {
                // ( 4.935E-1  4.140E-2 -4.916E-2)
                //( 4.764E-1  1.000E-3 -6.294E-2)
                new Vector3f(0.4776f,0.044f,-0.06378f),
                // ( 3.501E-1  4.420E-2  1.829E-1)
                //( 3.767E-1  1.000E-3  1.790E-1)
                new Vector3f(0.3767f,0.044f,0.1790f),
                // ( 6.036E-2  4.420E-2  1.092E-1)
                //( 3.424E-1  1.000E-3  5.408E-1)
                new Vector3f(0.3444f,0.044f,0.5408f),
                // (-2.591E-1  4.420E-2  1.106E-1)
                //( 4.353E-2  3.660E-2  1.151E-1)
                new Vector3f(0.04353f,0.044f,0.1151f),
                // ( 3.251E-1  4.460E-2  5.189E-1)
                //(-2.313E-1  3.340E-2  1.070E-1)
                new Vector3f(-0.2313f,0.044f,0.107f)
        };
        for (int i=0;i<_pointLightPositions.length;i++){
            uniformsMap.setUniform("pointLights["+ i +"].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLights["+ i +"].ambient", new Vector3f(0.06f,0.06f,0.06f));
            uniformsMap.setUniform("pointLights["+ i +"].diffuse", new Vector3f(0.8f,0.8f,0.8f));
            uniformsMap.setUniform("pointLights["+ i +"].specular", new Vector3f(1.0f,1.0f,1.0f));
            uniformsMap.setUniform("pointLights["+ i +"].constant",1.0f );
            uniformsMap.setUniform("pointLights["+ i +"].linear", 0.09f);
            uniformsMap.setUniform("pointLights["+ i +"].quadratic", 0.032f);
//            float dist = camera.getPosition().distance(_pointLightPositions[i]);
//            uniformsMap.setUniform("pointLights["+ i +"].position", _pointLightPositions[i]);
//            uniformsMap.setUniform("pointLights["+ i +"].ambient", new Vector3f(0.06f,0.06f,0.06f));
//            uniformsMap.setUniform("pointLights["+ i +"].diffuse", new Vector3f(0.8f,0.8f,0.8f).div(dist));
//            uniformsMap.setUniform("pointLights["+ i +"].specular", new Vector3f(1.0f,1.0f,1.0f).div(dist));
//            uniformsMap.setUniform("pointLights["+ i +"].constant",1.0f );
//            uniformsMap.setUniform("pointLights["+ i +"].linear", 0.09f);
//            uniformsMap.setUniform("pointLights["+ i +"].quadratic", 0.032f);
        }

        // spotlight
//        uniformsMap.setUniform("spotLight.position", camera.getPosition());
//        uniformsMap.setUniform("spotLight.direction", camera.getDirection());
//        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
//        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(1.0f, 1.0f, 1.0f));
//        uniformsMap.setUniform("spotLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));
//        uniformsMap.setUniform("spotLight.constant", 1.0f);
//        uniformsMap.setUniform("spotLight.linear", 0.09f);
//        uniformsMap.setUniform("spotLight.quadratic", 0.032f);
//        uniformsMap.setUniform("spotLight.cutOff", (float) Math.cos(Math.toRadians(12.5f)));
//        uniformsMap.setUniform("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(12.5f)));
//        uniformsMap.setUniform("viewPos", camera.getPosition());
//        uniformsMap.setUniform("textureSampler", 0);
        // color
        uniformsMap.setUniform("Lampu", new Vector3f(1.0f,0.0f,0.0f));

        // bind texture
        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
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
        this.textureCoordinates = Utils.floatToList2(texCoordArr);
    }

    private static void processVertex(int pos, int texCoord, int normal, List<Vector2f> texCoordList, List<Vector3f> normalList, List<Integer> indicesList, float[] texCoordArr, float[] normalArr) {
        indicesList.add(pos);
        if (texCoord >= 0) {
            Vector2f texCoordVec = texCoordList.get(texCoord);
            texCoordArr[pos * 2] = texCoordVec.x;
            texCoordArr[pos * 2 + 1] = 1 - texCoordVec.y;
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

    public void loadTexture(String filename) throws Exception{
        int width, height;
        ByteBuffer buffer;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null)
                throw new Exception("Image File " + filename + " not load " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int texID = GL11.glGenTextures();

        //bind texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        tex_tbo = texID;
    }
}