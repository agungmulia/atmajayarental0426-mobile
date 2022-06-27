package com.agungmuliaekoputra.atmajayarental_0426.models;

public class Driver {
    private int ID_DRIVER;
    private String NAMA_DRIVER;
    private String ALAMAT_DRIVER;
    private String JENIS_KELAMIN_DRIVER;
    private String EMAIL_DRIVER;
    private String PASSWORD_DRIVER;
    private String NO_TELP_DRIVER;
    private String FOTO_DRIVER;
    private String RATING;


    public Driver(String NAMA_DRIVER, String ALAMAT_DRIVER, String EMAIL_DRIVER, String PASSWORD_DRIVER, String NO_TELP_DRIVER, String FOTO_DRIVER) {
        this.NAMA_DRIVER = NAMA_DRIVER;
        this.ALAMAT_DRIVER = ALAMAT_DRIVER;
        this.EMAIL_DRIVER = EMAIL_DRIVER;
        this.PASSWORD_DRIVER = PASSWORD_DRIVER;
        this.NO_TELP_DRIVER = NO_TELP_DRIVER;
        this.FOTO_DRIVER = FOTO_DRIVER;
    }

    public int getID_DRIVER() {
        return ID_DRIVER;
    }

    public void setID_DRIVER(int ID_DRIVER) {
        this.ID_DRIVER = ID_DRIVER;
    }

    public String getNAMA_DRIVER() {
        return NAMA_DRIVER;
    }

    public void setNAMA_DRIVER(String NAMA_DRIVER) {
        this.NAMA_DRIVER = NAMA_DRIVER;
    }

    public String getALAMAT_DRIVER() {
        return ALAMAT_DRIVER;
    }

    public void setALAMAT_DRIVER(String ALAMAT_DRIVER) {
        this.ALAMAT_DRIVER = ALAMAT_DRIVER;
    }

    public String getJENIS_KELAMIN_DRIVER() {
        return JENIS_KELAMIN_DRIVER;
    }

    public void setJENIS_KELAMIN_DRIVER(String JENIS_KELAMIN_DRIVER) {
        this.JENIS_KELAMIN_DRIVER = JENIS_KELAMIN_DRIVER;
    }

    public String getEMAIL_DRIVER() {
        return EMAIL_DRIVER;
    }

    public void setEMAIL_DRIVER(String EMAIL_DRIVER) {
        this.EMAIL_DRIVER = EMAIL_DRIVER;
    }

    public String getPASSWORD_DRIVER() {
        return PASSWORD_DRIVER;
    }

    public void setPASSWORD_DRIVER(String PASSWORD_DRIVER) {
        this.PASSWORD_DRIVER = PASSWORD_DRIVER;
    }

    public String getNO_TELP_DRIVER() {
        return NO_TELP_DRIVER;
    }

    public void setNO_TELP_DRIVER(String NO_TELP_DRIVER) {
        this.NO_TELP_DRIVER = NO_TELP_DRIVER;
    }

    public String getFOTO_DRIVER() {
        return FOTO_DRIVER;
    }

    public void setFOTO_DRIVER(String FOTO_DRIVER) {
        this.FOTO_DRIVER = FOTO_DRIVER;
    }

    public String getRATING() {
        return RATING;
    }

    public void setRATING(String RATING) {
        this.RATING = RATING;
    }
}
