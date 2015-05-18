package com.neeraja.imagesearch.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.neeraja.imagesearch.R;
import com.neeraja.imagesearch.models.SettingsModel;

/**
 * Created by Neeraja on 5/14/2015.
 */
public class SettingsFragment extends DialogFragment{
    SettingsModel settings1;

    public static SettingsFragment newInstance(String title) {
        SettingsFragment frag = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.settings_fragment, null);
        // image size spinner
        final Spinner spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.imageSize_array, android.R.layout.simple_spinner_item);
        imageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageSize.setAdapter(imageSizeAdapter);

        // image color spinner
        final Spinner spImageColor = (Spinner) view.findViewById(R.id.spImageColor);
        ArrayAdapter<CharSequence> imageColorAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.imageColor_array, android.R.layout.simple_spinner_item);
        imageColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageColor.setAdapter(imageColorAdapter);


        // image type spinner
        final Spinner spImageType = (Spinner) view.findViewById(R.id.spImageType);
        ArrayAdapter<CharSequence> imageTypeAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.imageType_array, android.R.layout.simple_spinner_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageType.setAdapter(imageTypeAdapter);

        final EditText etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        final SettingsModel settings = SettingsModel.sharedInstance();
            spImageSize.setSelection(imageSizeAdapter.getPosition(settings.imageSize));
            spImageColor.setSelection(imageColorAdapter.getPosition(settings.imageColor));
            spImageType.setSelection(imageTypeAdapter.getPosition(settings.imageType));
            etSiteFilter.setText(settings.siteFilter);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(title)
                    .setPositiveButton(getActivity().getString(R.string.bOk), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            settings.imageSize = spImageSize.getSelectedItem().toString();
                            settings.imageColor = spImageColor.getSelectedItem().toString();
                            settings.imageType = spImageType.getSelectedItem().toString();
                            settings.siteFilter = etSiteFilter.getText().toString();
                            dialogListener = (OnDialogCompleteListener) getActivity();
                            dialogListener.onDialogComplete(settings);
                        }
                    })
                    .setNegativeButton(getActivity().getString(R.string.bCancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                }
                            });

            builder.setView(view);
        return builder.create();
    }

    public static interface OnDialogCompleteListener {
        public abstract void onDialogComplete(SettingsModel settings);
    }

    private OnDialogCompleteListener dialogListener;
}
