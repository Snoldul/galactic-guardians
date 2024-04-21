package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tdt4240gr18.game.entity.components.EnemyComponent;
import com.tdt4240gr18.game.entity.components.LivesComponent;
import com.tdt4240gr18.game.entity.components.MovementPropertiesComponent;
import com.tdt4240gr18.game.entity.components.MovementStateComponent;
import com.tdt4240gr18.game.entity.components.PlayerComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

import com.tdt4240gr18.states.PlayState;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240gr18.game.entity.components.BulletComponent;
import com.tdt4240gr18.game.entity.components.CollisionComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;

public class EnemyControlSystem extends IteratingSystem {
    private final ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<MovementStateComponent> stateMapper = ComponentMapper.getFor(MovementStateComponent.class);
    private final ComponentMapper<MovementPropertiesComponent> propertiesMapper = ComponentMapper.getFor(MovementPropertiesComponent.class);
    private final PlayState playstate;
    private final Texture bullet;
    private final PooledEngine engine;
    private final PlayerControlSystem playerCS;

    public EnemyControlSystem(PlayState playstate, PooledEngine engine, PlayerControlSystem playerControlSystem){
        super(Family.all(TransformComponent.class, VelocityComponent.class, EnemyComponent.class, MovementStateComponent.class, MovementPropertiesComponent.class).get());
        this.playstate = playstate;
        // Specify that this system uses entities with both Transform and Velocity components
        this.engine = engine;
        bullet = new Texture("pew2.png");
        playerCS = playerControlSystem;
    }
    // Return player's move area
    public Rectangle getPlayerMoveArea(){
        return playerCS.getMoveArea();
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementStateComponent stateComp = stateMapper.get(entity);
        MovementPropertiesComponent props = propertiesMapper.get(entity);
        TransformComponent pos = pm.get(entity);


        stateComp.elapsedTime += deltaTime;

        // check if enemy is below the player's move area
        if(pos.position.y < getPlayerMoveArea().height) {
            getEngine().removeEntity(entity);

            //Remove Life if enemy gets beyond play area
            Family playerFamily = Family.all(PlayerComponent.class).get();
            ImmutableArray<Entity> playerEntities = engine.getEntitiesFor(playerFamily);
            Entity playerEntity = playerEntities.get(0);
            LivesComponent playerLives = playerEntity.getComponent(LivesComponent.class);

            playerLives.decrementLives(1);
        }
        switch (stateComp.state) {
            case ENTERING:

                processSwoopingEntry(entity, deltaTime, props, stateComp);
                break;
            case FLOATING:

                processFloating(entity, deltaTime, props);
                break;
            case DIVING:

                processDiving(entity, deltaTime, props);
                break;
            default:
                processFloating(entity, deltaTime, props);
                break;
        }

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
            collision.bounds = new Rectangle(enemyTransform.position.x, enemyTransform.position.y - enemyHeight, bullet.getWidth() * scale, bullet.getHeight() * scale);

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
    private void processSwoopingEntry(Entity entity, float deltaTime, MovementPropertiesComponent props, MovementStateComponent stateComp) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);

        float entryPhaseDuration = 2.0f;
        if (stateComp.elapsedTime < entryPhaseDuration) {
            float targetY = Gdx.graphics.getHeight() * 0.8f;
            float progress = stateComp.elapsedTime / entryPhaseDuration;

            // Interpolating position for Y
            float newY = MathUtils.lerp(Gdx.graphics.getHeight(), targetY, progress);
            vel.velocity.y = (newY - pos.position.y) / deltaTime;

            // Sine wave motion for X
            float amplitude = props.amplitude;
            float frequency = props.frequency;
            float desiredX = pos.position.x + amplitude * MathUtils.sin(frequency * stateComp.elapsedTime);
            vel.velocity.x = (desiredX - pos.position.x) / deltaTime;

            // Ensure the entity does not go off screen horizontally
            float screenWidth = Gdx.graphics.getWidth();
            if (desiredX < 10 || desiredX > screenWidth) {
                vel.velocity.x = -vel.velocity.x; // Reverse the direction if it goes off screen
            }
        } else {
            // Transition to next state and reset necessary properties
            stateComp.state = MovementStateComponent.State.FLOATING;
            vel.velocity.y = 0; // Stop vertical movement after entry phase
            vel.velocity.x = 0; // Reset horizontal velocity to avoid initial burst movement
            stateComp.elapsedTime = 0;
        }

