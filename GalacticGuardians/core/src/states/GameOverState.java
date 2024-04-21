package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.AudioManager;
import com.tdt4240gr18.game.DatabaseInterface;
import com.tdt4240gr18.game.MenuButton;
import com.tdt4240gr18.game.UserSession;
import com.tdt4240gr18.game.ggTexture;


public class GameOverState extends State{
    private static final String TITLE_TEXT = "Game Over";
    private static final String NEW_GAME_TEXT = "Start a new game?";
    private static final float SCALE_FACTOR = 0.85f;
    private static final float TEXT_SCALE = 2.5f;
    private static final float NEW_GAME_TEXT_SCALE = 1.7f;
    private static final float BTN_SCALE_FACTOR = 0.2f;
    private static final float BTN_OFFSET_FACTOR = 0.1f;
    private final AudioManager audioManager;
    private final Texture menu;
    private final Texture yesNoBtn;
    private float menuWidth;
    private float menuHeight;
    private float menuX;
    private float menuY;
    private float titleX;
    private float titleY;
    private final BitmapFont fontTitle;
    private final BitmapFont fontNewGame;
    private final GlyphLayout layout;
    private final GlyphLayout newGameLayout;
    private final DatabaseInterface databaseInterface;
    private MenuButton yesButton;
    private MenuButton noButton;



    public GameOverState(GameStateManager gsm, DatabaseInterface databaseInterface, int score) {
        super(gsm);
        this.databaseInterface = databaseInterface;
        audioManager = AudioManager.getInstance();
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
        if (UserSession.getInstance().isLoggedIn()) {
            databaseInterface.getScoreFromLeaderboard(UserSession.getInstance().getUsername(), new DatabaseInterface.OnEntryLoadedListener() {
                @Override
                public void onSuccess(String entry) {
                    int highScore = Integer.parseInt(entry);
                    if (score > highScore) {
                        databaseInterface.addScoreToLeaderboard(UserSession.getInstance().getUsername(), score);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Gdx.app.log("Database Error", errorMessage);
                }
            });
        }


    }

    public void exitToMenu() {
        while ((gsm.peek() instanceof PlayState || gsm.peek() instanceof GameOverState) && gsm.getStack().size() > 1) {
            gsm.popAndReturn().dispose();
        }
    }

    private void initializeMenu() {
        menuWidth = Gdx.graphics.getWidth() * SCALE_FACTOR;
        menuHeight = menu.getHeight() * (menuWidth / menu.getWidth());
        menuX = (Gdx.graphics.getWidth() - menuWidth) / 2;
        menuY = (float) Gdx.graphics.getHeight() /2 ;
    }

    private void initializeTitle() {
        float titleWidth = layout.width;
        float titleHeight = layout.height;
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
        float buttonWidthRatio = 0.2f;

        // Create the "Yes" button
        yesButton = new MenuButton(new ggTexture("YesNoBtn.png", buttonWidthRatio ), "Yes", btnX, btnY);

        // Create the "No" button
        noButton = new MenuButton(new ggTexture("YesNoBtn.png", buttonWidthRatio), "No", btnX + btnWidth + btnOffset, btnY);
    }

    @Override
    protected void handleInput() {
        // Handle input for button clicks
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the "Yes" button is clicked
            if (yesButton.isClicked(touchX, touchY)) {
                audioManager.playButtonSound();
                exitToMenu();
                gsm.push(new PlayState(gsm, databaseInterface));
            }

            // Check if the "No" button is clicked
            if (noButton.isClicked(touchX, touchY)) {
                audioManager.playButtonSound();
                exitToMenu();
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

        // Draw the buttons
        yesButton.render(sb);
        noButton.render(sb);

        sb.end();
    }

    @Override
    public void dispose() {
        menu.dispose();
        yesButton.dispose();
        noButton.dispose();
        fontNewGame.dispose();
        fontTitle.dispose();
    }
}
