/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.utils;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import techcamp.nextnumber.manager.ResourceManager;

public abstract class ScaleButton extends Sprite {

	private boolean TOUCH_STARTED = false;
	private boolean TOUCHED = false;
	private boolean CLICKED = false;
	private boolean SCALED = false;
	private boolean TOUCH_ENABLE = true;
	private float scale = 1.0f;
	private Text text = null;
	private int soundType = 1;
	private static boolean sizeFollowText = false;

	public ScaleButton(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, float scale) {
		super(pX, pY, pTextureRegion, vbom);
		this.scale = scale;
	}

	public ScaleButton(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, float scale, Text text) {
		this(pX, pY, pTextureRegion, vbom, scale);
		this.text = text;
		this.attachChild(text);
	}

	public ScaleButton(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, Font f, float scale, String s) {
		this(pX, pY, pTextureRegion, vbom, scale);
		Text text = new Text(0, 0, f, s, vbom);
		this.text = text;
		text.setPosition(this.getWidth() / 2 - text.getWidth() / 2,
				this.getHeight() / 2 - text.getHeight() / 2);
		this.attachChild(text);
	}

	public Text getText() {
		return this.text;
	}
	
	/**
	 * #control allow change size depend on text or not
	 * @param followText
	 */
	public static void setSizeFollowText(boolean followText){
		sizeFollowText = followText;
	}

	public void setEnable(boolean touchEnable) {
		TOUCH_ENABLE = touchEnable;
	}

	public void setSound(int soundType) {
		this.soundType = soundType;
	}

	public abstract void onClick();
	
	/*	 
	 * @see org.andengine.entity.shape.RectangularShape#getWidth()
	 * #modify for suitable with text attached
	 */
	@Override
	public float getWidth(){
		if (!sizeFollowText)
			return super.getWidth();
		if (text != null)
			return text.getWidth();
		else
			return super.getWidth();
	}
	
	/**
	 * #modify for suitable with text attached 
	 */
	@Override
	public float getHeight(){
		if (!sizeFollowText)
			return super.getHeight();
		if (text != null)
			return text.getHeight();
		else
			return super.getHeight();
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (TOUCH_ENABLE) {
			if (!SCALED && TOUCHED) {
				this.registerEntityModifier(new ScaleModifier(0.05f, 1f, scale) {
					@Override
					protected void onModifierFinished(final IEntity pItem) {
						super.onModifierFinished(pItem);
						SCALED = true;
					}
				});
			} else if (SCALED && !TOUCHED) {
				this.registerEntityModifier(new ScaleModifier(0.05f, scale, 1f) {
					@Override
					protected void onModifierFinished(final IEntity pItem) {
						super.onModifierFinished(pItem);
						SCALED = false;
						if (CLICKED) {
							CLICKED = false;
							onClick();
						}
					}
				});
				SCALED = false;
			}
		}
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (TOUCH_ENABLE) {
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
				if (pTouchAreaLocalX > this.getWidth() || pTouchAreaLocalX < 0f
						|| pTouchAreaLocalY > this.getHeight()
						|| pTouchAreaLocalY < 0f) {
					TOUCH_STARTED = false;
				} else {
					TOUCH_STARTED = true;
					TOUCHED = true;
				}				
			} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
				if (pTouchAreaLocalX > this.getWidth() || pTouchAreaLocalX < 0f
						|| pTouchAreaLocalY > this.getHeight()
						|| pTouchAreaLocalY < 0f) {
					if (TOUCHED) {
						TOUCHED = false;
					}
				} else {
					if (TOUCH_STARTED && !TOUCHED)
						TOUCHED = true;
				}
			} else {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP
						&& TOUCHED && TOUCH_STARTED) {
					TOUCHED = false;
					CLICKED = true;
					TOUCH_STARTED = false;
						ResourceManager.getInstance().playSound(soundType);
				}
			}
		}
		return true;
	}
}