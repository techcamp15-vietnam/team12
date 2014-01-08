package techcamp.nextnumber.utils;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import techcamp.nextnumber.manager.ResourceManager;

public abstract class ScaleButton extends Sprite {

	private boolean TOUCH_STARTED = false;
	private boolean TOUCHED = false;
	private boolean CLICKED = false;
	private boolean SCALED = false;
	private float scale = 1.0f;

	public ScaleButton(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, float scale) {
		super(pX, pY, pTextureRegion, vbom);
		this.scale = scale;
	}
	
	public abstract void onClick();

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (!SCALED && TOUCHED) {
			this.registerEntityModifier(new ScaleModifier(
					0.05f, 1f, scale){
				@Override
				protected void onModifierFinished(final IEntity pItem) {
					super.onModifierFinished(pItem);
					SCALED = true;
				}
			});
		} else if (SCALED && !TOUCHED) {
			this.registerEntityModifier(new ScaleModifier(
					0.05f, scale, 1f) {
				@Override
				protected void onModifierFinished(final IEntity pItem) {
					super.onModifierFinished(pItem);
					SCALED = false;
					if (CLICKED) {
						onClick();
						CLICKED = false;
					}
				}

				private void onClick() {
					// TODO Auto-generated method stub
					
				}
			});
			SCALED = false;
		}				
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			if (pTouchAreaLocalX > this.getWidth() || pTouchAreaLocalX < 0f
					|| pTouchAreaLocalY > this.getHeight()
					|| pTouchAreaLocalY < 0f) {
				TOUCH_STARTED = false;
			} else {
				TOUCH_STARTED = true;
			}
			TOUCHED = true;
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
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP
				&& TOUCHED && TOUCH_STARTED) {
			TOUCHED = false;
			CLICKED = true;
			TOUCH_STARTED = false;
			ResourceManager.getInstance().playSound();
		}
		return true;
	}
}
