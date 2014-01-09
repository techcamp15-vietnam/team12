/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.scenes;

import org.andengine.entity.sprite.Sprite;


public class MultiGameMenu extends AbstractScene{

	@Override
	public void loadResources() {	
		res.loadMenuBackground();
		res.loadFonts();
		res.loadMusic();
		res.loadSounds();
	}

	@Override
	public void create() {
		attachChild(new Sprite(0,0,res.mMenuBackground,vbom));		
		
	}

	@Override
	public void unloadResources() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHiddenLayer() {
		// TODO Auto-generated method stub
		
	}
	
}