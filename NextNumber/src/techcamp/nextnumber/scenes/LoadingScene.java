/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01/2013
 */
package techcamp.nextnumber.scenes;

import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.manager.ResourceManager;
import android.util.Log;

public class LoadingScene extends AbstractScene {

	private Text loadText;

	@Override
	public void loadResources() {
		ResourceManager.getInstance().loadLoadingBackground();
		ResourceManager.getInstance().loadFonts();
		loadText = new Text(MainActivity.W * 0.3f, MainActivity.H * 0.5f,
				ResourceManager.getInstance().mHeaderFont, "Loading...", vbom);
	}

	@Override
	public void create() {
		Log.i("Loading","create");
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0,
				res.mBackgroundRegion, ResourceManager.getInstance().vbom)));
		setBackground(background);
		setBackgroundEnabled(true);
		attachChild(loadText);		
	}

	@Override
	public void unloadResources() {
		ResourceManager.getInstance().unloadBackground();
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
	public void onHiddenLayer() {
		// TODO Auto-generated method stub
		
	}

}
