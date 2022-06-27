package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agungmuliaekoputra.atmajayarental_0426.adapters.LoginRequest;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiClient;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiInterface;
import com.agungmuliaekoputra.atmajayarental_0426.models.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://192.168.55.4:8000/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    public static  final  String session = "Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(LoginActivity.this,"Username / Password Required", Toast.LENGTH_LONG).show();
                }else{
                    //proceed to login

                    login();
                }

            }
        });
    }
    private static String token;

    public void login(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email.getText().toString());
        loginRequest.setPassword(password.getText().toString());

        Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Berhasil, anda login sebagai "+response.body().getResponseList().getRole(), Toast.LENGTH_LONG).show();
                    LoginResponse loginResponse = response.body();
                    token = response.body().getToken();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pref = getSharedPreferences(session, MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putString("getEmail",response.body().getResponseList().getEmail());
                            editor.putString("getPassword",response.body().getResponseList().getPassword());
                            editor.putString("getId",Integer.toString(response.body().getResponseList().getId()));
                            editor.putString("getToken",response.body().getToken());

                            editor.apply();
                            if (response.body().getResponseList().getRole().equalsIgnoreCase("customer")){
                                Intent moveActivity = new Intent(LoginActivity.this, TampilTransaksiActivity.class);
                                startActivity(moveActivity);
                            }else if (response.body().getResponseList().getRole().equalsIgnoreCase("driver")){
                                Intent moveActivity = new Intent(LoginActivity.this, DriverActivity.class);
                                startActivity(moveActivity);
                            } else {
                                Intent moveActivity = new Intent(LoginActivity.this, ManagerMenuActivity.class);
                                startActivity(moveActivity);
                            }


                        }
                    },700);
                }else{
                    Toast.makeText(LoginActivity.this,"Email atau Password Salah!", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}