package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.tdt4240gr18.game.entity.components.BulletComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

public class BulletControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    private int speed = 400;

    public BulletControlSystem() {
        // Specify that this system uses entities with both Transform and Velocity components
        super(Family.all(TransformComponent.class, VelocityComponent.class, BulletComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);

        // Basic behavior: move in an oscillating pattern horizontally and downward movement
        float verticalSpeed = speed; // speed at which the bullet moves upwards

        // Calculate horizontal velocity using a sine wave based on the entity's Y position
        vel.velocity.y = verticalSpeed;

        // Update position based on velocity
        pos.position.add(0, vel.velocity.y * deltaTime, 0);

        // Check if the bullet is off the screen
        if (pos.position.y > Gdx.graphics.getHeight()+200) {
            // Remove the bullet from the engine
            getEngine().removeEntity(entity);
        }
    }
}
