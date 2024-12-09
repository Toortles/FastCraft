package org.digitalcow;

import org.joml.Vector3f;

public class Entity {
    public Vector3f position;
    public Vector3f velocity;
    public Vector3f rotation;

    public Entity() {
        position = new Vector3f(0.0f);
        
        init();
    }

    public Entity(Vector3f position) {
        this.position = position;

        init();
    }

    private void init() {
        rotation = new Vector3f(0.0f);
    }

    public void tick() {
        position.add(velocity);
    }
}
