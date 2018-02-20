package com.dou.packer;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dou.packer.DataManager.SinglePackingManager;
import com.dou.packer.DataManager.items.PackingItem;

import java.util.ArrayList;
import java.util.List;

import static com.dou.packer.utils.IntentKeys.NEW_PACKING_FLAG;
import static com.dou.packer.utils.IntentKeys.PACKING_NAME;

public class Packing extends AppCompatActivity {

    private TextView packingTitle;

//    List<PackingItem> data = new ArrayList<>();

    private RecyclerView recyclerViewPackingItems;
    private PackingRecyclerViewAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    private EditText editTextAddItem;
    private ImageButton buttonAddItem;

    private Context context;

    boolean isPackingNew;

    SinglePackingManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing);

        context = this;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isPackingNew = getIntent().getBooleanExtra(NEW_PACKING_FLAG,false);

        packingTitle = (TextView) findViewById(R.id.textViewPackingTitle);
        packingTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                View packingTitleEditorLayout = LayoutInflater.from(context).inflate(R.layout.packing_title_editor,null);

                final EditText packingTitleEditor = (EditText)packingTitleEditorLayout.findViewById(R.id.editText_packing_editor);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

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
                                packingTitle.setText(packingTitleString);
                                // TODO: 2018-2-20 rename at storage
                            }
                        });

                builder.show();

                return true;
            }
        });

        recyclerViewPackingItems = (RecyclerView)findViewById(R.id.recyclerViewPacking);

        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPackingItems.setLayoutManager(mLayoutManager);

        adapter = new PackingRecyclerViewAdapter(new ArrayList<PackingItem>(), context);
        recyclerViewPackingItems.setAdapter(adapter);

        // pack item
        adapter.setOnItemClickListener(new PackingRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
//                Toast.makeText(context, ""+itemView, Toast.LENGTH_SHORT).show();
                if (itemView instanceof CheckBox) {
                    ((CheckBox) itemView)
                            .setTextColor(
                                    ContextCompat.getColor(context,
                                            (((CheckBox) itemView).isChecked() ?
                                                    R.color.colorPackedItem :
                                                    R.color.colorUnpackItem)
                                    )
                            );
                    ((CheckBox) itemView).getPaint()
                            .setStrikeThruText(((CheckBox) itemView).isChecked());
                    ((CheckBox) itemView).getPaint()
                            .setFakeBoldText(!((CheckBox) itemView).isChecked());
                    adapter.setPackingItemChecked(pos, ((CheckBox) itemView).isChecked());
                }
            }
        });

        // delete item
        adapter.setOnItemLongClickListener(new PackingRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int pos) {
                Toast.makeText(context, ""+adapter.getItem(pos), Toast.LENGTH_SHORT).show();
                adapter.deleteItem(pos);
                return true;
            }
        });

        editTextAddItem = (EditText)findViewById(R.id.edit_text_packing_add_item);
        buttonAddItem = (ImageButton)findViewById(R.id.image_button_packing_add_item);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextAddItem.getText().toString().equals("")){
                    adapter.addItem(new PackingItem(editTextAddItem.getText().toString(),false));
                    editTextAddItem.setText("");
                    recyclerViewPackingItems.scrollToPosition(adapter.getItemCount()-1);
                }

            }
        });

        editTextAddItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    if (!editTextAddItem.getText().toString().equals("")){
                        adapter.addItem(new PackingItem(editTextAddItem.getText().toString(),false));
                        editTextAddItem.setText("");
                        recyclerViewPackingItems.scrollToPosition(adapter.getItemCount()-1);
                        return true;
                    }else {
                        return false;
                        // TODO: 2018-2-19 clear focus
//                        editTextAddItem.clearFocus();
                    }
                }
                return false;
            }
        });


