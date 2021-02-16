package anamapp.pro.belajar.models;

public class MahasiwaModel {

    private String nim;
    private String nama;

    public MahasiwaModel(String nim, String nama) {
        this.nim = nim;
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return nama;
    }

    public boolean contains(String text) {
        text = text.toLowerCase();
        return nama.toLowerCase().contains(text) || nim.toLowerCase().contains(text);
    }
}
