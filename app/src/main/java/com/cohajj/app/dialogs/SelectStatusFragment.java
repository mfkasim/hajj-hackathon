package com.cohajj.app.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cohajj.app.R;

public class SelectStatusFragment extends DialogFragment implements View.OnClickListener {

    View txtFire, txtHealth, txtCrowd, txtslowDown;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_feedback, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setTitle(R.string.has_feedback);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtCrowd = view.findViewById(R.id.txt_good);
        txtslowDown = view.findViewById(R.id.txt_tutrle);
        txtFire = view.findViewById(R.id.txt_fire);
        txtHealth = view.findViewById(R.id.txt_plus);


        txtCrowd.setOnClickListener(this);
        txtslowDown.setOnClickListener(this);
        txtHealth.setOnClickListener(this);
        txtFire.setOnClickListener(this);

    }

    View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener listener) {
        onClickListener = listener;


    }

    @Override
    public void onClick(View view) {
        if (onClickListener != null)
            onClickListener.onClick(view);

        dismiss();
    }
}
