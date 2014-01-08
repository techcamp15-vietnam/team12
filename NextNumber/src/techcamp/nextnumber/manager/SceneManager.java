/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.manager;

import org.andengine.util.debug.Debug;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.scenes.AbstractScene;
import techcamp.nextnumber.scenes.LoadingScene;
import techcamp.nextnumber.scenes.MenuScene;
import techcamp.nextnumber.scenes.SplashScene;
import android.os.AsyncTask;
import android.util.Log;

public class SceneManager {

	private static final SceneManager INSTANCE = new SceneManager();

	protected static final int SINGLE = MenuScene.SINGLE;
	protected static final int MULTI = MenuScene.MULTI;
	protected static final int CLASSIC = MenuScene.CLASSIC;
	protected static final int CHALLENGE = MenuScene.CHALLENGE;

	private ResourceManager res = ResourceManager.getInstance();
	private AbstractScene currentScene;
	private LoadingScene loadingScene;

	private SceneManager() {
	}

	/**
	 * Shows splash screen and loads resources on background
	 */
	public void showSplash() {
		final SplashScene splash = new SplashScene();
		setCurrentScene(splash);
		splash.loadResources();
		splash.create();
		res.engine.setScene(splash);
		Log.i("Scene", "show SPLASH");

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				long timestamp = System.currentTimeMillis();

				MenuScene menu = new MenuScene();
				menu.loadResources();
				menu.create();
				loadingScene = new LoadingScene();
				loadingScene.loadResources();
				loadingScene.create();
				// we want to show the splash at least SPLASH_DURATION
				// miliseconds
				long elapsed = System.currentTimeMillis() - timestamp;
				if (elapsed < MainActivity.SPLASH_DURATION) {
					try {
						Thread.sleep(MainActivity.SPLASH_DURATION - elapsed);
					} catch (InterruptedException e) {
						Debug.w("This should not happen");
					}
				}
				setCurrentScene(menu);
				res.engine.setScene(menu);
				splash.destroy();
				splash.unloadResources();
				return null;
			}
		}.execute();

	}

	public void showGameClassicPlay() {

	}

	public void showScene(Class<? extends AbstractScene> sceneClazz) {
		if (sceneClazz == LoadingScene.class) {
			throw new IllegalArgumentException(
					"You can't switch to Loading scene");
		}

		try {
			final AbstractScene scene = sceneClazz.newInstance();
			Debug.i("Showing scene " + scene.getClass().getName());

			final AbstractScene oldScene = getCurrentScene();
			setCurrentScene(loadingScene);
			res.engine.setScene(loadingScene);
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					if (oldScene != null) {
						oldScene.destroy();
						oldScene.unloadResources();
					}
					scene.loadResources();
					scene.create();
					setCurrentScene(scene);
					res.engine.setScene(scene);
					return null;
				}

			}.execute();

		} catch (Exception e) {
			String message = "Error while changing scene";
			Debug.e(message, e);
			throw new RuntimeException(message, e);
		}
	}

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public AbstractScene getCurrentScene() {
		return currentScene;
	}

	private void setCurrentScene(AbstractScene currentScene) {
		this.currentScene = currentScene;
	}

	/**
	 * show Game Scene mode SINGLE/MULTI
	 * 
	 * @param mode
	 */
	public static void showGameMode(int mode) {

	}

	public static void showMultiMenu(int mode) {
		// TODO Auto-generated method stub

	}

}
