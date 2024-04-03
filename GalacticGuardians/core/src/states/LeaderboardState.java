package states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class LeaderboardState extends State{
    private final Texture logo = new Texture("logo.png");
    private final boolean isAndr;



    protected LeaderboardState(GameStateManager gsm) {
        super(gsm);
        isAndr = isAndroid();
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

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
            if (isAndr) {
                sb.draw(logo, 50, 800);
            }
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
