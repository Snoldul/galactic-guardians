package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tdt4240gr18.game.entity.components.EnemyComponent;
import com.tdt4240gr18.game.entity.components.MovementPropertiesComponent;
import com.tdt4240gr18.game.entity.components.MovementStateComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

import states.PlayState;

public class EnemyControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<MovementStateComponent> stateMapper = ComponentMapper.getFor(MovementStateComponent.class);
    private ComponentMapper<MovementPropertiesComponent> propertiesMapper = ComponentMapper.getFor(MovementPropertiesComponent.class);
    private PlayState playstate;


    public EnemyControlSystem(PlayState playstate) {
        // Specify that this system uses entities with both Transform and Velocity components
        super(Family.all(TransformComponent.class, VelocityComponent.class, EnemyComponent.class, MovementStateComponent.class, MovementPropertiesComponent.class).get());
        this.playstate = playstate;
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementStateComponent stateComp = stateMapper.get(entity);
        MovementPropertiesComponent props = propertiesMapper.get(entity);

        stateComp.elapsedTime += deltaTime;

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
    }
    private void processSwoopingEntry(Entity entity, float deltaTime, MovementPropertiesComponent props, MovementStateComponent stateComp) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);

        float entryPhaseDuration = 2.0f;
        if (stateComp.elapsedTime < entryPhaseDuration) {
            float targetY = Gdx.graphics.getHeight() * 0.8f;
            float progress = stateComp.elapsedTime / entryPhaseDuration;

            // Interpolating position
            float newY = MathUtils.lerp(Gdx.graphics.getHeight(), targetY, progress);
            vel.velocity.y = (newY - pos.position.y) / deltaTime;

            // Sine wave motion
            float originalX = pos.position.x;  // This should be stored or derived if dynamic
            float amplitude = props.amplitude;
            float frequency = props.verticalSpeed;  // Consider renaming to frequency if it's not indicative of vertical speed
            float desiredX = originalX + amplitude * MathUtils.sin(frequency * stateComp.elapsedTime);
            vel.velocity.x = (desiredX - pos.position.x) / deltaTime;
        } else {
            // Transition to next state and reset necessary properties
            stateComp.state = MovementStateComponent.State.FLOATING;
            vel.velocity.y = 0;  // Stopping vertical movement as we enter floating
            // Resetting elapsed time can be useful if the next state also relies on timing
            stateComp.elapsedTime = 0;
        }
    }
    private void processFloating(Entity entity, float deltaTime, MovementPropertiesComponent props) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);

        // Assuming the verticalSpeed variable might be used as a frequency factor for oscillation,
        // which should be clarified or renamed in your actual implementation.
        float frequency = props.verticalSpeed; // This should be renamed if it represents frequency.

        // Calculate and apply the new velocity
        // The sine function will adjust the x velocity to create an oscillating motion
        // Multiplying by deltaTime ensures that changes in velocity are frame-rate independent
        float newVelocityX = props.amplitude * MathUtils.sin(frequency * pos.position.y);
        vel.velocity.x = newVelocityX * deltaTime; // Apply delta time scaling to make motion smooth and consistent

        // Apply a similar deltaTime adjustment to the y velocity, assuming a constant floating velocity
        vel.velocity.y = -props.verticalSpeed * deltaTime; // Negative to ensure it moves downward or adjust as needed
    }
    private void processDiving(Entity entity, float deltaTime, MovementPropertiesComponent props) {
        TransformComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);
        Vector2 playerPosition = playstate.getPlayerPosition();

        if (playerPosition != null) {
            Vector2 diveDirection = new Vector2(playerPosition.x - pos.position.x, playerPosition.y - pos.position.y).nor();
            vel.velocity.x = diveDirection.x * props.amplitude * deltaTime;
            vel.velocity.y = diveDirection.y * props.verticalSpeed * deltaTime;
        } else {
            vel.velocity.x = 0;
            vel.velocity.y = 0;
            // Consider state change if applicable
            // stateComp.state = MovementStateComponent.State.FLOATING;
            // stateComp.elapsedTime = 0; // Resetting time if transitioning states
        }
    }
}
