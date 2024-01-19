package com.example.newsapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editFirstNameEditText;
    private EditText editLastNameEditText;
    // Add other profile edit fields as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editFirstNameEditText = findViewById(R.id.editFirstNameEditText);
        editLastNameEditText = findViewById(R.id.editLastNameEditText);
        SharedPreferences preferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");
        editFirstNameEditText.setText(firstName);
        editLastNameEditText.setText(lastName);
        Button saveProfileButton = findViewById(R.id.saveProfileButton);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        String editedFirstName = editFirstNameEditText.getText().toString();
        String editedLastName = editLastNameEditText.getText().toString();
        saveEditedProfile(editedFirstName, editedLastName);
    }

    private void saveEditedProfile(String editedFirstName, String editedLastName) {
        SharedPreferences preferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firstName", editedFirstName);
        editor.putString("lastName", editedLastName);
        editor.apply();
        Toast.makeText(EditProfileActivity.this, "Profile saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
        finish();
    }
}
