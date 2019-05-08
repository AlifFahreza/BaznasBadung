package com.baznas.badung;

public class Data {
    private String id_zis, nama_zis;

    public Data() {
    }

    public Data(String id_zis, String nama_zis) {
        this.id_zis = id_zis;
        this.nama_zis = nama_zis;
    }

    public String getId_zis() {
        return id_zis;
    }

    public void setId_zis(String id_zis) {
        this.id_zis = id_zis;
    }

    public String getNama_zis() {
        return nama_zis;
    }

    public void setNama_zis(String nama_zis) {
        this.nama_zis = nama_zis;
    }
}
