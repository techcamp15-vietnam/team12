/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01/2013
 */
package techcamp.nextnumber.scenes;

import java.util.Vector;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.manager.AchievementManager;
import techcamp.nextnumber.manager.ResourceManager;
import techcamp.nextnumber.manager.StorageManager;
import techcamp.nextnumber.utils.ScaleButton;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AchievementScene extends AbstractScene {

	private Text title;
	private Entity homeLayer;
	private BuildableBitmapTextureAtlas achievTextureAtlas;
	private ITextureRegion achievRegion;

	@Override
	public void loadResources() {
		res.loadMenuBackground();
		res.loadAchievementResources();
		res.loadFonts();

	}

	@Override
	public void create() {
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0,
				res.mMenuBackground, ResourceManager.getInstance().vbom)));
		setBackground(background);
		setBackgroundEnabled(true);
		
		// update achievement
		AchievementManager.UpdateStatus();

		title = new Text(0, 0, res.mHeaderFont, "Achievement", vbom);
		title.setPosition(MainActivity.W / 2 - title.getWidth() / 2,
				MainActivity.H * .1f);
		this.attachChild(title);

		homeLayer = new Entity(0, MainActivity.H * .25f);
		Vector re = StorageManager.returnAchivement();
		long distance = 45;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				final int index = i * 3 + j;
				boolean stt = (Boolean) re.elementAt(index);
				if (stt) {
					ScaleButton sb = new ScaleButton(0, 0, res.mBadgeARegion,
							vbom, 1.2f) {

						@Override
						public void onClick() {
							openDialog(AchievementManager.AchieveName[index],
									AchievementManager.AchieveDes[index]);
						}
					};
					sb.setPosition(j * sb.getWidth() + distance * (j + 1), i
							* sb.getWidth()+ 60);
					homeLayer.attachChild(sb);
					this.registerTouchArea(sb);
				} else {
					ScaleButton sb = new ScaleButton(0, 0, res.mBadgeBRegion,
							vbom, 1.2f) {

						@Override
						public void onClick() {
							openDialog(AchievementManager.AchieveName[index],
									AchievementManager.AchieveDes[index]);
						}
					};
					sb.setPosition(j * sb.getWidth() + distance * (j + 1), i
							* sb.getWidth()+60);
					homeLayer.attachChild(sb);
					this.registerTouchArea(sb);
				}
			}
		}
		this.attachChild(homeLayer);
	}

	protected void openDialog(final String name, final String des) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle(name);									
				builder.setMessage(des).setPositiveButton("Ok, I got it", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int id) {						
					}
				}).setCancelable(false);
				final AlertDialog alert = builder.create();
				alert.show();
			}
		});
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
