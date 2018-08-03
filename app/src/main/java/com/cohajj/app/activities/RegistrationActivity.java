package com.cohajj.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cohajj.app.R;
import com.cohajj.app.Utils;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    Button btnLocate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editText = findViewById(R.id.editText);
        btnLocate = findViewById(R.id.btnLocate);
        btnLocate.setOnClickListener(this);
        getSupportActionBar().setTitle(R.string.basic_info);
    }


    @Override
    public void onClick(View view) {
        handleContinueToRoutesScreen();
    }

    private void handleContinueToRoutesScreen() {

        if (editText.getText().toString().isEmpty() || Integer.parseInt(editText.getText().toString())>100) {
//            Toast.makeText(this,R.string.please_enter_a_valid_number,Toast.LENGTH_LONG).show();
            editText.setError(getString(R.string.please_enter_a_valid_number));
        } else {
            editText.setError(null);
            if (Utils.checkLocationServiceAvailablity(this)) {

                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);


            }

        }

    }
}
