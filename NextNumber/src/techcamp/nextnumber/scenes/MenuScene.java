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
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private BuildableBitmapTextureAtlas mGameBitmapTextureAtlas;
	protected static final int SINGLE = 0;
	protected static final int MULTI = 1;

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
		attachChild(new Sprite(0, 0, res.mMenuBackgroud, vbom));

		homeMenuChild = new Entity() {
			public boolean first = false;

			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (!this.first) {
					Log.i("Menu", "onManageUpdate");
					this.first = true;
					this.registerEntityModifier(new MoveModifier(0.25f, 0f,
							MainActivity.H, 0f, 0f));
				}
			}
		};
		gameMenuChild = new Entity(MainActivity.W, 0f);
		Log.i("Menu", "load entity");

		homeMenuChild.attachChild(home_title);
		ScaleButton playSingleBtn = new ScaleButton(0.75f * MainActivity.W,
				0.3f * MainActivity.H, play_single_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				goToGameScreen(SINGLE);
			}
		};
		homeMenuChild.attachChild(playSingleBtn);		
		this.registerTouchArea(playSingleBtn);	
		attachChild(homeMenuChild);
		ScaleButton playMultiBtn = new ScaleButton(playSingleBtn.getX(),
				playSingleBtn.getY() + 15, play_multi_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				goToGameScreen(MULTI);
			}
		};
		homeMenuChild.attachChild(playMultiBtn);
		this.registerTouchArea(playMultiBtn);
		ScaleButton achievBtn = new ScaleButton(playMultiBtn.getX(),
				playMultiBtn.getY() + 15, achievement_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Achievement scene show
			}
		};
		homeMenuChild.attachChild(achievBtn);		
		this.registerTouchArea(achievBtn);
		ScaleButton highBtn = new ScaleButton(achievBtn.getX(),
				achievBtn.getY() + 15, highscore_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Achievement scene show
			}
		};					
		homeMenuChild.attachChild(highBtn);				
		this.registerTouchArea(highBtn);

		/* Setting for gameMenuChild */
		ScaleButton classicBtn = new ScaleButton(0.5f * MainActivity.W,
				0.4f * MainActivity.H, classic_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Game play show: classic mode
			}
		};
		ScaleButton challengeBtn = new ScaleButton(classicBtn.getX(),
				classicBtn.getY() + 15, challenge_region, vbom, 1.2f) {
			@Override
			public void onClick() {
				// Game play show: challenge mode
			}
		};
		gameMenuChild.attachChild(classicBtn);
		gameMenuChild.attachChild(challengeBtn);
		this.registerTouchArea(classicBtn);
		this.registerTouchArea(challengeBtn);
		
		attachChild(gameMenuChild);
		Log.i("Menu", "" + homeMenuChild.getX() + "," + homeMenuChild.getY());
	}

	protected void goToHomeScreen() {
		home_title.setText("NEXtNUMber");
		home_title.resetSkewCenter();

		homeMenuChild.registerEntityModifier(new MoveModifier(0.25f,
				homeMenuChild.getX(), homeMenuChild.getY(), 0f, 0f));
		gameMenuChild
				.registerEntityModifier(new MoveModifier(0.25f, gameMenuChild
						.getX(), gameMenuChild.getY(), MainActivity.W, 0f));
	}

	protected void goToGameScreen(int mode) {
		if (mode == SINGLE)
			home_title.setText("singleplayer");
		else
			home_title.setText("multiplayer");
		home_title.resetSkewCenter();

		homeMenuChild
				.registerEntityModifier(new MoveModifier(0.25f, homeMenuChild
						.getX(), homeMenuChild.getY(), -MainActivity.W, 0f));
		gameMenuChild.registerEntityModifier(new MoveModifier(0.25f,
				gameMenuChild.getX(), gameMenuChild.getY(), 0f, 0f));
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