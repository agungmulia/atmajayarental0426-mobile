package com.agungmuliaekoputra.atmajayarental_0426;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ManagerMenuActivity extends AppCompatActivity {
    CardView cvLaporanPenyewaan,cvTopDriver,cvPerformaDriver,cvTopCustomer,cvPendapatan;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Toast.makeText(ManagerMenuActivity.this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                Intent logout = new Intent(ManagerMenuActivity.this, LoginActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);
        cvTopCustomer = findViewById(R.id.cv_top_customer);
        cvPendapatan = findViewById(R.id.cv_pendapatan);
        cvLaporanPenyewaan = findViewById(R.id.cv_laporanPenyewaan);
        cvTopDriver = findViewById(R.id.cv_top_driver);
        cvPerformaDriver = findViewById(R.id.cv_performa_driver);

        cvLaporanPenyewaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveActivity = new Intent(ManagerMenuActivity.this, LaporanPenyewaanActivity.class);
                startActivity(moveActivity);
            }
        });

        cvTopDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveActivity = new Intent(ManagerMenuActivity.this, TopDriverActivity.class);
                startActivity(moveActivity);
            }
        });
        cvTopCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveActivity = new Intent(ManagerMenuActivity.this, TopCustomerActivity.class);
                startActivity(moveActivity);
            }
        });

        cvPendapatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveActivity = new Intent(ManagerMenuActivity.this, PendapatanActivity.class);
                startActivity(moveActivity);
            }
        });




        cvPerformaDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveActivity = new Intent(ManagerMenuActivity.this, PerformaDriverActivity.class);
                startActivity(moveActivity);
            }
        });

    }
}