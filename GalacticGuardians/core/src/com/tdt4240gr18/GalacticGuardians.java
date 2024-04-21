package com.tdt4240gr18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.services.audio.AudioManager;
import com.tdt4240gr18.services.database.DatabaseInterface;
import com.tdt4240gr18.states.GameStateManager;
import com.tdt4240gr18.states.MenuState;

public class GalacticGuardians extends ApplicationAdapter {
	private final DatabaseInterface databaseInterface;
	private GameStateManager gsm;
	private SpriteBatch batch;

	public GalacticGuardians(DatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		//databaseInterface.fetchDataFromDatabase();
		gsm = new GameStateManager();
		AudioManager audioManager = AudioManager.getInstance();
		audioManager.playMusic();
		gsm.push(new MenuState(gsm, databaseInterface));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}

}

