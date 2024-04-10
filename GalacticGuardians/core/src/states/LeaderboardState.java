package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240gr18.game.LeaderboardEntry;
import com.tdt4240gr18.game.MenuButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeaderboardState extends State{
    private final boolean isAndr;
    private final List<MenuButton> buttons;
    private final int width;
    public int height;
    private final int buttonOffsetY;
    private final int entryoffsetX, entryMargin;
    private final BitmapFont font;
    private GlyphLayout layout;
    //private final Rectangle bounds;
    private final Texture Backdrop;
    private List<LeaderboardEntry> EntriesList;
    // TEMPORARY:
    LeaderboardEntry temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp0;




    protected LeaderboardState(GameStateManager gsm) {
        super(gsm);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        isAndr = isAndroid();
        buttons = new ArrayList<>();
        EntriesList = new ArrayList<>();
        buttonOffsetY = 20;
        buttons.add(addButton("Back"));
        if (isAndr) {
            buttons.add(addButton("Log in"));
        }
        this.font = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        //this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        Backdrop = new Texture("backdrop1x2.png");
        entryoffsetX = (width - Backdrop.getWidth()) / 2;
        entryMargin = 60;

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

    private boolean isAndroid() {
        try {
            Class.forName("android.os.Build");
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    private MenuButton addButton(String text) {
        Texture buttonTexture = new Texture("menuBtn.png");
        float x = (float) (width - buttonTexture.getWidth()) / 2;
        float y = (float) buttonOffsetY + buttons.size() * (buttonTexture.getHeight() + buttonOffsetY);
        return new MenuButton(buttonTexture, text, x, y);
    }

    private void sortList(ArrayList<LeaderboardEntry> list) {
        Collections.sort(EntriesList, new Comparator<LeaderboardEntry>() {
            @Override
            public int compare(LeaderboardEntry t1, LeaderboardEntry t2) {
                return t2.getScore() - t1.getScore();
            }
        });
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
                        gsm.set(new MenuState(gsm));
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
        font.getData().setScale(2, 2);
        sb.begin();
        //this.layout = new GlyphLayout(font, buttonText);
        sb.draw(Backdrop, (float) width /2 - (float) Backdrop.getWidth() / 2, height - (Backdrop.getHeight() + buttonOffsetY));
        if (isAndr) {

            }
        for (MenuButton button : buttons) {
            button.render(sb);
        }
        for (int i = 0; i < EntriesList.size(); i++) {
            //EntriesList.get(i).render(sb,font, entryoffsetX + entryMargin, height - 100 - (10) * i, Backdrop.getWidth() - entryMargin * 2);
            EntriesList.get(i).render(sb,font, entryoffsetX + entryMargin, height - 100 - ((int) font.getLineHeight() + buttonOffsetY) * i, Backdrop.getWidth() - entryMargin * 2);
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
