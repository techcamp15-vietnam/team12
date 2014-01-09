/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.text.Text;

import android.util.Log;

import techcamp.nextnumber.MainActivity;
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
	private CellArray C;

	@Override
	public void loadResources() {
		GameScene.modeGameplay = GameScene.CHALLENGE;
		super.loadResources();
		res.mHeaderFont.prepareLetters("Next :1234567890.".toCharArray());
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
		C = new CellArray();
		C.CreateData(1);
		float boundSpace = (MainActivity.W - LIMIT_COL * res.mSquare.getWidth())
				/ LIMIT_COL; // calculate suitable space between squares
		Log.i("Game", "" + boundSpace + "," + res.mSquare.getWidth());		
		for (int i = 0; i < LIMIT_ROW; i++) {
			for (int j = 0; j < LIMIT_COL; j++) {
				Square s = new Square(0, 0, res.mSquareDoubleRegion, vbom, res.mHeaderFont,
						1.2f, "" + (C.CA[i * 5 + j].getValue()),Square.DOUBLE) {
					@Override
					public void onClick() {
						if (GameScene.modePlayer == GameScene.SINGLE) {							
							if (this.value == C.nextint) {
								if (C.nextint == LIMIT_SIZE) {
									started = false;
									this.setAlpha(.5f);
									this.setEnable(false);
									finish();
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
		switch (square.getType()){
		case Square.NONE:
			C.nextint++;
			square.setAlpha(.5f);
			square.setEnable(false);
			break;
		case Square.DOUBLE:			
			table.detachChild(square);			
			square = new Square(square.getX(), square.getY(), res.mSquare, vbom,res.mHeaderFont,1.2f,""+square.getValue(),Square.NONE) {				
				@Override
				public void onClick() {
					if (GameScene.modePlayer == GameScene.SINGLE) {							
						if (this.value == C.nextint) {
							if (C.nextint == LIMIT_SIZE) {
								started = false;
								this.setAlpha(.5f);
								this.setEnable(false);
								finish();
							} else {
								handlerSquare(this);			
							}
						}
					}
				}
			};
			table.attachChild(square);
			this.registerTouchArea(square);
			break;
		case Square.TRIPLE:
			square.setDoubleType();
			break;
		case Square.BONUS:
		}
	}

	protected void finish() {
		// TODO Auto-generated method stub

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
		timeText = new Text(0, 0, res.mHeaderFont, "00.00", vbom) {
			long current = System.currentTimeMillis();
			long t = LIMIT_TIME;

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (started) {
					if (t <= 0) {
						started = false;
					} else {
						final long second = System.currentTimeMillis();
						if (second - current > 10) {
							long i = (second - current) % 10;
							current += 10 * i;
							t -= i;
							this.setText(String.format("%02d.%02d", (t / 100),
									t % 100));
						}
					}
				}
				super.onManagedUpdate(pSecondsElapsed);
			}

			public long getRestTime() {
				return (0 > t) ? 0 : t;
			}
		};
		timeText.setPosition(time.getX() - timeText.getWidth() - 15,
				time.getY() + 40);
		time.attachChild(timeText);
		this.headLayer.attachChild(time);

		next = new Text(0, 0, res.mHeaderFont, "Next:  0", vbom) {
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
		next.setPosition(15, headLayer.getY() + 40);
		this.headLayer.attachChild(next);
	}
}
