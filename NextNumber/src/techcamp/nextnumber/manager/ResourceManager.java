/**
 * @author pvhau
 * @team TechCamp Group 12 - 3H
 * @date 06/01/2013
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
import org.andengine.util.debug.Debug;

import techcamp.nextnumber.MainActivity;
import android.content.Context;
import android.graphics.Typeface;

public class ResourceManager {

	private static ResourceManager instance = new ResourceManager();

	private static final int HEIGHT = 800;
	private static final int WIDTH = 480;

	public MainActivity activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	public ITextureRegion mClassicBackgroundRegion;
	public ITextureRegion mChallengeBackgroundRegion;
	public ITextureRegion mMenuBackgroundRegion;
	public ITextureRegion mSplashRegion;
	public ITextureRegion mOptionRegion;
	public Sound mSound;
	public Music mMusic;
	public Font mFont;
	public ITextureRegion mAchivementRegion;
	public ITextureRegion mHighscoreRegion;

	private ResourceManager() {
	}

	public static ResourceManager getInstance() {
		if (instance == null) {

		}

		return instance;
	}

	public void init(MainActivity activity) {
		this.activity = activity;
		this.engine = activity.getEngine();
		this.camera = engine.getCamera();
		this.vbom = engine.getVertexBufferObjectManager();
	}

	public synchronized void loadClassicGameResources() {
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), WIDTH, HEIGHT,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mClassicBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity,
						"classic_game_background.png");
		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 1));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public synchronized void loadChallengeGameResources() {
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), WIDTH, HEIGHT,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mChallengeBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity,
						"challenge_game_background.png");
		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 1));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public synchronized void loadMenuResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), WIDTH, HEIGHT,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mMenuBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity,
						"menu_background.png");
		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 1));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public synchronized void loadSplashResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), WIDTH, HEIGHT,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mSplashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.png");
		try {
			splashTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			splashTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadOptionResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), WIDTH, HEIGHT,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mOptionRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "option.png");
		try {
			splashTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			splashTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadHighscoreResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), WIDTH, HEIGHT,
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
				engine.getTextureManager(), WIDTH, HEIGHT,
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

	public synchronized void loadFonts(Engine pEngine) {
		Font mFont = FontFactory.create(pEngine.getFontManager(),
				pEngine.getTextureManager(), 256, 256,
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

	public synchronized void loadMusic() {
		MusicFactory.setAssetBasePath("sfx/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(
					engine.getMusicManager(), activity, "music.mp3");
			mMusic.setVolume(0.5f);
			mMusic.setLooping(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void unloadClassicGame() {
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mClassicBackgroundRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		// ... Continue to unload all textures related to the 'Game' scene
		// Once all textures have been unloaded, attempt to invoke the Garbage
		// Collector
		System.gc();
	}

	public synchronized void unloadChallengeGame() {
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mChallengeBackgroundRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		// ... Continue to unload all textures related to the 'Game' scene
		// Once all textures have been unloaded, attempt to invoke the Garbage
		// Collector
		System.gc();
	}

	public synchronized void unloadOption() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mOptionRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadMenu() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mMenuBackgroundRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadSplash() {
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mSplashRegion
				.getTexture();
		mBitmapTextureAtlas.unload();
		System.gc();
	}
}