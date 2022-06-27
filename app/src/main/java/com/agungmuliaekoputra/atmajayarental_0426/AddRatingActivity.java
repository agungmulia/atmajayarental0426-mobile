package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agungmuliaekoputra.atmajayarental_0426.api.ApiClient;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiInterface;
import com.agungmuliaekoputra.atmajayarental_0426.models.Transaksi;
import com.agungmuliaekoputra.atmajayarental_0426.models.TransaksiResponse;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRatingActivity extends AppCompatActivity {
    private EditText etRatingAjr, etRatingDriver;
    TextView tvNama, tvTotalPembayaran, tvKodePromo,tvPersentase,tvTanggalMulai,tvTanggalSelesai,tvNamaDriver;

    TextInputLayout layoutRatingAjr,layoutRatingDriver;
    ImageView ivFotoMobil,ivFotoDriver;
    private LinearLayout layoutLoading;
    private Bitmap bitmap = null;
    private ApiInterface apiService;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static final String session = "Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        etRatingAjr = findViewById(R.id.et_rating_ajr);
        etRatingDriver = findViewById(R.id.et_rating_driver);

        layoutRatingDriver = findViewById(R.id.layout_driver);
        layoutRatingAjr = findViewById(R.id.layout_ajr);

        ivFotoDriver = findViewById(R.id.iv_gambar_driver_rating);
        tvNamaDriver = findViewById(R.id.tv_nama_driver);
        ivFotoMobil = findViewById(R.id.iv_gambar_mobil_rating);
        tvTanggalMulai = findViewById(R.id.tv_tanggal_mulai);
        tvTanggalSelesai = findViewById(R.id.tv_tanggal_selesai);
        tvTotalPembayaran = findViewById(R.id.tv_total_pembayaran);
        tvKodePromo = findViewById(R.id.tv_kode_promo);
        tvPersentase = findViewById(R.id.tv_persentase);
        tvNama = findViewById(R.id.tv_nama_mobil_rating);
        layoutLoading = findViewById(R.id.layout_loading);
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button btnSave = findViewById(R.id.btn_save);
        TextView tvTitle = findViewById(R.id.tv_title);
        int ID_TRANSAKSI = getIntent().getIntExtra("ID_TRANSAKSI", -1);
        tvTitle.setText("Beri Rating");
        getTransaksiById(ID_TRANSAKSI);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransaksi(ID_TRANSAKSI);
            }
        });
    }
    private void updateTransaksi(int ID_TRANSAKSI) {
        // TODO: Tambahkan fungsi untuk mengubah data buku.
        pref = getSharedPreferences(session,MODE_PRIVATE);
        String getToken = pref.getString("getToken","Not Found");
        setLoading(true);

        String ratingAjr = etRatingAjr.getText().toString();
        String ratingDriver = etRatingDriver.getText().toString();
        String totalPembayaran = tvTotalPembayaran.getText().toString();
        Transaksi transaksi = new Transaksi(ratingDriver, ratingAjr);
        Call<TransaksiResponse> call = apiService.updateTransaksi(ID_TRANSAKSI, transaksi,"Bearer "+getToken);
        call.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddRatingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(AddRatingActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(AddRatingActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setLoading(false);
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(AddRatingActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
    }


    private void getTransaksiById(int ID_TRANSAKSI) {
        setLoading(true);
        pref = getSharedPreferences(session,MODE_PRIVATE);
        String getToken = pref.getString("getToken","Not Found");


        Call<TransaksiResponse> call = apiService.getTransaksiById(ID_TRANSAKSI,"Bearer "+getToken);
        call.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response <TransaksiResponse> response) {

                Locale localeID = new Locale("in", "ID");
                if (response.isSuccessful()) {
                    Transaksi transaksi = response.body().getTransaksiList().get(0);
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String tanggal_mulai = transaksi.getTANGGAL_MULAI_SEWA();
                    String tanggal_selesai = transaksi.getTANGGAL_SELESAI_SEWA();

                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = inputFormat.parse(tanggal_mulai);
                        date2 = inputFormat.parse(tanggal_selesai);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String tanggalMulai = outputFormat.format(date1);
                    String tanggalSelesai = outputFormat.format(date2);

                    tvNama.setText(transaksi.getNAMA_MOBIL());
                    if (transaksi.getKODE_PROMO() == null){
                        tvKodePromo.setVisibility(View.GONE);
                    }else{
                        tvKodePromo.setText(transaksi.getKODE_PROMO()+"-"+transaksi.getPERSENTASE()+"%");
                    }

                    tvTotalPembayaran.setText(printCurrency(localeID,transaksi.getTOTAL_PEMBAYARAN()));
                    tvTanggalMulai.setText(tanggalMulai+" - ");
                    tvTanggalSelesai.setText(tanggalSelesai);
                    tvNamaDriver.setText(transaksi.getNAMA_DRIVER());

                    if(transaksi.getNAMA_DRIVER() == null){
                        etRatingDriver.setVisibility(View.GONE);
                        ivFotoDriver.setVisibility(View.GONE);
                        layoutRatingDriver.setVisibility(View.GONE);
                    }

                    Glide.with(AddRatingActivity.this)
                            .load(transaksi.getFOTO_MOBIL()).into(ivFotoMobil);
                    Glide.with(AddRatingActivity.this)
                            .load(transaksi.getFOTO_DRIVER()).into(ivFotoDriver);

                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(AddRatingActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                setLoading(false);
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(AddRatingActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
    }

    static String printCurrency(Locale locale, float value){
        NumberFormat formatter=NumberFormat.getCurrencyInstance(locale);
        return formatter.format(value);
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }
    }

}