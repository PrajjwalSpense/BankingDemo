package com.spenseapp.spensebankingappdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.spenseapp.spensebankingappdemo.databinding.ActivityGetInfoBinding;

public class GetInfoActivity extends AppCompatActivity {

    ActivityGetInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_info);

        binding.saveButton.setOnClickListener(view -> {
            if(binding.userName.getText().toString().isEmpty() || binding.emailEditText.getText().toString().isEmpty() || binding.phoneEditText.getText().toString().isEmpty()){
                Toast.makeText(this,"Please enter the value in all fields", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("name", binding.userName.getText().toString().trim());
                intent.putExtra("email", binding.emailEditText.getText().toString().trim());
                intent.putExtra("phone", binding.phoneEditText.getText().toString().trim());
                startActivity(intent);
            }
        });


    }
}