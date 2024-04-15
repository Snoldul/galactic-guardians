package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class OptionsState extends State{
    private final BitmapFont fontTitle;
    private final BitmapFont fontText;
    private GlyphLayout layout;
    private Texture optionsMenu;
    private Rectangle xBtnBounds;
    private Texture xBtn;
    private int width;
    private int height;
    private final String titleText;
    private float newHeight;
    private float newWidth;
    private float x;
    private float y;

    public OptionsState(GameStateManager gsm) {
        super(gsm);
        optionsMenu = new Texture("optionsBackground.png");
        xBtn = new Texture("xBtn.png");
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        fontTitle = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        fontText = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        titleText = "Options";
        float textScale = 3;
        fontTitle.getData().setScale(textScale);

        newWidth = width * 0.95f;
        newHeight = ((float) optionsMenu.getHeight() / (float) optionsMenu.getWidth()) * newWidth;
        x = (width - newWidth) / 2;
        y = (height - newHeight) / 2 + height * 0.05f;
        float xBtnWidth = width * 0.15f;
        float xBtnHeight = xBtnWidth;
        float xButtonX = (width - xBtnWidth) / 2;
        float xButtonY = (height - y - newHeight) + xBtnHeight;
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
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(optionsMenu, x, y, newWidth, newHeight);
        sb.draw(xBtn, xBtnBounds.x, xBtnBounds.y, xBtnBounds.width, xBtnBounds.height);
        layout = new GlyphLayout(fontTitle, titleText);
        float titleWidth = layout.width;
        float titleHeight = layout.height;

        float titleX = x + ((newWidth - titleWidth) / 2);
        float titleY = y + newHeight - titleHeight - newHeight * 0.02f;

        fontTitle.draw(sb, layout, titleX, titleY);
        sb.end();
    }

    @Override
    public void dispose() {
        optionsMenu.dispose();
    }
}
