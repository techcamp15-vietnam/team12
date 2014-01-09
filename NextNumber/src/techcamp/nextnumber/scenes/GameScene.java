/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.Sprite;

import techcamp.nextnumber.MainActivity;

public abstract class GameScene extends AbstractScene {

	public static final int SINGLE = 0;
	public static final int MULTI = 1;
	public static final int CLASSIC = 2;
	public static final int CHALLENGE = 3;
	public static final int LIMIT_SIZE = 25;
	public static final int LIMIT_COL = 5;
	public static final int LIMIT_ROW = 5;
	public static int modePlayer; // mode gameplay CLASSIC/CHALLENGE
	public static int modeGameplay; // mode player SINGLE/CHALLENGE
	protected Entity headLayer;
	protected Entity mainLayer;	
	protected Entity resultLayer;	

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
		
		headLayer = new Entity(0,MainActivity.H){
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
		mainLayer = new Entity(0, MainActivity.H){
			public boolean first = false;
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (!this.first) {					
					this.first = true;
					this.registerEntityModifier(new MoveModifier(0.45f, 0f, 0f,
							MainActivity.H, 0.25f*MainActivity.H));
				}
			}
		};
		this.currentLayer = mainLayer;
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

	@Override
	public void onHiddenLayer(){		
		if (currentLayer != mainLayer){
			goToHomeScreen();
		}
	}

	private void goToHomeScreen() {
		resultLayer.registerEntityModifier(new MoveModifier(1.0f, resultLayer.getX(), 0, resultLayer.getY(), MainActivity.H));
	}
}
