package com.agungmuliaekoputra.atmajayarental_0426.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.Date;

public class Transaksi {
    private int ID_CUSTOMER;
    private int ID_TRANSAKSI;

    private String RATING_DRIVER;
    private  String RATING_AJR;
    private String KODE_PROMO;
    private String PERSENTASE;
    private String NAMA_MOBIL;
    private String TANGGAL_MULAI_SEWA;
    private String TANGGAL_SELESAI_SEWA;
    private String NAMA_DRIVER;
    private String FOTO_DRIVER;
    private String TIPE_MOBIL;
    private String JUMLAH_PEMINJAMAN;
    private String PENDAPATAN;
    private String JUMLAH_TRANSAKSI;
    private String RERATA_RATING;
    private String FOTO_CUSTOMER;
    private String ALAMAT_CUSTOMER;
    private String JENIS_TRANSAKSI;
    private String JENIS_PROMO;
    private String EMAIL_DRIVER;
    private String NO_TELP_DRIVER;
    private String ALAMAT_DRIVER;

    public String getALAMAT_DRIVER() {
        return ALAMAT_DRIVER;
    }

    public void setALAMAT_DRIVER(String ALAMAT_DRIVER) {
        this.ALAMAT_DRIVER = ALAMAT_DRIVER;
    }

    public String getEMAIL_DRIVER() {
        return EMAIL_DRIVER;
    }

    public void setEMAIL_DRIVER(String EMAIL_DRIVER) {
        this.EMAIL_DRIVER = EMAIL_DRIVER;
    }

    public String getNO_TELP_DRIVER() {
        return NO_TELP_DRIVER;
    }

    public void setNO_TELP_DRIVER(String NO_TELP_DRIVER) {
        this.NO_TELP_DRIVER = NO_TELP_DRIVER;
    }

    public String getJENIS_PROMO() {
        return JENIS_PROMO;
    }

    public void setJENIS_PROMO(String JENIS_PROMO) {
        this.JENIS_PROMO = JENIS_PROMO;
    }

    public String getKETERANGAN_PROMO() {
        return KETERANGAN_PROMO;
    }

    public void setKETERANGAN_PROMO(String KETERANGAN_PROMO) {
        this.KETERANGAN_PROMO = KETERANGAN_PROMO;
    }

    private String KETERANGAN_PROMO;

    public String getJENIS_TRANSAKSI() {
        return JENIS_TRANSAKSI;
    }

    public void setJENIS_TRANSAKSI(String JENIS_TRANSAKSI) {
        this.JENIS_TRANSAKSI = JENIS_TRANSAKSI;
    }

    public String getFOTO_CUSTOMER() {
        return FOTO_CUSTOMER;
    }

    public void setFOTO_CUSTOMER(String FOTO_CUSTOMER) {
        this.FOTO_CUSTOMER = FOTO_CUSTOMER;
    }

    public String getALAMAT_CUSTOMER() {
        return ALAMAT_CUSTOMER;
    }

    public void setALAMAT_CUSTOMER(String ALAMAT_CUSTOMER) {
        this.ALAMAT_CUSTOMER = ALAMAT_CUSTOMER;
    }

    public String getJUMLAH_TRANSAKSI() {
        return JUMLAH_TRANSAKSI;
    }

    public void setJUMLAH_TRANSAKSI(String JUMLAH_TRANSAKSI) {
        this.JUMLAH_TRANSAKSI = JUMLAH_TRANSAKSI;
    }

    public String getRERATA_RATING() {
        return RERATA_RATING;
    }

    public void setRERATA_RATING(String RERATA_RATING) {
        this.RERATA_RATING = RERATA_RATING;
    }

    public String getTIPE_MOBIL() {
        return TIPE_MOBIL;
    }

    public void setTIPE_MOBIL(String TIPE_MOBIL) {
        this.TIPE_MOBIL = TIPE_MOBIL;
    }

    public String getJUMLAH_PEMINJAMAN() {
        return JUMLAH_PEMINJAMAN;
    }

