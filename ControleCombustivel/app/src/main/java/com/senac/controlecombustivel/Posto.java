package com.senac.controlecombustivel;

/**
 * Created by Dantieris on 26/04/2015.
 */
public class Posto {
    private int id;
    private String endereco;
    private String nome;
    private double latitude;
    private double longitude;
    private Bandeira bandeira;

    public Posto() {}

    public Posto(int id) {
        this.id = id;
    }

    public Posto(int id, String endereco, String nome, double latitude, double longitude, Bandeira bandeira) {
        this.id = id;
        this.endereco = endereco;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.setBandeira(bandeira);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }


    public Bandeira getBandeira() { return bandeira; }

    public void setBandeira(Bandeira bandeira) { this.bandeira = bandeira; }
}
