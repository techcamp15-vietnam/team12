/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.scenes;

import java.util.Vector;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.R;
import techcamp.nextnumber.manager.AchievementManager;
import techcamp.nextnumber.manager.ResourceManager;
import techcamp.nextnumber.manager.StorageManager;
import techcamp.nextnumber.utils.CellArray;
import techcamp.nextnumber.utils.ScaleButton;
import techcamp.nextnumber.utils.Square;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ClassicGameScene extends GameScene {

	private boolean started = false;
	private Entity table;
	private Text next;
	private Text timeText;
	private CellArray C = MainActivity.cellBoardClassicalMode;	
	private static ClassicGameScene instance;
	private boolean win = true;

	@Override
	public void loadResources() {
		GameScene.modeGameplay = GameScene.CLASSIC;
		super.loadResources();
		res.mFont.prepareLetters("Next :1234567890.".toCharArray());
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
		C = new CellArray();
		C.CreateData(1);
		float boundSpace = (MainActivity.W - LIMIT_COL * res.mSquare.getWidth())
				/ LIMIT_COL; // calculate suitable space between squares
		Log.i("Game", "" + boundSpace + "," + res.mSquare.getWidth());
		for (int i = 0; i < LIMIT_ROW; i++) {
			for (int j = 0; j < LIMIT_COL; j++) {
				Square s = new Square(0, 0, res.mSquare, vbom, res.mWFont,
						1.2f, "" + (C.CA[i * 5 + j].getValue())) {
					@Override
					public void onClick() {
						if (this.value == C.nextint) {
							if (C.nextint == LIMIT_SIZE) {
								started = false;
								this.setAlpha(.5f);
								this.setEnable(false);
								win = true;
								finish();
							} else {
								C.nextint++;

								this.setAlpha(.5f);
								this.setEnable(false);
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

		ScaleButton start = new ScaleButton(0, 0, res.mStartRegion, vbom, 1.0f) {
			@Override
			public void onClick() {
				ClassicGameScene.getIntance().unregisterTouchArea(this);
				this.registerEntityModifier(new MoveModifier(.5f, this.getX(),
						MainActivity.W * 1.5f, this.getY(), this.getY()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						setStartTouch();
					};
				});
			}
		};
		start.setPosition(MainActivity.W*.5f - start.getWidth()*.5f,MainActivity.H*.375f - start.getHeight()*.5f);
		this.registerTouchArea(start);
		this.mainLayer.attachChild(start);

	}

	protected void finish() {
		for (int i = 0; i < table.getChildCount(); i++) {
			this.unregisterTouchArea((Square) table.getChildByIndex(i));
		}
		
		AchievementManager.Update();
		Sprite s;
		if (win){
			AchievementManager.game_play++;
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
		
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			    // Get the layout inflater
			    LayoutInflater inflater = activity.getLayoutInflater();
			    final View v = inflater.inflate(R.layout.result, null);
			    builder.setView(v);			  			    
			    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			               @Override
			               public void onClick(final DialogInterface dialog, int id) {//			            	
			           		String s = timeText.getText().toString();
			           		EditText text = (EditText) v.findViewById(R.id.editText);
			           		Log.i("Game", "" + text);
			           		long timeT = Integer.parseInt(s.substring(0, 1)) * 60000
			           				+ Integer.parseInt(s.substring(2, 4)) * 1000
			           				+ Integer.parseInt(s.substring(5, 7)) * 10;
			           		Log.i("Game", "" + timeT+"."+text);
			           		Vector n = new Vector();			           		
			           		n.add(text.getText().toString());
			           		n.add(timeT);
			           		StorageManager.AddHighScore(0, n);
			               }
			           })
			           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {			                   
			               }
			           }).setCancelable(false);
			    AlertDialog alert = builder.create();			    			    
			    alert.show();
			}
		});
				
	}

	protected static ClassicGameScene getIntance() {
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
		timeText = new Text(0, 0, res.mFont, "0:00.00", vbom) {
			long current;
			boolean first = true;
			public long t = 0; // time in miliseconds/10

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (started) {
					if (first) {
						Log.i("Game", "start clock");
						current = System.currentTimeMillis();
						first = false;
					}
					final long second = System.currentTimeMillis();
					if (second - current > 10) {
						long i = (second - current) % 10;
						current += 10 * i;
						t += i;
						if (t / 6000 < 10) {
							this.setText(String.format("%d:%02d.%02d",
									t / 6000, (t / 100) % 60, t % 100));
						} else {
							started = false;
							win = false;
						}
					}
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		timeText.setPosition(time.getX() - timeText.getWidth() - 15,
				time.getY() + 40);
		time.attachChild(timeText);

		next = new Text(0, 0, res.mFont, "Next: 0", vbom) {
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
		next.setPosition(15, 40);
		this.headLayer.attachChild(time);
		this.headLayer.attachChild(next);
		Log.i("Game", "Attach next");

	}
}
