package model;

public class Medicamento {

    private String nome;
    private String dosagem;
    private String posologia; // Instruções de uso (ex.: "1 comprimido a cada 8 horas")
    private double preco;

    public Medicamento(String nome, String dosagem, String posologia, double preco) {
        this.nome = nome;
        this.dosagem = dosagem;
        this.posologia = posologia;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
