package com.senac.controlecombustivel.model;

import java.util.Date;

public class Abastecimento {

    private TiposCombustivel tiposCombustivel;
    private double valorTotal;
    private double litros;
    private String idAndroid;
    private int id;
    private Date data;

    public Abastecimento(double valorTotal, String idAndroid, double litros, TiposCombustivel tiposCombustivel, Date data) {
        this.valorTotal = valorTotal;
        this.idAndroid = idAndroid;
        this.litros = litros;
        this.tiposCombustivel = tiposCombustivel;
        this.data = data;
    }

    public Abastecimento(int id, double valorTotal, String idAndroid, double litros, TiposCombustivel tiposCombustivel, Date data) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.idAndroid = idAndroid;
        this.litros = litros;
        this.tiposCombustivel = tiposCombustivel;
        this.data = data;
    }

    public Abastecimento(){}

    public Abastecimento(int id) {
        this.id = id;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public TiposCombustivel getTiposCombustivel() {
        return tiposCombustivel;
    }

    public void setTiposCombustivel(TiposCombustivel tiposCombustivel) {
        this.tiposCombustivel = tiposCombustivel;
    }

    public String getIdAndroid() {
        return idAndroid;
    }

    public void setIdAndroid(String idAndroid) {
        this.idAndroid = idAndroid;
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Abastecimento{" +
                "valorTotal=" + valorTotal +
                ", data=" + data +
                ", idAndroid='" + idAndroid + '\'' +
                ", litros=" + litros +
                ", tiposCombustivel=" + tiposCombustivel +
                '}';
    }
}
