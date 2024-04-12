package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OptionsState extends State{
    public OptionsState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
