/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.utils;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class Square extends ScaleButton{

	private int type;
	protected int value; // number in square	
	
	private Square(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, float scale) {
		super(pX, pY, pTextureRegion, vbom, scale);
	}
	
	public Square(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, Font f, float scale, String s, int type) {
		super(pX, pY, pTextureRegion, vbom, f, scale, s);
		this.type = type;
		this.getText().setVisible(false);
		value = Integer.parseInt(s);
	}
	
	public Square(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, Font f, float scale, String s) {
		super(pX, pY, pTextureRegion, vbom, f, scale, s);
		this.type = 0;
		this.getText().setVisible(false);
		value = Integer.parseInt(s);
	}
}