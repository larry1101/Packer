package com.dou.packer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dou.packer.DataManager.PackingsManager;
import com.dou.packer.DataManager.items.CardInfo;
import com.dou.packer.utils.GlobalKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackingSorter extends AppCompatActivity {

    List<CardInfo> data = new ArrayList<>();

    PackingsManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_sorter);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (readPackings()){

        }else {
            Toast.makeText(this, "Error occured...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.sorter_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            case R.id.sorter_action_save: {
                Toast.makeText(this, "save...", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean readPackings() {
        try {
            manager = new PackingsManager(this);

            data = manager.getData(0);

            // TODO: 2018-3-4

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
