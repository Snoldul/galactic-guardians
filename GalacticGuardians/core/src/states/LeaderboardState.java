package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.DatabaseInterface;
import com.tdt4240gr18.game.LeaderboardEntry;
import com.tdt4240gr18.game.MenuButton;
import com.tdt4240gr18.game.OnDataLoadedCallback;
import com.tdt4240gr18.game.ggTexture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LeaderboardState extends State{
    private final List<MenuButton> buttons;
    private final int width;
    private final int height;
    private final int buttonOffsetY;
    private final int entryoffsetX;
    private final float entryMargin;
    private final int boxWidth;
    private final BitmapFont font;
    private final ggTexture Backdrop;
    private final int backdropWidth;
    private List<LeaderboardEntry> EntriesList;
    private List<LeaderboardEntry> allEntries;
    DatabaseInterface databaseInterface;
    private final MenuButton leftArrow, rightArrow;
    private int currentPage;
    private final int entriesPerPage;
    private int totalAmountOfEntries;
    private final LeaderboardEntry currentUser;
    private int userRank = 0;
    private final int userYValue;
    private final MenuButton myRankButton, topRankButton;
    private final GlyphLayout loadingLayout;


    protected LeaderboardState(GameStateManager gsm, DatabaseInterface databaseInterface) {
        super(gsm);
        this.databaseInterface = databaseInterface;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        buttons = new ArrayList<>();
        float buttonWidth = 0.7f;
        float backdropWidthScale = 0.9f;
        buttonOffsetY = height / 120;
        buttons.add(addMenuButton("Back", buttonWidth));
        //buttons.add(addMenuButton("Log in", buttonWidth));
        this.font = new BitmapFont(Gdx.files.internal("RetroText.fnt"));
        Backdrop = new ggTexture("backdrop.png", backdropWidthScale);
        backdropWidth = Backdrop.getWidth();
        entryoffsetX = (width - Backdrop.getWidth()) / 2;
        entryMargin = 5/100f; // 100 is the width of the backdrop, the entries should start on pixel 5 and end on pixel 95
        boxWidth = backdropWidth - (int) (2* backdropWidth * entryMargin);
        // This is the "maximum" width of the text, it will be scaled down if the text is too long
        String textScaleTargetWidth = "10. USERNAMEUSERNAME.. 4000";
        setFontScale(font, textScaleTargetWidth, boxWidth);
        String loading = "Loading...";
        loadingLayout = new GlyphLayout(font, loading);


        /*databaseInterface.addScoreToLeaderboard("Johannes", 55);
        for (int i = 10; i < 1000; i += 500){
            databaseInterface.addScoreToLeaderboard("testForUser" + i, i);
        }*/

        currentPage = 1;
        entriesPerPage = 10;
        float arrowWidthRatio = buttonWidth / 77 * 12;
        int arrowY = (int) (height - ((int) font.getLineHeight() + buttonOffsetY) * (entriesPerPage + 3) - arrowWidthRatio * width / 2 * 3); // 3/2 is the ratio of height/width
        leftArrow = addArrow(true, 0.2f * width - arrowWidthRatio * width / 2, arrowY, arrowWidthRatio);
        rightArrow = addArrow(false, 0.8f * width - arrowWidthRatio * width / 2, arrowY, arrowWidthRatio);

        databaseInterface.getAllEntries(new OnDataLoadedCallback() {
            @Override
            public void onDataLoaded(ArrayList<LeaderboardEntry> entries) {
                allEntries = entries;
                Collections.sort(allEntries, (t1, t2) -> t2.getScore() - t1.getScore());
                totalAmountOfEntries = allEntries.size();
                userRank = allEntries.indexOf(currentUser) + 1;
            }
        });

        // Change current user to the user that is logged in
        currentUser = databaseInterface.getEntry("Johannes");
        userYValue = (int) (height - ((int) font.getLineHeight() + buttonOffsetY) * (entriesPerPage + 1.5f));
        myRankButton = addSmallButton("Me", buttonWidth / 77*24, 0.38f * width - width * buttonWidth / 77*24 / 2, arrowY);
        topRankButton = addSmallButton("Top", buttonWidth / 77*24, 0.62f * width - width * buttonWidth / 77*24 / 2, arrowY);
        myRankButton.setY(arrowY);




        // Fetch data from database
        updateEntriesList();

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

    private MenuButton addMenuButton(String text, float buttonWidthRatio) {
        ggTexture buttonTexture = new ggTexture("menuBtn.png", buttonWidthRatio);
        float x = (float) (width - buttonTexture.getWidth()) / 2;
        float y = (float) buttonOffsetY + buttons.size() * (buttonTexture.getHeight() + buttonOffsetY);
        return new MenuButton(buttonTexture, text, x, y);
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
                if (currentPage > 1) {
                    currentPage--;
                    updateEntriesList();
                }
            }
            else if (rightArrow.isClicked(touchX, touchY)) {
                if (totalAmountOfEntries >= entriesPerPage * currentPage) {
                    currentPage++;
                    updateEntriesList();
                }
            }
            else if (myRankButton.isClicked(touchX, touchY)) {
                if (currentPage != userRank / entriesPerPage + 1){
                    currentPage = userRank / entriesPerPage + 1;
                    updateEntriesList();
                }
            }
            else if (topRankButton.isClicked(touchX, touchY)) {
                if (currentPage != 1) {
                    currentPage = 1;
                    updateEntriesList();
                }
            }
            for (MenuButton button : buttons) {
                if (button.isClicked(touchX, touchY)) {
                    if (button.getButtonText().equals("Back")) {
                        gsm.pop();
                        dispose();
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
        myRankButton.render(sb);
        topRankButton.render(sb);

        for (MenuButton button : buttons) {
            button.render(sb);
        }
        if (totalAmountOfEntries != 0 && EntriesList != null) {
            for (int i = 0; i < EntriesList.size(); i++) {
                EntriesList.get(i).render(sb, font, entryoffsetX + (int) (entryMargin * backdropWidth), height - ((int) font.getLineHeight() + buttonOffsetY) * (i + 1), boxWidth, (i + 1) + (currentPage - 1) * entriesPerPage);
            }
            font.setColor(Color.valueOf("ffffb4"));
            currentUser.render(sb, font, entryoffsetX + (int) (entryMargin * backdropWidth), userYValue, boxWidth, userRank);
            font.setColor(Color.WHITE);
        }
        else {
            font.draw(sb, loadingLayout, (float) width / 2 - loadingLayout.width/ 2, (float) height / 3*2);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        for (MenuButton button : buttons) {
            button.dispose();
        }
        font.dispose();
    }
}
