package states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tdt4240gr18.game.AudioManager;
import com.tdt4240gr18.game.entity.components.BulletComponent;
import com.tdt4240gr18.game.entity.components.CollisionComponent;
import com.tdt4240gr18.game.entity.components.HeartComponent;
import com.tdt4240gr18.game.entity.components.ScoreComponent;
import com.tdt4240gr18.game.entity.systems.BulletControlSystem;
import com.tdt4240gr18.game.entity.components.EnemyComponent;
import com.tdt4240gr18.game.entity.components.LivesComponent;
import com.tdt4240gr18.game.entity.components.MovementPropertiesComponent;
import com.tdt4240gr18.game.entity.components.MovementStateComponent;
import com.tdt4240gr18.game.entity.components.PlayerComponent;
import com.tdt4240gr18.game.entity.systems.EnemyControlSystem;
import com.tdt4240gr18.game.entity.systems.ExplosionSystem;
import com.tdt4240gr18.game.entity.systems.PlayerControlSystem;
import com.tdt4240gr18.game.entity.systems.MovementSystem;
import com.tdt4240gr18.game.entity.systems.RenderingSystem;
import com.tdt4240gr18.game.entity.components.TransformComponent;
import com.tdt4240gr18.game.entity.components.VelocityComponent;
import com.tdt4240gr18.game.entity.components.TextureComponent;
import com.tdt4240gr18.game.ScrollingBackground;
import com.tdt4240gr18.game.entity.systems.ScoreSystem;

public class PlayState extends State {

    private final ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private final BitmapFont title = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
    private final PooledEngine engine = new PooledEngine();
    private final ScrollingBackground scrollingBackground = new ScrollingBackground();
    private final Texture player;
    private final Texture enemy;

    private final Texture pauseBtn;
    private final Texture movementSpace;
    private final Rectangle pauseBtnBounds;
    private final SpriteBatch sb;
    private Entity playerEntity;
    private boolean isPaused;
    private final Texture bullet;
    private final Texture heart;
    private final AudioManager audioManager;

    private float spawnTimer;
    private float shotTimer;


