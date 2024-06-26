package com.tdt4240gr18.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.misc.LeaderboardEntry;
import com.tdt4240gr18.game.misc.UserSession;
import com.tdt4240gr18.graphics.ggTexture;
import com.tdt4240gr18.services.audio.AudioManager;
import com.tdt4240gr18.services.database.DatabaseInterface;
import com.tdt4240gr18.ui.MenuButton;

import java.util.Collections;
import java.util.List;


public class LeaderboardState extends State{
    // Screen dimensions
    private final int width;
    private final int height;

    // UI elements
    private final BitmapFont font;
    private final float buttonWidth;
    private final MenuButton leftArrow, rightArrow;
    private final MenuButton myRankButton;
    private final MenuButton topRankButton;
    private final MenuButton backButton;
    private final ggTexture Backdrop;
    private final GlyphLayout loadingLayout;

    // UI layout parameters
    private final int buttonOffsetY;
    private final int entryoffsetX;
    private final float entryMargin;
    private final int boxWidth;
    private final int backdropWidth;
    private final int userYValue;

    // Leaderboard data
    private List<LeaderboardEntry> EntriesList;
    private List<LeaderboardEntry> allEntries;
    private LeaderboardEntry currentUser;
    private int userRank;

    // Pagination
    private int currentPage;
    private final int entriesPerPage;
    private int totalAmountOfEntries;

    // Services
    DatabaseInterface databaseInterface;
    private final AudioManager audioManager;


    protected LeaderboardState(GameStateManager gsm, DatabaseInterface databaseInterface) {
        super(gsm);
        this.databaseInterface = databaseInterface;
        this.audioManager = AudioManager.getInstance();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        buttonOffsetY = height / 120;

        // Backdrop setup
        float backdropWidthScale = 0.9f;
        Backdrop = new ggTexture("backdrop.png", backdropWidthScale);
        backdropWidth = Backdrop.getWidth();
        entryoffsetX = (width - Backdrop.getWidth()) / 2;
        entryMargin = 5/100f; // 100 is the width of the backdrop, the entries should start on pixel 5 and end on pixel 95
        boxWidth = backdropWidth - (int) (2* backdropWidth * entryMargin);

        // Font setup
        font = new BitmapFont(Gdx.files.internal("RetroText.fnt"));
        String textScaleTargetWidth = "10. USERNAMEUSERNAME.. 4000";
        setFontScale(font, textScaleTargetWidth, boxWidth);
        String loading = "Loading...";
        loadingLayout = new GlyphLayout(font, loading);

        // Pagination setup
        currentPage = 1;
        entriesPerPage = 10;
        updateEntriesList();

        // Arrow buttons setup
        buttonWidth = 0.7f;
        float arrowWidthRatio = buttonWidth / 77 * 12;
        float arrowY = (int) (height - ((int) font.getLineHeight() + buttonOffsetY) * (entriesPerPage + 3) - arrowWidthRatio * width / 2 * 3); // 3/2 is the ratio of height/width
        leftArrow = addArrow(true, 0.2f * width - arrowWidthRatio * width / 2, arrowY, arrowWidthRatio);
        rightArrow = addArrow(false, 0.8f * width - arrowWidthRatio * width / 2, arrowY, arrowWidthRatio);

        // Rank buttons setup
        float buttonWidthRatio = buttonWidth / 77 * 24; //Rexture size of buttons is 77*24. This will not scale and for further implementation should not be hardcoded
        topRankButton = addSmallButton("Top", buttonWidthRatio, (0.62f * width - width * buttonWidth / 77*24 / 2), arrowY);
        myRankButton = addSmallButton("Me", buttonWidthRatio, - width, arrowY);
        topRankButton.setX((float) (width - topRankButton.getTexture().getWidth()) / 2);

        // User setup

        userYValue = (int) (height - ((int) font.getLineHeight() + buttonOffsetY) * (entriesPerPage + 1.5f));
        databaseInterface.getAllEntries(entries -> {
            allEntries = entries;
            Collections.sort(allEntries, (t1, t2) -> t2.getScore() - t1.getScore());
            setCurrentUser();
            totalAmountOfEntries = allEntries.size();
            if (currentUser != null) {
                Gdx.app.log("Current user", currentUser.toString());
            }
            if (allEntries != null) {
                Gdx.app.log("All entries", allEntries.toString());
            }
            if (UserSession.getInstance().isLoggedIn()) {
                Gdx.app.log("userSession", UserSession.getInstance().getUsername());
            }
            if (userRank != 0) {
                Gdx.app.log("User rank", String.valueOf(userRank));
            }
        });

        // Back button setup
        backButton = addBackButton(buttonWidth);
    }

