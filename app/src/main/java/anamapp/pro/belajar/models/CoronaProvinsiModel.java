package anamapp.pro.belajar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoronaProvinsiModel {

    @SerializedName("FID")
    @Expose
    private Integer fID;
    @SerializedName("Kode_Provi")
    @Expose
    private Integer kodeProvi;
    @SerializedName("Provinsi")
    @Expose
    private String provinsi;
    @SerializedName("Kasus_Posi")
    @Expose
    private Integer kasusPosi;
    @SerializedName("Kasus_Semb")
    @Expose
    private Integer kasusSemb;
    @SerializedName("Kasus_Meni")
    @Expose
    private Integer kasusMeni;

    /**
     * No args constructor for use in serialization
     */
    public CoronaProvinsiModel() {
    }

    /**
     * @param fID
     * @param provinsi
     * @param kasusMeni
     * @param kodeProvi
     * @param kasusSemb
     * @param kasusPosi
     */
    public CoronaProvinsiModel(Integer fID, Integer kodeProvi, String provinsi, Integer kasusPosi, Integer kasusSemb, Integer kasusMeni) {
        super();
        this.fID = fID;
        this.kodeProvi = kodeProvi;
        this.provinsi = provinsi;
        this.kasusPosi = kasusPosi;
        this.kasusSemb = kasusSemb;
        this.kasusMeni = kasusMeni;
    }

    public Integer getFID() {
        return fID;
    }

    public void setFID(Integer fID) {
        this.fID = fID;
    }

    public Integer getKodeProvi() {
        return kodeProvi;
    }

    public void setKodeProvi(Integer kodeProvi) {
        this.kodeProvi = kodeProvi;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public Integer getKasusPosi() {
        return kasusPosi;
    }

    public void setKasusPosi(Integer kasusPosi) {
        this.kasusPosi = kasusPosi;
    }

    public Integer getKasusSemb() {
        return kasusSemb;
    }

    public void setKasusSemb(Integer kasusSemb) {
        this.kasusSemb = kasusSemb;
    }

    public Integer getKasusMeni() {
        return kasusMeni;
    }

    public void setKasusMeni(Integer kasusMeni) {
        this.kasusMeni = kasusMeni;
    }

    public boolean contains(String text) {
        text = text.toLowerCase();
        return provinsi.toLowerCase().contains(text);
    }

}
