package com.senac.controlecombustivel.model;

import java.util.List;

/**
 * Created by Dantieris on 17/06/2015.
 */
public class Relatorio {

    private List<Abastecimento> abastecimentos;
    private double valorTotal;
    private String combustivelMaisUsado;
    private String postoMaisUsado;

    public Relatorio() {}

    public Relatorio(List<Abastecimento> abastecimentos, String combustivelMaisUsado, String postoMaisUsado, double valorTotal) {
        this.abastecimentos = abastecimentos;
        this.combustivelMaisUsado = combustivelMaisUsado;
        this.postoMaisUsado = postoMaisUsado;
        this.valorTotal = valorTotal;
    }

    public List<Abastecimento> getAbastecimentos() {
        return abastecimentos;
    }

    public void setAbastecimentos(List<Abastecimento> abastecimentos) {
        this.abastecimentos = abastecimentos;
    }

    public String getCombustivelMaisUsado() {
        return combustivelMaisUsado;
    }

    public void setCombustivelMaisUsado(String combustivelMaisUsado) {
        this.combustivelMaisUsado = combustivelMaisUsado;
    }

    public String getPostoMaisUsado() {
        return postoMaisUsado;
    }

    public void setPostoMaisUsado(String postoMaisUsado) {
        this.postoMaisUsado = postoMaisUsado;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() {
        return "Relatorio{" +
                "abastecimentos=" + abastecimentos +
                ", valorTotal=" + valorTotal +
                ", combustivelMaisUsado='" + combustivelMaisUsado + '\'' +
                ", postoMaisUsado='" + postoMaisUsado + '\'' +
                '}';
    }
}
