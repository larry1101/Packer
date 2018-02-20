package com.dou.packer.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dou.packer.R;

/**
 * Created by Administrator on 2018-2-19.
 */
// TODO: 2018-2-19 how????????
public class PopupEditText {
    Context context;
    AlertDialog.Builder builder;
    String title;

    public PopupEditText(Context context) {
        this.context = context;

        View packingTitleEditorLayout = LayoutInflater.from(context).inflate(R.layout.packing_title_editor,null);

        final EditText packingTitleEditor = (EditText)packingTitleEditorLayout.findViewById(R.id.editText_packing_editor);

        builder = new AlertDialog.Builder(context);

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
                            title = null;
                            return;
                        }
                        title = packingTitleString;
                    }
                });
        builder.show();
    }

    public String getTitle(){
        return title;
    }
}
