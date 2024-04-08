package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.MenuButton;

import java.util.ArrayList;
import java.util.List;

public class MenuState extends State{
    private final float BUTTON_OFFSET;
    private final int width;
    public int height;
    private final Texture logo;
    private final List<MenuButton> buttons;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        logo = new Texture("logo.png");
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        buttons = new ArrayList<>();
        BUTTON_OFFSET = 20;
        buttons.add(addButton("Start"));
        buttons.add(addButton("Leaderboard"));
        buttons.add(addButton("Options"));
        buttons.add(addButton("Quiit"));
    }

    private MenuButton addButton(String text) {
        Texture buttonTexture = new Texture("menuBtn.png");
        float x = (float) (width - buttonTexture.getWidth()) / 2;
        float y = ((float) height / 2) * 0.7f - buttons.size() * (buttonTexture.getHeight() + BUTTON_OFFSET);
        return new MenuButton(buttonTexture, text, x, y);
    }



    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = height - Gdx.input.getY();

            for (MenuButton button : buttons) {
                if (button.isClicked(touchX, touchY)) {
                    // Burde egentlig bruke push og pop i stedet for set men får ikke til, skjermen blir bare svart etter å pop-e
                    if (button.getButtonText().equals("Start")) {
                        gsm.set(new PlayState(gsm));
                        dispose();
                    }
                    if (button.getButtonText().equals("Leaderboard")) {
                        gsm.set(new LeaderboardState(gsm));
                        dispose();
                    }
                }
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(logo, (float) ((width - logo.getWidth()) / 2),height - logo.getHeight());
        for (MenuButton button : buttons) {
            button.render(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        logo.dispose();
        for (MenuButton button : buttons) {
            button.dispose();
        }
    }
}