    private void addCurrentUserButtons() {
        if (currentUser != null) {
            topRankButton.setX(0.62f * width - width * buttonWidth / 77*24 / 2);
            myRankButton.setX(0.38f * width - width * buttonWidth / 77*24 / 2);
        }
    }

    private void setCurrentUser() {
        if (UserSession.getInstance().isLoggedIn()){
            databaseInterface.getEntry(UserSession.getInstance().getUsername(), new DatabaseInterface.onFullEntryLoadedListener() {
                @Override
                public void onSuccess(LeaderboardEntry entry) {
                    currentUser = entry;
                    userRank = allEntries.indexOf(currentUser) + 1;
                    if (userRank != 0) {
                        addCurrentUserButtons();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                }
            });
        }
        else {
            this.currentUser = null;
        }
    }

    private MenuButton addArrow(boolean left, float x, float y, float widthRatio) {
        ggTexture arrowTexture;
        if (left) {
            arrowTexture = new ggTexture("leftArrow.png", widthRatio);
        } else {
            arrowTexture = new ggTexture("rightArrow.png", widthRatio);
        }
        return new MenuButton(arrowTexture, x, y);
    }

    private MenuButton addSmallButton(String text, float buttonWidthRatio, float x, float y) {
        ggTexture buttonTexture = new ggTexture("smallBtn.png", buttonWidthRatio);
        return new MenuButton(buttonTexture, text, x, y);
    }

    private MenuButton addBackButton(float buttonWidthRatio) {
        ggTexture buttonTexture = new ggTexture("menuBtn.png", buttonWidthRatio);
        float x = (float) (width - buttonTexture.getWidth()) / 2;
        return new MenuButton(buttonTexture, "Back", x, (float) buttonOffsetY);
    }


    private void setFontScale(BitmapFont font, String targetText, float targetWidth) {
        GlyphLayout layout = new GlyphLayout(font, targetText);
        float scale = targetWidth / layout.width;
        font.getData().setScale(scale); // Could alternatively scale only X and have Y set in order to have set space underneath
    }

    private void updateEntriesList() {
        EntriesList = null;
        databaseInterface.getEntriesPerPage(currentPage, entriesPerPage, entries -> {
            EntriesList = entries;
            Collections.sort(EntriesList, (t1, t2) -> t2.getScore() - t1.getScore());
        });
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = height - Gdx.input.getY();

            if (leftArrow.isClicked(touchX, touchY)) {
                audioManager.playButtonSound();
                if (currentPage > 1) {
                    currentPage--;
                    updateEntriesList();
                }
            }
            else if (rightArrow.isClicked(touchX, touchY)) {
                audioManager.playButtonSound();
                if (totalAmountOfEntries - 1 >= entriesPerPage * currentPage) {
                    currentPage++;
                    updateEntriesList();
                }
            }
            else if (backButton.isClicked(touchX, touchY)) {
                audioManager.playButtonSound();
                gsm.popAndReturn().dispose();
            }
            else if (topRankButton.isClicked(touchX, touchY)) {
                audioManager.playButtonSound();
                if (currentPage != 1) {
                    currentPage = 1;
                    updateEntriesList();
                }
            }
            else if (myRankButton != null){
                if (myRankButton.isClicked(touchX, touchY)) {
                    audioManager.playButtonSound();
                    if (currentPage != (userRank - 1) / entriesPerPage + 1) {
                        currentPage = (userRank - 1) / entriesPerPage + 1;
                        updateEntriesList();
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
        sb.draw(Backdrop, (float) width /2 - (float) backdropWidth / 2, height - (Backdrop.getHeight() + buttonOffsetY));
        leftArrow.render(sb, false);
        rightArrow.render(sb, false);
        topRankButton.render(sb);
        backButton.render(sb);
        if (myRankButton != null) {
            myRankButton.render(sb);
        }

        if (totalAmountOfEntries != 0 && EntriesList != null) {
            for (int i = 0; i < EntriesList.size(); i++) {
                EntriesList.get(i).render(sb, font, entryoffsetX + (int) (entryMargin * backdropWidth), height - ((int) font.getLineHeight() + buttonOffsetY) * (i + 1), boxWidth, (i + 1) + (currentPage - 1) * entriesPerPage);
            }
            font.setColor(Color.valueOf("ffffb4"));
            if (currentUser != null && userRank != 0) {
                currentUser.render(sb, font, entryoffsetX + (int) (entryMargin * backdropWidth), userYValue, boxWidth, userRank);
            }
            font.setColor(Color.WHITE);
        }
        else {
            font.draw(sb, loadingLayout, (float) width / 2 - loadingLayout.width/ 2, (float) height / 3*2);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        Backdrop.dispose();
        rightArrow.dispose();
        leftArrow.dispose();
        topRankButton.dispose();
        backButton.dispose();
        font.dispose();
        if (myRankButton != null) {
            myRankButton.dispose();
        }
    }
}
