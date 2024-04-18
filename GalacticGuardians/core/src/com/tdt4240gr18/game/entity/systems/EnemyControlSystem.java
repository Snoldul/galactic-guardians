package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240gr18.game.entity.components.BulletComponent;
import com.tdt4240gr18.game.entity.components.CollisionComponent;
import com.tdt4240gr18.game.entity.components.EnemyComponent;
import com.tdt4240gr18.game.entity.components.LivesComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

import java.util.Random;
import java.util.random.RandomGenerator;

public class EnemyControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    private final Texture bullet;
    private PooledEngine engine;
    public EnemyControlSystem(PooledEngine engine) {
        // Specify that this system uses entities with both Transform and Velocity components
        super(Family.all(EnemyComponent.class).get());
        this.engine = engine;
        bullet = new Texture("pew2.png");
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

        // Update collisionComponent position
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        CollisionComponent bulletCollision = entity.getComponent(CollisionComponent.class);
        Rectangle bounds = (Rectangle) bulletCollision.bounds;
        bounds.x = transform.position.x;
        bounds.y = transform.position.y;

        // Shoots somewhat randomly
        if (MathUtils.random() < 0.004) {
            // Creating a bullet
            Entity bulletEntity = engine.createEntity();
            TransformComponent bulletTransform = engine.createComponent(TransformComponent.class);
            VelocityComponent bulletVelocity = engine.createComponent(VelocityComponent.class);
            TextureComponent bulletTexture = engine.createComponent(TextureComponent.class);
            BulletComponent bulletCmp = engine.createComponent(BulletComponent.class);
            CollisionComponent collision = engine.createComponent(CollisionComponent.class);

            TransformComponent enemyTransform = pm.get(entity);

            // Set component values
            bulletCmp.speed = -200;

            CollisionComponent collisionComponent = entity.getComponent(CollisionComponent.class);
            Rectangle enemyBounds = (Rectangle) collisionComponent.bounds;
            int enemyHeight = (int) enemyBounds.getHeight();

            bulletTransform.position.set(enemyTransform.position.x, enemyTransform.position.y - enemyHeight, 0);
            float scale = 7f;
            bulletTransform.scale.set(scale, scale, scale);
            bulletTexture.region = new TextureRegion(bullet);

            // Assuming you're using a Rectangle for collision bounds
            collision.bounds = new Rectangle(enemyTransform.position.x, enemyTransform.position.y - enemyHeight, bullet.getWidth()*scale, bullet.getHeight()*scale);

            // Add components to player entity
            bulletEntity.add(bulletTransform);
            bulletEntity.add(bulletVelocity);
            bulletEntity.add(bulletTexture);
            bulletEntity.add(bulletCmp);
            bulletEntity.add(collision); // Add the CollisionComponent

            // Add the entity to the engine
            engine.addEntity(bulletEntity);
        }
    }
}
