package com.dou.packer.DataManager;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018-2-19.
 */

public abstract class PackerStorageManager {
    protected boolean mExternalStorageWriteable;

    protected String STORAGE_ROOT;
    protected String DATA_ROOT;
    protected File DIR_DATA_ROOT;
    protected String PACKINGS_DIR;
    protected File DIR_PACKINGS;

    protected Context context;

    public boolean checkStorageStatus(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public abstract List<?> getData(int showingPackingsItemsUpperLimit);

    public abstract boolean saveData(List<?> data);

    public PackerStorageManager(Context context) {
        this.context = context;
        mExternalStorageWriteable = checkStorageStatus();
        if (mExternalStorageWriteable){
            STORAGE_ROOT =Environment.getExternalStorageDirectory().getAbsolutePath();
            DATA_ROOT = STORAGE_ROOT + File.separator + "Packer";
            PACKINGS_DIR = DATA_ROOT + File.separator + "Packings";

            // TODO: 2018-2-20 不要在这里做检查？
            DIR_DATA_ROOT = new File(DATA_ROOT);
            if (!DIR_DATA_ROOT.exists() || !DIR_DATA_ROOT.isDirectory()){
                DIR_DATA_ROOT.mkdirs();
            }
            DIR_PACKINGS = new File(PACKINGS_DIR);
            if (!DIR_PACKINGS.exists() || !DIR_PACKINGS.isDirectory()){
                DIR_PACKINGS.mkdirs();
            }
        }

    }
}
