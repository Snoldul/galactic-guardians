package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

public class PlayerControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    // Define movement areas for touch input
    private Rectangle MoveArea;
    private float speed = 10; // Value to control player speed

    public PlayerControlSystem() {
        super(Family.all(TransformComponent.class, VelocityComponent.class).get());

        // Initialize movement area
        MoveArea = new Rectangle(10, 10, Gdx.graphics.getWidth()-10, Gdx.graphics.getHeight()/4f);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Call to process each entity

        // Handle touch input for mobile devices
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y; // Adjust for y-coordinate flip

            // Check if touch input is within movement area
            if (MoveArea.contains(touchPos.x, touchPos.y)) {

                for (Entity entity : getEntities()) {
                    // Get components
                    VelocityComponent velocity = vm.get(entity);
                    TransformComponent transform = pm.get(entity);

                    // Calculate movement direction
                    Vector2 touchCenter = new Vector2(MoveArea.x + MoveArea.width / 2f, MoveArea.y + MoveArea.height / 2f);
                    Vector2 moveCenter = new Vector2(Gdx.graphics.getWidth() / 2f, (Gdx.graphics.getHeight() / 2f) - 200);
                    Vector2 direction = new Vector2(touchPos.x - touchCenter.x, touchPos.y - touchCenter.y);

                    // Calculate target position
                    Vector2 target = new Vector2(moveCenter.x + direction.x, moveCenter.y + direction.y);

                    // Set velocity to move towards target
                    velocity.velocity.x = (target.x - transform.position.x) * speed;
                    velocity.velocity.y = (target.y - transform.position.y) * speed;
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