        // Apply boundary checks
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        if (pos.position.x < 0) {
            pos.position.x = 0; // Stick to the left boundary
        } else if (pos.position.x > screenWidth) {
            pos.position.x = screenWidth; // Stick to the right boundary
        }

        if (pos.position.y < 0) {
            pos.position.y = 0; // Stick to the bottom boundary
        } else if (pos.position.y > screenHeight) {
            pos.position.y = screenHeight; // Stick to the top boundary
        }
    }
    private void processFloating(Entity entity, float deltaTime, MovementPropertiesComponent props) {
        VelocityComponent vel = vm.get(entity);

        // Frequency for oscillation
        float frequency = props.frequency;
        // Amplitude for oscillation
        float amplitude = props.amplitude + MathUtils.random(1, 5) * 5;

        // Horizontal Oscillation
        if(props.oscillationPhase == 0) {
            props.oscillationPhase = MathUtils.random(0, 2 * MathUtils.PI);
        }
        props.oscillationPhase += deltaTime * frequency;
        vel.velocity.x = amplitude * MathUtils.sin(props.oscillationPhase); // Delta time scaling to make motion smooth and consistent

        // Vertical movement
        // A small, constant downward velocity to simulate slow floating.
        float constantDownwardVelocity = 20f;
        vel.velocity.y = -constantDownwardVelocity;

        // Check if the enemy should dive
        if (shouldDive(entity)) {
            MovementStateComponent stateComp = stateMapper.get(entity);
            stateComp.state = MovementStateComponent.State.DIVING;
            stateComp.elapsedTime = 0; // Resetting time if transitioning states
        }
    }

    //Check if the enemy should dive based on its position
    private boolean shouldDive(Entity entity) {
        TransformComponent pos = pm.get(entity);
        MovementPropertiesComponent props = propertiesMapper.get(entity);

        // Calculate the difference between current y position and target dive y
        float distanceToDiveLine = Math.abs(pos.position.y - props.targetDiveY);

        // Threshold for how close they need to be to the line to trigger a dive
        float threshold = 10; // 10 pixels proximity to the line

        return distanceToDiveLine <= threshold;
    }
    private void processDiving(Entity entity, float deltaTime, MovementPropertiesComponent props) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);
        Vector2 playerPosition = playstate.getPlayerPosition();

        // Ensures the dive direction is set towards the player, favoring downward movement
        if (props.diveDirection == null && playerPosition != null) {
            // Creating a direction vector that aims below the player's position
            props.diveDirection = new Vector2(playerPosition.x - pos.position.x, (playerPosition.y - 50) - pos.position.y).nor();
        }

        // If dive direction is set, apply a downward-biased movement
        if (props.diveDirection != null) {
            float speed = 10;
            vel.velocity.x = props.diveDirection.x * speed;
            vel.velocity.y = props.diveDirection.y * speed * 10; // Biasing downward movement
        }

        // Update position based on velocity
        pos.position.add(vel.velocity.x * deltaTime, vel.velocity.y * deltaTime, 0);

        // Boundary check to prevent moving off-screen horizontally
        float screenWidth = Gdx.graphics.getWidth();
        if (pos.position.x < 0 || pos.position.x > screenWidth) {
            props.diveDirection.x = -props.diveDirection.x; // Reverse horizontal direction if off-screen
        }
    }
}
