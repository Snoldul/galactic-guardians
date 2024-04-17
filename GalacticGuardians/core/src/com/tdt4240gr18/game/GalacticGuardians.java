package com.tdt4240gr18.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import states.GameStateManager;
import states.MenuState;

public class GalacticGuardians extends ApplicationAdapter {
	private final DatabaseInterface databaseInterface;
	private GameStateManager gsm;
	private SpriteBatch batch;
	private Music music;

	public GalacticGuardians(DatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		//databaseInterface.fetchDataFromDatabase();
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm, databaseInterface));

		Music music = Gdx.audio.newMusic(Gdx.files.internal("RetroMusic.mp3"));
		music.setLooping(true);
		music.setVolume(1f);
		music.play();
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

