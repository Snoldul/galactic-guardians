package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
    private final ComponentMapper<TransformComponent> trm;
    private final ComponentMapper<TextureComponent> tm;
    private final Array<Entity> renderQueue;
    private final SpriteBatch batch;

    public RenderingSystem(SpriteBatch batch) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());
        trm = ComponentMapper.getFor(TransformComponent.class);
        tm = ComponentMapper.getFor(TextureComponent.class);

        renderQueue = new Array<>();
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(new ZComparator());
        batch.begin();
        for (Entity entity : renderQueue) {
            TextureComponent tex = tm.get(entity);
            TransformComponent transform = trm.get(entity);

            if (tex.region == null) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();
            float originX = width / 2f; // Typically, the origin is set to the center for rotation
            float originY = height / 2f;

            batch.draw(tex.region,
                    transform.position.x - originX, transform.position.y - originY, // Drawing position adjusted for origin
                    originX, originY, // Origin for rotation and scaling
                    width, height, // Width and height of the texture region
                    transform.scale.x, transform.scale.y, // Scale X, Scale Y
                    transform.rotation.z); // Rotation around the Z axis, assuming degrees
        }
        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    private static class ZComparator implements Comparator<Entity> {
        ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum(pm.get(e1).position.z - pm.get(e2).position.z);
        }
    }
}
