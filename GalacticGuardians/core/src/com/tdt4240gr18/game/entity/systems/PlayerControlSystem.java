package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;

public class PlayerControlSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> pm;
    private ComponentMapper<VelocityComponent> vm;

    public PlayerControlSystem() {
        super(Family.all(TransformComponent.class, VelocityComponent.class).get());
        pm = ComponentMapper.getFor(TransformComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
}