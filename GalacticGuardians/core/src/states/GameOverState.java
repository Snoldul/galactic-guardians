package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240gr18.game.DatabaseInterface;


public class GameOverState extends State{
    private static final String TITLE_TEXT = "Game Over";
    private static final String NEW_GAME_TEXT = "Start a new game?";
    private static final String YES_TEXT = "Yes";
    private static final String NO_TEXT = "No";
    private static final float SCALE_FACTOR = 0.85f;
    private static final float TEXT_SCALE = 2.5f;
    private static final float NEW_GAME_TEXT_SCALE = 1.7f;
    private static final float BTN_SCALE_FACTOR = 0.2f;
    private static final float BTN_OFFSET_FACTOR = 0.1f;
    private Texture menu;
    private Texture yesNoBtn;
    private float menuWidth;
    private float menuHeight;
    private float menuX;
    private float menuY;
    private float titleWidth;
    private float titleHeight;
    private float titleX;
    private float titleY;
    private float btnWidth;
    private float btnHeight;
    private float btnX;
    private float btnY;
    private float btnOffset;
    private BitmapFont fontTitle;
    private BitmapFont fontNewGame;
    private GlyphLayout layout;
    private GlyphLayout newGameLayout;
    private Rectangle btnBounds;
    private Rectangle btnBoundsYes;
    private Rectangle btnBoundsNo;


    public GameOverState(GameStateManager gsm) {
        super(gsm);

        menu = new Texture("pauseMenu.png");
        yesNoBtn = new Texture("YesNoBtn.png");
        fontTitle = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontTitle.getData().setScale(TEXT_SCALE);
        fontNewGame = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontNewGame.getData().setScale(NEW_GAME_TEXT_SCALE);
        layout = new GlyphLayout(fontTitle, TITLE_TEXT);
        newGameLayout = new GlyphLayout(fontNewGame, NEW_GAME_TEXT);
        initializeMenu();
        initializeTitle();
        initializeButtons();
    }

    private void initializeMenu() {
        menuWidth = Gdx.graphics.getWidth() * SCALE_FACTOR;
        menuHeight = menu.getHeight() * (menuWidth / menu.getWidth());
        menuX = (Gdx.graphics.getWidth() - menuWidth) / 2;
        menuY = (float) Gdx.graphics.getHeight() /2 ;
    }

    private void initializeTitle() {
        titleWidth = layout.width;
        titleHeight = layout.height;
        titleX = (Gdx.graphics.getWidth() - titleWidth) / 2;
        titleY = menuY + (menuHeight * 0.97f) - titleHeight;
    }

    private void initializeButtons() {
        float originalBtnWidth = yesNoBtn.getWidth();
        float originalBtnHeight = yesNoBtn.getHeight();
        float btnWidth = Gdx.graphics.getWidth() * BTN_SCALE_FACTOR;
        float btnHeight = btnWidth * (originalBtnHeight / originalBtnWidth); // Maintain the original aspect ratio
        float btnOffset = Gdx.graphics.getWidth() * BTN_OFFSET_FACTOR;
        float btnX = menuX + (menuWidth - (2 * btnWidth + btnOffset)) / 2;
        float btnY = menuY + (menuHeight * 0.3f) - (btnHeight / 2);

        // Rectangle for the "Yes" button
        btnBoundsYes = new Rectangle(btnX, btnY, btnWidth, btnHeight);

        // Rectangle for the "No" button
        btnBoundsNo = new Rectangle(btnX + btnWidth + btnOffset, btnY, btnWidth, btnHeight);
    }
    @Override
    protected void handleInput() {
        // Handle input for button clicks
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the "Yes" button is clicked
            if (btnBoundsYes.contains(touchX, touchY)) {
                gsm.set(new PlayState(gsm));
            }

            // Check if the "No" button is clicked
            if (btnBoundsNo.contains(touchX, touchY)) {
                gsm.pop();
                gsm.pop();
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
        sb.draw(menu, menuX, menuY, menuWidth, menuHeight);
        fontTitle.draw(sb, layout, titleX, titleY);
        fontNewGame.draw(sb, newGameLayout, (Gdx.graphics.getWidth() - newGameLayout.width) / 2, titleY - newGameLayout.height * 6);

        // Draw the "Yes" button
        sb.draw(yesNoBtn, btnBoundsYes.x, btnBoundsYes.y, btnBoundsYes.width, btnBoundsYes.height);
        GlyphLayout yesLayout = new GlyphLayout(fontNewGame, YES_TEXT);
        float yesTextX = btnBoundsYes.x + (btnBoundsYes.width - yesLayout.width) / 2;
        float yesTextY = btnBoundsYes.y + (btnBoundsYes.height + yesLayout.height) / 2;
        fontNewGame.draw(sb, YES_TEXT, yesTextX, yesTextY);

        // Draw the "No" button
        sb.draw(yesNoBtn, btnBoundsNo.x, btnBoundsNo.y, btnBoundsNo.width, btnBoundsNo.height);
        GlyphLayout noLayout = new GlyphLayout(fontNewGame, NO_TEXT);
        float noTextX = btnBoundsNo.x + (btnBoundsNo.width - noLayout.width) / 2;
        float noTextY = btnBoundsNo.y + (btnBoundsNo.height + noLayout.height) / 2;
        fontNewGame.draw(sb, NO_TEXT, noTextX, noTextY);

        sb.end();
    }

    @Override
    public void dispose() {
        menu.dispose();
    }
}