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

public class TransaksiAdapter extends
        RecyclerView.Adapter<TransaksiAdapter.ViewHolder>
        implements Filterable {
    private List<Transaksi>transaksiList, filteredTransaksiList;
    private Context context;
    public TransaksiAdapter(List<Transaksi> transaksiList, Context context) {
        this.transaksiList = transaksiList;
        filteredTransaksiList = new ArrayList<>(transaksiList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Locale localeID = new Locale("in", "ID");
        Transaksi transaksi = filteredTransaksiList.get(position);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
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
        holder.tvNama.setText(transaksi.getNAMA_MOBIL());
        holder.tvTanggalMulai.setText(tanggalMulai);
        holder.tvTanggalSelesai.setText(tanggalSelesai);
        holder.tvTotalPembayaran.setText(printCurrency(localeID,transaksi.getTOTAL_PEMBAYARAN()));

        if(transaksi.getKODE_PROMO() == null){
            holder.tvKodePromo.setVisibility(View.GONE);
            holder.tvPersentase.setVisibility(View.GONE);
        }else{
            holder.tvKodePromo.setText(transaksi.getKODE_PROMO() + " - " + transaksi.getPERSENTASE()+"%");
        }

        if(transaksi.getNAMA_DRIVER() == null){
            holder.tvDriver.setVisibility(View.GONE);
            holder.ivFotoDriver.setVisibility(View.GONE);
            holder.tvRatingDriver.setVisibility(View.GONE);
            holder.tvDriverCapt.setVisibility(View.GONE);
        }else{
            holder.tvDriver.setText(transaksi.getNAMA_DRIVER());
            Glide.with(context)
                    .load(transaksi.getFOTO_DRIVER())
                    .placeholder(R.drawable.no_image)
                    .into(holder.ivFotoDriver);
        }




        if (transaksi.getRATING_AJR() == null   && transaksi.getNAMA_DRIVER() == null){
            holder.btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AddRatingActivity.class);
                    i.putExtra("ID_TRANSAKSI", transaksi.getID_TRANSAKSI());
                    if (context instanceof TampilTransaksiActivity)
                        ((TampilTransaksiActivity) context).startActivityForResult(i,
                                TampilTransaksiActivity.LAUNCH_ADD_ACTIVITY);
                }
            });
            holder.tvRatingAjr.setVisibility(View.GONE);
            holder.tvRatingDriver.setVisibility(View.GONE);
        }else if(transaksi.getNAMA_DRIVER() != null){
            if(transaksi.getRATING_DRIVER() != null && transaksi.getRATING_AJR() != null){
                holder.tvRatingDriver.setText("Rating Driver : "+transaksi.getRATING_DRIVER());
                holder.tvRatingAjr.setText("Rating AJR : "+transaksi.getRATING_AJR());
                holder.btnRating.setVisibility(View.GONE);
            }else if(transaksi.getRATING_AJR() != null && transaksi.getRATING_DRIVER() == null){
                holder.btnRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, AddRatingActivity.class);
                        i.putExtra("ID_TRANSAKSI", transaksi.getID_TRANSAKSI());
                        if (context instanceof TampilTransaksiActivity)
                            ((TampilTransaksiActivity) context).startActivityForResult(i,
                                    TampilTransaksiActivity.LAUNCH_ADD_ACTIVITY);
                    }
                });
                holder.tvRatingDriver.setVisibility(View.GONE);
                holder.tvRatingAjr.setText("Rating AJR : "+transaksi.getRATING_AJR());
            }else if(transaksi.getRATING_AJR() == null && transaksi.getRATING_DRIVER() != null){
                holder.btnRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, AddRatingActivity.class);
                        i.putExtra("ID_TRANSAKSI", transaksi.getID_TRANSAKSI());
                        if (context instanceof TampilTransaksiActivity)
                            ((TampilTransaksiActivity) context).startActivityForResult(i,
                                    TampilTransaksiActivity.LAUNCH_ADD_ACTIVITY);
                    }
                });
                holder.tvRatingDriver.setText("Rating Driver : "+transaksi.getRATING_DRIVER());
                holder.tvRatingAjr.setVisibility(View.GONE);
            }else if(transaksi.getRATING_AJR() != null && transaksi.getNAMA_DRIVER() == null){
                holder.tvRatingDriver.setVisibility(View.GONE);
                holder.tvRatingAjr.setVisibility(View.GONE);
                holder.btnRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, AddRatingActivity.class);
                        i.putExtra("ID_TRANSAKSI", transaksi.getID_TRANSAKSI());
                        if (context instanceof TampilTransaksiActivity)
                            ((TampilTransaksiActivity) context).startActivityForResult(i,
                                    TampilTransaksiActivity.LAUNCH_ADD_ACTIVITY);
                    }
                });
            }else{
                holder.tvRatingDriver.setVisibility(View.GONE);
                holder.tvRatingAjr.setVisibility(View.GONE);
                holder.btnRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, AddRatingActivity.class);
                        i.putExtra("ID_TRANSAKSI", transaksi.getID_TRANSAKSI());
                        if (context instanceof TampilTransaksiActivity)
                            ((TampilTransaksiActivity) context).startActivityForResult(i,
                                    TampilTransaksiActivity.LAUNCH_ADD_ACTIVITY);
                    }
                });
            }
        }else if(transaksi.getRATING_AJR() != null && transaksi.getNAMA_DRIVER() == null){
            holder.btnRating.setVisibility(View.GONE);
            holder.tvRatingDriver.setVisibility(View.GONE);
            holder.tvRatingAjr.setText("Rating AJR : "+transaksi.getRATING_AJR());
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
        TextView tvNama, tvTotalPembayaran, tvKodePromo,tvPersentase,tvTanggalMulai,tvTanggalSelesai,tvDriver,tvDriverCapt,tvRatingAjr,tvRatingDriver;
        ImageView ivFotoMobil,ivFotoDriver;
        CardView cvTransaksi;
        Button btnRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoMobil = itemView.findViewById(R.id.iv_gambar_mobil);
            tvTanggalSelesai = itemView.findViewById(R.id.tv_tanggal_selesai);
            tvTanggalMulai = itemView.findViewById(R.id.tv_tanggal_mulai);
            tvTotalPembayaran = itemView.findViewById(R.id.tv_total_pembayaran);
            tvKodePromo = itemView.findViewById(R.id.tv_kode_promo);
            tvPersentase = itemView.findViewById(R.id.tv_persentase);
            tvNama = itemView.findViewById(R.id.tv_title);
            cvTransaksi = itemView.findViewById(R.id.cv_transaksi);
            btnRating = itemView.findViewById(R.id.btn_rating);
            tvDriver = itemView.findViewById(R.id.tv_nama_driver);
            ivFotoDriver = itemView.findViewById(R.id.iv_gambar_driver);
            tvRatingAjr = itemView.findViewById(R.id.tv_rating_ajr);
            tvRatingDriver = itemView.findViewById(R.id.tv_rating_driver);
            tvDriverCapt = itemView.findViewById(R.id.tv_driverCapt);
        }
    }


}