    public PlayState(GameStateManager gsm){
        super(gsm);
        audioManager = AudioManager.getInstance();
        player = new Texture("Player.png");
        enemy = new Texture("Enemy.png");
        pauseBtn = new Texture("pauseBtn.png");
        bullet = new Texture("pew1.png");

        movementSpace = new Texture("backdrop.png");
        heart = new Texture("heart.png");
        sb = new SpriteBatch();
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new BulletControlSystem(engine));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderingSystem(sb));
        engine.addSystem(new EnemyControlSystem(this, engine));
        engine.addSystem(new ExplosionSystem(engine));
        engine.addSystem(new ScoreSystem(engine));
        createPlayer();


        isPaused = false;
        pauseBtnBounds = new Rectangle();
        positionPauseButton();
        createScore();
        updateHearts();
    }

    private void createPlayer(){
        // Create player entity and components
        playerEntity = engine.createEntity();
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent playerCmp = engine.createComponent(PlayerComponent.class);
        CollisionComponent collision = engine.createComponent(CollisionComponent.class);
        LivesComponent lives = engine.createComponent(LivesComponent.class);

        // Set component values
        float xPosition = Gdx.graphics.getWidth() / 2f;
        float yPosition = (Gdx.graphics.getHeight() / 3f)-100;
        transform.position.set(xPosition, yPosition, 0);
        float scale = 7f;
        transform.scale.set(scale, scale, scale);
        texture.region = new TextureRegion(player);
        lives.lives = 3;

        collision.bounds = new Rectangle(1, 1, player.getWidth()*scale, player.getHeight()*scale/2);


        // Add components to player entity
        playerEntity.add(transform);
        playerEntity.add(velocity);
        playerEntity.add(texture);
        playerEntity.add(playerCmp);
        playerEntity.add(collision);
        playerEntity.add(lives);

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
        MovementStateComponent state = engine.createComponent(MovementStateComponent.class);
        MovementPropertiesComponent properties = engine.createComponent(MovementPropertiesComponent.class);
        CollisionComponent collision = engine.createComponent(CollisionComponent.class);

        // Initialize properties
        properties.amplitude = 10;
        properties.frequency = 5;
        properties.verticalSpeed = 10;

        // Set a random target dive Y within the middle 30% of the screen
        float middleStart = Gdx.graphics.getHeight() * 0.35f;
        float middleEnd = Gdx.graphics.getHeight() * 0.65f;
        properties.targetDiveY = MathUtils.random(middleStart, middleEnd);

        // Initialize components
        transform.position.set(x, y, 0);
        transform.scale.set(5f, 5f, 5f);
        lives.lives = 4;
        float scale = 5f;
        transform.scale.set(scale,scale,scale);
        lives.lives = 3;
        texture.region = new TextureRegion(enemy);

        collision.bounds = new Rectangle(x, y, enemy.getWidth()*scale, enemy.getHeight()*scale);

        // Add components to enemy entity
        enemyEntity.add(transform);
        enemyEntity.add(velocity);
        enemyEntity.add(texture);
        enemyEntity.add(lives);
        enemyEntity.add(enemyCmp);
        enemyEntity.add(state);
        enemyEntity.add(properties);
        enemyEntity.add(collision);

        // Add the entity to the engine
        engine.addEntity(enemyEntity);
    }
    public Vector2 getPlayerPosition() {
        TransformComponent playerPos;
        if (playerEntity != null) {
            playerPos = playerEntity.getComponent(TransformComponent.class);
        if (playerPos != null) {
            return new Vector2(playerPos.position.x, playerPos.position.y);
            }
        }
        return null;
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

    private void createBullet(){
        Entity bulletEntity = engine.createEntity();
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        BulletComponent bulletCmp = engine.createComponent(BulletComponent.class);
        CollisionComponent collision = engine.createComponent(CollisionComponent.class);

        TransformComponent playerTransform = pm.get(playerEntity);

        // Set component values
        bulletCmp.speed = 400;
        transform.position.set(playerTransform.position.x, playerTransform.position.y + 50, 0);
        float scale = 7f;
        transform.scale.set(scale, scale, scale);
        texture.region = new TextureRegion(bullet);

        collision.bounds = new Rectangle(playerTransform.position.x, playerTransform.position.y + 50, bullet.getWidth()*scale, bullet.getHeight()*scale);

        // Add components to player entity
        bulletEntity.add(transform);
        bulletEntity.add(velocity);
        bulletEntity.add(texture);
        bulletEntity.add(bulletCmp);
        bulletEntity.add(collision); // Add the CollisionComponent

        // Add the entity to the engine
        engine.addEntity(bulletEntity);

        // Play the shooting sound
        audioManager.playLaserSound();
    }

    private void createScore() {
        Entity scoreEntity = new Entity();
        ScoreComponent score = engine.createComponent(ScoreComponent.class);

        scoreEntity.add(score);

        engine.addEntity(scoreEntity);
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

    @Override
    public void update(float dt) {

        handleInput();
        if (playerEntity.getComponent(LivesComponent.class).lives <= 0) {
            // If no lives left, transition to GameOverState
            gsm.push(new GameOverState(gsm));
            togglePaused();
            return; // Exit the update method
        }

        if(!Gdx.input.isTouched()){

        shotTimer += dt;
        spawnTimer += dt;
        if(spawnTimer >= 2){
         float x = MathUtils.random(0, Gdx.graphics.getWidth());
            float y = Gdx.graphics.getHeight();
            createEnemy(x, y);
            spawnTimer = 0;
        }

        if (shotTimer >= playerEntity.getComponent(PlayerComponent.class).firerate){
            createBullet();
            shotTimer = 0;
        }

        if (playerEntity.getComponent(LivesComponent.class).lifeLoss){
            updateHearts();
            playerEntity.getComponent(LivesComponent.class).lifeLoss = false;
        }

        engine.update(dt);
    }
        }

    private void updateHearts() {
        // Create a family to get entities with HeartComponent
        Family heartFamily = Family.all(HeartComponent.class).get();

        // Get all entities with HeartComponent
        ImmutableArray<Entity> heartEntities = engine.getEntitiesFor(heartFamily);

        // Collect entities to remove
        Array<Entity> entitiesToRemove = new Array<>();
        for (Entity entity : heartEntities) {
            entitiesToRemove.add(entity);
        }


// Remove collected entities
        for (Entity entity : entitiesToRemove) {
            engine.removeEntity(entity);
        }

        // Get the number of player's lives
        LivesComponent livesComponent = playerEntity.getComponent(LivesComponent.class);
        int numLives = livesComponent.lives;

        Rectangle moveArea = engine.getSystem(PlayerControlSystem.class).getMoveArea();
        float moveAreaHeight = moveArea.height;

        System.out.print("lives left creating: " + numLives);
        for (int i = 0; i < numLives; i++) {
            Entity heartEntity = new Entity();
            HeartComponent heartComponent = new HeartComponent();
            TransformComponent transformComponent = new TransformComponent();
            TextureComponent textureComponent = new TextureComponent();

            float scale = 7f;
            transformComponent.scale.set(scale, scale, scale);
            textureComponent.region = new TextureRegion(heart);

            transformComponent.position.set(60 + (textureComponent.region.getRegionWidth() * scale + 20) * i, moveAreaHeight + textureComponent.region.getRegionHeight() * scale, 0);

            heartEntity.add(heartComponent);
            heartEntity.add(transformComponent);
            heartEntity.add(textureComponent);

            engine.addEntity(heartEntity);
        }

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
        bullet.dispose();

    }
}
