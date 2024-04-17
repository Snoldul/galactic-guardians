package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.tdt4240gr18.game.entity.components.BulletComponent;
import com.tdt4240gr18.game.entity.components.CollisionComponent;
import com.tdt4240gr18.game.entity.components.LivesComponent;
import com.tdt4240gr18.game.entity.components.PlayerComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;
import com.tdt4240gr18.game.entity.components.EnemyComponent;

public class BulletControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);


    private PooledEngine engine;

    public BulletControlSystem(PooledEngine engine) {
        super(Family.all(BulletComponent.class).get());
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);

        // Calculate horizontal velocity using a sine wave based on the entity's Y position
        vel.velocity.y = entity.getComponent(BulletComponent.class).speed;

        // Update position based on velocity
        pos.position.add(0, vel.velocity.y * deltaTime, 0);

        float bulletY = pos.position.y;

        // Check if the bullet is off the screen
        if (bulletY > Gdx.graphics.getHeight()+200 || bulletY < 0) {
            // Remove the bullet from the engine
            getEngine().removeEntity(entity);
        }

        // Update collisionComponent position
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        CollisionComponent bulletCollision = entity.getComponent(CollisionComponent.class);
        Rectangle bounds = (Rectangle) bulletCollision.bounds;
        bounds.x = transform.position.x;
        bounds.y = transform.position.y;

        // HIT DETECTION
        BulletComponent bullet = entity.getComponent(BulletComponent.class);

        // Player hit detection
        Family playerFamily = Family.all(PlayerComponent.class).get();
        ImmutableArray<Entity> playerEntities = engine.getEntitiesFor(playerFamily);
        Entity playerEntity = playerEntities.get(0);
        CollisionComponent playerCollision = playerEntity.getComponent(CollisionComponent.class);
        LivesComponent playerLives = playerEntity.getComponent(LivesComponent.class);
        if (bulletCollision.overlaps(playerCollision) && bullet.speed < 0){
            if (playerLives != null && bullet != null) {
                // Decrement the player's lives by the damage of the bullet
                playerLives.lives -= bullet.damage;

                // check if its lives are 0
                if (playerLives.lives <= 0) {
                    System.out.println("dead");
                }

                // Optionally, you can also remove the bullet entity
                engine.removeEntity(entity);
                // Add any additional logic you want to execute when a collision occurs
            }
        }

        // Enemy hit detection
        Family enemyFamily = Family.all(EnemyComponent.class).get();
        ImmutableArray<Entity> enemyEntities = engine.getEntitiesFor(enemyFamily);

        // Iterate over enemy entities
        for (Entity enemyEntity : enemyEntities) {
            CollisionComponent enemyCollision = enemyEntity.getComponent(CollisionComponent.class);

            // Perform collision detection between bullet and enemy
            LivesComponent enemyLives = enemyEntity.getComponent(LivesComponent.class);

            if (bulletCollision.overlaps(enemyCollision) && bullet.speed > 0) {
                if (enemyLives != null && bullet != null) {
                    // Decrement the enemy's lives by the damage of the bullet
                    enemyLives.lives -= bullet.damage;

                    // check if its lives are 0
                    if (enemyLives.lives <= 0) {
                        engine.removeEntity(enemyEntity);
                    }

                    // Optionally, you can also remove the bullet entity
                    engine.removeEntity(entity);
                    // Add any additional logic you want to execute when a collision occurs
                }
            }
        }

    }
}
