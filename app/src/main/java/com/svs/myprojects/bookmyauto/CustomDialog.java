package com.svs.myprojects.bookmyauto;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by snehalsutar on 2/3/16.
 */
public class CustomDialog extends DialogFragment {

    public interface CustomDialogListener {
        void onDialogPositiveClick(String address, String type, String selectedItems);
    }

    // Use this instance of the interface to deliver action events
    CustomDialogListener mListener;
    EditText mEditAddress;

    String mAddressType;
    String mMultipleOptions[];
    ArrayList<Integer> mSelectedItems = new ArrayList<>();  // Where we track the selected items
    String LOG_TAG = "svs_me";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mAddressType = bundle.getString(Constants.ADDRESS_TYPE);
        mMultipleOptions = bundle.getStringArray(Constants.SET_MULTIPLE_OPTIONS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogBoxStyle);

        if (getParentFragment() instanceof CustomDialogListener) {
            mListener = (CustomDialogListener) getParentFragment();
        }

        if (mAddressType != null) {
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View rootView = inflater.inflate(R.layout.dialog_get_location, null);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(rootView)
                    // Add action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                            mEditAddress = (EditText) rootView.findViewById(R.id.address);
//                        mListener.onDialogPositiveClick(mEditAddress.getText().toString(), mAddresstype);

                            if (mAddressType != null) {
                                mListener.onDialogPositiveClick(mEditAddress.getText().toString(), mAddressType, null);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CustomDialog.this.getDialog().cancel();
                        }
                    });


        } else if (mMultipleOptions != null) {


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String str = "";
                    for (int i = 0; i < mSelectedItems.size(); i++) {
//                        selectedOptionsArray[i] = mMultipleOptions[mSelectedItems.get(i)];
                        switch (mSelectedItems.get(i)) {
                            case 0:
                                str = str + Constants.AVOID_TOLLS_STRING + ",";
                                break;
                            case 1:
                                str = str + Constants.AVOID_HIGHWAYS_STRING + ",";
                                break;
                            case 2:
                                str = str + Constants.AVOID_FERRIES_STRING + ",";
                                break;
                        }
                    }
                    mListener.onDialogPositiveClick(null, null, str);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CustomDialog.this.getDialog().cancel();
                        }
                    });
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
            builder.setMultiChoiceItems(mMultipleOptions, null,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                mSelectedItems.add(which);
                                Log.i(LOG_TAG, "which:" + which + " boolean: " + isChecked);
                            } else if (mSelectedItems.contains(which)) {
                                // Else, if the item is already in the array, remove it
                                mSelectedItems.remove(Integer.valueOf(which));
                            }
                        }
                    });

        }
        return builder.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null) {
//            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
//
//            int messageId = getDialog().getContext().getResources().getIdentifier("android:id/message", null, null);
////            View divider = getDialog().findViewById(messageId);
////            divider.setBackgroundColor(getResources().getColor(R.color.));
//            TextView textView = (TextView)getDialog().findViewById(messageId);
//            textView.setTextColor(getResources().getColor(R.color.colorBlack0));


        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
