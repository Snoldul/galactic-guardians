package states;

import com.badlogic.ashley.core.Component;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tdt4240gr18.game.entity.components.BulletComponent;
import com.tdt4240gr18.game.entity.components.CollisionComponent;
import com.tdt4240gr18.game.entity.components.HeartComponent;
import com.tdt4240gr18.game.entity.components.ScoreComponent;
import com.tdt4240gr18.game.entity.systems.BulletControlSystem;
import com.tdt4240gr18.game.entity.components.EnemyComponent;
import com.tdt4240gr18.game.entity.components.LivesComponent;
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

import java.awt.TextComponent;


public class PlayState extends State {

    private ComponentMapper<TransformComponent> pm = ComponentMapper.getFor(TransformComponent.class);
    private final BitmapFont title = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
    private final PooledEngine engine = new PooledEngine();
    private final ScrollingBackground scrollingBackground = new ScrollingBackground();
    private final Texture player;
    private final Texture enemy;
    private final Texture bullet;
    private final Texture movementSpace;
    private final Texture heart;
    private SpriteBatch sb;
    private Entity playerEntity;
    private Entity scoreEntity;

    private float spawnTimer;
    private float shotTimer;


    public PlayState(GameStateManager gsm){
        super(gsm);
        player = new Texture("Player.png");
        enemy = new Texture("Enemy.png");
        bullet = new Texture("pew1.png");
        movementSpace = new Texture("backdrop.png");
        heart = new Texture("heart.png");
        sb = new SpriteBatch();
        engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new BulletControlSystem(engine));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderingSystem(sb));
        engine.addSystem(new EnemyControlSystem(engine));
        engine.addSystem(new ExplosionSystem(engine));
        engine.addSystem(new ScoreSystem(engine));
        createPlayer();
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
        CollisionComponent collision = engine.createComponent(CollisionComponent.class);

        // Set component values
        transform.position.set(x, y, 0);
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
        enemyEntity.add(collision);

        // Add the entity to the engine
        engine.addEntity(enemyEntity);
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
    }

    private void createScore() {
        scoreEntity = new Entity();
        ScoreComponent score = engine.createComponent(ScoreComponent.class);

        scoreEntity.add(score);

        engine.addEntity(scoreEntity);
    }


    @Override
    protected void handleInput() {

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
        if(!Gdx.input.isTouched()){
         movePlayer(0, 0);
        }
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
        sb.end();

        engine.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        sb.dispose();
        player.dispose();
        enemy.dispose();
        title.dispose();
        bullet.dispose();
    }
}
