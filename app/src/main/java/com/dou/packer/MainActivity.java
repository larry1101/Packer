package com.dou.packer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dou.packer.DataManager.PackingsManager;
import com.dou.packer.DataManager.items.PackingItem;
import com.dou.packer.adapters.RecyclerAdapter;
import com.dou.packer.DataManager.items.CardInfo;
import com.dou.packer.utils.IntentKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.dou.packer.utils.IntentKeys.NEW_PACKING_FLAG;
import static com.dou.packer.utils.IntentKeys.PACKING_NAME;

public class MainActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener
{

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    private LinearLayoutManager mLayoutManager;
//    List<CardInfo> list = new ArrayList<>();

    PackingsManager packingsManager;

    Context context;

    private static final int REQUEST_STORAGE_READ_PERMISSION = 2;
    private static final int REQUEST_STORAGE_WRITE_PERMISSION = 3;
    private void checkHuaweiPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_READ_PERMISSION);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_WRITE_PERMISSION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        // TODO: 2018-2-20 huawei 权限检查
        checkHuaweiPermission();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_main);

        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerAdapter(new ArrayList<CardInfo>());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Intent intent = new Intent(MainActivity.this,Packing.class);
                intent.putExtra(NEW_PACKING_FLAG, false);

                intent.putExtra(PACKING_NAME, adapter.getItemTitle(pos));
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int pos) {
                // TODO: 2018-2-20 在UI线程IO？
                packingsManager.attemptToDeleteItem(adapter.getItemTitle(pos));
                return true;
            }
        });


        try {
            packingsManager = new PackingsManager(context);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Storage Error", Toast.LENGTH_SHORT).show();
        }

//        adapter.addCards(packingsManager.getData());

    }

    // TODO: 2018-2-20 在UI线程IO？
    public void refreshRecyclerView(){
        adapter.clearCards();
        adapter.addCards(packingsManager.getData());
    }

    @Override
    protected void onResume() {
        refreshRecyclerView();
//        adapter.clearCards();
//        adapter.addCards(packingsManager.getData());

        super.onResume();
    }

//    private List<CardInfo> getData() {
//        List<CardInfo> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            if (i == 0){
//                CardInfo ci = new CardInfo();
//                ci.setTitle("Packer");
//                ci.setContent("电脑\n电源\n手机\n开发机\n相机\n数据线\n相机\n剃须刀\n苏菲\n移动硬盘\n驾照\n" +
//                        "充电器\n身份证\nyume");
//                list.add(ci);
//                continue;
//            }
//            CardInfo ci = new CardInfo();
//            ci.setContent("内容......");
//            ci.setTitle("标题 " + i);
//            list.add(ci);
//        }
//        return list;
//    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    AlertDialog alertDialog = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.title_bar_new) {

            View packingTitleEditorLayout = LayoutInflater.from(context).inflate(R.layout.packing_title_editor,null);

            final EditText packingTitleEditor = (EditText)packingTitleEditorLayout.findViewById(R.id.editText_packing_editor);

            // TODO: 2018-2-19 can have bug?
            packingTitleEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE){
                        if (!packingTitleEditor.getText().toString().equals("")){
                            String packingTitleString = packingTitleEditor.getText().toString();
                            if (packingTitleString.equals("")){
                                return false;
                            }
                            Intent intent = new Intent(MainActivity.this, Packing.class);
                            if (adapter.redundant(packingTitleString)){
                                intent.putExtra(NEW_PACKING_FLAG, false);
                                Toast.makeText(context, "Packing redundant", Toast.LENGTH_SHORT).show();
                            }else {
                                intent.putExtra(NEW_PACKING_FLAG, true);
                            }
                            intent.putExtra(PACKING_NAME,packingTitleString);
                            if (alertDialog != null){
                                alertDialog.cancel();
                            }
                            startActivity(intent);
                            return true;
                        }
                    }
                    return false;
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder = new AlertDialog.Builder(context);

            builder.setView(packingTitleEditorLayout);

            builder.setCancelable(true)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String packingTitleString = packingTitleEditor.getText().toString();
                            if (packingTitleString.equals("")){
                                dialogInterface.cancel();
                                return;
                            }
                            Intent intent = new Intent(MainActivity.this, Packing.class);
                            if (adapter.redundant(packingTitleString)){
                                intent.putExtra(NEW_PACKING_FLAG, false);
                                Toast.makeText(context, "Packing redundant", Toast.LENGTH_SHORT).show();
                            }else {
                                intent.putExtra(NEW_PACKING_FLAG, true);
                            }
                            intent.putExtra(PACKING_NAME,packingTitleString);

                            startActivity(intent);
                        }
                    });

            alertDialog = builder.show();
            // TODO: 2018-2-20 popup ime
            return true;
        }else if (id == R.id.title_bar_help){
            Toast.makeText(context, "Connect WeChat @ larry11011101", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
