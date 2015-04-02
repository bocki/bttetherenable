package net.bocki.bluetoothtetherenabler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


public class EnableTether extends Activity {

    BluetoothAdapter mBluetoothAdapter = null;
    Class<?> classBluetoothPan = null;
    Constructor<?> conBluetoothPan = null;
    Method mEnableTether;
    Object BTServiceInstance = null;
    boolean turnonoff=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        turnonoff=intent.getBooleanExtra(MainActivity.EXTRA_TURNONOFF, true);
        actuallyEnableTether();
    }

    private void actuallyEnableTether() {
        Context myContext=getApplicationContext();
        mBluetoothAdapter=getBTAdapter();

        try {
            classBluetoothPan = Class.forName("android.bluetooth.BluetoothPan");

            Class[] paramSet = new Class[1];
            paramSet[0] = boolean.class;

            mEnableTether = classBluetoothPan.getDeclaredMethod("setBluetoothTethering", paramSet);
            conBluetoothPan = classBluetoothPan.getDeclaredConstructor(Context.class, BluetoothProfile.ServiceListener.class);
            conBluetoothPan.setAccessible(true);
            BTServiceInstance=conBluetoothPan.newInstance(myContext, new BTPanServiceListener(myContext));
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reallyEnableTether() {
        if (mBluetoothAdapter != null) {
            try { mEnableTether.invoke(BTServiceInstance, turnonoff); }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (turnonoff==true) {
                Toast.makeText(getApplicationContext(), R.string.bluetooth_enabled, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.bluetooth_disabled, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BluetoothAdapter getBTAdapter() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return BluetoothAdapter.getDefaultAdapter();
        }
        else {
            BluetoothManager bm = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            return bm.getAdapter();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public class BTPanServiceListener implements BluetoothProfile.ServiceListener {
        private final Context context;

        public BTPanServiceListener(final Context context) {
            this.context=context;
        }

        @Override
        public void onServiceConnected(final int profile, final BluetoothProfile proxy) {
            // Log.i("BluetoothTetherEnabler", "onServiceConnected");
            reallyEnableTether();

        }

        @Override
        public void onServiceDisconnected(final int profile) {
            // Log.i("BluetoothTetherEnabler", "onServiceDisconnected");
        }
    }
}

