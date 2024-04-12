package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.DatabaseInterface;
import com.tdt4240gr18.game.LeaderboardEntry;
import com.tdt4240gr18.game.MenuButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LeaderboardState extends State{
    private final List<MenuButton> buttons;
    private final int width;
    public int height;
    private final int buttonOffsetY;
    float buttonWidth;
    private final int entryoffsetX, entryMargin;
    private final BitmapFont font;
    private final Texture Backdrop;
    private final int backdropWidth;
    private final List<LeaderboardEntry> EntriesList;
    DatabaseInterface databaseInterface;

    // TEMPORARY:
    LeaderboardEntry temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp0;




    protected LeaderboardState(GameStateManager gsm, DatabaseInterface databaseInterface) {
        super(gsm);
        this.databaseInterface = databaseInterface;
        databaseInterface.fetchDataFromDatabase();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        buttons = new ArrayList<>();
        buttonWidth = 0.7f;
        EntriesList = new ArrayList<>();
        buttonOffsetY = 20;
        buttons.add(addButton("Back"));
        buttons.add(addButton("Log in"));
        this.font = new BitmapFont(Gdx.files.internal("RetroText.fnt"));
        //this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        Backdrop = resizeTexture(new Texture("backdrop.png"),0.9f);
        backdropWidth = Backdrop.getWidth();
        entryoffsetX = (width - Backdrop.getWidth()) / 2;
        entryMargin = 60;
        // This is the "maximum" width of the text, it will be scaled down if the text is too long
        String textScaleTargetWidth = "10. USERNAMEUSERNAME.. 4000";
        setFontScale(font, textScaleTargetWidth, backdropWidth*0.9f);


        // TEMPORARY:
        temp1 = new LeaderboardEntry("B01", 100);
        temp2 = new LeaderboardEntry("B02", 200);
        temp3 = new LeaderboardEntry("B03", 50);
        temp4 = new LeaderboardEntry("B04", 400);
        temp5 = new LeaderboardEntry("B05", 400);
        temp6 = new LeaderboardEntry("B06", 0);
        temp7 = new LeaderboardEntry("B07", 10);
        temp8 = new LeaderboardEntry("B08", 4000);
        temp9 = new LeaderboardEntry("B09", 420);
        temp0 = new LeaderboardEntry("B00000", 400);
        EntriesList.add(temp1);
        EntriesList.add(temp2);
        EntriesList.add(temp3);
        EntriesList.add(temp4);
        EntriesList.add(temp5);
        EntriesList.add(temp6);
        EntriesList.add(temp7);
        EntriesList.add(temp8);
        EntriesList.add(temp9);
        EntriesList.add(temp0);

        //To sort according to score
        sortList((ArrayList<LeaderboardEntry>) EntriesList);
    }

    private MenuButton addButton(String text) {
        Texture buttonTexture = resizeTexture(new Texture("menuBtn.png"), buttonWidth);
        float x = (float) (width - buttonTexture.getWidth()) / 2;
        float y = (float) buttonOffsetY + buttons.size() * (buttonTexture.getHeight() + buttonOffsetY);
        return new MenuButton(buttonTexture, text, x, y);
    }

    private void sortList(ArrayList<LeaderboardEntry> list) {
        Collections.sort(list, (t1, t2) -> t2.getScore() - t1.getScore());
    }

    private void setFontScale(BitmapFont font, String targetText, float targetWidth) {
        GlyphLayout layout = new GlyphLayout(font, targetText);
        float scale = targetWidth / layout.width;
        font.getData().setScale(scale); // Could alternatively scale only X and have Y set in order to have set space underneath
    }

    private Texture resizeTexture(Texture originalTexture, float widthRatio, float heightRatio) {
        originalTexture.getTextureData().prepare();

        // Calculate the new width and height
        int newWidth = (int) (Gdx.graphics.getWidth() * widthRatio);
        int newHeight = (int) (Gdx.graphics.getHeight() * heightRatio);

        // Create a new Pixmap with the desired size
        Pixmap scaledPixmap = new Pixmap(newWidth, newHeight, originalTexture.getTextureData().getFormat());

        // Draw the original texture onto the new Pixmap
        Pixmap originalPixmap = originalTexture.getTextureData().consumePixmap();
        scaledPixmap.drawPixmap(originalPixmap,
                0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
                0, 0, scaledPixmap.getWidth(), scaledPixmap.getHeight());

        // Dispose the original texture and pixmap
        originalTexture.dispose();
        originalPixmap.dispose();

        // Create a new Texture from the Pixmap
        Texture resizedTexture = new Texture(scaledPixmap);

        // Dispose the pixmap
        scaledPixmap.dispose();

        return resizedTexture;
    }

    private Texture resizeTexture(Texture originalTexture, float widthRatio) {
        originalTexture.getTextureData().prepare();
        // Calculate the new width
        int newWidth = (int) (Gdx.graphics.getWidth() * widthRatio);

        // Calculate the height ratio based on the original texture's aspect ratio
        float aspectRatio = (float) originalTexture.getHeight() / originalTexture.getWidth();
        int newHeight = (int) (newWidth * aspectRatio);

        // Create a new Pixmap with the desired size
        Pixmap scaledPixmap = new Pixmap(newWidth, newHeight, originalTexture.getTextureData().getFormat());

        // Draw the original texture onto the new Pixmap
        Pixmap originalPixmap = originalTexture.getTextureData().consumePixmap();
        scaledPixmap.drawPixmap(originalPixmap,
                0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
                0, 0, scaledPixmap.getWidth(), scaledPixmap.getHeight());

        // Dispose the original texture and pixmap
        originalTexture.dispose();
        originalPixmap.dispose();

        // Create a new Texture from the Pixmap
        Texture resizedTexture = new Texture(scaledPixmap);

        // Dispose the pixmap
        scaledPixmap.dispose();

        return resizedTexture;
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = height - Gdx.input.getY();

            for (MenuButton button : buttons) {
                if (button.isClicked(touchX, touchY)) {
                    if (button.getButtonText().equals("Back")) {
                        // Burde egentlig bruke push og pop i stedet for set men får ikke til, skjermen blir bare svart etter å pop-e
                        gsm.set(new MenuState(gsm, databaseInterface));
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
        //this.layout = new GlyphLayout(font, buttonText);
        sb.draw(Backdrop, (float) width /2 - (float) backdropWidth / 2, height - (Backdrop.getHeight() + buttonOffsetY));
        for (MenuButton button : buttons) {
            button.render(sb);
        }
        for (int i = 0; i < EntriesList.size(); i++) {
            //EntriesList.get(i).render(sb,font, entryoffsetX + entryMargin, height - 100 - (10) * i, Backdrop.getWidth() - entryMargin * 2);
            EntriesList.get(i).render(sb,font, entryoffsetX + entryMargin, height - 100 - ((int) font.getLineHeight() + buttonOffsetY) * i, Backdrop.getWidth() - entryMargin * 2, i + 1);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        for (MenuButton button : buttons) {
            button.dispose();
        }
    }
}
