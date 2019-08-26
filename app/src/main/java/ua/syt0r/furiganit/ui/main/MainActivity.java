package ua.syt0r.furiganit.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import ua.syt0r.furiganit.R;
import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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



                break;
        }
        return super.onOptionsItemSelected(item);
    }




}
