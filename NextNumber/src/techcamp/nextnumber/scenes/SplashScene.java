/**
 * @author pvhau
 * @team TechCamp Group 12 - 3H
 * @date 06/01/2013
 */

package techcamp.nextnumber.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.debug.Debug;

public class SplashScene extends AbstractScene {

	public static final long SPLASH_DURATION = 6000;

	private Sprite mSplash;

	@Override
	public void loadResources() {
		res.loadSplashResources();
	}

	@Override
	public void create() {
		mSplash = new Sprite(0, 0, res.mSplashRegion, res.vbom);
		mSplash.registerEntityModifier(new DelayModifier(
				SPLASH_DURATION / 2000f) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				Debug.d("Changing splash!");
				super.onModifierFinished(pItem);
				mSplash.setVisible(false);
			}
		});

	}

	@Override
	public void unloadResources() {
		res.unloadSplash();
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
