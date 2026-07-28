package pvz.smoke;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.pam.PamClip;
import pvz.libpvz.textures.TextureBank;

/**
 * A minimal playground to test the libpvz rendering and the new PamClip API.
 */
public class SmokeTest extends ApplicationAdapter {
    private TextureBank textures;
    private PamPlayer player;
    private PamClip zombieClip;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float stateTime = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(1280, 720);
        camera.position.set(0, 0, 0); // Center the camera
        camera.update();

        String rootPath = System.getProperty("pvz.assets");
        if (rootPath == null || rootPath.isEmpty()) {
            rootPath = System.getenv("PVZ_ASSETS");
        }
        if (rootPath == null || rootPath.isEmpty()) {
            System.err.println("ERROR: You must specify the assets directory.");
            System.err.println("Pass -Dpvz.assets=/path/to/assets to Gradle, or set the PVZ_ASSETS environment variable.");
            System.exit(1);
        }

        FileHandle assetsFolder = Gdx.files.absolute(rootPath);
        textures = new TextureBank("768", assetsFolder);
        player = new PamPlayer(textures, assetsFolder);
    }

    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();
        
    
        ScreenUtils.clear(Color.DARK_GRAY);

        textures.update();

        if (zombieClip == null) {
            String targetPam = System.getProperty("pvz.pam", "ZOMBIES/ZOMBIE_EGYPT_BASIC/ZOMBIE_EGYPT_BASIC.PAM");
            String targetClip = System.getProperty("pvz.clip", "idle");
            zombieClip = player.getClip(targetPam, targetClip); 
        }

        if (zombieClip != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            
            player.draw(batch, zombieClip, stateTime, 0, 0, true);
            
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        textures.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("libpvz Smoke Test Playground");
        config.setWindowedMode(1280, 720);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new SmokeTest(), config);
    }
}
