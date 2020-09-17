package com.example.wellspa.model;

public class service {
    private String kode , nama , price,key;

    public service(){

    }

    public service(String kode, String nama, String price) {
        this.kode = kode;
        this.nama = nama;
        this.price = price;
    }


    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
