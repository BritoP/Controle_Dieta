package br.edu.utfpr.britop.controledieta;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "alimento",
        foreignKeys = @ForeignKey(
                entity = Refeicao.class,
                parentColumns = "id",
                childColumns = "refeicaoId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("refeicaoId")}
)
public class Alimento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private int quantidade;
    private int calorias;
    private boolean caseira;
    private TipoNutriente tipoNutriente;
    private int refeicaoId;

    public Alimento(String nome, int quantidade, int calorias, boolean caseira,
                    TipoNutriente tipoNutriente, int refeicaoId) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.calorias = calorias;
        this.caseira = caseira;
        this.tipoNutriente = tipoNutriente;
        this.refeicaoId = refeicaoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public boolean isCaseira() { return caseira; }
    public void setCaseira(boolean caseira) { this.caseira = caseira; }

    public TipoNutriente getTipoNutriente() { return tipoNutriente; }
    public void setTipoNutriente(TipoNutriente tipoNutriente) { this.tipoNutriente = tipoNutriente; }

    public int getRefeicaoId() { return refeicaoId; }
    public void setRefeicaoId(int refeicaoId) { this.refeicaoId = refeicaoId; }
}