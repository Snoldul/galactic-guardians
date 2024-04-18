package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240gr18.game.AudioManager;
import com.tdt4240gr18.game.entity.components.BulletComponent;
import com.tdt4240gr18.game.entity.components.CollisionComponent;
import com.tdt4240gr18.game.entity.components.ExplosionComponent;
import com.tdt4240gr18.game.entity.components.LivesComponent;
import com.tdt4240gr18.game.entity.components.PlayerComponent;
import com.tdt4240gr18.game.entity.components.ScoreComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;
import com.tdt4240gr18.game.entity.components.EnemyComponent;

public class BulletControlSystem extends IteratingSystem {
    private final ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    private final Texture explosion1Texture;
    private final PooledEngine engine;
    private final AudioManager audioManager;

    public BulletControlSystem(PooledEngine engine) {
        super(Family.all(BulletComponent.class).get());
        this.engine = engine;
        explosion1Texture = new Texture("explosion1.png");
        audioManager = AudioManager.getInstance();
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
            if (playerLives != null) {
                // Decrement the player's lives by the damage of the bullet
                playerLives.decrementLives(bullet.damage);

                // Play death sound
                audioManager.playDeathSound();

                // Play death sound
                audioManager.playDeathSound();

                // check if its lives are 0
                if (playerLives.lives <= 0) {
                    System.out.println("dead");
                }

                engine.removeEntity(entity);
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
                if (enemyLives != null) {
                    // Decrement the enemy's lives by the damage of the bullet
                    enemyLives.lives -= bullet.damage;

                    // check if its lives are 0
                    if (enemyLives.lives <= 0) {
                        TransformComponent enemyTransform = enemyEntity.getComponent(TransformComponent.class);


                        createExplosion((int) enemyTransform.position.x, (int) enemyTransform.position.y - (int) enemyTransform.scale.y);
                        engine.removeEntity(enemyEntity);

                        // Update score
                        Family scoreFamily = Family.all(ScoreComponent.class).get();
                        ImmutableArray<Entity> entitiesWithScore = engine.getEntitiesFor(scoreFamily);

                        if (entitiesWithScore.size() > 0) {
                            Entity entityWithScore = entitiesWithScore.first(); // Assuming there is only one entity
                            ScoreComponent scoreComponent = entityWithScore.getComponent(ScoreComponent.class);
                            scoreComponent.incrementScore(1);
                        }
                        // Play hit sound
                        audioManager.playHitSound();

                    }

                    engine.removeEntity(entity);
                }
            }
        }

    }

    private void createExplosion(int x, int y) {
        Entity explosionEntity = engine.createEntity();
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        ExplosionComponent explosion = engine.createComponent(ExplosionComponent.class);

        transform.position.set(x, y, 0);
        float scale = 10f;
        transform.scale.set(scale, scale, scale);
        texture.region = new TextureRegion(explosion1Texture);
        explosion.frame = 0;

        explosionEntity.add(transform);
        explosionEntity.add(texture);
        explosionEntity.add(explosion);

        engine.addEntity(explosionEntity);
    }
}
