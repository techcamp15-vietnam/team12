/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.utils.Square;

public class ClassicGameScene extends GameScene {

	private Text timeText;
	private boolean started = false;

	@Override
	public void loadResources() {
		this.modeGameplay = 0;
		super.loadResources();
		res.mHeaderFont.prepareLetters("Time: 1234567890.".toCharArray());
	}

	@Override
	public void create() {
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
		Entity table = new Entity(0, 0);
		int boundSpace = 2;
		for (int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				Text t = new Text(0,0,res.mHeaderFont,""+i*5+j+1,vbom);
				Square s = new Square(0,0,res.mSquare,vbom,1.1f,t,0);
				table.attachChild(s);
				s.setPosition(j*t.getWidth()+boundSpace*(j+1),i*t.getHeight()+boundSpace*(i+1));
			}
		}
		this.mainLayer.attachChild(table);
		
	}

	@Override
	public void addToHeadLayer() {
		Entity time = new Entity(MainActivity.W / 2, 0);
		timeText = new Text(0, 0, res.mHeaderFont, "00.00", vbom) {
			long current = System.currentTimeMillis();
			long t = 0;

			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (started) {
					final long second = System.currentTimeMillis();
					if (second - current > 10) {
						long i = (second - current) % 10;
						current += 10 * i;
						t += i;
						this.setText(String.format("%02d.%02d", t / 100,
								t % 100));
					}
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		timeText.setPosition(time.getX() - timeText.getWidth(),
				time.getY() + 40);
		time.attachChild(new Sprite(15, 15, res.mClockRegion, vbom));
		time.attachChild(timeText);
		this.headLayer.attachChild(time);
	}
}
