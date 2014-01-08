/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.Sprite;

import android.util.Log;

import techcamp.nextnumber.MainActivity;

public abstract class GameScene extends AbstractScene {

	private static final int CLASSIC = 0;
	private static final int CHALLENGE = 1;
	public static final int LIMIT_SIZE = 25;
	protected static int modePlayer;
	protected static int modeGameplay;
	protected Entity headLayer;
	protected Entity mainLayer;	

	@Override
	public void loadResources() {
		res.loadMenuBackground();
		if (modeGameplay == CLASSIC) {
			res.loadClassicGameResources();
		} else if (modeGameplay == CHALLENGE) {
			res.loadChallengeGameResources();
		}
		res.loadFonts();		
	}

	@Override
	public void create() {
		attachChild(new Sprite(0, 0, res.mMenuBackground, vbom));
		res.resumeMusic();
		
		headLayer = new Entity(0,0){
			public boolean first = false;
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (!this.first) {
					Log.i("Game", "onManageUpdate");
					this.first = true;
					this.registerEntityModifier(new MoveModifier(0.5f, 0f, 0f,
							MainActivity.H, 0f));
				}
			}
		};		
		mainLayer = new Entity(0, 0.25f*MainActivity.H){
			public boolean first = false;
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (!this.first) {
					Log.i("Game", "onManageUpdate");
					this.first = true;
					this.registerEntityModifier(new MoveModifier(0.45f, 0f, 0f,
							MainActivity.H, 0.25f*MainActivity.H));
				}
			}
		};
					
	}

	public abstract void addToMainLayer();

	public abstract void addToHeadLayer();

	@Override
	public void unloadResources() {
		if (modeGameplay == CLASSIC) {
			res.unloadClassicGame();
		} else if (modeGameplay == CHALLENGE) {
			res.unloadChallengeGame();
		}
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
