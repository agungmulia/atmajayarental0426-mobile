package com.agungmuliaekoputra.atmajayarental_0426.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.agungmuliaekoputra.atmajayarental_0426.AddRatingActivity;
import com.agungmuliaekoputra.atmajayarental_0426.R;
import com.agungmuliaekoputra.atmajayarental_0426.TampilTransaksiActivity;
import com.agungmuliaekoputra.atmajayarental_0426.models.Transaksi;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DriverAdapter extends
        RecyclerView.Adapter<DriverAdapter.ViewHolder>
        implements Filterable {
    private List<Transaksi>transaksiList, filteredTransaksiList;
    private Context context;
    public DriverAdapter(List<Transaksi> transaksiList, Context context) {
        this.transaksiList = transaksiList;
        filteredTransaksiList = new ArrayList<>(transaksiList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_driver, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Locale localeID = new Locale("in", "ID");
        Transaksi transaksi = filteredTransaksiList.get(position);

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
        holder.tvNamaMobil.setText(transaksi.getNAMA_MOBIL());
        holder.tvNamaCustomer.setText(transaksi.getNAMA_CUSTOMER());
        holder.tvTanggalMulai.setText(tanggalMulai);
        holder.tvTanggalSelesai.setText(tanggalSelesai);
        if (transaksi.getRATING_DRIVER() == null){
            holder.tvRating.setText("Belum ada rating");
        }else{
            holder.tvRating.setText(transaksi.getRATING_DRIVER());
        }

        Glide.with(context)
                .load(transaksi.getFOTO_MOBIL())
                .placeholder(R.drawable.no_image)
                .into(holder.ivFotoMobil);
    }
    static String printCurrency(Locale locale, float value){
        NumberFormat formatter=NumberFormat.getCurrencyInstance(locale);
        return formatter.format(value);
    }

    @Override
    public int getItemCount() {
        return filteredTransaksiList.size();
    }
    public void setTransaksiList(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
        filteredTransaksiList = new ArrayList<>(transaksiList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Transaksi> filtered = new ArrayList<>();
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(transaksiList);
                } else {
                    for (Transaksi transaksi : transaksiList) {
                        if (transaksi.getNAMA_MOBIL().toLowerCase()
                                .contains(charSequenceString.toLowerCase()))
                            filtered.add(transaksi);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults
                    filterResults) {
                filteredTransaksiList.clear();
                filteredTransaksiList.addAll((List<Transaksi>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMobil, tvNamaCustomer,tvTanggalMulai,tvTanggalSelesai,tvRating;
        ImageView ivFotoMobil;
        CardView cvDriver;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoMobil = itemView.findViewById(R.id.iv_gambar_mobil);
            tvTanggalMulai = itemView.findViewById(R.id.tv_tanggal_mulai);
            tvTanggalSelesai = itemView.findViewById(R.id.tv_tanggal_selesai);
            tvNamaCustomer = itemView.findViewById(R.id.tv_nama_customer);
            tvNamaMobil = itemView.findViewById(R.id.tv_nama_mobil);
            cvDriver = itemView.findViewById(R.id.cv_driver);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }
    }


}
