/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.manager;

import org.andengine.util.debug.Debug;

import techcamp.nextnumber.scenes.AbstractScene;
import techcamp.nextnumber.scenes.ChallengeGameScene;
import techcamp.nextnumber.scenes.ClassicGameScene;
import techcamp.nextnumber.scenes.GameScene;
import techcamp.nextnumber.scenes.LoadingScene;
import techcamp.nextnumber.scenes.MenuScene;
//import techcamp.nextnumber.scenes.MultiChallengeGameScene;
import techcamp.nextnumber.scenes.MultiClassicGameScene;
import techcamp.nextnumber.scenes.MultiGameMenu;
import techcamp.nextnumber.scenes.SplashScene;
import android.os.AsyncTask;
import android.util.Log;

public class SceneManager {

	private static final SceneManager INSTANCE = new SceneManager();	

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

		loadingScene = new LoadingScene();
		loadingScene.loadResources();
		loadingScene.create();

		showMenu();
	}

	private void showMenu() {
		showScene(MenuScene.class);
	}

	/**
	 * @usage show screen
	 * @param sceneClazz: class scene
	 */
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

	/**
	 * @usage set current scene
	 * @param currentScene
	 */
	private void setCurrentScene(AbstractScene currentScene) {
		this.currentScene = currentScene;
	}

	/**
	 * @usage show Game Scene mode SINGLE/MULTI
	 * @param mode
	 */
	public void showGameMode() {		
		if (GameScene.modeGameplay == GameScene.CLASSIC){
			if(GameScene.modePlayer == GameScene.SINGLE){
				showScene(ClassicGameScene.class);
			}else showScene(MultiClassicGameScene.class);
			
		} else if (GameScene.modeGameplay == GameScene.CHALLENGE){
			if(GameScene.modePlayer == GameScene.MULTI){
			//	showScene(MultiChallengeGameScene.class);
			}else showScene(ChallengeGameScene.class);
		}
	}

	/**
	 * @usage show multiplayer Menu Screen after select mode gameplay
	 * @param mode
	 */
	public void showMultiMenu(int mode) {
		GameScene.modeGameplay = mode;
		GameScene.modePlayer = GameScene.MULTI;

		showScene(MultiGameMenu.class);
	}
}
