/**
 * @author pvhau
 * @team TechCamp Group 12 - 3H
 * @date 06/01/2013
 */

package techcamp.nextnumber.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import techcamp.nextnumber.MainActivity;
import techcamp.nextnumber.manager.ResourceManager;

public abstract class AbstractScene extends Scene{
	protected ResourceManager res;

    protected Engine engine;
    protected MainActivity activity;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;
    
    public void initialize(MainActivity activity, ResourceManager res) {
    	this.res = res;
    	this.activity = activity;
    	this.vbom = activity.getVertexBufferObjectManager();
    	this.engine = activity.getEngine();
    	this.camera = engine.getCamera();
    }
	public abstract void loadResources();
	public abstract void create();
	public abstract void unloadResources();
	public abstract void destroy();
	public void onBackKeyPressed() {
		Debug.d("Back key pressed");
	}
    public abstract void onPause();
    public abstract void onResume();
}