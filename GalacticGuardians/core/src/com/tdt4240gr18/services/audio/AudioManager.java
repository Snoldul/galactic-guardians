package com.tdt4240gr18.services.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static AudioManager instance;
    private final Music music;
    private final Sound buttonSound;
    private final Sound deathSound;
    private final Sound hitSound;
    private final Sound laserSound;
    private boolean musicOn;
    private boolean soundsOn;

    private AudioManager() {
        music = Gdx.audio.newMusic(Gdx.files.internal("RetroMusic.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        musicOn = true;
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("ButtonSound.wav"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("DeathSound.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("HitSound.mp3"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("LaserSound.wav"));
        soundsOn = true;
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playMusic() {
        music.play();
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void toggleMuteMusic() {
        if (music.getVolume() == 0.1f) {
            music.setVolume(0f);
            musicOn = false;
        } else {
            music.setVolume(0.1f);
            musicOn = true;
        }
    }

    public boolean isSoundsOn() {
        return soundsOn;
    }

    public void toggleMuteSounds() {
        soundsOn = !soundsOn;
    }

    public void playButtonSound() {
        if (soundsOn) {
            buttonSound.play();
        }
    }

    public void playDeathSound() {
        if (soundsOn) {
            deathSound.play();
        }
    }

    public void playHitSound() {
        if (soundsOn) {
            hitSound.play(0.1f);
        }
    }

    public void playLaserSound() {
        if (soundsOn) {
            laserSound.play(0.1f);
        }
    }

    public void dispose() {
        music.dispose();
        buttonSound.dispose();
        deathSound.dispose();
        hitSound.dispose();
        laserSound.dispose();
    }
}
