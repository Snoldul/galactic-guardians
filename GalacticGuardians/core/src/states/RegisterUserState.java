package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.tdt4240gr18.game.MenuButton;
import com.tdt4240gr18.game.ggTexture;

import java.util.Arrays;

public class RegisterUserState  extends State{

    private static final float OPTION_OFFSET = 2f;
    private static final float TITLE_SCALE = 3f;
    private static final float FONT_SCALE = 1.5f;
    private static final String TITLE_TEXT = "Register";

    private BitmapFont fontTitle, font;
    private GlyphLayout titleLayout, entryLayout;
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
    private MenuButton loginButton;
    private MenuButton registerButton;
    private String username = "Johannes123", email = "", password = "", passwordAst = "";
    private Input.TextInputListener emailListener, usernameListener, passwordListener;
    private Rectangle emailBounds, usernameBounds, passwordBounds;
    private float offsetFromTop;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private String loginRegisterPrompt;
    private GlyphLayout loginRegisterPromtLayout;
    private int loginPromtY;
    private char[] asterisks;
    private float titleX, titleY;

    public RegisterUserState(GameStateManager gsm) {
        super(gsm);
        initializeTextures();
        initializeDimensions();
        initializeFont();
        initializeXButtonBounds();
        initializeMenuButtons();
        initializeTextFields();
    }

    private void initializeTextFields(){
        float inputWidth = menuWidth * 0.8f;
        emailListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                email = text;
                //options.get(0).setOptionText(email);
            }

            @Override
            public void canceled() {
                email = "";
            }
        };
        usernameListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                if (text.length() > 16) {
                    text = text.substring(0, 16);
                }
                username = text;
                //options.get(1).setOptionText(username);
            }

            @Override
            public void canceled() {
                username = "Username";
            }
        };
        passwordListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                password = text;
                asterisks = new char[password.length()];
                Arrays.fill(asterisks, '*');
                passwordAst = new String(asterisks);
            }

            @Override
            public void canceled() {
                password = "Password";
            }
        };

        emailBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (1) * (onTexture.getHeight() * OPTION_OFFSET) - offsetFromTop, inputWidth, onTexture.getHeight());
        usernameBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (2) * (onTexture.getHeight() * OPTION_OFFSET) - offsetFromTop, inputWidth, onTexture.getHeight());
        passwordBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (3) * (onTexture.getHeight() * OPTION_OFFSET) - offsetFromTop, inputWidth, onTexture.getHeight());

    }

    private void initializeMenuButtons(){
        float buttonWidthRatio = 0.7f;
        registerButton = addButton("Register", buttonWidthRatio, (int) (menuPosY + menuHeight * 0.1f));
        loginPromtY = (int) (menuHeight * 0.1f + registerButton.getTexture().getHeight() + font.getLineHeight());
        loginButton = addButton("Login", buttonWidthRatio, (int) (menuHeight * 0.1f));
    }

    private MenuButton addButton(String text, float buttonWidthRatio, int y) {
        ggTexture buttonTexture = new ggTexture("menuBtn.png", buttonWidthRatio);
        float x = (float) (width - buttonTexture.getWidth()) / 2;
        return new MenuButton(buttonTexture, text, x, y);
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
        menuPosY = (height - menuHeight) / 2 + height * 0.05f;
        offsetFromTop = 0.2f * menuHeight;
    }

    private void initializeFont(){
        fontTitle = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontTitle.getData().setScale(TITLE_SCALE);
        font = new BitmapFont(Gdx.files.internal("RetroText.fnt"));
        font.getData().setScale(FONT_SCALE);

        entryLayout = new GlyphLayout();
        titleLayout = new GlyphLayout(fontTitle, TITLE_TEXT);
        titleX = menuPosX + ((menuWidth - titleLayout.width) / 2);
        titleY = menuPosY + menuHeight - titleLayout.height - menuHeight * 0.02f;
        loginRegisterPrompt = "Already created a user?";
        loginRegisterPromtLayout = new GlyphLayout(font, loginRegisterPrompt);
    }

    private void initializeXButtonBounds(){
        float xBtnWidth = xBtn.getWidth();
        float xBtnHeight = xBtn.getHeight();
        float xButtonX = (width - xBtnWidth) / 2;
        float xButtonY = menuPosY - xBtnHeight / 2 + 1030f / 30 / 2; // 1030 is height of current menu, 30 is height of bottow row
        xBtnBounds = new Rectangle(xButtonX, xButtonY, xBtnWidth, xBtnHeight);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = height - Gdx.input.getY();
            if (xBtnBounds.contains(x, y)) {
                gsm.pop();
            }
            if (emailBounds.contains(x, y)) {
                Gdx.input.getTextInput(emailListener, "Email", "", "Enter your email");
            }
            if (usernameBounds.contains(x, y)) {
                Gdx.input.getTextInput(usernameListener, "Username", "", "Enter your username");
            }
            if (passwordBounds.contains(x, y)) {
                Gdx.input.getTextInput(passwordListener, "Password", "", "Enter your password");
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
        loginButton.render(sb);
        registerButton.render(sb);
        renderTextField(sb, "Email", email, emailBounds);
        renderTextField(sb, "Username", username, usernameBounds);
        renderTextField(sb, "Password", passwordAst, passwordBounds);
        fontTitle.setColor(Color.WHITE);
        font.draw(sb, loginRegisterPrompt, (width - loginRegisterPromtLayout.width) / 2, loginPromtY);

        sb.end();
    }

    private void renderTextField(SpriteBatch sb, String infoText, String entryText, Rectangle bounds){
        if (entryText.isEmpty()) {
            fontTitle.setColor(0.7f,0.7f,0.7f,1); // Set the color to red if the entryText is empty
        } else {
            fontTitle.setColor(Color.WHITE); // Set the color to white if the entryText is not empty
        }
        fontTitle.draw(sb, infoText, bounds.x, bounds.y + bounds.height);

        entryLayout.setText(font, entryText, Color.WHITE, bounds.width, Align.left, true);

        font.draw(sb, entryLayout, bounds.x, bounds.y);

    }

    private void renderOptionsMenu(SpriteBatch sb){
        sb.draw(optionsMenu,menuPosX, menuPosY, menuWidth, menuHeight);
        sb.draw(xBtn, xBtnBounds.x, xBtnBounds.y, xBtnBounds.width, xBtnBounds.height);
    }

    private void renderTitle(SpriteBatch sb){
        fontTitle.draw(sb, titleLayout, titleX, titleY);
    }

    @Override
    public void dispose() {
        optionsMenu.dispose();
        xBtn.dispose();
        onTexture.dispose();
        offTexture.dispose();
        shapeRenderer.dispose();
        font.dispose();
        fontTitle.dispose();
    }
}
