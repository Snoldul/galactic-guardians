package com.tdt4240gr18.game.entity.systems;


import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tdt4240gr18.game.entity.components.CollisionComponent;
import com.tdt4240gr18.game.entity.components.ExplosionComponent;
import com.tdt4240gr18.game.entity.components.PlayerComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;
public class ExplosionSystem extends IteratingSystem {

    private final Texture explosion1Texture;
    private final Texture explosion2Texture;
    private final Texture explosion3Texture;
    private final Texture explosion4Texture;
    private final Texture explosion5Texture;
    private final Texture explosion6Texture;
    private final Texture explosion7Texture;

    private PooledEngine engine;

    public ExplosionSystem(PooledEngine engine) {
        super(Family.all(ExplosionComponent.class).get());
        this.engine = engine;
        explosion1Texture = new Texture("explosion1.png");
        explosion2Texture = new Texture("explosion2.png");
        explosion3Texture = new Texture("explosion3.png");
        explosion4Texture = new Texture("explosion4.png");
        explosion5Texture = new Texture("explosion5.png");
        explosion6Texture = new Texture("explosion6.png");
        explosion7Texture = new Texture("explosion7.png");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        TextureComponent texture = entity.getComponent(TextureComponent.class);

        int frame = explosion.frame;

        if (frame == 6) {
            texture.region = new TextureRegion(explosion2Texture);
        } else if (frame == 12) {
            texture.region = new TextureRegion(explosion3Texture);
        }else if (frame == 18) {
            texture.region = new TextureRegion(explosion4Texture);
        }else if (frame == 24) {
            texture.region = new TextureRegion(explosion5Texture);
        }else if (frame == 30) {
            texture.region = new TextureRegion(explosion6Texture);
        }else if (frame == 36) {
            texture.region = new TextureRegion(explosion7Texture);
        }else if (frame == 42) {
            engine.removeEntity(entity);
            return;
        }

        explosion.frame ++;
    }
}
