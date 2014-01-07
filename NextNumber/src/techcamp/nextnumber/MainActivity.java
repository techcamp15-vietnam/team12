/**
 * @author pvhau
 * @team TechCamp Group 12 - 3H
 * @date 06/01/2013
 */

package techcamp.nextnumber;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

public class MainActivity extends BaseGameActivity {

	// The following constants will be used to define the width and height
	// of our game's camera view
	private static final int WIDTH = 480;
	private static final int HEIGHT = 800;
	private static final String TAG = "SceneActivity";	 
	
	private Engine mEngine;
	private Camera mCamera;
	private Scene mScene;

	private Sound mSound;
	private Music mMusic;

	@Override
	public synchronized void onResumeGame() {
		if (mMusic != null && !mMusic.isPlaying()) {
			mMusic.play();
		}
		super.onResumeGame();
	}

	@Override
	public synchronized void onPauseGame() {
		if (mMusic != null && mMusic.isPlaying()) {
			mMusic.pause();
		}
		super.onPauseGame();
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		mEngine = new Engine(pEngineOptions);
		return mEngine;
	}

	/*
	 * The onCreateEngineOptions method is responsible for creating the options
	 * to be applied to the Engine object once it is created. The options
	 * include, but are not limited to enabling/disable sounds and music,
	 * defining multitouch options, changing rendering options and more.
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {

		// Define our mCamera object
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		// Declare & Define our engine options to be applied to our Engine
		// object
		EngineOptions engineOptions = new EngineOptions(false,
				ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(),
				mCamera);

		// It is necessary in a lot of applications to define the following
		// wake lock options in order to disable the device's display
		// from turning off during gameplay due to inactivity
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

		// Enable music and sound
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);

		// Return the engineOptions object, passing it to the engine
		return engineOptions;
	}

	/*
	 * The onCreateResources method is in place for resource loading, including
	 * textures, sounds, and fonts for the most part.
	 */
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {				

		/*
		 * We should notify the pOnCreateResourcesCallback that we've finished
		 * loading all of the necessary resources in our game AFTER they are
		 * loaded. onCreateResourcesFinished() should be the last method called.
		 */
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		mMusic.play();
		mMusic.setVolume(0.5f);
		mMusic.setLooping(true);
	}

	/*
	 * The onCreateScene method is in place to handle the scene initialization
	 * and setup. In this method, we must at least *return our mScene object*
	 * which will then be set as our main scene within our Engine object
	 * (handled "behind the scenes"). This method might also setup touch
	 * listeners, update handlers, or more events directly related to the scene.
	 */
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		// Create the Scene object
		mScene = new Scene();

		// Notify the callback that we're finished creating the scene, returning
		// mScene to the mEngine object (handled automatically)
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	/*
	 * The onPopulateScene method was introduced to AndEngine as a way of
	 * separating scene-creation from scene population. This method is in place
	 * for attaching child entities to the scene once it has already been
	 * returned to the engine and set as our main scene.
	 */
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		// onPopulateSceneFinished(), similar to the resource and scene callback
		// methods, should be called once we are finished populating the scene.
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

}