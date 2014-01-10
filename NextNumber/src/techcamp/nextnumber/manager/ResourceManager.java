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
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import techcamp.nextnumber.MainActivity;
import android.util.Log;

public class ResourceManager {
	private static final ResourceManager instance = new ResourceManager();

	// common objects
	public MainActivity activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	// splash
	public ITextureRegion mSplashRegion;

	public ITextureRegion mClassicBackgroundRegion;

	public ITextureRegion mChallengeBackgroundRegion;

	public ITextureRegion mBackgroundRegion;

	public Music mMusic;

	public Sound mSound;	

	public ITextureRegion mHighscoreRegion;

	public ITextureRegion mOptionRegion;

	public ITextureRegion mMenuBackground;

	public Font mFont;
	
	public Font mSmallFont;

	public ITextureRegion mClockRegion;

	public ITextureRegion mSquare;

	private BuildableBitmapTextureAtlas splashTextureAtlas;

	private BuildableBitmapTextureAtlas mGameBitmapTextureAtlas;

	private BuildableBitmapTextureAtlas mClassicBitmapTextureAtlas;

	public ITextureRegion mSquareBonusRegion;

	private BuildableBitmapTextureAtlas mChallengeBitmapTextureAtlas;

	public TextureRegion mSquareDoubleRegion;

	public TextureRegion mSquareTripleRegion;

	public TextureRegion mSquareBlinkRegion;

	private BuildableBitmapTextureAtlas highTextureAtlas;

	private BuildableBitmapTextureAtlas achievTextureAtlas;

	private BuildableBitmapTextureAtlas mMenuBitmapTextureAtlas;

	private BuildableBitmapTextureAtlas mBackgroundBitmapTextureAtlas;	

	public ITextureRegion mStartRegion;

	public ITextureRegion mCountDownRegion;

	public Font mWFont;

	public Font mHeaderFont;

	public ITextureRegion mBadgeARegion;

	public ITextureRegion mBadgeBRegion;

	private Sound mClick;

	private Sound mDoubleClick;

	private Sound mWinSound;

	private Sound mLoseSound;

	private Sound mCountdown;

	public ITextureRegion mWinRegion;

	public ITextureRegion mLoseRegion;

	public ResourceManager() {
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
		splashTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);

		mSplashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
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

	private synchronized void loadGameResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		mGameBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mSquare = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameBitmapTextureAtlas, activity.getAssets(), "square.png");
		mWinRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameBitmapTextureAtlas, activity.getAssets(), "win.png");
		mLoseRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameBitmapTextureAtlas, activity.getAssets(), "lose.png");

		try {
			mGameBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mGameBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadClassicGameResources() {
		loadGameResources();
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		mClassicBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);		
		mStartRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mClassicBitmapTextureAtlas, activity.getAssets(), "start.png");
		try {
			mClassicBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mClassicBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadChallengeGameResources() {
		loadGameResources();
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		mChallengeBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);		
		mCountDownRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mChallengeBitmapTextureAtlas, activity.getAssets(),
						"countdown.png");
		mSquareBonusRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mChallengeBitmapTextureAtlas,
						activity.getAssets(), "square_bonus.png");
		mSquareDoubleRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mChallengeBitmapTextureAtlas,
						activity.getAssets(), "square_double.png");
		mSquareTripleRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mChallengeBitmapTextureAtlas,
						activity.getAssets(), "square_triple.png");
		mSquareBlinkRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mChallengeBitmapTextureAtlas,
						activity.getAssets(), "square_blink.png");
		try {
			mChallengeBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mChallengeBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadMenuBackground() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mMenuBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mMenuBackground = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mMenuBitmapTextureAtlas, activity,
						"menu_background.png");

		try {
			mMenuBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mMenuBitmapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadHighscoreResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		highTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mHighscoreRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(highTextureAtlas, activity, "highscore.png");
		try {
			highTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			highTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadAchievementResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/achiev/");
		achievTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);		
		mBadgeARegion = BitmapTextureAtlasTextureRegionFactory				
				.createFromAsset(achievTextureAtlas, activity, "complete.png");
		mBadgeBRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(achievTextureAtlas, activity, "uncomplete.png");
		try {
			achievTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			achievTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	public synchronized void loadLoadingBackground() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBackgroundBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBackgroundBitmapTextureAtlas,
						activity.getAssets(), "background.png");

		try {
			mBackgroundBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBackgroundBitmapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	public synchronized void loadFonts() {
		FontFactory.setAssetBasePath("font/");
		mSmallFont = FontFactory
				.createFromAsset(engine.getFontManager(),
						engine.getTextureManager(), 400, 400,
						activity.getAssets(), "Hand_Of_Sean_Demo.ttf", 40f,
						true, Color.BLACK_ARGB_PACKED_INT);
		mFont = FontFactory
				.createFromAsset(engine.getFontManager(),
						engine.getTextureManager(), 400, 400,
						activity.getAssets(), "Hand_Of_Sean_Demo.ttf", 60f,
						true, Color.BLACK_ARGB_PACKED_INT);		
		mWFont = FontFactory
				.createFromAsset(engine.getFontManager(),
						engine.getTextureManager(), 400, 400,
						activity.getAssets(), "Hand_Of_Sean_Demo.ttf", 60f,
						true, Color.WHITE_ARGB_PACKED_INT);		
		mHeaderFont = FontFactory
				.createFromAsset(engine.getFontManager(),
						engine.getTextureManager(), 400, 400,
						activity.getAssets(), "Hand_Of_Sean_Demo.ttf", 70f,
						true, Color.BLACK_ARGB_PACKED_INT);
		mSmallFont.load();
		mFont.load();
		mHeaderFont.load();
		mWFont.load();
	}

	public synchronized void loadSounds() {
		SoundFactory.setAssetBasePath("sfx/");
		try {
			mSound = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "sound.mp3");
			mClick = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "click.mp3");
			mDoubleClick = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "double.mp3");
			mWinSound = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "win.mp3");
			mLoseSound = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "lose.mp3");
			mCountdown = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "lose.mp3");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void playSound(int type) {
		if (mSound != null) {
			switch (type) {
			case 0:
				break;
			case 1:
				mSound.play();
				break;
			case 2:
				mClick.play();break;
			case 3:
				mDoubleClick.play();break;
			case 4:
				mWinSound.play();break;
			case 5:
				mLoseSound.play();break;
			case 6:
				mCountdown.play();break;
			}
		}
	}

	public synchronized void loadMusic() {
		MusicFactory.setAssetBasePath("sfx/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(
					engine.getMusicManager(), activity, "music.mp3");
			mMusic.setVolume(0.5f);
			mMusic.setLooping(true);
			mMusic.play();
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

	private synchronized void unloadGame() {
		if (mGameBitmapTextureAtlas != null)
			mGameBitmapTextureAtlas.unload();
	}

	public synchronized void unloadClassicGame() {
		unloadGame();
		mClassicBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadChallengeGame() {
		unloadGame();
		mChallengeBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadMenuBackground() {
		mMenuBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadAchievement() {
		achievTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadBackground() {
		mBackgroundBitmapTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadHighscore() {
		highTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadSplash() {
		splashTextureAtlas.unload();
		System.gc();
	}

	public synchronized void unloadFont() {
		mFont.unload();
		mHeaderFont.unload();
		mWFont.unload();
	}

	// public void unloadSplashResources() {
	// splashTextureAtlas.unload();
	// }
}
