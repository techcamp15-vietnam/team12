/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01/2013
 */
package techcamp.nextnumber.scenes;

import java.util.Vector;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.manager.ResourceManager;
import techcamp.nextnumber.manager.StorageManager;
import android.util.Log;

public class HighscoreScene extends AbstractScene{

	private Text title;
	private Entity mainLayer;
//	private float mTouchX = 0, mTouchY = 0, mTouchOffsetX = 0,
//			mTouchOffsetY = 0;	

	@Override
	public void loadResources() {
		res.loadMenuBackground();
		res.loadHighscoreResources();
		res.loadFonts();
	}

	@Override
	public void create() {
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0,
				res.mMenuBackground, ResourceManager.getInstance().vbom)));
		setBackground(background);
		setBackgroundEnabled(true);
		title = new Text(0, 0, res.mHeaderFont, "Highscore", vbom);
		title.setPosition(MainActivity.W / 2 - title.getWidth() / 2,
				MainActivity.H * .1f);
		attachChild(title);
		attachChild(new Line(MainActivity.W * 0.1f, MainActivity.W * .3f,
				MainActivity.W * 0.9f, MainActivity.W * .3f, vbom));
		mainLayer = new Entity(0, MainActivity.H * .25f);
		Vector v = StorageManager.returnHighScore(0);
		for (int i = 0; i < v.size(); i++) {
			Vector tmp = (Vector) v.get(i);
			String name = (String) tmp.elementAt(0);
			long _l = (Long) tmp.elementAt(1);
			Log.i("High", ">>" + _l);
			Text tName = new Text(0, 0, res.mSmallFont, "" + name, vbom);
			Text tScore = new Text(0, 0, res.mSmallFont, String.format("%.2f",
					_l / 1000f), vbom);
			Log.i("High", "" + name);
			tName.setPosition(MainActivity.W * 0.1f,
					(i + 1) * tName.getHeight() + i * 40);
			tScore.setPosition(MainActivity.W * 0.65f,
					(i + 1) * tScore.getHeight() + i * 40);
			mainLayer.attachChild(tName);
			mainLayer.attachChild(tScore);
		}
		attachChild(mainLayer);		
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

//	@Override
//	public boolean onSceneTouchEvent(Scene scene, TouchEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			mTouchX = event.getMotionEvent().getX();
//			mTouchY = event.getMotionEvent().getY();
//		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			float newY = event.getMotionEvent().getY();
//			mTouchOffsetY = (newY - mTouchY);
//			float newScrollY = camera.getCenterY() - mTouchOffsetY;
//			if ((mainLayer.getY() <= MainActivity.H * .25f && mTouchOffsetY < 0)||(mainLayer.getY() >= MainActivity.H * .25f && mTouchOffsetY < 0)) {
//			} else {
//				camera.setCenter(MainActivity.W / 2, newScrollY);
//				mTouchY = newY;
//			}
//		}
//		return true;
//	}

}
