package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240gr18.game.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionsState extends State{
    private static final float OPTION_OFFSET = 20f;
    private static final float TEXT_SCALE = 3f;
    private static final String TITLE_TEXT = "Options";

    private BitmapFont fontTitle;
    private final List<Option> options = new ArrayList<>();

    private GlyphLayout layout;
    private Texture optionsMenu;
    private Rectangle xBtnBounds;
    private Texture xBtn;
    private int width;
    private int height;
    private float newHeight;
    private float newWidth;
    private float menuPosX;
    private float menuPosY;
    private Texture onTexture;
    private Texture offTexture;

    public OptionsState(GameStateManager gsm) {
        super(gsm);
        initializeTextures();
        initializeDimensions();
        initializeFont();
        initializeOptions();
        initializeXButtonBounds();
    }

    public void initializeTextures(){
        optionsMenu = new Texture("optionsBackground.png");
        xBtn = new Texture("xBtn.png");
        onTexture = new Texture("onBtn.png");
        offTexture = new Texture("offBtn.png");
    }

    public void initializeDimensions(){
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        newWidth = width * 0.95f;
        newHeight = ((float) optionsMenu.getHeight() / (float) optionsMenu.getWidth()) * newWidth;
        // X and Y pos of optionsMenu
        menuPosX = (width - newWidth) / 2;
        menuPosY = (height - newHeight) / 2 + height * 0.05f;
    }

    private void initializeFont(){
        fontTitle = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontTitle.getData().setScale(TEXT_SCALE);
    }

    public void initializeOptions(){
        addOption("Music");
        addOption("Sound");
        addOption("Tutorial");
    }

    private void initializeXButtonBounds(){
        float xBtnWidth = width * 0.15f;
        float xBtnHeight = xBtnWidth;
        float xButtonX = (width - xBtnWidth) / 2;
        float xButtonY = (height - menuPosY - newHeight) + xBtnHeight;
        xBtnBounds = new Rectangle(xButtonX, xButtonY, xBtnWidth, xBtnHeight);
    }
    public void addOption(String name) {
        // Space from top of screen to first option
        float additionalOffset = 400f;
        float optionHeight = height * 0.1f;
        float optionWidth = (float) optionsMenu.getWidth();
        float optionY = menuPosY + newHeight - (options.size() + 1) * (optionHeight + OPTION_OFFSET) - additionalOffset;
        float optionX = menuPosX + (newWidth-optionWidth)/2;
        Rectangle bounds = new Rectangle(optionX, optionY, optionWidth, optionHeight);
        options.add(new Option(name, onTexture, offTexture, bounds));
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = height - Gdx.input.getY();
            if (xBtnBounds.contains(x, y)) {
                gsm.pop();
            }
            for (Option option : options){
                // If the touch was within the option bounds, toggle the option
                if (option.contains(x,y)){
                    option.toggle();
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
        sb.draw(optionsMenu,menuPosX, menuPosY, newWidth, newHeight);
        sb.draw(xBtn, xBtnBounds.x, xBtnBounds.y, xBtnBounds.width, xBtnBounds.height);
    }

    private void renderTitle(SpriteBatch sb){
        layout = new GlyphLayout(fontTitle, TITLE_TEXT);
        float titleWidth = layout.width;
        float titleHeight = layout.height;
        float titleX = menuPosX + ((newWidth - titleWidth) / 2);
        float titleY = menuPosY + newHeight - titleHeight - newHeight * 0.02f;
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
