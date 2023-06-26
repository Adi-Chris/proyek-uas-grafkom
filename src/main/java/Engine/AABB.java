package Engine;

import org.joml.Vector3f;

public class AABB {
    private Vector3f min; // Minimum point of the AABB
    private Vector3f max; // Maximum point of the AABB

    public AABB(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }

    public void updateAABBMixMax(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public boolean AABBIntersects(AABB aabb1, AABB aabb2) {
        return !(aabb1.max.x < aabb2.min.x || aabb1.min.x > aabb2.max.x ||
                aabb1.max.y < aabb2.min.y || aabb1.min.y > aabb2.max.y ||
                aabb1.max.z < aabb2.min.z || aabb1.min.z > aabb2.max.z);
    }
}

