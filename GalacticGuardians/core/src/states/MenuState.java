package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.AudioManager;
import com.tdt4240gr18.game.DatabaseInterface;
import com.tdt4240gr18.game.MenuButton;
import com.tdt4240gr18.game.ggTexture;

import java.util.ArrayList;
import java.util.List;

public class MenuState extends State{
    private final float BUTTON_OFFSET;
    private final int width;
    public int height;
    private final ggTexture logo;
    public final float buttonWidthRatio;
    public final float logoWidthRatio;
    private final List<MenuButton> buttons;
    private final DatabaseInterface databaseInterface;
    private final AudioManager audioManager;

    public MenuState(GameStateManager gsm, DatabaseInterface databaseInterface) {
        super(gsm);
        this.databaseInterface = databaseInterface;
        this.audioManager = AudioManager.getInstance();
        buttonWidthRatio = 0.7f;
        logoWidthRatio = 0.9f;
        logo = new ggTexture("logo.png", logoWidthRatio);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        buttons = new ArrayList<>();
        BUTTON_OFFSET = height / 120f;
        buttons.add(addButton("Start"));
        buttons.add(addButton("Leaderboard"));
        buttons.add(addButton("Options"));
        buttons.add(addButton("Quit"));
    }

    private MenuButton addButton(String text) {
        ggTexture buttonTexture = new ggTexture("menuBtn.png", buttonWidthRatio);
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
                        audioManager.playButtonSound();
                        gsm.push(new PlayState(gsm));
                    }
                    if (button.getButtonText().equals("Leaderboard")) {
                        audioManager.playButtonSound();
                        gsm.set(new LeaderboardState(gsm, databaseInterface));
                        dispose();
                    }
                    if (button.getButtonText().equals("Options")) {
                        audioManager.playButtonSound();
                        gsm.push(new OptionsState(gsm));
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
