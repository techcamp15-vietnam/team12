/**
 * @author -  12C - Nguyen Phi Hiep
 *
 * */
package techcamp.nextnumber.connect;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import techcamp.nextnumber.R;

/**
 * This is the main activity to show data transfer via  bluetooth
 * Main activity shows common data will be used to syn data between two devices
 * value 0 at index i means no one touch on the number i;
 * value 1 means this user got the number i
 * value 2 means opposite user got the number i
 */
public class BluetoothConnect extends Activity {
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

    // Layout Views
    private EditText mOutEditText;
    private Button mSendButton;
    private Button mConnectButton;
    private Button mWaitButton;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    //Data and view of this user's status
    private int[] mMyData = new int[MAX_NUMBER];
    private TextView mMyDataTextView;
    //Data and view of opposite user's status
    private int[] mFriendData = new int[MAX_NUMBER];
    private TextView mFiendDataTextView;
    //Data and view of common data
    private int[] mCommonData = new  int[MAX_NUMBER];
    private TextView mCommonDataTextView;

    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothConnectionService mBluetoothService = null;

    /*
     * Set the main layout,
     * check if bluetooth supported on device, set this device to discoverable mode.
     * make connection to another device.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the window layout
        setContentView(R.layout.connect_main);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Connect to another bluetooth device
        mConnectButton = (Button)findViewById(R.id.button_connect);
        mConnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnectButton.setVisibility(View.GONE);
                mWaitButton.setVisibility(View.GONE);
                connectToDevices();
            }
        });

        // ensure this device is discoverable by others and wait for another send connect request.
        mWaitButton = (Button)findViewById(R.id.button_wait);
        mWaitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                // do nothing
                mConnectButton.setVisibility(View.GONE);
                mWaitButton.setVisibility(View.GONE);
                setStatus("Waiting...");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mBluetoothService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothService.getState() == BluetoothConnectionService.STATE_NONE) {
              // Start the Bluetooth chat services
              mBluetoothService.start();
            }
        }
    }

    private void setupChat() {

        // Initialize the array adapter for the conversation thread
        Arrays.fill(mCommonData,0);
        Arrays.fill(mMyData,0);
        Arrays.fill(mFriendData, 0);

        //Get This user's data view
        mMyDataTextView = (TextView)findViewById(R.id.textview_my_data);
        //Get opposite user's  data view
        mFiendDataTextView = (TextView)findViewById(R.id.textview_friend_data);
        //Get common data view
        mCommonDataTextView = (TextView)findViewById(R.id.textview_data);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });

        // Initialize the BluetoothConnectionService to perform bluetooth connections
        mBluetoothService = new BluetoothConnectionService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mBluetoothService != null) mBluetoothService.stop();
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothConnectionService to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }

    // The Handler that gets information back from the BluetoothConnectionService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int index; // index in array of the number transferred between two devices
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothConnectionService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    break;
                case BluetoothConnectionService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BluetoothConnectionService.STATE_LISTEN:
                case BluetoothConnectionService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
//                mConversationArrayAdapter.add("Me:  " + writeMessage);
                index = writeMessage.charAt(0) - '0';
                //if index value unavailable in data set then break;
                if (index >= MAX_NUMBER || index < 0)
                    break;
                //index available, update common data and this user's data;
                if(mCommonData[index] == 0){
                    mCommonData[index] = 1;
                    mMyData[index] = 1;
                    mCommonDataTextView.setText(Arrays.toString(mCommonData));
                    mMyDataTextView.setText(Arrays.toString(mMyData));

                }
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                index = readMessage.charAt(0) - '0';
                //if index value unavailable in data set then break;
                if (index >= MAX_NUMBER || index < 0)
                    break;
                //index available, update common data and opposite user's data;
                if(mCommonData[index] == 0){
                    mCommonData[index] = 2;
                    mFriendData[index] = 1;
                    mCommonDataTextView.setText(Arrays.toString(mCommonData));
                    mFiendDataTextView.setText(Arrays.toString(mFriendData));
                }
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occurred
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, secure);
    }


    public boolean connectToDevices() {
        Intent serverIntent = null;
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
         }

}
