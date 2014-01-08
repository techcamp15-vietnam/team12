/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber.utils;

import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Square extends ScaleButton {
	private int type;
	
	public Square(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom, float scale, Text text, int type) {
		super(pX, pY, pTextureRegion, vbom, scale,text);
		this.type = type;
	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub

	}

}
