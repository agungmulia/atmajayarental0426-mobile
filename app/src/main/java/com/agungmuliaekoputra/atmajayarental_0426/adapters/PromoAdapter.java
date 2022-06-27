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

public class PromoAdapter extends
        RecyclerView.Adapter<PromoAdapter.ViewHolder>
        implements Filterable {
    private List<Transaksi>transaksiList, filteredTransaksiList;
    private Context context;
    public PromoAdapter(List<Transaksi> transaksiList, Context context) {
        this.transaksiList = transaksiList;
        filteredTransaksiList = new ArrayList<>(transaksiList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_promo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Locale localeID = new Locale("in", "ID");
        Transaksi transaksi = filteredTransaksiList.get(position);

        holder.tvKodePromo.setText(transaksi.getKODE_PROMO()+" - "+transaksi.getPERSENTASE()+"%");
        holder.tvKeteranganPromo.setText(transaksi.getKETERANGAN_PROMO());
        holder.tvJenisPromo.setText(transaksi.getJENIS_PROMO());
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
                        if (transaksi.getKODE_PROMO().toLowerCase()
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
        TextView tvKodePromo,tvKeteranganPromo,tvJenisPromo;
        CardView cvPromo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJenisPromo = itemView.findViewById(R.id.tv_jenis_promo);
            tvKeteranganPromo = itemView.findViewById(R.id.tv_keterangan);
            tvKodePromo = itemView.findViewById(R.id.tv_nama_promo);
            cvPromo = itemView.findViewById(R.id.cv_promo);
        }
    }


}
