/**
 * @author pvhau
 * @team TechCamp G12
 * @date 07/01
 */

package techcamp.nextnumber;

import java.io.IOException;
import java.util.Arrays;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import techcamp.nextnumber.connect.BluetoothConnectionService;
import techcamp.nextnumber.connect.DeviceListActivity;
import techcamp.nextnumber.manager.ResourceManager;
import techcamp.nextnumber.manager.SceneManager;
import techcamp.nextnumber.manager.StorageManager;
import techcamp.nextnumber.scenes.AbstractScene;
import techcamp.nextnumber.scenes.ClassicGameScene;
import techcamp.nextnumber.scenes.LoadingScene;
import techcamp.nextnumber.scenes.MenuScene;
import techcamp.nextnumber.scenes.MultiClassicGameScene;
import techcamp.nextnumber.scenes.MultiGameMenu;
import techcamp.nextnumber.scenes.SplashScene;
import techcamp.nextnumber.utils.CellArray;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends BaseGameActivity {
	/** Screen width, standard 700p */
	public static final int W = 600;
	/**
	 * Screen height, non-standard - will make it look ok on most 16:9 screens
	 * with or without soft buttons
	 */
	public static final int H = 1024;
	/** FPS limit of the engine */
	public static final int FPS_LIMIT = 60;

	public static final long SPLASH_DURATION = 4000;

	private Camera camera;
	private Scene mScene;

	/*
	 * ================================================================== Nguyen
	 * Phi Hiep Bluetooth connection component
	 * ==================================================================
	 */
	// Message types sent from the BluetoothConnectionService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MAX_NUMBER = 10;

	// Key names received from the BluetoothConnectionService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_ENABLE_BT = 3;

	// Name of the connected device
	private String mConnectedDeviceName = null;

	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Bluetooth connection Service object
	private BluetoothConnectionService mBluetoothService = null;

	public static CellArray cellBoardClassicalMode = new CellArray();
	boolean checkInitConnect = false;
	boolean checkHost = false;
	public static boolean checkMultiMode = false;

	/*
	 * =====================================================================
	 * Connection funtion
	 * =====================================================================
	 */

	// this funtion check if BT availble on device if not return false
	// else return true and setup connection session
	//
	public boolean checkBluetoothAvailable() {
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return false;
		}
		// enable Bluetooth
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			// setup connection service
			mBluetoothService = new BluetoothConnectionService(this, mHandler);
		}
		return true;
	}

	// This function start the Bluetooth connection services
	public boolean startBluetoothService() {
		if (mBluetoothService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mBluetoothService.getState() == BluetoothConnectionService.STATE_NONE) {
				// Start the Bluetooth connection services
				mBluetoothService.start();
				return true;
			}
		}
		return true;
	}

	// Hander to processing bluetooth messages
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int index; // index in array of the number transferred between two
						// devices
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothConnectionService.STATE_CONNECTED:
					if (checkHost && checkInitConnect) {
						checkInitConnect = false;
						if (!checkMultiMode) {
							String tmp = initMultiClassModeHost();
							mSendMessage(tmp);
						} else {
							String tmp = initMultiClgModeHost();
							mSendMessage(tmp);
						}
					}
					Toast.makeText(getApplicationContext(),
							msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
							.show();
					break;
				case BluetoothConnectionService.STATE_CONNECTING:
					// setStatus(R.string.title_connecting);
					break;
				case BluetoothConnectionService.STATE_LISTEN:
				case BluetoothConnectionService.STATE_NONE:
					// setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
			    if (writeMessage.compareTo("...") == 0) {
					MultiGameMenu.instance.showGame();
				}
				Log.i("sendMess", "" + writeMessage);

				// index available, update common data and this user's data;

				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				if (readMessage.compareTo("...") == 0) {
					MultiGameMenu.instance.showGame();
					Log.i("readMess", "" + readMessage);
				} else
				if (readMessage.length() < 5) {
					MultiClassicGameScene.instance
							.scanGameBoard(Integer.parseInt(readMessage));
					cellBoardClassicalMode.nextint++;
				} else if (readMessage.length() > 10) {
					if (!checkMultiMode) {
						initMultiClassModeClient(readMessage);
					} else
						initMultiClgModeClient(readMessage);
					mSendMessage("...");

				} 

				
					break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				mBluetoothService.stop();
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				checkHost = true;
				checkInitConnect = true;
				connectDevice(data, true);
			} else if (resultCode == Activity.RESULT_FIRST_USER) {

			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				mBluetoothService = new BluetoothConnectionService(this,
						mHandler);
			} else {
				// User did not enable Bluetooth or an error occurred
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mBluetoothService.connect(device, secure);

	}

	public boolean connectToDevices() {
		Intent serverIntent = null;
		// Launch the DeviceListActivity to see devices and do scan
		serverIntent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
		return true;
	}

	public void ensureDiscoverable() {
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/* Syn data */
	public String initMultiClassModeHost() {
		cellBoardClassicalMode.CreateData(1);
		int[] initData = new int[25];
		for (int i = 0; i < 25; i++) {
			initData[i] = cellBoardClassicalMode.CA[i].getValue();
		}

		String stringBuff = Arrays.toString(initData);
		String subStringBuff = stringBuff.substring(1, stringBuff.length() - 1);
		String[] data = subStringBuff.split(", ");
		int[] ints = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			try {
				ints[i] = Integer.parseInt(data[i]);
			} catch (NumberFormatException nfe) {
				// Not an integer, do some
			}
		}
		Log.i("datainit", "" + Arrays.toString(ints));
		Log.i("datainit", "" + stringBuff);
		Log.i("datainit", "" + subStringBuff);

		return subStringBuff;
	}

	// This function convert info from a cel array data to string to send via
	// bluetooth
	public void initMultiClassModeClient(String msg) {
		String[] data = msg.split(", ");
		int[] ints = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			try {
				ints[i] = Integer.parseInt(data[i]);
			} catch (NumberFormatException nfe) {
				// Not an integer, do some
			}
		}
		for (int i = 0; i < 25; i++) {
			cellBoardClassicalMode.CA[i].setValue(ints[i]);
		}
	}

	/* Syn data */
	public String initMultiClgModeHost() {
		cellBoardClassicalMode.CreateData(2);
		int[] initData = new int[50];
		for (int i = 0; i < 25; i++) {
			initData[i] = cellBoardClassicalMode.CA[i].getValue();
		}
		for (int i = 25; i < 50; i++) {
			initData[i] = cellBoardClassicalMode.CA[i - 25].geteffect();
		}

		String stringBuff = Arrays.toString(initData);
		String subStringBuff = stringBuff.substring(1, stringBuff.length() - 1);
		String[] data = subStringBuff.split(", ");
		int[] ints = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			try {
				ints[i] = Integer.parseInt(data[i]);
			} catch (NumberFormatException nfe) {
				// Not an integer, do some
			}
		}
		Log.i("e", "" + Arrays.toString(ints));
		Log.i("datainit", "" + stringBuff);
		Log.i("datainit", "" + subStringBuff);

		return subStringBuff;
	}

	// This function convert info from a cel array data to string to send via
	// bluetooth
	public void initMultiClgModeClient(String msg) {
		String[] data = msg.split(", ");
		int[] ints = new int[data.length];
		for (int i = 0; i < 50; i++) {
			try {
				ints[i] = Integer.parseInt(data[i]);
			} catch (NumberFormatException nfe) {
				// Not an integer, do some
			}
		}
		for (int i = 0; i < 25; i++) {
			cellBoardClassicalMode.CA[i].setValue(ints[i]);
			cellBoardClassicalMode.CA[i].seteffect(ints[25 + i]);
		}
	}

	/**
	 * Sends a message.
	 */
	public void mSendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mBluetoothService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// Check that there's actually something to send
		if (message.length() > 0) {
			if (message.compareTo("...") == 0) {
				// MenuScene.instance.startGameMultiMode();
			}
			// Get the message bytes and tell the BluetoothConnectionService to
			// write
			byte[] send = message.getBytes();

			mBluetoothService.write(send);
			// Reset out string buffer to zero and clear the edit text field
		}
	}

	/**
	 * ========================================================================
	 */

	@Override
	public synchronized void onResumeGame() {
		ResourceManager.getInstance().resumeMusic();
		super.onResumeGame();
	}

	@Override
	public synchronized void onPauseGame() {
		ResourceManager.getInstance().pauseMusic();
		super.onPauseGame();
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// create simple camera
		camera = new Camera(0, 0, W, H);
		// use the easiest resolution policy
		IResolutionPolicy resolutionPolicy = new RatioResolutionPolicy(W, H);
		// play only in portrait mode, we will need music, sound and no
		// multitouch, dithering just in case
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, resolutionPolicy, camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		Engine engine = new LimitedFPSEngine(pEngineOptions, FPS_LIMIT);
		return engine;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		StorageManager.setmain(this);
		ResourceManager.getInstance().init(this);
		ResourceManager.getInstance().loadMusic();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		mScene = new Scene();
		/* Enable touch area binding on the mScene object */
		mScene.setTouchAreaBindingOnActionDownEnabled(true);

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		SceneManager.getInstance().showSplash();
		ResourceManager.getInstance().resumeMusic();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (ResourceManager.getInstance().engine != null) {
				AbstractScene cur = SceneManager.getInstance()
						.getCurrentScene();
				if (cur.isLayerShown()) {
					cur.onHiddenLayer();
				} else {
					Class<? extends AbstractScene> instance = cur.getClass();
					if (instance.equals(SplashScene.class)
							|| instance.equals(LoadingScene.class)
							|| instance.equals(MenuScene.class)) {
						System.exit(0);
					} else {
						SceneManager.getInstance().showScene(MenuScene.class);
					}
				}
			}
		}
		return true;
	}
}
