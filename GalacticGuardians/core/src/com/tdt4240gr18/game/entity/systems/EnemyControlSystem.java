package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.tdt4240gr18.game.entity.components.EnemyComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

public class EnemyControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    public EnemyControlSystem() {
        // Specify that this system uses entities with both Transform and Velocity components
        super(Family.all(TransformComponent.class, VelocityComponent.class, EnemyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);

        // Basic behavior: move in an oscillating pattern horizontally and downward movement
        float amplitude = 50; // amplitude of the oscillation
        float frequency = 0.5f; // frequency of the oscillation
        float verticalSpeed = -50; // speed at which the enemy moves downwards

        // Calculate horizontal velocity using a sine wave based on the entity's Y position
        vel.velocity.x = amplitude * MathUtils.sin(frequency * pos.position.y);
        vel.velocity.y = verticalSpeed;

        // Update position based on velocity
        pos.position.add(vel.velocity.x * deltaTime, vel.velocity.y * deltaTime, 0);
    }
}
