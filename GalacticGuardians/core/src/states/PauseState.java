package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.List;

public class PauseState extends State {
    private static final String TITLE_TEXT = "Pause";
    private static final float SCALE_FACTOR = 0.85f;
    private static final float TEXT_SCALE = 4.6f;
    private static final float ICON_SCALE_FACTOR = 0.2f;
    private static final float ICON_OFFSET_FACTOR = 0.1f;

    private final Texture pauseMenu;
    private final BitmapFont fontTitle;
    private final List<Texture> icons;
    private final GlyphLayout layout;
    private Rectangle iconBounds;

    private float menuWidth;
    private float menuHeight;
    private float menuX;
    private float menuY;
    private float titleWidth;
    private float titleHeight;
    private float titleX;
    private float titleY;
    private float iconSize;
    private float iconOffset;
    private float iconX;
    private float iconY;
    private PlayState playState;

    protected PauseState(GameStateManager gsm, PlayState playState) {
        super(gsm);
        this.playState = playState;
        pauseMenu = new Texture("pauseMenu.png");
        icons = Arrays.asList(new Texture("playIcon.png"), new Texture("restartIcon.png"));
        fontTitle = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontTitle.getData().setScale(TEXT_SCALE);
        layout = new GlyphLayout(fontTitle, TITLE_TEXT);
        initializeMenu();
        initializeTitle();
        initializeIcons();
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the play icon is clicked
            if (new Rectangle(iconX, iconY, iconSize, iconSize).contains(touchX, touchY)) {
                // Resume the game
                playState.togglePaused();
                gsm.pop();
            }
            // Check if the new game icon is clicked
            else if (new Rectangle(iconX + (iconSize + iconOffset), iconY, iconSize, iconSize).contains(touchX, touchY)) {
                // Start a new game
                gsm.set(new PlayState(gsm));
            }
        }
    }


    @Override
    public void update(float dt) {
        handleInput();
    }

    private void initializeMenu() {
        menuWidth = Gdx.graphics.getWidth() * SCALE_FACTOR;
        menuHeight = pauseMenu.getHeight() * (menuWidth / pauseMenu.getWidth());
        menuX = (Gdx.graphics.getWidth() - menuWidth) / 2;
        menuY = (float) Gdx.graphics.getHeight() /2 ;
    }

    private void initializeTitle() {
        titleWidth = layout.width;
        titleHeight = layout.height;
        titleX = (Gdx.graphics.getWidth() - titleWidth) / 2;
        titleY = menuY + (menuHeight * 1.07f) - titleHeight;
    }

    private void initializeIcons() {
        iconSize = Gdx.graphics.getWidth() * ICON_SCALE_FACTOR;
        iconOffset = Gdx.graphics.getWidth() * ICON_OFFSET_FACTOR;
        iconX = menuX + (menuWidth - ((icons.size() - 1) * iconOffset + icons.size() * iconSize)) / 2;
        iconY = menuY + (menuHeight * 0.4f) - (iconSize / 2);
        iconBounds = new Rectangle(iconX, iconY, iconSize, iconSize);
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.begin();
        sb.draw(pauseMenu, menuX, menuY, menuWidth, menuHeight);
        fontTitle.draw(sb, layout, titleX, titleY);
        for (int i = 0; i < icons.size(); i++) {
            sb.draw(icons.get(i), iconX + i * (iconSize + iconOffset), iconY, iconSize, iconSize);
        }
        sb.end();


    }

    @Override
    public void dispose() {
        pauseMenu.dispose();
    }
}
