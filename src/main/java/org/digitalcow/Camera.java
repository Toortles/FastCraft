package org.digitalcow;

import org.joml.Vector3f;
import org.joml.Matrix4f;

public class Camera {
    public Vector3f position;
    public Vector3f rotation;

    private Matrix4f viewMatrix;

    public Camera() {
        position = new Vector3f(0.0f);

        init();
    }

    public Camera(Vector3f position) {
        this.position = position;

        init();
    }

    private void init() {
        rotation = new Vector3f(0.0f);

        viewMatrix = new Matrix4f();
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity()
            .rotateX(rotation.x)
            .rotateY(rotation.y)
            .translate(-position.x, -position.y, -position.z);

        return viewMatrix;
    }
}
