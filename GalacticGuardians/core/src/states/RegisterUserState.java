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
import com.tdt4240gr18.game.AudioManager;
import com.tdt4240gr18.game.DatabaseInterface;
import com.tdt4240gr18.game.MenuButton;
import com.tdt4240gr18.game.UserSession;
import com.tdt4240gr18.game.ggTexture;

import java.util.Arrays;

public class RegisterUserState  extends State{

    private static final float OPTION_OFFSET = 2f;
    private static final float TITLE_SCALE = 3f;
    private static final float FONT_SCALE = 1.5f;
    private static final String TITLE_TEXT = "Register";

    private BitmapFont fontTitle, font, invalidFont;
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
    private MenuButton loginButton, registerButton, backButton;
    private String username = "", email = "", password = "", passwordAst = "";
    private Input.TextInputListener emailListener, usernameListener, passwordListener;
    private Rectangle emailBounds, usernameBounds, passwordBounds;
    private float offsetFromTop;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private GlyphLayout loginPromptLayout;
    private int loginPromtY;
    private char[] asterisks;
    private float titleX, titleY;
    private boolean validEmail = false, validUsername = false, validPassword = false;
    private final DatabaseInterface databaseInterface;
    private final AudioManager audioManager;
    private String invalidEmail, invalidUsername, invalidPassword;
    State tempState;


    public RegisterUserState(GameStateManager gsm, DatabaseInterface databaseInterface) {
        super(gsm);
        audioManager = AudioManager.getInstance();
        initializeTextures();
        initializeDimensions();
        initializeFont();
        initializeXButtonBounds();
        initializeMenuButtons();
        initializeTextFields();
        this.databaseInterface = databaseInterface;
    }

    public void exitToMenu() {
        while ((gsm.peek() instanceof RegisterUserState || gsm.peek() instanceof LoginState) && gsm.getStack().size() > 1) {
            gsm.popAndReturn().dispose();
        }
    }

    public boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void initializeTextFields(){
        float inputWidth = menuWidth * 0.8f;
        emailListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                email = text.toLowerCase();
                validEmail = isEmailValid(text);
            }

