/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.manager.AchievementManager;
import techcamp.nextnumber.manager.ResourceManager;
import techcamp.nextnumber.utils.Cell;
import techcamp.nextnumber.utils.CellArray;
import techcamp.nextnumber.utils.ScaleButton;
import techcamp.nextnumber.utils.Square;

public class ChallengeGameScene extends GameScene {

	protected static long LIMIT_TIME = 4000;
	private static ChallengeGameScene instance;
	private Text timeText;
	protected boolean started = false;
	protected long restTime = 0;
	private Text next;
	private Entity table;
	private CellArray C = MainActivity.cellBoardClassicalMode;
	private boolean win;
	private int count_bonus = 0;
	private int count_double = 0;
	private int count_triple = 0;
	private int count_blink = 0;

	@Override
	public void loadResources() {
		GameScene.modeGameplay = GameScene.CHALLENGE;
		super.loadResources();
		res.mFont.prepareLetters("Next :1234567890.".toCharArray());
		Log.i("Game", "end load");
	}

	@Override
	public void create() {
		super.create();
		instance = this;
		addToHeadLayer();
		addToMainLayer();
		this.attachChild(this.headLayer);
		this.attachChild(this.mainLayer);
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

	@Override
	public void addToMainLayer() {
		table = new Entity(0, 0);		
		float boundSpace = (MainActivity.W - LIMIT_COL * res.mSquare.getWidth())
				/ LIMIT_COL; // calculate suitable space between squares
		Log.i("Game", "" + boundSpace + "," + res.mSquare.getWidth());
		for (int i = 0; i < LIMIT_ROW; i++) {
			for (int j = 0; j < LIMIT_COL; j++) {
				ITextureRegion it = null;
				switch (C.CA[5 * i + j].geteffect()) {
				case Cell.BLINK:
					it = res.mSquareBlinkRegion;
					break;
				case Cell.BONUS:
					it = res.mSquareBonusRegion;
					break;
				case Cell.DOUBLE:
					it = res.mSquareDoubleRegion;
					break;
				case Cell.TRIPLE:
					it = res.mSquareTripleRegion;
					break;
				default:
					it = res.mSquare;
					break;
				}
				Square s = new Square(0, 0, it, vbom, res.mFont, 1.2f, ""
						+ (C.CA[i * 5 + j].getValue()),
						C.CA[i * 5 + j].geteffect()) {
					@Override
					public void onClick() {
						if (GameScene.modePlayer == GameScene.SINGLE) {
							if (this.value == C.nextint) {
								if (C.nextint >= LIMIT_SIZE) {
									if (this.getType() == Cell.NONE
											|| this.getType() == Cell.BONUS
											|| this.getType() == Cell.BLINK) {
										started = false;
										this.setAlpha(.5f);
										this.setEnable(false);
										win = true;
										finish();
									} else {
										handlerSquare(this);
									}
								} else {
									handlerSquare(this);
								}
							}
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

		ScaleButton start = new ScaleButton(0, 0, res.mCountDownRegion, vbom,
				1.0f) {
			@Override
			public void onClick() {
				ChallengeGameScene.getIntance().unregisterTouchArea(this);
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

	protected void handlerSquare(Square square) {
		switch (square.getType()) {
		case Cell.NONE:
			C.nextint++;
			square.setAlpha(.5f);
			square.setEnable(false);
			break;
		case Cell.DOUBLE:
			count_double++;
			table.detachChild(square);
			this.unregisterTouchArea(square);
			square = new Square(square.getX(), square.getY(), res.mSquare,
					vbom, res.mFont, 1.2f, "" + square.getValue(), Cell.NONE) {
				@Override
				public void onClick() {
					if (GameScene.modePlayer == GameScene.SINGLE) {
						if (this.value == C.nextint) {
							if (C.nextint == LIMIT_SIZE) {
								if (this.getType() == Cell.NONE
										|| this.getType() == Cell.BONUS
										|| this.getType() == Cell.BLINK){
								started = false;
								this.setAlpha(.5f);
								this.setEnable(false);
								win = true;
								finish();
								}
							} else {
								handlerSquare(this);
							}
						}
					}
				}
			};
			square.getText().setVisible(true);
			table.attachChild(square);
			this.registerTouchArea(square);
			break;
		case Cell.TRIPLE:
			count_triple++;
			table.detachChild(square);
			this.unregisterTouchArea(square);
			square = new Square(square.getX(), square.getY(),
					res.mSquareDoubleRegion, vbom, res.mFont, 1.2f, ""
							+ square.getValue(), Cell.DOUBLE) {
				@Override
				public void onClick() {
					if (GameScene.modePlayer == GameScene.SINGLE) {
						if (this.value == C.nextint) {
							if (C.nextint == LIMIT_SIZE) {
								if (this.getType() == Cell.NONE
										|| this.getType() == Cell.BONUS
										|| this.getType() == Cell.BLINK){
								started = false;
								this.setAlpha(.5f);
								this.setEnable(false);
								win = true;
								finish();
								}
							} else {
								handlerSquare(this);
							}
						}
					}
				}
			};
			square.getText().setVisible(true);
			table.attachChild(square);
			this.registerTouchArea(square);
			break;
		case Cell.BONUS:			
			count_bonus++;
			C.nextint++;
			square.setEnable(false);
			for (int i = 0; i < LIMIT_SIZE; i++) {
				final Square s = (Square) table.getChildByIndex(i);
				if (s.getValue() == C.nextint) {
					s.registerEntityModifier(new ScaleModifier(0.05f, 1, 1.2f) {
						@Override
						protected void onModifierFinished(final IEntity pItem) {
							super.onModifierFinished(pItem);
							s.registerEntityModifier(new ScaleModifier(0.05f,
									1.2f, 1f) {
								@Override
								protected void onModifierFinished(
										final IEntity pItem) {
									super.onModifierFinished(pItem);
									s.setAlpha(.5f);
								}
							});
						}
					});
					square.setAlpha(.5f);
					handlerSquare(s);
					break;
				}
			}
			break;
		case Cell.BLINK:
			count_blink++;
			C.nextint++;
			square.setAlpha(.5f);
			square.setEnable(false);
			break;
		}
	}

	protected void finish() {
		for (int i = 0; i < table.getChildCount(); i++) {
			this.unregisterTouchArea((Square) table.getChildByIndex(i));
		}
		
		if (count_blink >= 1){
			AchievementManager.num_blink++;
		}
		if (count_bonus >=1){
			AchievementManager.num_bonus++;
		}
		if (count_double >=1){
			AchievementManager.num_double++;
		}
		if (count_triple >=1){
			AchievementManager.num_triple++;
		}
		
		AchievementManager.Update();
		Sprite s;
		if (win){
			s = new Sprite(0,0,res.mWinRegion,vbom);
			s.setPosition(MainActivity.W*.5f-s.getWidth()*.5f,MainActivity.H*.5f-s.getHeight()*.5f);
			this.attachChild(s);
			ResourceManager.getInstance().playSound(4);// sound win
		} else {
			s = new Sprite(0,0,res.mWinRegion,vbom);
			s.setPosition(MainActivity.W*.5f-s.getWidth()*.5f,MainActivity.H*.5f-s.getHeight()*.5f);
			this.attachChild(s);
			ResourceManager.getInstance().playSound(5);// sound lose
		}				
		s.registerEntityModifier(new DelayModifier(4000));
	}

	protected static ChallengeGameScene getIntance() {
		return instance;
	}

	private void setStartTouch() {
		Log.i("Game", "" + table.getChildCount());
		for (int i = 0; i < table.getChildCount(); i++) {
			this.registerTouchArea((Square) table.getChildByIndex(i));
			((Square) table.getChildByIndex(i)).getText().setVisible(true);
		}
		started = true;
	}

	@Override
	public void addToHeadLayer() {
		Entity time = new Entity(MainActivity.W / 2, 0);
		timeText = new Text(0, 0, res.mFont, "00.00", vbom) {
			long current;
			boolean first = true;
			long t = LIMIT_TIME;

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (started) {
					if (first) {
						first = false;
						current = System.currentTimeMillis();
					}
					if (t <= 0) {
						started = false;
						win = false;
						finish();
					} else {
						final long second = System.currentTimeMillis();
						if (second - current > 10) {
							long i = (second - current) % 10;
							current += 10 * i;
							t -= i;
							if (t < 0)
								t = 0;
							this.setText(String.format("%02d.%02d", (t / 100),
									t % 100));
						}
					}
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};

		timeText.setPosition(time.getX() - timeText.getWidth() - 15,
				time.getY() + 45);
		time.attachChild(timeText);
		this.headLayer.attachChild(time);

		next = new Text(0, 0, res.mFont, "Next:  0", vbom) {
			protected int current = 0;

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (started) {
					if (current != C.nextint) {
						current = C.nextint;
						this.setText(String.format("Next: %2d", current));
					}
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		next.setPosition(15, 45);
		this.headLayer.attachChild(next);
	}
}
