/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
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
import techcamp.nextnumber.utils.CellArray;
import techcamp.nextnumber.utils.ScaleButton;
import techcamp.nextnumber.utils.Square;
import android.util.Log;

public class ClassicGameScene extends GameScene {

	private Text timeText;
	private boolean started = false;
	private ITextureRegion startRegion;
	private Entity table;
	private static ClassicGameScene instance;

	@Override
	public void loadResources() {
		GameScene.modeGameplay = GameScene.CLASSIC;
		super.loadResources();
		res.mHeaderFont.prepareLetters(":1234567890.".toCharArray());
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BuildableBitmapTextureAtlas mGameBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), MainActivity.W, MainActivity.H,
				BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
		startRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mGameBitmapTextureAtlas, activity.getAssets(), "start.png");

		try {
			mGameBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			mGameBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.e("Resource", e.getMessage());
		}
	}

	@Override
	public void create() {
		instance = this;
		super.create();
		addToHeadLayer();
		addToMainLayer();
		attachChild(headLayer);
		attachChild(mainLayer);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();
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
	public void addToMainLayer() {
		table = new Entity(0, 0);
		final CellArray C = new CellArray();
		C.CreateData(1);
		float boundSpace = (MainActivity.W - LIMIT_COL * res.mSquare.getWidth())
				/ LIMIT_COL; // calculate suitable space between squares
		Log.i("Game", "" + boundSpace + "," + res.mSquare.getWidth());
		for (int i = 0; i < LIMIT_ROW; i++) {
			for (int j = 0; j < LIMIT_COL; j++) {
				Square s = new Square(0, 0, res.mSquare, vbom, res.mHeaderFont,
						1.2f, "" + (C.CA[i * 5 + j].getValue())) {
					@Override
					public void onClick() {
						if (this.value == C.nextint) {
							if (C.nextint == LIMIT_SIZE) {
								started = false;
								finish();
							}
							C.nextint++;

							this.setAlpha(.5f);
							this.setEnable(false);
						}
					}

				};
				s.setSound(0);
				table.attachChild(s);
				if (j == 0) {
					s.setPosition(j * s.getWidth() + boundSpace / 2,
							i * s.getHeight() + boundSpace * (i + 1));
				} else {
					s.setPosition(j * s.getWidth() + boundSpace * (j + 0.5f), i
							* s.getHeight() + boundSpace * (i + 1));
				}
			}
		}
		this.mainLayer.attachChild(table);

		ScaleButton start = new ScaleButton(0, 0, startRegion, vbom, 1.0f) {
			@Override
			public void onClick() {
				ClassicGameScene.getIntance().unregisterTouchArea(this);				
				this.registerEntityModifier(new MoveModifier(.5f, 0,
						MainActivity.W * 1.5f, 0, 0) {
					@Override
					protected void onModifierFinished(IEntity pItem) {						
						setStartTouch();
					};
				});
			}
		};
		this.registerTouchArea(start);
		this.mainLayer.attachChild(start);

	}

	protected void finish() {
		// TODO Auto-generated method stub

	}

	protected static ClassicGameScene getIntance() {
		return instance;
	}

	private void setStartTouch() {
		Log.i("Game", "" + table.getChildCount());
		for (int i = 0; i < table.getChildCount(); i++) {
			this.registerTouchArea((Square) table.getChildByIndex(i));			
		}
		started = true;
	}

	@Override
	public void addToHeadLayer() {
		Entity time = new Entity(MainActivity.W / 2, 0);
		timeText = new Text(0, 0, res.mHeaderFont, "00:00.00", vbom) {
			long current = System.currentTimeMillis();
			long t = 0;

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (started) {
					final long second = System.currentTimeMillis();
					if (second - current > 10) {
						long i = (second - current) % 10;
						current += 10 * i;
						t += i;
						this.setText(String.format("%02d:%02d.%02d", t / 10000,
								t / 100, t % 100));
					}
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		timeText.setPosition(time.getX() - timeText.getWidth() - 15,
				time.getY() + 40);
		time.attachChild(new Sprite(15, 15, res.mClockRegion, vbom));
		time.attachChild(timeText);
		this.headLayer.attachChild(time);
	}
}
