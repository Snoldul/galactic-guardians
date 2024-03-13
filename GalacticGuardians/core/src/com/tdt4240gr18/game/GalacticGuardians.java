package com.tdt4240gr18.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GalacticGuardians extends ApplicationAdapter {

	GameStateInterface currentState;
	SpriteBatch batch;
	public void changeState(GameStateInterface newState){
		currentState = newState;
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		currentState = new StartScreenState(this);
	}

	@Override
	public void render () {

		if(currentState == null){
			return;
		}
		float dt = Gdx.graphics.getDeltaTime();
		currentState.handleInput(dt);
		currentState.update(dt);
		currentState.render(batch);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