    public void setJUMLAH_PEMINJAMAN(String JUMLAH_PEMINJAMAN) {
        this.JUMLAH_PEMINJAMAN = JUMLAH_PEMINJAMAN;
    }

    public String getPENDAPATAN() {
        return PENDAPATAN;
    }

    public void setPENDAPATAN(String PENDAPATAN) {
        this.PENDAPATAN = PENDAPATAN;
    }

    public String getFOTO_DRIVER() {
        return FOTO_DRIVER;
    }

    public void setFOTO_DRIVER(String FOTO_DRIVER) {
        this.FOTO_DRIVER = FOTO_DRIVER;
    }

    public String getNAMA_DRIVER() {
        return NAMA_DRIVER;
    }

    public void setNAMA_DRIVER(String NAMA_DRIVER) {
        this.NAMA_DRIVER = NAMA_DRIVER;
    }

    private String FOTO_MOBIL;

    public String getTANGGAL_MULAI_SEWA() {
        return TANGGAL_MULAI_SEWA;
    }

    public void setTANGGAL_MULAI_SEWA(String TANGGAL_MULAI_SEWA) {
        this.TANGGAL_MULAI_SEWA = TANGGAL_MULAI_SEWA;
    }

    private String NAMA_CUSTOMER;
    private float TOTAL_PEMBAYARAN;


    public String getTANGGAL_SELESAI_SEWA() {
        return TANGGAL_SELESAI_SEWA;
    }

    public void setTANGGAL_SELESAI_SEWA(String TANGGAL_SELESAI_SEWA) {
        this.TANGGAL_SELESAI_SEWA = TANGGAL_SELESAI_SEWA;
    }

    public String getRATING_DRIVER() {
        return RATING_DRIVER;
    }

    public void setRATING_DRIVER(String RATING_DRIVER) {
        this.RATING_DRIVER = RATING_DRIVER;
    }

    public String getRATING_AJR() {
        return RATING_AJR;
    }

    public void setRATING_AJR(String RATING_AJR) {
        this.RATING_AJR = RATING_AJR;
    }

    public Transaksi(String RATING_DRIVER, String RATING_AJR) {
        this.RATING_DRIVER = RATING_DRIVER;
        this.RATING_AJR = RATING_AJR;
    }

    public String getFOTO_MOBIL() {
        return FOTO_MOBIL;
    }

    public void setFOTO_MOBIL(String FOTO_MOBIL) {
        this.FOTO_MOBIL = FOTO_MOBIL;
    }

    public int getID_CUSTOMER() {
        return ID_CUSTOMER;
    }


    public void setID_CUSTOMER(int ID_CUSTOMER) {
        this.ID_CUSTOMER = ID_CUSTOMER;
    }

    public int getID_TRANSAKSI() {
        return ID_TRANSAKSI;
    }

    public void setID_TRANSAKSI(int ID_TRANSAKSI) {
        this.ID_TRANSAKSI = ID_TRANSAKSI;
    }

    public String getKODE_PROMO() {
        return KODE_PROMO;
    }

    public void setKODE_PROMO(String KODE_PROMO) {
        this.KODE_PROMO = KODE_PROMO;
    }

    public String getPERSENTASE() {
        return PERSENTASE;
    }

    public void setPERSENTASE(String PERSENTASE) {
        this.PERSENTASE = PERSENTASE;
    }

    public String getNAMA_MOBIL() {
        return NAMA_MOBIL;
    }

    public void setNAMA_MOBIL(String NAMA_MOBIL) {
        this.NAMA_MOBIL = NAMA_MOBIL;
    }

    public String getNAMA_CUSTOMER() {
        return NAMA_CUSTOMER;
    }

    public void setNAMA_CUSTOMER(String NAMA_CUSTOMER) {
        this.NAMA_CUSTOMER = NAMA_CUSTOMER;
    }

    public float getTOTAL_PEMBAYARAN() {
        return TOTAL_PEMBAYARAN;
    }

    public void setTOTAL_PEMBAYARAN(float TOTAL_PEMBAYARAN) {
        this.TOTAL_PEMBAYARAN = TOTAL_PEMBAYARAN;
    }
}
