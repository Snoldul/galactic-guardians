package com.tdt4240gr18.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tdt4240gr18.game.entity.components.TextureComponent;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;
import com.tdt4240gr18.game.entity.systems.MovementSystem;
import com.tdt4240gr18.game.entity.systems.PlayerControlSystem;
import com.tdt4240gr18.game.entity.systems.RenderingSystem;


public class PlayingState implements GameStateInterface{
    private final GalacticGuardians game;
    private final BitmapFont title = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
    private final PooledEngine engine = new PooledEngine();

    public PlayingState(GalacticGuardians game){
        SpriteBatch sb = new SpriteBatch();
        this.game = game;
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderingSystem(sb));
        createPlayer();
    }

    private void createPlayer(){
        // Create player entity and components
        Entity player = engine.createEntity();
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        // Set component values
        transform.position.set(0, 0, 0);
        texture.region = new TextureRegion(new Texture(Gdx.files.internal("Player.png")));

        // Add components to player entity
        player.add(transform);
        player.add(velocity);
        player.add(texture);

        // Add the entity to the engine
        engine.addEntity(player);
    }


    @Override
    public void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
