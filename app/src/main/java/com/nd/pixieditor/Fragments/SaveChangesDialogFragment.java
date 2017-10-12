package com.nd.pixieditor.Fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.nd.pixieditor.ImgEditorActivity;
import com.nd.pixieditor.R;

public class SaveChangesDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    public SaveChangesDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog alertDialog =  new AlertDialog.Builder(getActivity())
                .setTitle(R.string.ImgEditor_saveDialog_Title)
                .setPositiveButton(R.string.ImgEditor_saveDialog_SaveBtnText, this)
                .setNegativeButton(R.string.ImgEditor_saveDialog_discardBtnText, this)
                .setCancelable(false)
                .setMessage(R.string.save_changes_for_this_image)
                .show();
                //.create();

        TextView messageView = (TextView) alertDialog.findViewById(android.R.id.message);
        if(messageView != null)
            messageView.setGravity(Gravity.CENTER);

        return alertDialog;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                ((ImgEditorActivity)getActivity()).onDialogChangesShouldBeSaved();
                break;
            case Dialog.BUTTON_NEGATIVE:
                ((ImgEditorActivity)getActivity()).onDialogDiscardChanges();
                break;

        }

    }
}
