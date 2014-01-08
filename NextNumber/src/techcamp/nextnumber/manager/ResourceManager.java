/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import techcamp.nextnumber.MainActivity;
import android.graphics.Typeface;
import android.util.Log;

public class ResourceManager {
	private static final ResourceManager instance = new ResourceManager();

	// common objects
	public MainActivity activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	// splash
	public ITextureRegion splashRegion;

	public ITextureRegion mClassicBackgroundRegion;

	public ITextureRegion mChallengeBackgroundRegion;

	public ITextureRegion mBackgroundRegion;

	public Music mMusic;

	public Sound mSound;

	public ITextureRegion mAchivementRegion;

	public ITextureRegion mHighscoreRegion;

	public ITextureRegion mOptionRegion;

	public ITextureRegion mMenuBackground;

	public Font mFont;

	private ResourceManager() {
	}

	public static ResourceManager getInstance() {
		return instance;
	}

	public void init(MainActivity activity) {
		if (instance == null)
			return;
		instance.activity = activity;
		instance.engine = activity.getEngine();
		instance.camera = engine.getCamera();
		instance.vbom = engine.getVertexBufferObjectManager();
	}

	// splash
	public void loadSplashResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);

		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity.getAssets(), "splash.png");

		try {
			if (splashTextureAtlas != null) {
				splashTextureAtlas
						.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
								0, 0, 0));
				splashTextureAtlas.load();
			}

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadClassicGameResources() {
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mClassicBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"classic_game_background.png");
		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadChallengeGameResources() {
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mChallengeBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"challenge_game_background.png");
		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadOptionResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mOptionRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity.getAssets(), "option.png");
		try {
			splashTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			splashTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadMenuBackground() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mMenuBackground = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"menu_background.png");

		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBitmapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadHighscoreResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mHighscoreRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTextureAtlas, activity, "highscore.png");
		try {
			splashTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			splashTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadAchivementResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mAchivementRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTextureAtlas, activity, "achive.png");
		try {
			splashTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			splashTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadLoadingBackground() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"background.png");

		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBitmapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadFonts() {
		mFont = FontFactory.create(engine.getFontManager(),
				engine.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 32f, true,
				Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
	}

	public synchronized void loadSounds() {
		SoundFactory.setAssetBasePath("sfx/");
		try {
			mSound = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "sound.mp3");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void playSound() {
		if (mSound != null) {
			mSound.play();
		}
	}

	public synchronized void loadMusic() {
		MusicFactory.setAssetBasePath("sfx/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(
					engine.getMusicManager(), activity, "music.mp3");
			mMusic.setVolume(0.5f);
			mMusic.setLooping(true);
			mMusic.pause();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void resumeMusic() {
		if (mMusic != null && !mMusic.isPlaying()) {
			mMusic.play();
		}
	}

	public synchronized void pauseMusic() {
		if (mMusic != null && mMusic.isPlaying()) {
			mMusic.pause();
		}
	}

	public synchronized void unloadClassicGame() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mClassicBackgroundRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadChallengeGame() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mChallengeBackgroundRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadOption() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mOptionRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadMenuBackground() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mMenuBackground
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadAchievement() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mAchivementRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadBackground() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mBackgroundRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadHighscore() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mHighscoreRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadSplash() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) splashRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadFont() {
		mFont.unload();
	}

	public void loadGameResources() {
		// TODO Auto-generated method stub
		
	}

	// public void unloadSplashResources() {
	// splashTextureAtlas.unload();
	// }
}
