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
import com.agungmuliaekoputra.atmajayarental_0426.models.Mobil;
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

public class BrosurAdapter extends
        RecyclerView.Adapter<BrosurAdapter.ViewHolder>
        implements Filterable {
    private List<Mobil>mobilList, filteredMobilList;
    private Context context;
    public BrosurAdapter(List<Mobil> mobilList, Context context) {
        this.mobilList = mobilList;
        filteredMobilList = new ArrayList<>(mobilList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_brosur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mobil mobil = filteredMobilList.get(position);
        Locale localeID = new Locale("in", "ID");

        holder.tvNamaMobil.setText(mobil.getNAMA_MOBIL());
        holder.tvJenisBahanBakar.setText(mobil.getJENIS_BAHAN_BAKAR_MOBIL());
        holder.tvTipeMobil.setText(mobil.getTIPE_MOBIL());
        holder.tvFasilitas.setText(mobil.getFASILITAS_MOBIL());
        holder.tvWarnaMobil.setText(mobil.getWARNA_MOBIL());
        holder.tvVolumeBagasi.setText(mobil.getVOLUME_BAGASI_MOBIL());
        holder.tvHargaSewaHarian.setText(printCurrency(localeID,Float.parseFloat(mobil.getHARGA_SEWA_HARIAN_MOBIL())));
        Glide.with(context)
                .load(mobil.getFOTO_MOBIL())
                .placeholder(R.drawable.no_image)
                .into(holder.ivFotoMobil);
    }
    static String printCurrency(Locale locale, float value){
        NumberFormat formatter=NumberFormat.getCurrencyInstance(locale);
        return formatter.format(value);
    }

    @Override
    public int getItemCount() {
        return filteredMobilList.size();
    }
    public void setMobilList(List<Mobil> mobilList) {
        this.mobilList = mobilList;
        filteredMobilList = new ArrayList<>(mobilList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Mobil> filtered = new ArrayList<>();
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(mobilList);
                } else {
                    for (Mobil mobil : mobilList) {
                        if (mobil.getNAMA_MOBIL().toLowerCase()
                                .contains(charSequenceString.toLowerCase()))
                            filtered.add(mobil);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults
                    filterResults) {
                filteredMobilList.clear();
                filteredMobilList.addAll((List<Mobil>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipeMobil, tvJenisTransmisi,tvJenisBahanBakar,tvFasilitas,tvWarnaMobil,tvHargaSewaHarian, tvNamaMobil,tvVolumeBagasi;
        CardView cvPenyewaan;
        ImageView ivFotoMobil;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoMobil = itemView.findViewById(R.id.iv_gambar_mobil);
            tvJenisTransmisi = itemView.findViewById(R.id.tv_jenis_transmisi);
            tvJenisBahanBakar = itemView.findViewById(R.id.tv_jenis_bahan_bakar);
            tvTipeMobil = itemView.findViewById(R.id.tv_tipe_mobil);
            tvNamaMobil = itemView.findViewById(R.id.tv_nama_mobil);
            tvVolumeBagasi = itemView.findViewById(R.id.tv_volume_bagasi);
            tvFasilitas = itemView.findViewById(R.id.tv_fasilitas);
            tvWarnaMobil = itemView.findViewById(R.id.tv_warna_mobil);
            tvHargaSewaHarian = itemView.findViewById(R.id.tv_harga_sewa);
            cvPenyewaan = itemView.findViewById(R.id.cv_laporanPenyewaan);
        }
    }


}
