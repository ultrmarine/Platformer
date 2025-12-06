package io.github.Platformer.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

public class AudioService implements Music{
    private Music music;

    public AudioService(String fileName) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/"+ fileName));
    }

    public void play(){
        music.play();
    }

    @Override
    public void pause() {
        music.pause();
    }

    public void stop(){
        music.stop();
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void setLooping(boolean isLooping) {

    }

    @Override
    public boolean isLooping() {
        return false;
    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public float getVolume() {
        return 0;
    }

    @Override
    public void setPan(float pan, float volume) {

    }

    @Override
    public void setPosition(float position) {

    }

    @Override
    public float getPosition() {
        return 0;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {

    }

}
