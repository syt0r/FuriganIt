package ua.syt0r.furiganit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;

    private boolean canDrawOverlays;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setElevation(0);
        }catch (NullPointerException e){}

        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.text);

        checkPermission();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastReceiver = new ServiceListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FuriganaService.START_ACTION);
        intentFilter.addAction(FuriganaService.STOP_ACTION);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                break;
            case R.id.about:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermission(){

        canDrawOverlays = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            canDrawOverlays = Settings.canDrawOverlays(this);

        updateText();

    }

    private void updateText(){

        if (!canDrawOverlays){

            button.setText(R.string.go_to_settings);
            textView.setText(R.string.got_to_sett_hint);

        }else if (FuriganaService.isRunning){

            button.setText(R.string.stop);
            textView.setText(R.string.stop_hint);

        }else{

            button.setText(R.string.start);
            textView.setText(R.string.start_hint);

        }

    }

    public void Click(View v){

        button.setText(R.string.loading);

        if (!canDrawOverlays && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }

        Intent intent = new Intent(this,FuriganaService.class);

        if (!FuriganaService.isRunning)
            startService(intent);
        else
            stopService(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkPermission();
    }

    class ServiceListener extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FuriganaService.START_ACTION)){
                updateText();
            }
            if (intent.getAction().equals(FuriganaService.STOP_ACTION)){
                updateText();
            }
        }
    }
}
