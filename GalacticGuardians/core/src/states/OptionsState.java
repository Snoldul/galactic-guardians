package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240gr18.game.AudioManager;
import com.tdt4240gr18.game.Option;
import com.tdt4240gr18.game.ggTexture;

import java.util.ArrayList;
import java.util.List;

public class OptionsState extends State{
    private static final float OPTION_OFFSET = 1.5f;
    private static final float TEXT_SCALE = 3f;
    private static final String TITLE_TEXT = "Options";

    private BitmapFont fontTitle;
    private final List<Option> options = new ArrayList<>();
    private final AudioManager audioManager;

    private Texture optionsMenu;
    private Rectangle xBtnBounds;
    private Texture xBtn;
    private int width;
    private int height;
    private float menuHeight;
    private float menuWidth;
    private float menuPosX;
    private float menuPosY;
    private Texture onTexture;
    private Texture offTexture;

    public OptionsState(GameStateManager gsm) {
        super(gsm);
        this.audioManager = AudioManager.getInstance();
        initializeTextures();
        initializeDimensions();
        initializeFont();
        initializeOptions();
        initializeXButtonBounds();
    }

    public void initializeTextures(){
        float widthRatio = 0.95f;
        optionsMenu = new ggTexture("optionsBackground.png", widthRatio);
        float xBtnWidthRatio = 0.15f;
        xBtn = new ggTexture("xBtn.png", xBtnWidthRatio);
        onTexture = new ggTexture("onBtn.png", 0.15f);
        offTexture = new ggTexture("offBtn.png", 0.15f);
    }

    public void initializeDimensions(){
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        menuWidth = optionsMenu.getWidth();
        menuHeight = optionsMenu.getHeight();
        // X and Y pos of optionsMenu
        menuPosX = (width - menuWidth) / 2;
        menuPosY = (height - menuHeight);
    }

    private void initializeFont(){
        fontTitle = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontTitle.getData().setScale(TEXT_SCALE);
    }

    public void initializeOptions(){
        addOption("Music", audioManager.isMusicOn());
        addOption("Sound", audioManager.isSoundsOn());
    }

    private void initializeXButtonBounds(){
        float xBtnWidth = xBtn.getWidth();
        float xBtnHeight = xBtn.getHeight();
        float xButtonX = (width - xBtnWidth) / 2;
        float xButtonY = menuPosY - xBtnHeight / 2 + 1030f / 30 / 2; // 1030 is height of current menu, 30 is height of bottow row
        xBtnBounds = new Rectangle(xButtonX, xButtonY, xBtnWidth, xBtnHeight);
    }
    public void addOption(String text, boolean soundsOn) {
        Rectangle optionBounds = calculateOptionBounds();
        Rectangle buttonBounds = calculateButtonBounds(optionBounds);
        options.add(new Option(text, onTexture, offTexture, optionBounds, buttonBounds, soundsOn));
    }

    private Rectangle calculateOptionBounds(){
        // Space from top of screen to first option
        float additionalOffset = 0.25f * menuHeight;
        float optionWidth = menuWidth * 0.8f;
        float optionHeight = onTexture.getHeight() * 1.5f;
        float optionX = menuPosX + (menuWidth - optionWidth) / 2;
        float optionY = menuPosY + menuHeight - (options.size() + 1) * (optionHeight * OPTION_OFFSET) - additionalOffset;
        return new Rectangle(optionX, optionY, optionWidth, optionHeight);
    }

    private Rectangle calculateButtonBounds(Rectangle optionBounds){
        float buttonWidth = onTexture.getWidth();
        float buttonHeight = onTexture.getHeight();
        float imageX = optionBounds.x + optionBounds.width - buttonWidth;
        float imageY = optionBounds.y + (optionBounds.height - buttonHeight) / 2;
        return new Rectangle(imageX, imageY, buttonWidth, buttonHeight);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = height - Gdx.input.getY();
            if (xBtnBounds.contains(x, y)) {
                audioManager.playButtonSound();
                // Her burde singleton bli brukt for å lagre options
                gsm.popAndReturn().dispose();
            }
            for (Option option : options){
                // If the touch was within the option bounds, toggle the option
                if (option.contains(x,y)){
                    if (option.getOptionText().equals("Music")) {
                        audioManager.toggleMuteMusic();
                        audioManager.playButtonSound();
                        option.toggle();
                    }
                    if (option.getOptionText().equals("Sound")) {
                        audioManager.toggleMuteSounds();
                        audioManager.playButtonSound();
                        option.toggle();
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
        renderOptionsMenu(sb);
        renderTitle(sb);
        renderOptions(sb);
        sb.end();
    }

    private void renderOptionsMenu(SpriteBatch sb){
        sb.draw(optionsMenu,menuPosX, menuPosY, menuWidth, menuHeight);
        sb.draw(xBtn, xBtnBounds.x, xBtnBounds.y, xBtnBounds.width, xBtnBounds.height);
    }

    private void renderTitle(SpriteBatch sb){
        GlyphLayout layout = new GlyphLayout(fontTitle, TITLE_TEXT);
        float titleWidth = layout.width;
        float titleHeight = layout.height;
        float titleX = menuPosX + ((menuWidth - titleWidth) / 2);
        float titleY = menuPosY + menuHeight - titleHeight - menuHeight * 0.02f;
        fontTitle.draw(sb, layout, titleX, titleY);
    }
    private void renderOptions(SpriteBatch sb){
        for (Option option : options){
            option.render(sb);
        }
    }

    @Override
    public void dispose() {
        optionsMenu.dispose();
        xBtn.dispose();
        onTexture.dispose();
        offTexture.dispose();
        for (Option option : options){
            option.dispose();
        }
    }
}
