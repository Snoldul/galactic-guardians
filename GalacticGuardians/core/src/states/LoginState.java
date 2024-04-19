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
import com.tdt4240gr18.game.DatabaseInterface;
import com.tdt4240gr18.game.MenuButton;
import com.tdt4240gr18.game.UserSession;
import com.tdt4240gr18.game.ggTexture;

import java.util.Arrays;

public class LoginState  extends State{

    private static final float OPTION_OFFSET = 2f;
    private static final float TITLE_SCALE = 3f;
    private static final float FONT_SCALE = 1.5f;
    private static final String TITLE_TEXT = "Login";

    private BitmapFont fontTitle, font, invalidFont;
    private GlyphLayout titleLayout, entryLayout, infoLayout;
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
    private String accountEntry = "", password = "", passwordAst = "";
    private String invalidAccount, invalidPassword;
    private Input.TextInputListener accountListener, passwordListener;
    private Rectangle accountBounds, passwordBounds;
    private float offsetFromTop;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private GlyphLayout registerPromptLayout;
    private int registerPromptY;
    private char[] asterisks;
    private float titleX, titleY;
    private boolean validEmail = false, validUsername = false, validPassword = false;
    private final DatabaseInterface databaseInterface;
    State tempState;
    private String username;


    public LoginState(GameStateManager gsm, DatabaseInterface databaseInterface) {
        super(gsm);
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
        accountListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                if (isEmailValid(text)){
                    accountEntry = text.toLowerCase();
                    validEmail = true;
                }
                else {
                    validEmail = false;
                    if (text.length() > 16) {
                        text = text.substring(0, 16);
                        validUsername = false;
                    }
                    else {
                        validUsername = true;
                    }
                    accountEntry = text.toLowerCase();
                }
            }

            @Override
            public void canceled() {
                accountEntry = "";
                validEmail = false;
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

        accountBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (1) * (fontTitle.getLineHeight() * OPTION_OFFSET) - fontTitle.getLineHeight() - offsetFromTop - invalidFont.getLineHeight(), inputWidth, fontTitle.getLineHeight() * 2);
        passwordBounds = new Rectangle(menuPosX + (menuWidth - menuWidth * 0.8f) / 2,menuPosY + menuHeight - (3) * (fontTitle.getLineHeight() * OPTION_OFFSET) - offsetFromTop - invalidFont.getLineHeight(), inputWidth, fontTitle.getLineHeight());

    }

    private void initializeMenuButtons(){
        float buttonWidthRatio = 0.7f;
        int BUTTON_OFFSET = height / 120;
        backButton = addButton("Back", buttonWidthRatio, BUTTON_OFFSET);
        loginButton = addButton("Login", buttonWidthRatio, (int) (menuPosY + menuHeight * 0.1f));
        registerPromptY = (int) (BUTTON_OFFSET * 2 + backButton.getTexture().getHeight() * 2 + font.getLineHeight());
        registerButton = addButton("Register", buttonWidthRatio, BUTTON_OFFSET * 2 + backButton.getTexture().getHeight());
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

        invalidAccount = "Invalid email/username";
        invalidPassword = "Min 8 characters";

        entryLayout = new GlyphLayout();
        infoLayout = new GlyphLayout();
        titleLayout = new GlyphLayout(fontTitle, TITLE_TEXT);
        titleX = menuPosX + ((menuWidth - titleLayout.width) / 2);
        titleY = menuPosY + menuHeight - titleLayout.height - menuHeight * 0.02f;
        String registerPrompt = "Not created a user yet?";
        registerPromptLayout = new GlyphLayout(font, registerPrompt);
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
                exitToMenu();
            }
            if (accountBounds.contains(x, y)) {
                Gdx.input.getTextInput(accountListener, "Email or Username", accountEntry, "Enter your email or username");
            }
            if (passwordBounds.contains(x, y)) {
                Gdx.input.getTextInput(passwordListener, "Password", "", "Enter your password");
            }
            if (registerButton.isClicked(x, y)) {
                tempState = gsm.getStateAt(gsm.getStack().size() - 2);
                if (tempState instanceof RegisterUserState) {
                    gsm.pushToTop(tempState);
                } else {
                    gsm.push(new RegisterUserState(gsm, databaseInterface));
                }
            }
            if (loginButton.isClicked(x, y)) {
                if ((validEmail || validUsername) && validPassword) {
                    if (validUsername) {
                        username = accountEntry;
                        databaseInterface.getEmailByUsername(accountEntry, new DatabaseInterface.OnEntryLoadedListener() {
                            @Override
                            public void onSuccess(String entry) {
                                accountEntry = entry;
                                databaseInterface.loginUser(accountEntry, password, new DatabaseInterface.OnLoginListener() {
                                    @Override
                                    public void onSuccess() {
                                        UserSession.getInstance().setUsername(username);
                                        UserSession.getInstance().setIsLoggedIn(true);
                                        exitToMenu();
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        // Show error message
                                        gsm.push(new ErrorState(gsm, "Login failed", font));
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                // Show error message
                                Gdx.app.log("Email fetch failed", "Error fetching email from given username");
                                gsm.push(new ErrorState(gsm, "Email failed", font));
                            }
                        });
                    }
                    else {
                        databaseInterface.getUsernameByEmail(accountEntry, new DatabaseInterface.OnEntryLoadedListener() {
                            @Override
                            public void onSuccess(String entry) {
                                username = entry;
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                // Show error message
                                Gdx.app.log("3", "Login failed");
                                gsm.push(new ErrorState(gsm, "Username could not be retrieved from given email", font));
                            }
                        });
                        databaseInterface.loginUser(accountEntry, password, new DatabaseInterface.OnLoginListener() {
                            @Override
                            public void onSuccess() {
                                UserSession.getInstance().setUsername(username);
                                UserSession.getInstance().setIsLoggedIn(true);
                                exitToMenu();
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                // Show error message
                                Gdx.app.log("4", "Login failed");
                                gsm.push(new ErrorState(gsm, "test", font));
                            }
                        });
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
            if ((validEmail || validUsername) && validPassword) {
                loginButton.render(sb);
            }
            registerButton.render(sb);
            backButton.render(sb);
            renderTextField(sb, "Email or  username", accountEntry, accountBounds, (validEmail || validUsername), invalidAccount);
            renderTextField(sb, "Password", passwordAst, passwordBounds, validPassword, invalidPassword);
            fontTitle.setColor(Color.WHITE);
            font.draw(sb, registerPromptLayout, (width - registerPromptLayout.width) / 2, registerPromptY);
            sb.end();
        }

        private void renderTextField(SpriteBatch sb, String infoText, String entryText, Rectangle bounds, boolean valid, String invalidText){
            if (entryText.isEmpty()) {
                infoLayout.setText(fontTitle, infoText, Color.LIGHT_GRAY, bounds.width, Align.left, true);
            } else if (!valid){
                infoLayout.setText(fontTitle, infoText, Color.RED, bounds.width, Align.left, true);
                invalidFont.draw(sb, invalidText, bounds.x, bounds.y + bounds.height + invalidFont.getLineHeight());

            }
            else {
                infoLayout.setText(fontTitle, infoText, Color.WHITE, bounds.width, Align.left, true);
            }

            fontTitle.draw(sb, infoLayout, bounds.x, bounds.y + bounds.height);

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
            shapeRenderer.dispose();
            font.dispose();
            fontTitle.dispose();
        }
    }
