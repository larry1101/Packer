package com.dou.packer.DataManager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.dou.packer.DataManager.items.CardInfo;
import com.dou.packer.MainActivity;
import com.dou.packer.R;

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
 * Packing管理者
 * Created by ladoudou on 2018-2-19.
 */

public class PackingsManager extends PackerStorageManager {

    private String PackingsFileName = "Packings.pkm";
    private File FILE_PACKINGS;

    public PackingsManager(Context context) throws IOException {
        super(context);
        FILE_PACKINGS = new File(DATA_ROOT+File.separator+PackingsFileName);
        if (!FILE_PACKINGS.exists()){
            FILE_PACKINGS.createNewFile();
        }

    }

    // TODO: 2018-2-19 merge new files to manager
//    public void mergePackings(){
//
//    }

    @Override
    public ArrayList<CardInfo> getData(int showingPackingsItemsUpperLimit) {

        ArrayList<CardInfo> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PACKINGS));
            while (true){
                String line = reader.readLine();
                if (line == null) break;

                if (showingPackingsItemsUpperLimit>0){
                    SinglePackingManager singlePacking = new SinglePackingManager(context,line,false);
                    singlePacking.getData(-1);
                    data.add(
                            new CardInfo(line,
                                    singlePacking.getUnpackItemString(showingPackingsItemsUpperLimit),
                                    singlePacking.getCntUnpack(),
                                    singlePacking.getItemCount()
                            )
                    );
                }else {
                    data.add(new CardInfo(line));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "FileNotFoundException occurs while reading data", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IOException occurs while reading data", Toast.LENGTH_SHORT).show();
        }
        return data;
        // TODO: 2018-3-4 close reader?
    }

    @Deprecated
    @Override
    public boolean saveData(List<?> data) {
        return false;
    }

    public void addPacking(String packing_name) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PACKINGS, true));
            writer.write(packing_name + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IOException occurs while reading data", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteItem(String packingTitle) {
//        Toast.makeText(context, "del "+packingTitle , Toast.LENGTH_SHORT).show();

        File fileToDel = new File(PACKINGS_DIR+File.separator+packingTitle+".pki");
        if (fileToDel.exists()){
            if(fileToDel.delete()){
                Toast.makeText(context, "Del "+packingTitle+" successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Del "+packingTitle+" Unsuccessfully", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(context, "Error, please merge packings", Toast.LENGTH_SHORT).show();
            return false;
        }

        String newPackingsString = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_PACKINGS));
            while (true){
                String line = reader.readLine();
                if (line == null) break;
                if (!line.equals(packingTitle)){
                    newPackingsString+=line+"\n";
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PACKINGS));
            writer.write(newPackingsString);
            writer.close();
        } catch (IOException e) {
            Toast.makeText(context, "IOException occurs while reading data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        ((MainActivity)context).refreshRecyclerView();

        return true;
    }

    public void attemptToDeleteItem(final String itemTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getText(R.string.alert_del_packing))
                .setCancelable(true)
                .setNegativeButton(context.getText(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(context.getText(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteItem(itemTitle);
                    }
                });

        builder.show();
    }

    // TODO: 2018-2-19 mk logger
}
