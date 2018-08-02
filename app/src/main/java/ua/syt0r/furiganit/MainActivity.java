package ua.syt0r.furiganit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final String PRODUCT_ID = "ua.syt0r.furiganit.support_item";

    private TextView textView;
    private Button button;

    private boolean canDrawOverlays;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    //prevent from attempt to press button while loading
    private boolean isLoading;

    private BillingProcessor billingProcessor;
    private boolean isBillingAvailable;
    private boolean isBillingReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setElevation(0);
        }catch (NullPointerException e){}

        button = findViewById(R.id.button);
        textView = findViewById(R.id.text);

        checkPermission();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastReceiver = new ServiceListener();

        isBillingAvailable = BillingProcessor.isIabServiceAvailable(this);
        isBillingReady = false;
        if (isBillingAvailable){
            billingProcessor = new BillingProcessor(this, getString(R.string.license_key),
                    new BillingHandler());
            //billingProcessor.initialize();
        }

        isLoading = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FuriganaService.START_ACTION);
        intentFilter.addAction(FuriganaService.STOP_ACTION);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        updateText();
    }

    @Override
    protected void onPause() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (billingProcessor != null)
            billingProcessor.release();

        super.onDestroy();
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

                final SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

                View settingsView = LayoutInflater.from(this).inflate(R.layout.dialog_settings, null);

                final SeekBar seekBar = (SeekBar)settingsView.findViewById(R.id.seekBar);
                final TextView seekBarValue = settingsView.findViewById(R.id.seek_bar_value);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        seekBarValue.setText(String.valueOf(i+1));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                long seconds = sharedPreferences.getLong("timeout",5);
                seekBar.setProgress((int)seconds-1);
                seekBarValue.setText(String.valueOf(seconds));

                settingsView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharedPreferences.edit()
                                .putInt("xPos",0)
                                .putInt("yPos",0)
                                .apply();
                    }
                });

                AlertDialog.Builder settingsAlertDialogBuilder = new AlertDialog.Builder(this)
                        .setView(settingsView)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sharedPreferences.edit()
                                        .putLong("timeout",seekBar.getProgress()+1)
                                        .apply();
                                Toast.makeText(MainActivity.this, R.string.apply_changes, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                settingsAlertDialogBuilder.create().show();

                break;
            case R.id.about:

                View aboutView = LayoutInflater.from(this).inflate(R.layout.dialog_about, null);

                if (isBillingAvailable){
                    (aboutView.findViewById(R.id.support_button)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isBillingReady)
                                billingProcessor.purchase(MainActivity.this, PRODUCT_ID);
                            else
                                Toast.makeText(MainActivity.this, R.string.bill_not_ready, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    aboutView.findViewById(R.id.purchase_layout).setVisibility(GONE);

                AlertDialog.Builder aboutAlertDialogBuilder = new AlertDialog.Builder(this)
                        .setView(aboutView);
                aboutAlertDialogBuilder.create().show();

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

        if (isLoading){
            Toast.makeText(this,R.string.wait,Toast.LENGTH_SHORT).show();
            return;
        }

        if (!canDrawOverlays && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
            return;
        }

        button.setText(R.string.loading);

        isLoading = true;

        Intent intent = new Intent(this,FuriganaService.class);

        if (!FuriganaService.isRunning)
            startService(intent);
        else
            stopService(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkPermission();
        if (billingProcessor != null && !billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class ServiceListener extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FuriganaService.START_ACTION)){
                updateText();
                isLoading = false;
            }
            if (intent.getAction().equals(FuriganaService.STOP_ACTION)){
                updateText();
                isLoading = false;
            }
        }
    }

    private class BillingHandler implements BillingProcessor.IBillingHandler{

        @Override
        public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
            Toast.makeText(MainActivity.this, R.string.thanks, Toast.LENGTH_LONG).show();
            billingProcessor.consumePurchase(PRODUCT_ID);
        }

        @Override
        public void onPurchaseHistoryRestored() {

        }

        @Override
        public void onBillingError(int errorCode, @Nullable Throwable error) {
            Toast.makeText(MainActivity.this, R.string.bill_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBillingInitialized() {
            isBillingReady = true;
        }
    }

}
