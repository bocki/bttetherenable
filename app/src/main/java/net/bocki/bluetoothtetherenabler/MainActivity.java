package net.bocki.bluetoothtetherenabler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {
    public final static String EXTRA_TURNONOFF="net.bocki.bluetoothtetherenabler.TURNONOFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enableBTTether(View view) {
        Intent intent=new Intent(this, EnableTether.class);
        intent.putExtra(EXTRA_TURNONOFF, true);
        startActivity(intent);
    }
    public void disableBTTether(View view) {
        Intent intent=new Intent(this, EnableTether.class);
        intent.putExtra(EXTRA_TURNONOFF, false);
        startActivity(intent);
    }
}