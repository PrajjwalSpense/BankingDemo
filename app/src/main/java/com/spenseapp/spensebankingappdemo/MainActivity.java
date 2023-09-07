package com.spenseapp.spensebankingappdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.spenseapp.spensebankingappdemo.databinding.ActivityMainBinding;
import com.spensesdk.spensebank.SpenseSdk;

public class MainActivity extends AppCompatActivity {

    String token;
    String name, email, phone;
    ActivityMainBinding binding;
    boolean isSessionCreated = false, isLive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);



        getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dashboard_pending_main));

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");

        binding.heading.setText("Hi "+name+",");

        SpenseSdk sdkBankInitiailizer = new SpenseSdk(this,"Demo",getString(R.string.api_key),getString(R.string.secret_key));
        token = sdkBankInitiailizer.createToken("/banking/spense",email,"+91"+phone,name,"");
        sdkBankInitiailizer.getSession(token, response -> {
            Toast.makeText(this, "Session Created", Toast.LENGTH_SHORT).show();
            isSessionCreated = true;
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isSessionCreated) {
                    sdkBankInitiailizer.getLiveStatus(MainActivity.this, response -> {
                        if(response.getBoolean("result")){
                            binding.image.setImageDrawable(getResources().getDrawable(R.drawable.live));
                            binding.topLayout.setBackground(getResources().getDrawable(R.color.dashboard_live_main));
                            binding.secondaryCard.setBackground(getResources().getDrawable(R.color.dashboard_live_secondary));
                            getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dashboard_live_main));
                            isLive = true;
                            sdkBankInitiailizer.getPassbookBalance(MainActivity.this, response1 -> {
                                System.out.println("passbook balance : "+response1);
                                if(response1.has("balance")){
                                    binding.balance.setText(getString(R.string.amount,String.valueOf(response1.getDouble("balance"))));
                                }
                            });
                            Toast.makeText(MainActivity.this, "Banking is Live.", Toast.LENGTH_SHORT);
                        }
                        else {
                            binding.image.setImageDrawable(getResources().getDrawable(R.drawable.live));
                            binding.topLayout.setBackground(getResources().getDrawable(R.color.dashboard_pending_main));
                            binding.secondaryCard.setBackground(getResources().getDrawable(R.color.dashboard_pending_secondary));
                            getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dashboard_pending_main));
                            isLive = false;
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Error Creating session. Please try again after sometime.", Toast.LENGTH_SHORT);
                }
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        binding.image.setOnClickListener(view -> {
            if(isLive)
                sdkBankInitiailizer.open("/banking/spense",email,"+91"+phone,name,"");
            else
                Toast.makeText(MainActivity.this, "Banking coming soon.", Toast.LENGTH_SHORT);

        });



    }
}