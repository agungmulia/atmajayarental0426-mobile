package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.agungmuliaekoputra.atmajayarental_0426.R;
import com.agungmuliaekoputra.atmajayarental_0426.adapters.PenyewaanAdapter;
import com.agungmuliaekoputra.atmajayarental_0426.adapters.TransaksiAdapter;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiClient;
import com.agungmuliaekoputra.atmajayarental_0426.api.ApiInterface;
import com.agungmuliaekoputra.atmajayarental_0426.models.Transaksi;
import com.agungmuliaekoputra.atmajayarental_0426.models.TransaksiResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

public class LaporanPenyewaanActivity extends AppCompatActivity {
    public static final int LAUNCH_ADD_ACTIVITY = 123;
    private SwipeRefreshLayout srPenyewaan;
    private PenyewaanAdapter adapter;
    private ApiInterface apiService;
    private SearchView svPenyewaan;
    private LinearLayout layoutLoading;
    private Button btnPenyewaan;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static final String session = "Session";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penyewaan);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        layoutLoading = findViewById(R.id.layout_loading);
        srPenyewaan = findViewById(R.id.sr_penyewaan);
        svPenyewaan = findViewById(R.id.sv_penyewaan);
        btnPenyewaan = findViewById((R.id.btn_penyewaan));
        srPenyewaan.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTransaksi();
            }
        });
        svPenyewaan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        RecyclerView rvPenyewaan = findViewById(R.id.rv_penyewaan);
        adapter = new PenyewaanAdapter(new ArrayList<>(), this);
        rvPenyewaan.setLayoutManager(new LinearLayoutManager(LaporanPenyewaanActivity.this,
                LinearLayoutManager.VERTICAL, false));
        rvPenyewaan.setAdapter(adapter);
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


        Call<TransaksiResponse> call = apiService.getPenyewaanMobil("Bearer "+getToken);
        srPenyewaan.setRefreshing(true);
        call.enqueue(new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call,
                                   Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    btnPenyewaan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                cetakPdf(response.body().getTransaksiList());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    adapter.setTransaksiList(response.body().getTransaksiList());
                    adapter.getFilter().filter(svPenyewaan.getQuery());
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(LaporanPenyewaanActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LaporanPenyewaanActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                srPenyewaan.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(LaporanPenyewaanActivity.this, "Network error",
                        Toast.LENGTH_SHORT).show();
                srPenyewaan.setRefreshing(false);
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

    private void cetakPdf(List<Transaksi> transz) throws FileNotFoundException, DocumentException {
 /*
 * Untuk Android 11 nanti file pdf tidak bisa diakses lewat file
manager HP langsung
 * jadi harus konek lewat PC gara gara implementasi Scoped Storage.
 *
 * Kalau mau biar bisa di android 11 bisa pelajari sendiri tentang
penggunaan MediaStorage
 * */
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = "LaporanPenyewaanMobil" + ".pdf";
        File pdfFile = new File(folder.getAbsolutePath(), pdfName);
        FileOutputStream outputStream = new FileOutputStream(pdfFile);
        com.itextpdf.text.Document document = new
                com.itextpdf.text.Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        // bagian header
        Paragraph judul = new Paragraph("Laporan Penyewaan Mobil \n\n",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 16,
                        Font.BOLD, BaseColor.PINK));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        // Buat tabel
        PdfPTable tables = new PdfPTable(new float[]{16, 8});
        // Settingan ukuran tabel
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);
        tables.addCell(cellSupplier);
        Paragraph NomorTanggal = new Paragraph(
                        "Tanggal : " + new SimpleDateFormat("dd/MM/yyyy",
                        Locale.getDefault()).format(currentTime) + "\n",
                new
                        com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                        com.itextpdf.text.Font.NORMAL, BaseColor.BLACK));
        NomorTanggal.setPaddingTop(5);
        tables.addCell(NomorTanggal);
        document.add(tables);
        com.itextpdf.text.Font f = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nBerikut merupakan daftar penyewaan mobil AJR: \n\n", f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5, 5});

        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        // Setup Column
        PdfPCell h1 = new PdfPCell(new Phrase("Nama Mobil"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Tipe Mobil"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Jumlah Peminjaman"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);
        PdfPCell h4 = new PdfPCell(new Phrase("Pendapatan"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);
        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);
        tableHeader.addCell(h4);
        // Beri warna untuk kolumn
        for (PdfPCell cells : tableHeader.getRow(0).getCells()) {
            cells.setBackgroundColor(BaseColor.PINK);
        }
        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5, 5});

        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        // masukan data pegawai jadi baris
        for (Transaksi T : transz) {
            tableData.addCell(T.getNAMA_MOBIL());
            tableData.addCell(T.getJUMLAH_PEMINJAMAN());
            tableData.addCell(T.getTIPE_MOBIL());
            tableData.addCell(T.getPENDAPATAN());
        }
        document.add(tableData);
        com.itextpdf.text.Font h = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL);
        String tglDicetak = currentTime.toLocaleString();
        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);
        document.close();
        previewPdf(pdfFile);
        Toast.makeText(this, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show();
    }
    private void previewPdf(File pdfFile) {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(testIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Uri uri;
            uri = FileProvider.getUriForFile(this, getPackageName() +
                            ".provider",
                    pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            this.grantUriPermission(getPackageName(), uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        }
    }


}