            @Override
            public void canceled() {
                email = "";
                validEmail = false;
            }
        };
        usernameListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                if (text.length() > 16) {
                    username = text.substring(0, 16).toLowerCase();
                    validUsername = false;
                }
                else {
                    username = text.toLowerCase();
                    validUsername = true;
                }
            }

            @Override
            public void canceled() {
                username = "";
                validUsername = false;
            }
        };
        passwordListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                validPassword = text.length() >= 8;
                password = text;
                asterisks = new char[password.length()];
                Arrays.fill(asterisks, '*');
                passwordAst = new String(asterisks);
            }

            @Override
            public void canceled() {
                password = "";
                passwordAst = "";
                validPassword = false;
            }
        };

        emailBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (1) * (fontTitle.getLineHeight() * OPTION_OFFSET) - offsetFromTop - invalidFont.getLineHeight(), inputWidth, fontTitle.getLineHeight());
        usernameBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (2) * (fontTitle.getLineHeight() * OPTION_OFFSET) - offsetFromTop - invalidFont.getLineHeight(), inputWidth, fontTitle.getLineHeight());
        passwordBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (3) * (fontTitle.getLineHeight() * OPTION_OFFSET) - offsetFromTop - invalidFont.getLineHeight(), inputWidth, fontTitle.getLineHeight());

    }

    private void initializeMenuButtons(){
        float buttonWidthRatio = 0.7f;
        int BUTTON_OFFSET = height / 120;
        backButton = addButton("Back", buttonWidthRatio, BUTTON_OFFSET);
        registerButton = addButton("Register", buttonWidthRatio, (int) (menuPosY + menuHeight * 0.1f));
        loginPromtY = (int) (BUTTON_OFFSET * 2 + backButton.getTexture().getHeight() * 2 + font.getLineHeight());
        loginButton = addButton("Login", buttonWidthRatio, BUTTON_OFFSET * 2 + backButton.getTexture().getHeight());
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
    }

    public void initializeDimensions(){
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        menuWidth = optionsMenu.getWidth();
        menuHeight = optionsMenu.getHeight();
        menuPosX = (width - menuWidth) / 2;
        menuPosY = (height - menuHeight);
        offsetFromTop = 0.1f * menuHeight;
    }

    private void initializeFont(){
        fontTitle = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontTitle.getData().setScale(TITLE_SCALE);
        font = new BitmapFont(Gdx.files.internal("RetroText.fnt"));
        font.getData().setScale(FONT_SCALE);
        invalidFont = new BitmapFont(Gdx.files.internal("RetroText.fnt"));
        invalidFont.setColor(Color.RED);

        invalidEmail = "Invalid email";
        invalidUsername = "Max 16 characters";
        invalidPassword = "Min 8 characters";

        entryLayout = new GlyphLayout();
        titleLayout = new GlyphLayout(fontTitle, TITLE_TEXT);
        titleX = menuPosX + ((menuWidth - titleLayout.width) / 2);
        titleY = menuPosY + menuHeight - titleLayout.height - menuHeight * 0.02f;
        String loginPrompt = "Already created a user?";
        loginPromptLayout = new GlyphLayout(font, loginPrompt);
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
            if (backButton.isClicked(x, y) || xBtnBounds.contains(x, y)) {
                audioManager.playButtonSound();
                exitToMenu();
            }
            if (emailBounds.contains(x, y)) {
                Gdx.input.getTextInput(emailListener, "Email", email, "Enter your email");
            }
            if (usernameBounds.contains(x, y)) {
                Gdx.input.getTextInput(usernameListener, "Username", username, "Enter your username");
            }
            if (passwordBounds.contains(x, y)) {
                Gdx.input.getTextInput(passwordListener, "Password", "", "Enter your password");
            }
            if (loginButton.isClicked(x, y)) {
                audioManager.playButtonSound();
                tempState = gsm.getStateAt(gsm.getStack().size() - 2);
                if (tempState instanceof LoginState) {
                    gsm.pushToTop(tempState);
                } else {
                    gsm.push(new LoginState(gsm, databaseInterface));
                }
            }
            if (registerButton.isClicked(x, y)) {
                if (validEmail && validUsername && validPassword) {
                    audioManager.playButtonSound();
                    databaseInterface.checkIfUserExists(username, email, new DatabaseInterface.OnCheckUserListener() {
                        @Override
                        public void onSuccess(boolean exists) {
                            if (exists) {
                                gsm.push(new ErrorState(gsm, "User already exists", font));
                            } else {
                                databaseInterface.registerUser(email, username, password, new DatabaseInterface.OnRegistrationListener() {
                                    @Override
                                    public void onSuccess() {
                                        //Log in user
                                        databaseInterface.loginUser(email, password, new DatabaseInterface.OnLoginListener() {
                                            @Override
                                            public void onSuccess() {
                                                UserSession.getInstance().setUsername(username);
                                                UserSession.getInstance().setIsLoggedIn(true);
                                                exitToMenu();
                                            }

                                            @Override
                                            public void onFailure(String errorMessage) {
                                                Gdx.app.log("Register State Error", "Login failed");
                                                gsm.push(new ErrorState(gsm, "Login after registration failed", font));                                }
                                        });
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        // Show error message
                                        gsm.push(new ErrorState(gsm, "Registration failed", font));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            gsm.push(new ErrorState(gsm, "Failed to check if user exists", font));
                        }
                    });

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
        loginButton.render(sb);
        if (validEmail && validUsername && validPassword) {
            registerButton.render(sb);
        }
        backButton.render(sb);
        renderTextField(sb, "Email", email, emailBounds, validEmail, invalidEmail);
        renderTextField(sb, "Username", username, usernameBounds, validUsername, invalidUsername);
        renderTextField(sb, "Password", passwordAst, passwordBounds, validPassword, invalidPassword);
        fontTitle.setColor(Color.WHITE);
        font.draw(sb, loginPromptLayout, (width - loginPromptLayout.width) / 2, loginPromtY);
        sb.end();
    }

    private void renderTextField(SpriteBatch sb, String infoText, String entryText, Rectangle bounds, boolean valid, String invalidText) {
        if (entryText.isEmpty()) {
            fontTitle.setColor(Color.LIGHT_GRAY); // Set the color to red if the entryText is empty
        } else if (!valid){
            fontTitle.setColor(Color.RED); // Set the color to red if the entryText is not valid
            invalidFont.draw(sb, invalidText, bounds.x, bounds.y + bounds.height + invalidFont.getLineHeight());
        } else {
            fontTitle.setColor(Color.WHITE);
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
        shapeRenderer.dispose();
        font.dispose();
        fontTitle.dispose();
    }
}
