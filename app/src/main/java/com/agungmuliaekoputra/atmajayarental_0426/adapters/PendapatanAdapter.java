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
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class PendapatanAdapter extends
        RecyclerView.Adapter<PendapatanAdapter.ViewHolder>
        implements Filterable {
    private List<Transaksi>transaksiList, filteredTransaksiList;
    private Context context;
    public PendapatanAdapter(List<Transaksi> transaksiList, Context context) {
        this.transaksiList = transaksiList;
        filteredTransaksiList = new ArrayList<>(transaksiList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_pendapatan, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaksi transaksi = filteredTransaksiList.get(position);

        Locale localeID = new Locale("in", "ID");

        holder.tvNamaCustomer.setText(transaksi.getNAMA_CUSTOMER());
        holder.tvJenisTransaksi.setText(transaksi.getJENIS_TRANSAKSI());
        holder.tvJumlahTransaksi.setText(transaksi.getJUMLAH_TRANSAKSI());
        holder.tvNamaMobil.setText(transaksi.getNAMA_MOBIL());
        holder.tvPendapatan.setText(printCurrency(localeID,Float.parseFloat(transaksi.getPENDAPATAN())));
        Glide.with(context)
                .load(transaksi.getFOTO_CUSTOMER())
                .placeholder(R.drawable.no_image)
                .into(holder.ivFotoCustomer);
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
                        if (transaksi.getNAMA_CUSTOMER().toLowerCase()
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
        TextView tvJenisTransaksi, tvNamaCustomer,tvJumlahTransaksi,tvNamaMobil,tvPendapatan;
        CardView cvPendapatan;
        ImageView ivFotoCustomer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoCustomer = itemView.findViewById(R.id.iv_gambar_customer);
            tvJenisTransaksi = itemView.findViewById(R.id.tv_jenis_transaksi);
            tvJumlahTransaksi = itemView.findViewById(R.id.tv_jumlah_transaksi);
            tvNamaCustomer = itemView.findViewById(R.id.tv_nama_customer);
            tvNamaMobil = itemView.findViewById(R.id.tv_nama_mobil);
            tvPendapatan = itemView.findViewById(R.id.tv_pendapatan);
            cvPendapatan = itemView.findViewById(R.id.cv_pendapatans);
        }
    }


}
