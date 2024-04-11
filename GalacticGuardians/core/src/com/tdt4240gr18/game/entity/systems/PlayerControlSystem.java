package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

public class PlayerControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    // Define movement areas for touch input
    private Rectangle leftMoveArea;
    private Rectangle rightMoveArea;
    private float speed = 150; // Value to control player speed

    public PlayerControlSystem() {
        super(Family.all(TransformComponent.class, VelocityComponent.class).get());

        // Initialize movement areas
        leftMoveArea = new Rectangle(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        rightMoveArea = new Rectangle(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Call to process each entity

        // Handle touch input for mobile devices
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y; // Adjust for y-coordinate flip

            for (Entity entity : getEntities()) {
                VelocityComponent velocity = vm.get(entity);

                if (leftMoveArea.contains(touchPos.x, touchPos.y)) {
                    velocity.velocity.x = -speed;
                } else if (rightMoveArea.contains(touchPos.x, touchPos.y)) {
                    velocity.velocity.x = speed;
                }
            }
        } else {
            // Stop movement if no touch input is detected
            for (Entity entity : getEntities()) {
                VelocityComponent velocity = vm.get(entity);
                velocity.velocity.x = 0;
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocity = vm.get(entity);

        // Handle keyboard input for desktop platforms
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.velocity.x = -speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.velocity.x = speed;
        } else if (!Gdx.input.isTouched()) { // No movement if there's no input
            velocity.velocity.x = 0;
        }
    }
}