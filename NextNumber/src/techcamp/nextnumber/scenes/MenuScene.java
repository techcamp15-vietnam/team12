/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01/2013
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.manager.SceneManager;
import techcamp.nextnumber.utils.ScaleButton;
import android.util.Log;

public class MenuScene extends AbstractScene {

	private ITextureRegion play_single_region;
	private ITextureRegion play_multi_region;
	private ITextureRegion achievement_region;
	private ITextureRegion highscore_region;
	private ITextureRegion classic_region;
	private ITextureRegion challenge_region;
	private Entity homeMenuChild;
	private Entity gameMenuChild;
	private Text home_title;
	private Text game_title;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private BuildableBitmapTextureAtlas mGameBitmapTextureAtlas;
	private static int modePlayer = GameScene.SINGLE;
	public static MenuScene instance;

	// Load resources
	@Override
	public void loadResources() {
		instance = this;
		// load Background
		res.loadMenuBackground();
		res.loadFonts();
		res.loadSounds();
		res.loadMusic();

		// load home menu
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		play_single_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"nothing.png");
		play_multi_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"nothing.png");
		achievement_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"nothing.png");
		highscore_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"nothing.png");
		Log.i("Menu", "OK! load home");

		// load player menu
		mGameBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		classic_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mGameBitmapTextureAtlas, activity.getAssets(),
						"nothing.png");
		challenge_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mGameBitmapTextureAtlas, activity.getAssets(),
						"nothing.png");
		Log.i("Menu", "OK! load game");

		// Title
		home_title = new Text(0, MainActivity.H * 0.1f, res.mHeaderFont,
				"Next Number", vbom);
		game_title = new Text(0, MainActivity.H * 0.1f, res.mHeaderFont,
				"Singleplayer", vbom) {
			int currentMode = GameScene.SINGLE;

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (modePlayer != currentMode) {
					currentMode = modePlayer;
					if (modePlayer == GameScene.SINGLE) {
						this.setText("Singleplayer");
					} else {
						this.setText("Multiplayer");
					}
				}
			}
		};

		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBitmapTextureAtlas.load();
			mGameBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mGameBitmapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Log.e("Menu", e.getMessage());
		}
	}

	@Override
	public void create() {
		Log.i("Menu", "create");
		res.loadFonts();
		res.resumeMusic();
		// set Background
		attachChild(new Sprite(0, 0, res.mMenuBackground, vbom));

		// align for text
		home_title.setPosition(MainActivity.W / 2 - home_title.getWidth() / 2,
				home_title.getY());
		game_title.setPosition(MainActivity.W / 2 - game_title.getWidth() / 2,
				game_title.getY());

		homeMenuChild = new Entity(0, MainActivity.H) {
			public boolean first = false;

			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (!this.first) {
					this.first = true;
					this.registerEntityModifier(new MoveModifier(0.5f, 0f, 0f,
							MainActivity.H, 0f));
				}
			}
		};
		gameMenuChild = new Entity(MainActivity.W, 0);

		homeMenuChild.attachChild(home_title);
		ScaleButton playSingleBtn = new ScaleButton(0.5f * MainActivity.W
				- play_single_region.getWidth() / 2, 0.3f * MainActivity.H,
				play_single_region, vbom, 1.2f, new Text(20, 0, res.mFont,
						"Singleplayer", vbom)) {
			@Override
			public void onClick() {
				Log.i("Menu", "click single");
				goToGameScreen(GameScene.SINGLE);
			}
		};
		homeMenuChild.attachChild(playSingleBtn);
		this.registerTouchArea(playSingleBtn);
		attachChild(homeMenuChild);
		ScaleButton playMultiBtn = new ScaleButton(playSingleBtn.getX(),
				playSingleBtn.getY() + playSingleBtn.getHeight() + 45,
				play_multi_region, vbom, 1.2f, new Text(20, 0, res.mFont,
						"Multiplayer", vbom)) {
			@Override
			public void onClick() {
				Log.i("Menu", "click multi");
				if (activity.checkBluetoothAvailable()) {
					if (activity.startBluetoothService()) {
						goToGameScreen(GameScene.MULTI);
					}
				}
			}
		};
		homeMenuChild.attachChild(playMultiBtn);
		this.registerTouchArea(playMultiBtn);
		ScaleButton achievBtn = new ScaleButton(playSingleBtn.getX(),
				playMultiBtn.getY() + playMultiBtn.getHeight() + 45,
				achievement_region, vbom, 1.2f, new Text(20, 0, res.mFont,
						"Achievemenet", vbom)) {
			@Override
			public void onClick() {
				// Achievement layer show
				SceneManager.getInstance().showScene(AchievementScene.class);
			}
		};
		homeMenuChild.attachChild(achievBtn);
		this.registerTouchArea(achievBtn);
		ScaleButton highBtn = new ScaleButton(playSingleBtn.getX(),
				achievBtn.getY() + achievBtn.getHeight() + 45,
				highscore_region, vbom, 1.2f, new Text(20, 0, res.mFont,
						"Highscore", vbom)) {
			@Override
			public void onClick() {
				// Highscore layer show
				SceneManager.getInstance().showScene(HighscoreScene.class);
			}
		};
		Log.i("Menu", "W: " + achievBtn.getWidth());
		homeMenuChild.attachChild(highBtn);
		this.registerTouchArea(highBtn);

		/* Setting for gameMenuChild */
		gameMenuChild.attachChild(game_title);
		ScaleButton classicBtn = new ScaleButton(playSingleBtn.getX(),
				0.4f * MainActivity.H, classic_region, vbom, 1.2f, new Text(20,
						0, res.mFont, "Classic", vbom)) {
			@Override
			public void onClick() {
				// Game play show: classic mode
				if (modePlayer == GameScene.SINGLE) {
					MainActivity.cellBoardClassicalMode.CreateData(1);
					GameScene.modeGameplay = GameScene.CLASSIC;
					GameScene.modePlayer = GameScene.SINGLE;
					SceneManager.getInstance().showGameMode();
				} else {
					MainActivity.checkMultiMode = false;
					SceneManager.getInstance().showMultiMenu(GameScene.CLASSIC);
				}
			}
		};
		gameMenuChild.attachChild(classicBtn);
		this.registerTouchArea(classicBtn);
		ScaleButton challengeBtn = new ScaleButton(classicBtn.getX(),
				classicBtn.getY() + classicBtn.getHeight() + 55,
				challenge_region, vbom, 1.2f, new Text(20, 0, res.mFont,
						"Challenge", vbom)) {
			@Override
			public void onClick() {
				// Game play show: challenge mode
				if (modePlayer == GameScene.SINGLE) {
					MainActivity.cellBoardClassicalMode.CreateData(2);
					GameScene.modeGameplay = GameScene.CHALLENGE;
					GameScene.modePlayer = GameScene.SINGLE;
					SceneManager.getInstance().showGameMode();
				} else {
					MainActivity.checkMultiMode = true;
					SceneManager.getInstance().showMultiMenu(
							GameScene.CHALLENGE);
				}
			}
		};
		gameMenuChild.attachChild(challengeBtn);
		this.registerTouchArea(challengeBtn);

		attachChild(gameMenuChild);
		this.layerView = false;
		this.currentLayer = homeMenuChild;

	}

	protected void goToHomeScreen() {
		Log.i("Menu", "Go to Home");
		this.layerView = false;
		this.currentLayer = homeMenuChild;
		gameMenuChild
				.registerEntityModifier(new MoveModifier(0.5f, gameMenuChild
						.getX(), MainActivity.W, gameMenuChild.getY(), 0f));
		homeMenuChild.registerEntityModifier(new MoveModifier(0.5f,
				homeMenuChild.getX(), 0f, homeMenuChild.getY(), 0f));
	}

	protected void goToGameScreen(int mode) {
		modePlayer = mode;
		this.layerView = true;
		this.currentLayer = gameMenuChild;
		homeMenuChild
				.registerEntityModifier(new MoveModifier(0.5f, homeMenuChild
						.getX(), -MainActivity.W, homeMenuChild.getY(), 0f));
		gameMenuChild.registerEntityModifier(new MoveModifier(0.5f,
				gameMenuChild.getX(), 0f, gameMenuChild.getY(), 0f));
	}

	@Override
	public void unloadResources() {
		mBitmapTextureAtlas.unload();
		mGameBitmapTextureAtlas.unload();
		res.unloadMenuBackground();
		res.unloadFont();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onHiddenLayer() {
		if (currentLayer != homeMenuChild) {
			goToHomeScreen();
		}
	}

	public void startGameMultiMode() {
		GameScene.modeGameplay = GameScene.CLASSIC;
		GameScene.modePlayer = GameScene.SINGLE;
		SceneManager.getInstance().showGameMode();
	}

}