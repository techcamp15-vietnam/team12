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
	private Text single_title;
	private Text multi_title;
	private Text game_title;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private BuildableBitmapTextureAtlas mGameBitmapTextureAtlas;
	public static final int SINGLE = 0;
	public static final int MULTI = 1;
	public static final int CLASSIC = 2;
	public static final int CHALLENGE = 3;

	// Load resources
	@Override
	public void loadResources() {
		// load Background
		res.loadMenuBackground();
		res.loadFonts();

		// load home menu
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		play_single_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"play_single.png");
		play_multi_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"play_multi.png");
		achievement_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"achievement.png");
		highscore_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"highscore.png");
		Log.i("Menu", "OK! load home");

		// load player menu
		mGameBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		classic_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mGameBitmapTextureAtlas, activity.getAssets(),
						"classic.png");
		challenge_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mGameBitmapTextureAtlas, activity.getAssets(),
						"challenge.png");
		Log.i("Menu", "OK! load game");

		// Title
		home_title = new Text(MainActivity.W * 0.4f, MainActivity.H * 0.1f,
				res.mFont, "NEXtNUMber", vbom);
		single_title = new Text(MainActivity.W * 0.4f, MainActivity.H * 0.1f,
				res.mFont, "Singleplayer", vbom);
		multi_title = new Text(MainActivity.W * 0.4f, MainActivity.H * 0.1f,
				res.mFont, "Multiplayer", vbom);
		game_title = single_title;
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
		// set Background
		attachChild(new Sprite(0, 0, res.mMenuBackground, vbom));

		homeMenuChild = new Entity() {
			public boolean first = false;

			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (!this.first) {
					Log.i("Menu", "onManageUpdate");
					this.first = true;
					this.registerEntityModifier(new MoveModifier(0.25f, 0f, 0f,
							MainActivity.H, 0f));
				}
			}
		};
		gameMenuChild = new Entity(MainActivity.W, 0);
		Log.i("Menu", "load entity");

		homeMenuChild.attachChild(home_title);
		ScaleButton playSingleBtn = new ScaleButton(0.5f * MainActivity.W
				- play_single_region.getWidth() / 2, 0.3f * MainActivity.H,
				play_single_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				Log.i("Menu", "click single");
				goToGameScreen(SINGLE);
			}
		};
		homeMenuChild.attachChild(playSingleBtn);
		this.registerTouchArea(playSingleBtn);
		attachChild(homeMenuChild);
		ScaleButton playMultiBtn = new ScaleButton(playSingleBtn.getX(),
				playSingleBtn.getY() + playSingleBtn.getHeight() + 25,
				play_multi_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				Log.i("Menu", "click multi");
				goToGameScreen(MULTI);
			}
		};
		homeMenuChild.attachChild(playMultiBtn);
		this.registerTouchArea(playMultiBtn);
		ScaleButton achievBtn = new ScaleButton(playSingleBtn.getX(),
				playMultiBtn.getY() + playMultiBtn.getHeight() + 25,
				achievement_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Achievement scene show				
			}
		};
		homeMenuChild.attachChild(achievBtn);
		this.registerTouchArea(achievBtn);
		ScaleButton highBtn = new ScaleButton(playSingleBtn.getX(),
				achievBtn.getY() + achievBtn.getHeight() + 25,
				highscore_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Achievement scene show
			}
		};
		homeMenuChild.attachChild(highBtn);
		this.registerTouchArea(highBtn);

		/* Setting for gameMenuChild */
		gameMenuChild.attachChild(game_title);
		ScaleButton classicBtn = new ScaleButton(playSingleBtn.getX(),
				0.4f * MainActivity.H, classic_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Game play show: classic mode
				if (game_title == single_title) {
					SceneManager.getInstance().showGameMode(CLASSIC, SINGLE);
				} else {
					SceneManager.getInstance().showMultiMenu(CLASSIC);
				}
			}
		};
		gameMenuChild.attachChild(classicBtn);
		this.registerTouchArea(classicBtn);
		ScaleButton challengeBtn = new ScaleButton(classicBtn.getX(),
				classicBtn.getY() + classicBtn.getHeight() + 25,
				challenge_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Game play show: challenge mode
				if (game_title == single_title) {
					SceneManager.getInstance().showGameMode(CHALLENGE, SINGLE);
				} else {
					SceneManager.getInstance().showMultiMenu(CHALLENGE);
				}
			}
		};
		gameMenuChild.attachChild(challengeBtn);
		this.registerTouchArea(challengeBtn);

		attachChild(gameMenuChild);
		Log.i("Menu", "" + homeMenuChild.getX() + "," + homeMenuChild.getY());
	}

	protected void goToHomeScreen() {
		Log.i("Menu", "Go to Home");
		homeMenuChild.registerEntityModifier(new MoveModifier(0.25f,
				homeMenuChild.getX(), homeMenuChild.getY(), 0f, 0f));
		gameMenuChild
				.registerEntityModifier(new MoveModifier(0.25f, gameMenuChild
						.getX(), gameMenuChild.getY(), MainActivity.W, 0f));
	}

	protected void goToGameScreen(int mode) {
		if (mode == SINGLE) {
			if (game_title != single_title)
				game_title = single_title;
		} else if (mode == MULTI) {
			if (game_title != multi_title) {
				game_title = multi_title;
			}
		}
		homeMenuChild
				.registerEntityModifier(new MoveModifier(0.25f, homeMenuChild
						.getX(), -MainActivity.W, homeMenuChild.getY(), 0f));
		gameMenuChild.registerEntityModifier(new MoveModifier(0.25f,
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
}