package ua.syt0r.furiganit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.status);
        setStatus();

    }

    private void setStatus(){
        textView.setText(getString(R.string.service_status) + " " +
                (FuriganaService.isRunning ? getString(R.string.enabled) : getString(R.string.disabled)));
    }

    public void Click(View v){

        Log.wtf("test", "click" + FuriganaService.isRunning);

        Intent intent = new Intent(this,FuriganaService.class);

        if (!FuriganaService.isRunning)
            startService(intent);
        else
            stopService(intent);

        setStatus();

    }
}
