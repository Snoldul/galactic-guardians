package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.tdt4240gr18.game.entity.components.PositionComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<TextureComponent> tm;

    public RenderingSystem() {
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), new ZComparator());
        pm = ComponentMapper.getFor(PositionComponent.class);
        tm = ComponentMapper.getFor(TextureComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    private static class ZComparator implements Comparator<Entity> {
        ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum(pm.get(e1).position.z - pm.get(e2).position.z);
        }
    }
}
