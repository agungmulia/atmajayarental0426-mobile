package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.agungmuliaekoputra.atmajayarental_0426.R;
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

public class TampilTransaksiActivity extends AppCompatActivity {
    public static final int LAUNCH_ADD_ACTIVITY = 123;
    private SwipeRefreshLayout srTransaksi;
    private TransaksiAdapter adapter;
    private ApiInterface apiService;
    private SearchView svTransaksi;
    private LinearLayout layoutLoading;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static final String session = "Session";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_customer:
                Toast.makeText(TampilTransaksiActivity.this, "Home", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.promo:
                Intent moveActivity = new Intent(TampilTransaksiActivity.this, PromoActivity.class);
                startActivity(moveActivity);
                return true;

            case R.id.brosur:
                Intent brosur = new Intent(TampilTransaksiActivity.this, brosurActivity.class);
                startActivity(brosur);
                return true;

            case R.id.logout_customer:
                Intent logout = new Intent(TampilTransaksiActivity.this, LoginActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_transaksi);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        layoutLoading = findViewById(R.id.layout_loading);
        srTransaksi = findViewById(R.id.sr_transaksi);
        svTransaksi = findViewById(R.id.sv_transaksi);
        srTransaksi.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTransaksi();
            }
        });
        svTransaksi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        RecyclerView rvTransaksi = findViewById(R.id.rv_transaksi);
        adapter = new TransaksiAdapter(new ArrayList<>(), this);
        rvTransaksi.setLayoutManager(new LinearLayoutManager(TampilTransaksiActivity.this,
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
        String getId = pref.getString("getId","Not Found");
        String getToken = pref.getString("getToken","Not Found");


        Call<TransaksiResponse> call = apiService.getTransaksiByCustomer(Integer.parseInt(getId),"Bearer "+getToken);
        srTransaksi.setRefreshing(true);
        call.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call,
                                   Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    adapter.setTransaksiList(response.body().getTransaksiList());
                    adapter.getFilter().filter(svTransaksi.getQuery());
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(TampilTransaksiActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(TampilTransaksiActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                srTransaksi.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(TampilTransaksiActivity.this, "Network error",
                        Toast.LENGTH_SHORT).show();
                srTransaksi.setRefreshing(false);
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