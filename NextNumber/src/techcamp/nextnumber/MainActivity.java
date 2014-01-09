/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import techcamp.nextnumber.manager.ResourceManager;
import techcamp.nextnumber.manager.SceneManager;
import techcamp.nextnumber.scenes.AbstractScene;
import techcamp.nextnumber.scenes.LoadingScene;
import techcamp.nextnumber.scenes.MenuScene;
import techcamp.nextnumber.scenes.SplashScene;
import android.view.KeyEvent;

public class MainActivity extends BaseGameActivity {
	/** Screen width, standard 720p */
	public static final int W = 600;
	/**
	 * Screen height, non-standard - will make it look ok on most 16:9 screens
	 * with or without soft buttons
	 */
	public static final int H = 1024;
	/** FPS limit of the engine */
	public static final int FPS_LIMIT = 60;

	public static final long SPLASH_DURATION = 4000;

	private Camera camera;
	private Scene mScene;

	@Override
	public synchronized void onResumeGame() {
		ResourceManager.getInstance().resumeMusic();
		super.onResumeGame();
	}

	@Override
	public synchronized void onPauseGame() {
		ResourceManager.getInstance().pauseMusic();
		super.onPauseGame();
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// create simple camera
		camera = new Camera(0, 0, W, H);
		// use the easiest resolution policy
		IResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(W, H);
		// play only in portrait mode, we will need music, sound and no
		// multitouch, dithering just in case
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, resolutionPolicy, camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		Engine engine = new LimitedFPSEngine(pEngineOptions, FPS_LIMIT);
		return engine;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		ResourceManager.getInstance().init(this);
		ResourceManager.getInstance().loadMusic();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		mScene = new Scene();
		/* Enable touch area binding on the mScene object */
		mScene.setTouchAreaBindingOnActionDownEnabled(true);

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		SceneManager.getInstance().showSplash();
		ResourceManager.getInstance().resumeMusic();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (ResourceManager.getInstance().engine != null){
				AbstractScene cur = SceneManager.getInstance().getCurrentScene();
				if (cur.isLayerShown()){
					cur.onHiddenLayer();
				} else {
					Class<? extends AbstractScene> instance = cur.getClass();
					if (instance.equals(SplashScene.class)|| instance.equals(LoadingScene.class)|| instance.equals(MenuScene.class)){
						System.exit(0);
					} else {
						SceneManager.getInstance().showScene(MenuScene.class);
					}
				}
			}
		}
		return true;
	}
}
