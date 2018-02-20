package com.dou.packer.DataManager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.dou.packer.DataManager.items.PackingItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-2-19.
 */

public class SinglePackingManager extends PackerStorageManager {

    private String PACKING_NAME;
    private File FILE_PACKING;
    private boolean FILE_EXIST;
    private boolean NEW_PACKING;

    public SinglePackingManager(Context context, String packingName, boolean newPacking) {
        super(context);
        this.PACKING_NAME = packingName;
        FILE_PACKING = new File(PACKINGS_DIR+File.separator+PACKING_NAME+".pki");
        NEW_PACKING = newPacking;
        FILE_EXIST = FILE_PACKING.exists();
    }

    @Override
    public ArrayList<PackingItem> getData() {
        ArrayList<PackingItem> data = new ArrayList<>();

        if (!NEW_PACKING && FILE_EXIST){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(FILE_PACKING));
                while (true){
                    String line = reader.readLine();
                    if (line == null) break;
                    data.add(
                            new PackingItem(
                                    line.split(",")[0],
                                    line.split(",")[1].equals("1")
                            )
                    );
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context, "FileNotFoundException occurs while reading data", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "IOException occurs while reading data", Toast.LENGTH_SHORT).show();
            }
        }

        return data;
    }

    public String getUnpackItemString(int cnt){
        String itemsString = "";
        if (!NEW_PACKING && FILE_EXIST){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(FILE_PACKING));
                int item_added = 0;
                boolean nothing = true;
                while (true){
                    String line = reader.readLine();
                    if (line == null){
                        break;
                    }
                    if (nothing)
                        nothing = false;
                    if (line.split(",")[1].equals("0")) {
                        if (item_added >= cnt) {
                            itemsString+="    ......";
                            break;
                        }
                        itemsString+=line.split(",")[0]+"\n";
                        item_added ++;
                    }
                }
                if (nothing){
                    itemsString = "Nothing to pack...";
                } else if (item_added==0) {
                    itemsString+="All packed";
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context, "FileNotFoundException occurs while reading data", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "IOException occurs while reading data", Toast.LENGTH_SHORT).show();
            }
            // TODO: 2018-2-19 finally close reader?
        }

        return itemsString;
    }

    public String getUnpackItemString(){
        return getUnpackItemString(5);
    }

    @Override
    public boolean saveData(List<?> data) {
        if (NEW_PACKING || !FILE_EXIST){
            try {
                if (FILE_PACKING.createNewFile()){

                    BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PACKING));

                    for (Object item :
                            data) {
                        writer.write(
                                ((PackingItem)item).getItemName() +
                                        "," +
                                        (((PackingItem)item).isPacked()?"1":"0") +
                                        "\n"
                        );
                    }

                    writer.close();

                    // TODO: 2018-2-20 在UI线程IO？
                    new PackingsManager(context).addPacking(PACKING_NAME);

                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "IOException occurs while reading data", Toast.LENGTH_SHORT).show();
            }
        }else if (FILE_EXIST && !NEW_PACKING){
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(FILE_PACKING));

                for (Object item :
                        data) {
                    writer.write(
                            ((PackingItem) item).getItemName() +
                                    "," +
                                    (((PackingItem) item).isPacked() ? "1" : "0") +
                                    "\n"
                    );
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "IOException occurs while reading data", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "Error.........", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
