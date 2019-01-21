package com.voicecontroller.voicecontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText addressText = findViewById(R.id.etNetAddress);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String stringAddress = sharedPreferences.getString("IP_ADDRESS","192.168.43.236");
        addressText.setText(stringAddress);

        Button saveButton = findViewById(R.id.btSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("IP_ADDRESS", addressText.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(), "Saved",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
