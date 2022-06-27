package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.agungmuliaekoputra.atmajayarental_0426.adapters.BrosurAdapter;
import com.agungmuliaekoputra.atmajayarental_0426.adapters.PromoAdapter;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiClient;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiInterface;
import com.agungmuliaekoputra.atmajayarental_0426.models.MobilResponse;
import com.agungmuliaekoputra.atmajayarental_0426.models.TransaksiResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class brosurActivity extends AppCompatActivity {
    public static final int LAUNCH_ADD_ACTIVITY = 123;
    private SwipeRefreshLayout srBrosur;
    private BrosurAdapter adapter;
    private ApiInterface apiService;
    private SearchView svBrosur;
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
                Intent home = new Intent(brosurActivity.this, TampilTransaksiActivity.class);
                startActivity(home);
                return true;

            case R.id.promo:
                Intent moveActivity = new Intent(brosurActivity.this, PromoActivity.class);
                startActivity(moveActivity);
                return true;

            case R.id.brosur:
                Toast.makeText(brosurActivity.this, "Anda di halaman brosur", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout_customer:
                Intent logout = new Intent(brosurActivity.this, LoginActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brosur);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        layoutLoading = findViewById(R.id.layout_loading);
        srBrosur = findViewById(R.id.sr_brosur);
        svBrosur = findViewById(R.id.sv_brosur);
        srBrosur.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBrosur();
            }
        });
        svBrosur.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        RecyclerView rvBrosur = findViewById(R.id.rv_brosur);
        adapter = new BrosurAdapter(new ArrayList<>(), this);
        rvBrosur.setLayoutManager(new LinearLayoutManager(brosurActivity.this,
                LinearLayoutManager.VERTICAL, false));
        rvBrosur.setAdapter(adapter);
        getBrosur();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getBrosur() {
        pref = getSharedPreferences(session,MODE_PRIVATE);
        String getEmail = pref.getString("getEmail","Not Found");
        String getId = pref.getString("getId","Not Found");
        String getToken = pref.getString("getToken","Not Found");


        Call<MobilResponse> call = apiService.getBrosur("Bearer "+getToken);
        srBrosur.setRefreshing(true);
        call.enqueue(new Callback<MobilResponse>() {
            @Override
            public void onResponse(Call<MobilResponse> call,
                                   Response<MobilResponse> response) {
                if (response.isSuccessful()) {
                    adapter.setMobilList(response.body().getMobilList());
                    adapter.getFilter().filter(svBrosur.getQuery());
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(brosurActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(brosurActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                srBrosur.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<MobilResponse> call, Throwable t) {
                Toast.makeText(brosurActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                srBrosur.setRefreshing(false);
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