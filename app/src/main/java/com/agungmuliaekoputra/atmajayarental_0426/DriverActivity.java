package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.agungmuliaekoputra.atmajayarental_0426.R;
import com.agungmuliaekoputra.atmajayarental_0426.adapters.DriverAdapter;
import com.agungmuliaekoputra.atmajayarental_0426.adapters.TransaksiAdapter;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiClient;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiInterface;
import com.agungmuliaekoputra.atmajayarental_0426.models.TransaksiResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

public class DriverActivity extends AppCompatActivity {
    public static final int LAUNCH_ADD_ACTIVITY = 123;
    private SwipeRefreshLayout srDriver;
    private DriverAdapter adapter;
    private ApiInterface apiService;
    private SearchView svDriver;
    private LinearLayout layoutLoading;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static final String session = "Session";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Toast.makeText(DriverActivity.this, "Home", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profil_driver:
                Intent moveActivity = new Intent(DriverActivity.this, ProfilDriverActivity.class);
                startActivity(moveActivity);
                return true;
            case R.id.logout_driver:
                Intent logout = new Intent(DriverActivity.this, LoginActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        layoutLoading = findViewById(R.id.layout_loading);
        srDriver = findViewById(R.id.sr_driver);
        svDriver = findViewById(R.id.sv_driver);
        srDriver.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTransaksi();
            }
        });
        svDriver.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        RecyclerView rvTransaksi = findViewById(R.id.rv_driver);
        adapter = new DriverAdapter(new ArrayList<>(), this);
        rvTransaksi.setLayoutManager(new LinearLayoutManager(DriverActivity.this,
                LinearLayoutManager.VERTICAL, false));
        rvTransaksi.setAdapter(adapter);
        getAllTransaksi();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getAllTransaksi() {
        pref = getSharedPreferences(session,MODE_PRIVATE);
        String getEmail = pref.getString("getEmail","Not Found");
        String getPassword = pref.getString("getPassword","Not Found");
        String getToken = pref.getString("getToken","Not Found");
        String getId = pref.getString("getId","Not Found");


        Call<TransaksiResponse> call = apiService.getTransaksiByDriver(Integer.parseInt(getId),"Bearer "+getToken);
        srDriver.setRefreshing(true);
        call.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call,
                                   Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    adapter.setTransaksiList(response.body().getTransaksiList());
                    adapter.getFilter().filter(svDriver.getQuery());
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(DriverActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(DriverActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                srDriver.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(DriverActivity.this, "Network error",
                        Toast.LENGTH_SHORT).show();
                srDriver.setRefreshing(false);
            }
        });
    }
    // Fungsi untuk menampilkan layout loading
    private void setLoading(boolean isLoading) {
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.GONE);
        }
    }
}