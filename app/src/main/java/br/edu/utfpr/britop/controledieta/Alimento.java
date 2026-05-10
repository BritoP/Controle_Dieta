package br.edu.utfpr.britop.controledieta;

public class Alimento {
    private String nome;
    private int quantidade;
    private int calorias;
    boolean caseira;
    private TipoNutriente tipoNutriente;
    private int tipoRefeicao;

    public Alimento(String nome,int quantidade,int calorias,boolean caseira,  TipoNutriente tipoNutriente, int tipoRefeicao  ) {
        this.nome          = nome;
        this.quantidade    = quantidade;
        this.calorias      = calorias;
        this.caseira       = caseira;
        this.tipoNutriente = tipoNutriente;
        this.tipoRefeicao  = tipoRefeicao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public boolean isCaseira() {
        return caseira;
    }

    public void setCaseira(boolean caseira) {
        this.caseira = caseira;
    }

    public TipoNutriente getTipoNutriente() {
        return tipoNutriente;
    }

    public void setTipoNutriente(TipoNutriente tipoNutriente) {
        this.tipoNutriente = tipoNutriente;
    }

    public int getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(int tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    @Override
    public String toString() {
        return  nome + "\n" +
                quantidade + "\n"+
                calorias + "\n" +
                caseira + "\n" +
                tipoNutriente + "\n" +
                tipoRefeicao;
    }
}
