/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
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

public class MultiGameMenu extends AbstractScene {

	protected static final int CREATE = -1;
	protected static final int FIND = -2;
	private int select = CREATE;
	private Entity waitLayer;
	private Entity homeLayer;
	private ITextureRegion mFindRegion;
	private ITextureRegion mStartRegion;
	private ITextureRegion mCreateRegion;
	private BuildableBitmapTextureAtlas bTextureAtlas;

	@Override
	public void loadResources() {
		res.loadMenuBackground();
		res.loadFonts();
		res.loadMusic();
		res.loadSounds();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/multi/");
		bTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);

		mFindRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				bTextureAtlas, activity.getAssets(), "nothing.png");
		mStartRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				bTextureAtlas, activity.getAssets(), "nothing.png");
		mCreateRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				bTextureAtlas, activity.getAssets(), "nothing.png");

		try {
			if (bTextureAtlas != null) {
				bTextureAtlas
						.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
								0, 0, 0));
				bTextureAtlas.load();
			}

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}
	}

	@Override
	public void create() {
		attachChild(new Sprite(0, 0, res.mMenuBackground, vbom));
		homeLayer = new Entity(0, 0);
		waitLayer = new Entity(MainActivity.W, 0);
		ScaleButton createBtn = new ScaleButton(15, 0, mCreateRegion, vbom,
				1.2f) {

			@Override
			public void onClick() {
				goToWait(CREATE);
			}
		};
		createBtn.setPosition(MainActivity.W / 2 - createBtn.getWidth() / 2,
				MainActivity.H * 0.3f);
		homeLayer.attachChild(createBtn);
		this.registerTouchArea(createBtn);

		ScaleButton findBtn = new ScaleButton(0, 0, mFindRegion, vbom, 1.2f) {

			@Override
			public void onClick() {
				goToWait(FIND);
			}
		};
		findBtn.setPosition(MainActivity.W / 2 - findBtn.getWidth() / 2,
				createBtn.getY() + createBtn.getHeight() * 2);
		homeLayer.attachChild(findBtn);
		this.registerTouchArea(findBtn);

		Entity waitSub = new Entity(MainActivity.W, 0);
		waitLayer.attachChild(waitSub);
		ScaleButton startBtn = new ScaleButton(0f, 0f, mStartRegion, vbom,
				1.2f, new Text(0f, 0f, res.mFont, "Connect", vbom) {
					protected int current = CREATE;

					@Override
					protected void onManagedUpdate(float pSecondsElapsed) {
						if (current != select) {
							current = select;
							if (current == FIND) {
								this.setText("Start");
							} else {
								this.setText("Connect");
							}
						}
					}
				}) {

			@Override
			public void onClick() {
				SceneManager.getInstance().showGameMode();
			}
		};
		startBtn.setPosition(MainActivity.W / 2 - startBtn.getWidth() / 2,
				MainActivity.H * 0.9f);
		waitLayer.attachChild(startBtn);
		this.registerTouchArea(startBtn);

		this.attachChild(homeLayer);
		this.attachChild(waitLayer);
		this.currentLayer = homeLayer;
		this.layerView = false;
	}

	protected void goToWait(int type) {
		select = type;
		if (type == CREATE) {
			// Handler
		} else if (type == FIND) {
			// Handler
		}

		this.currentLayer = waitLayer;
		this.layerView = true;
		homeLayer.registerEntityModifier(new MoveModifier(0.5f, homeLayer
				.getX(), -MainActivity.W, homeLayer.getY(), 0f));
		waitLayer.registerEntityModifier(new MoveModifier(0.5f, waitLayer
				.getX(), 0f, waitLayer.getY(), 0f));
	}

	@Override
	public void unloadResources() {
		res.unloadMenuBackground();
		res.unloadFont();
		bTextureAtlas.unload();
	}

	@Override
	public void destroy() {	
		
	}

	@Override
	public void onPause() {		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHiddenLayer() {
		if (this.currentLayer != homeLayer) {
			this.currentLayer = homeLayer;
			this.layerView = false;
			homeLayer.registerEntityModifier(new MoveModifier(0.5f, homeLayer
					.getX(), 0f, homeLayer.getY(), 0f));
			waitLayer.registerEntityModifier(new MoveModifier(0.5f, waitLayer
					.getX(), MainActivity.W, waitLayer.getY(), 0f));
		}
	}

}