//        init data
        // TODO: 2018-2-20 在UI线程IO？
        manager = new SinglePackingManager(this,getIntent().getStringExtra(PACKING_NAME),isPackingNew);
        if (isPackingNew){
            packingTitle.setText(getIntent().getStringExtra(PACKING_NAME));
        }else {
            packingTitle.setText(getIntent().getStringExtra(PACKING_NAME));
            adapter.addData(manager.getData());
            recyclerViewPackingItems.scrollToPosition(adapter.getItemCount()-1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.packing_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                onBackPressed();
//                this.finish(); // back button
                return true;
            }
            case R.id.packing_action_delete:{
                Toast.makeText(context, "Delete packing at main activity", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.packing_action_help:{
                Toast.makeText(context, "No Help Here", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.packing_action_add_demo:{
                popupAddItemDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void popupAddItemDialog() {
        View layoutAddItem = LayoutInflater.from(context).inflate(R.layout.popup_packing_add_item, null);

        RecyclerView popupRecyclerViewPackingItems;
        final PackingRecyclerViewAdapter pupupAdapter;
        LinearLayoutManager popupLayoutManager;

        popupRecyclerViewPackingItems = (RecyclerView) layoutAddItem.findViewById(R.id.recycler_view_popup_add_item);

        //创建默认的线性LayoutManager
        popupLayoutManager = new LinearLayoutManager(this);
        popupRecyclerViewPackingItems.setLayoutManager(popupLayoutManager);

        pupupAdapter = new PackingRecyclerViewAdapter(new ArrayList<PackingItem>(), context);
        popupRecyclerViewPackingItems.setAdapter(pupupAdapter);

        // pack item
        pupupAdapter.setOnItemClickListener(new PackingRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
//                Toast.makeText(context, ""+itemView, Toast.LENGTH_SHORT).show();
                if (itemView instanceof CheckBox) {
                    ((CheckBox) itemView)
                            .setTextColor(
                                    ContextCompat.getColor(context,
                                            (((CheckBox) itemView).isChecked() ?
                                                    R.color.colorToPackItem :
                                                    R.color.colorUnpackItem)
                                    )
                            );
//                    ((CheckBox) itemView).getPaint()
//                            .setStrikeThruText(((CheckBox) itemView).isChecked());
                    ((CheckBox) itemView).getPaint()
                            .setUnderlineText(((CheckBox) itemView).isChecked());
                    pupupAdapter.setPackingItemChecked(pos, ((CheckBox) itemView).isChecked());
//                    Toast.makeText(context, "clicked"+((CheckBox)itemView).getText()+" "+((CheckBox)itemView).isChecked(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        pupupAdapter.addData(getDemoData());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(layoutAddItem)
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(context, "xixixi", Toast.LENGTH_SHORT).show();

                        for (PackingItem item:
                                pupupAdapter.getData()) {
                            if (item.isPacked()){
                                adapter.addItem(item.setPackStatus(false));
                            }
                        }
                    }
                })
                .setNeutralButton("ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (PackingItem item:
                                pupupAdapter.getData()) {
                            adapter.addItem(item.setPackStatus(false));
                        }
                    }
                })
        ;

        builder.show();
    }

    @Override
    protected void onPause() {

        manager.saveData(adapter.getData());

        super.onPause();
    }



//    private List<PackingItem> getData(){
//
//        List<PackingItem> data = new ArrayList<>();
//
//        for (int i = 0; i<2;i++){
//            data.add(new PackingItem("身份证",true));
//            data.add(new PackingItem("电脑",false));
//            data.add(new PackingItem("电源",false));
//            data.add(new PackingItem("手机",false));
//            data.add(new PackingItem("开发机",false));
//            data.add(new PackingItem("相机",false));
//            data.add(new PackingItem("数据线",false));
//            data.add(new PackingItem("剃须刀",false));
//            data.add(new PackingItem("苏菲",false));
//            data.add(new PackingItem("移动硬盘",false));
//            data.add(new PackingItem("驾照",false));
//            data.add(new PackingItem("充电器",false));
//        }
//
//        return data;
//    }

    public List<PackingItem> getDemoData() {
        ArrayList<PackingItem> demoData = new ArrayList<>();

        String[] demoItems = getResources().getStringArray(R.array.packing_items_demo);

        for (String demoItem :
                demoItems) {
            demoData.add(new PackingItem(demoItem, false));
        }

        return demoData;
    }

    public static class PackingRecyclerViewAdapter
            extends RecyclerView.Adapter<PackingRecyclerViewAdapter.PackingViewHolder>{

        private List<PackingItem> data = null;
        private OnItemClickListener mClickListener;
        private OnItemLongClickListener mLongClickListener;
        private Context context;

        public Context getContext() {
            return context;
        }

        PackingRecyclerViewAdapter(List<PackingItem> data, Context context) {
            this.data = data;
            this.context = context;
        }

        public void setData(List<PackingItem> data) {
            this.data = data;
        }

        // TODO: 2018-2-19 what to do here?
        @Override
        public PackingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(
                            R.layout.packing_item,
                            parent,
                            false);
//            final PackingViewHolder vh = new PackingViewHolder(view,mClickListener,mLongClickListener);
//            if (mClickListener != null) {
//                vh.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mClickListener.onItemClick(vh.itemView, vh.getLayoutPosition());
//                    }
//                });
//            }
//            return vh;
            return new PackingViewHolder(view,mClickListener,mLongClickListener);
        }

        @Override
        public void onBindViewHolder(PackingViewHolder holder, int position) {

            holder.packingItem.setText(data.get(position).getItemName());
            holder.packingItem.setChecked(data.get(position).isPacked());
            holder.packingItem.setTextColor(
                    (data.get(position).isPacked()?
                            ContextCompat.getColor(context,R.color.colorPackedItem) :
                            ContextCompat.getColor(context,R.color.colorUnpackItem))
            );
            holder.packingItem.getPaint().setFakeBoldText(!data.get(position).isPacked());
            holder.packingItem.getPaint().setStrikeThruText(data.get(position).isPacked());
//            holder.packingItem.getPaint().setFlags(data.get(position).isPacked()?Paint.STRIKE_THRU_TEXT_FLAG:Paint.FAKE_BOLD_TEXT_FLAG);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        void addItem(PackingItem packingItem) {
            data.add(packingItem);
            notifyDataSetChanged();
        }

        PackingItem getItem(int pos) {
            return data.get(pos);
        }

        void addData(List<PackingItem> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        void setPackingItemChecked(int pos, boolean packingItemChecked) {
            if (data == null)
                return;
            this.data.get(pos).setPackStatus(packingItemChecked);
        }

        public List<PackingItem> getData() {
            return data;
        }

        boolean deleteItem(int pos) {
            if (pos>0 && pos<getItemCount()){
                data.remove(pos);
                notifyDataSetChanged();
                return true;
            }
            return false;
        }


        class PackingViewHolder
                extends RecyclerView.ViewHolder
                implements View.OnClickListener, View.OnLongClickListener
        {

            private CheckBox packingItem;
            private OnItemClickListener mClickListener;
            private OnItemLongClickListener mLongClickListener;

            PackingViewHolder(
                    View itemView,
                    OnItemClickListener mClickListener,
                    OnItemLongClickListener mLongClickListener) {
                super(itemView);
                packingItem = (CheckBox) itemView.findViewById(R.id.checkBox_packing_item);
                this.mClickListener = mClickListener;
                this.mLongClickListener = mLongClickListener;
                packingItem.setOnClickListener(this);
                packingItem.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {

                if (mClickListener!=null){
                    mClickListener.onItemClick(view, getLayoutPosition());
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (mLongClickListener!=null){
                    return mLongClickListener.onItemLongClick(view, getLayoutPosition());
                }
                return false;
            }
        }


        private interface OnItemClickListener {
            void onItemClick(View itemView, int pos);
        }

        void setOnItemClickListener(OnItemClickListener listener) {
            this.mClickListener = listener;
        }


        private interface OnItemLongClickListener {
            boolean onItemLongClick(View itemView, int pos);
        }

        void setOnItemLongClickListener(OnItemLongClickListener listener) {
            this.mLongClickListener = listener;
        }
    }
}
