/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.utils;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import techcamp.nextnumber.scenes.GameScene;

import android.util.Log;

public abstract class Square extends ScaleButton {

	// types of square
	private int type;
	protected int value; // number in square

	private Square(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, float scale) {
		super(pX, pY, pTextureRegion, vbom, scale);
	}

	public Square(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, Font f, float scale, String s,
			int type) {
		super(pX, pY, pTextureRegion, vbom, f, scale, s);
		this.type = type;
		this.getText().setVisible(false);
		value = Integer.parseInt(s);
	}

	public Square(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, Font f, float scale, String s) {
		super(pX, pY, pTextureRegion, vbom, f, scale, s);
		this.type = Cell.NONE;
		this.getText().setVisible(false);
		value = Integer.parseInt(s);		
	}

	public int getType() {
		return type;
	}
	
	public int getValue() {
		return value;
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
						TOUCHED = false;
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
				Log.i("Touch", "Down");
				if (pTouchAreaLocalX > this.getWidth() || pTouchAreaLocalX < 0f
						|| pTouchAreaLocalY > this.getHeight()
						|| pTouchAreaLocalY < 0f) {
					TOUCH_STARTED = false;
				} else {
					TOUCH_STARTED = true;
					TOUCHED = true;
					CLICKED = true;
				}
			}
		}
		return true;
	}
}
