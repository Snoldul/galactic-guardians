package states;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.tdt4240gr18.game.entity.components.EnemyComponent;
import com.tdt4240gr18.game.entity.components.LivesComponent;
import com.tdt4240gr18.game.entity.components.PlayerComponent;
import com.tdt4240gr18.game.entity.systems.EnemyControlSystem;
import com.tdt4240gr18.game.entity.systems.PlayerControlSystem;
import com.tdt4240gr18.game.entity.systems.MovementSystem;
import com.tdt4240gr18.game.entity.systems.RenderingSystem;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;
import com.tdt4240gr18.game.ScrollingBackground;



public class PlayState extends State {
    private final BitmapFont title = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
    private final PooledEngine engine = new PooledEngine();
    private final ScrollingBackground scrollingBackground = new ScrollingBackground();
    private final Texture player;
    private final Texture enemy;
    private final Texture pauseBtn;
    private final Texture movementSpace;
    private final Rectangle pauseBtnBounds;
    private SpriteBatch sb;
    private Entity playerEntity;
    private boolean isPaused;
    private float spawnTimer;


    public PlayState(GameStateManager gsm){
        super(gsm);
        player = new Texture("Player.png");
        enemy = new Texture("Enemy.png");
        pauseBtn = new Texture("pauseBtn.png");
        movementSpace = new Texture("backdrop.png");
        sb = new SpriteBatch();
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderingSystem(sb));
        engine.addSystem(new EnemyControlSystem());
        createPlayer();

        isPaused = false;
        pauseBtnBounds = new Rectangle();
        positionPauseButton();
    }

    private void createPlayer(){
        // Create player entity and components
        playerEntity = engine.createEntity();
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent playerCmp = engine.createComponent(PlayerComponent.class);
        // Set component values
        float xPosition = Gdx.graphics.getWidth() / 2f;
        float yPosition = (Gdx.graphics.getHeight() / 3f)-100;
        transform.position.set(xPosition, yPosition, 0);
        transform.scale.set(7f,7f,7f);
        texture.region = new TextureRegion(player);

        // Add components to player entity
        playerEntity.add(transform);
        playerEntity.add(velocity);
        playerEntity.add(texture);
        playerEntity.add(playerCmp);

        // Add the entity to the engine
        engine.addEntity(playerEntity);
    }
    private void createEnemy(float x, float y){
// Create enemy entity and components
        Entity enemyEntity = engine.createEntity();
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        LivesComponent lives = engine.createComponent(LivesComponent.class);
        EnemyComponent enemyCmp = engine.createComponent(EnemyComponent.class);

        // Set component values
        transform.position.set(x, y, 0);
        transform.scale.set(5f,5f,5f);
        lives.lives = 4;
        texture.region = new TextureRegion(enemy);

        // Add components to enemy entity
        enemyEntity.add(transform);
        enemyEntity.add(velocity);
        enemyEntity.add(texture);
        enemyEntity.add(lives);
        enemyEntity.add(enemyCmp);

        // Add the entity to the engine
        engine.addEntity(enemyEntity);
    }

    private void positionPauseButton() {
        // The button width and height are set to 5% of the screen width
        float btnSize = Gdx.graphics.getWidth() * 0.05f;
        float btnX = Gdx.graphics.getWidth() - btnSize*2f;
        float btnY = Gdx.graphics.getHeight() - btnSize*2f;

        // Update the pause button bounds for touch input
        pauseBtnBounds.set(btnX, btnY, btnSize, btnSize);
    }
    public void togglePaused(){
        isPaused = !isPaused;
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // If pause btn is touched then engine.update is not rendered
            if (pauseBtnBounds.contains(touchX, touchY)) {
                togglePaused();
                gsm.push(new PauseState(gsm, this));
            }
        }
    }
    private void movePlayer(float xDirection, float yDirection) {
        VelocityComponent velocity = playerEntity.getComponent(VelocityComponent.class);
        if (velocity != null) {
            float speed = 100; // Adjust to control player speed
            velocity.velocity.x = xDirection * speed;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
     if(!Gdx.input.isTouched()){
         movePlayer(0, 0);
     }
     spawnTimer += dt;
     if(spawnTimer >= 2){
         float x = MathUtils.random(0, Gdx.graphics.getWidth());
            float y = Gdx.graphics.getHeight();
            createEnemy(x, y);
            spawnTimer = 0;
     }
     engine.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        sb.begin();
        scrollingBackground.render(Gdx.graphics.getDeltaTime(), sb);
        sb.draw(movementSpace, 10, 10, Gdx.graphics.getWidth() - 10, Gdx.graphics.getHeight() / 4f);
        sb.draw(pauseBtn, pauseBtnBounds.x, pauseBtnBounds.y, pauseBtnBounds.width, pauseBtnBounds.height);
        sb.end();


        if(!isPaused){
            engine.update(Gdx.graphics.getDeltaTime());
        }
    }
    @Override
    public void dispose() {
        sb.dispose();
        player.dispose();
        enemy.dispose();
        title.dispose();
        pauseBtn.dispose();
    }
}
