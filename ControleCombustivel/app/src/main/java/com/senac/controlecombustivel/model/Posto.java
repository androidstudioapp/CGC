package com.senac.controlecombustivel.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Dantieris on 26/04/2015.
 */
public class Posto implements Parcelable {
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

    @Override
    public String toString() {
        return "Posto{" +
                "bandeira=" + bandeira +
                ", id=" + id +
                ", endereco='" + endereco + '\'' +
                ", nome='" + nome + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(endereco);
        out.writeString(nome);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Posto> CREATOR = new Parcelable.Creator<Posto>() {
        public Posto createFromParcel(Parcel in) {
            return new Posto(in);
        }

        public Posto[] newArray(int size) {
            return new Posto[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Posto(Parcel in) {
        id = in.readInt();
        endereco = in.readString();
        nome = in.readString();
    }
}
