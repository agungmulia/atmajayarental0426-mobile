package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
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
import com.agungmuliaekoputra.atmajayarental_0426.models.Driver;
import com.agungmuliaekoputra.atmajayarental_0426.models.DriverResponse;
import com.agungmuliaekoputra.atmajayarental_0426.models.Transaksi;
import com.agungmuliaekoputra.atmajayarental_0426.models.TransaksiResponse;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilDriverActivity extends AppCompatActivity {
    private EditText etNamaDriver, etAlamatDriver,etPasswordDriver,etEmailDriver,etNoTelpDriver;
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;
    TextView tvRataRating, tvTotalPembayaran, tvKodePromo,tvPersentase,tvTanggalMulai,tvTanggalSelesai,tvNamaDriver;

    AlertDialog.Builder builder;

    TextInputLayout layoutRatingAjr,layoutRatingDriver;
    ImageView ivFotoDriver;
    private LinearLayout layoutLoading;
    private Bitmap bitmap = null;
    private ApiInterface apiService;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static final String session = "Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_driver);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        etNamaDriver = findViewById(R.id.et_nama_driver);
        etEmailDriver = findViewById(R.id.edt_email);
        etAlamatDriver = findViewById(R.id.et_alamat_driver);
        etNoTelpDriver = findViewById(R.id.et_no_telp);
        etPasswordDriver = findViewById(R.id.edt_password);
        builder = new AlertDialog.Builder(this);
        ivFotoDriver = findViewById(R.id.iv_gambar);
        tvRataRating = findViewById(R.id.tv_rata_rating);

        layoutLoading = findViewById(R.id.layout_loading);

        ivFotoDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        LayoutInflater.from(ProfilDriverActivity.this);
                View selectMediaView = layoutInflater
                        .inflate(R.layout.layout_select_media, null);
                final AlertDialog alertDialog = new AlertDialog
                        .Builder(selectMediaView.getContext()).create();
                Button btnKamera = selectMediaView.findViewById(R.id.btn_kamera);
                Button btnGaleri = selectMediaView.findViewById(R.id.btn_galeri);
                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) ==
                                PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA};
                            requestPermissions(permission, PERMISSION_REQUEST_CAMERA);
                        } else {
                            // Membuka kamera
                            Intent intent =
                                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        }
                        alertDialog.dismiss();
                    }
                });
                btnGaleri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Membuka galeri
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_PICTURE);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(selectMediaView);
                alertDialog.show();
            }
        });


        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button btnSave = findViewById(R.id.btn_save);
        TextView tvTitle = findViewById(R.id.tv_title);
        pref = getSharedPreferences(session,MODE_PRIVATE);
        String getId = pref.getString("getId","Not Found");
        tvTitle.setText("Profil");
        getTransaksiById(Integer.parseInt(getId));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Perhatian!")
                        .setMessage("Apakah anda yakin akan mengupdate profil?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateTransaksi(Integer.parseInt(getId));
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions,
             @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager
                    .PERMISSION_GRANTED) {
                // Membuka kamera
                Intent intent = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAMERA_REQUEST);
            } else {
                Toast.makeText(ProfilDriverActivity.this,
                        "Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(
            int requestCode, int resultCode,
            @Nullable Intent data) {
        super.onActivityResult(requestCode,
                resultCode, data);
        if (data == null)
            return;
        if (resultCode == RESULT_OK &&
                requestCode == GALLERY_PICTURE) {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream
                        = getContentResolver()
                        .openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(ProfilDriverActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK
                && requestCode == CAMERA_REQUEST) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        bitmap = getResizedBitmap(bitmap, 512);
        ivFotoDriver.setImageBitmap(bitmap);
    }
    private Bitmap getResizedBitmap(
            Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
    private String bitmapToBase64(Bitmap bitmap) {
        // TODO: Tambahkan fungsi untuk mengkonversi bitmap dengan output Base64 string hasil
        //  konversi. Gunakan fungsi ini saat menambah atau mengedit data produk.
        ByteArrayOutputStream byteArrayOutputStream
                = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG
                , 100, byteArrayOutputStream);
        byte[] byteArray
                = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private String cekNull(Bitmap bitmap){
        if(bitmap == null){
            BitmapDrawable drawable = (BitmapDrawable) ivFotoDriver.getDrawable();
            bitmap = drawable.getBitmap();
            return "data:image\\/png;base64,"+bitmapToBase64(bitmap);
        }else {
            return bitmapToBase64(bitmap);
        }
    }
    private void updateTransaksi(int ID_TRANSAKSI) {
        // TODO: Tambahkan fungsi untuk mengubah data buku.
        pref = getSharedPreferences(session,MODE_PRIVATE);
        String getToken = pref.getString("getToken","Not Found");
        setLoading(true);
        String namaDriver = etNamaDriver.getText().toString();
        String emailDriver = etEmailDriver.getText().toString();
        String passwordDriver = etPasswordDriver.getText().toString();
        String alamatDriver = etAlamatDriver.getText().toString();
        String noTelpDriver = etNoTelpDriver.getText().toString();

        String  fotoPalsu = "https://images.unsplash.com/photo-1552642986-ccb41e7059e7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGFuZHNvbWUlMjBtYW58ZW58MHx8MHx8&w=1000&q=80";

        Driver driver = new Driver(namaDriver,alamatDriver,emailDriver,passwordDriver,noTelpDriver,fotoPalsu);
        Call<DriverResponse> call = apiService.updateDriver(ID_TRANSAKSI, driver,"Bearer "+getToken);
        call.enqueue(new Callback<DriverResponse>() {
            @Override
            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfilDriverActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(ProfilDriverActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ProfilDriverActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setLoading(false);
            }
            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                Toast.makeText(ProfilDriverActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
    }


    private void getTransaksiById(int ID_TRANSAKSI) {
        setLoading(true);
        pref = getSharedPreferences(session,MODE_PRIVATE);
        String getToken = pref.getString("getToken","Not Found");


        Call<TransaksiResponse> call = apiService.getDriverById(ID_TRANSAKSI,"Bearer "+getToken);
        call.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response <TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    Transaksi transaksi = response.body().getTransaksiList().get(0);
                    if (transaksi.getRERATA_RATING() == null){
                        tvRataRating.setText("anda belum memiliki rating");
                    }else{
                        tvRataRating.setText(transaksi.getRERATA_RATING());
                    }

                    etNamaDriver.setText(transaksi.getNAMA_DRIVER());
                    etEmailDriver.setText(transaksi.getEMAIL_DRIVER());
                    etAlamatDriver.setText(transaksi.getALAMAT_DRIVER());
                    etNoTelpDriver.setText(transaksi.getNO_TELP_DRIVER());
                    Glide.with(ProfilDriverActivity.this)
                            .load(transaksi.getFOTO_DRIVER()).into(ivFotoDriver);


                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(ProfilDriverActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                setLoading(false);
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(ProfilDriverActivity.this,
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