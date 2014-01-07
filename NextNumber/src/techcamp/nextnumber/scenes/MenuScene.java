/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01/2013
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
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
import android.util.Log;

public class MenuScene extends AbstractScene implements
		IOnMenuItemClickListener {

	private ITextureRegion play_region;
	private ITextureRegion options_region;
	private org.andengine.entity.scene.menu.MenuScene homeMenuChild;
	private org.andengine.entity.scene.menu.MenuScene singleMenuChild;
	private org.andengine.entity.scene.menu.MenuScene multiMenuChild;
	private ITextureRegion home_title;
	private ITextureRegion single_title;
	private ITextureRegion multi_title;
	private ITextureRegion option_title;
	private ITextureRegion achive_title;
	private ITextureRegion classic_region;
	private ITextureRegion challenge_region;
	private ITextureRegion mul_classic_region;
	private ITextureRegion mul_challenge_region;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private BuildableBitmapTextureAtlas mSingleBitmapTextureAtlas;
	private BuildableBitmapTextureAtlas mMultiBitmapTextureAtlas;
	private Object mCurrentScreen;
	public static final int MENU_PLAY = 0;
	public static final int MENU_OPTIONS = 1;
	public static final int SINGLE_CLASSIC = 2;
	public static final int SINGLE_CHALLENGE = 3;
	public static final int MULTI_CLASSIC = 4;
	public static final int MULTI_CHALLENGE = 5;

	@Override
	public void loadResources() {
		// load Background
		res.loadMenuBackground();

		// load home menu
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		// title
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mBitmapTextureAtlas, activity.getAssets(), "play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTextureAtlas, activity.getAssets(),
						"option.png");
		Log.i("Menu", "OK! load home");

		mSingleBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		classic_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mSingleBitmapTextureAtlas,
						activity.getAssets(), "play.png");
		challenge_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mSingleBitmapTextureAtlas,
						activity.getAssets(), "option.png");
		Log.i("Menu", "OK! load single");

		mMultiBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		mul_classic_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mMultiBitmapTextureAtlas,
						activity.getAssets(), "play.png");
		mul_challenge_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mMultiBitmapTextureAtlas,
						activity.getAssets(), "option.png");
		Log.i("Menu", "OK! load multi");

		try {
			mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mBitmapTextureAtlas.load();

			mSingleBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mSingleBitmapTextureAtlas.load();
			mMultiBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mMultiBitmapTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			Log.e("Menu", e.getMessage());
		}
	}

	@Override
	public void create() {
		Log.i("Menu", "create");
		// set Background
		attachChild(new Sprite(0, 0, res.mMenuBackgroud, vbom));

		homeMenuChild = new org.andengine.entity.scene.menu.MenuScene(
				res.camera);
		singleMenuChild = new org.andengine.entity.scene.menu.MenuScene(
				res.camera);
		multiMenuChild = new org.andengine.entity.scene.menu.MenuScene(
				res.camera);
		homeMenuChild.setPosition(10, 10);
		singleMenuChild.setPosition(10 - camera.getWidth(), 10);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, play_region, vbom), 1.2f, 1);
		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_OPTIONS, options_region, vbom), 1.2f, 1);
		final IMenuItem classicItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(SINGLE_CLASSIC, classic_region, vbom), 1.2f,
				1);
		final IMenuItem challengeItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(SINGLE_CHALLENGE, challenge_region, vbom),
				1.2f, 1);

		homeMenuChild.addMenuItem(playMenuItem);
		homeMenuChild.addMenuItem(optionsMenuItem);
//		singleMenuChild.addMenuItem(classicItem);
//		singleMenuChild.addMenuItem(challengeItem);

		homeMenuChild.buildAnimations();		
		homeMenuChild.setBackgroundEnabled(false);
//		singleMenuChild.buildAnimations();
//		singleMenuChild.setBackgroundEnabled(false);

		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY());
		optionsMenuItem.setPosition(optionsMenuItem.getX(),
				optionsMenuItem.getY() + 15);

		homeMenuChild.setOnMenuItemClickListener(this);
		//singleMenuChild.setOnMenuItemClickListener(this);

		attachChild(homeMenuChild);
	//	attachChild(singleMenuChild);
	}

	@Override
	public void unloadResources() {

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

	private void movetoSingle() {
		mCurrentScreen = this.singleMenuChild;
		homeMenuChild.registerEntityModifier(new MoveModifier(0.25f,
				homeMenuChild.getX(), homeMenuChild.getY(), -camera.getWidth(),
				0f));
		singleMenuChild.registerEntityModifier(new MoveModifier(0.25f,
				singleMenuChild.getX(), singleMenuChild.getY(), 0f, 0f));
	}

	@Override
	public boolean onMenuItemClicked(
			org.andengine.entity.scene.menu.MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch (arg1.getID()) {
		case MENU_PLAY:

			return true;
		case MENU_OPTIONS:
			return true;
		case MULTI_CHALLENGE:
			return true;
		case MULTI_CLASSIC:
			return true;
		case SINGLE_CHALLENGE:
			return true;
		case SINGLE_CLASSIC:
			return true;
		default:
			return false;
		}
	}

}
