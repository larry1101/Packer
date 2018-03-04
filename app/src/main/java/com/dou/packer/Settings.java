package com.dou.packer;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dou.packer.utils.GlobalKeys;

public class Settings extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int showingPackingsItemsUpperLimit;
    boolean horizontal_cards;

    SeekBar seekBar;
    ToggleButton toggleButton;
    TextView textViewSeekbarProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences  = getSharedPreferences(GlobalKeys.SP_PACKER_SETTINGS,MODE_PRIVATE);
        showingPackingsItemsUpperLimit = sharedPreferences.getInt(GlobalKeys.SP_PACKER_SETTINGS_CNT,GlobalKeys.showingPackingsItemsUpperLimitDefault);
        horizontal_cards = sharedPreferences.getBoolean(GlobalKeys.SP_PACKER_SETTINGS_HORIZONTAL,false);

        seekBar = (SeekBar)findViewById(R.id.seekBar_setting_cnt);
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton_setting_horizontal);
        textViewSeekbarProgress = (TextView)findViewById(R.id.textView_seekbar_progress);

        // TODO: 2018-3-4 globalize / measure & cal
        seekBar.setMax(20);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewSeekbarProgress.setText(""+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekBar.setProgress(showingPackingsItemsUpperLimit, true);
        }else {
            seekBar.setProgress(showingPackingsItemsUpperLimit);
        }
        toggleButton.setChecked(horizontal_cards);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.setting_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            case R.id.setting_action_save: {
                saveSettings();
                Toast.makeText(this, "修改将在下次启动时生效", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveSettings() {
        sharedPreferences.edit()
                .putInt(GlobalKeys.SP_PACKER_SETTINGS_CNT, seekBar.getProgress())
                .putBoolean(GlobalKeys.SP_PACKER_SETTINGS_HORIZONTAL, toggleButton.isChecked())
                .apply();
    }
}
