/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01/2013
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.sprite.Sprite;

import techcamp.nextnumber.MainActivity;
import android.util.Log;

public class SplashScene extends AbstractScene {

	Sprite splash;

	@Override
	public void loadResources() {
		res.loadSplashResources();
	}

	@Override
	public void create() {
		splash = new Sprite(0, 0, res.splashRegion, vbom);
		attachChild(splash);
		splash.registerEntityModifier(new DelayModifier(
				MainActivity.SPLASH_DURATION / 2000f) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				Log.d("Splash", "Changing splash!");
				super.onModifierFinished(pItem);
				splash.setVisible(false);
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