package br.edu.utfpr.britop.controledieta;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "refeicao")
public class Refeicao {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int tipo; // 0=Café da manhã, 1=Almoço, 2=Café da tarde, 3=Janta

    private String data; // formato ISO: "2026-06-07"

    public Refeicao(int tipo, String data) {
        this.tipo = tipo;
        this.data = data;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTipo() { return tipo; }
    public void setTipo(int tipo) { this.tipo = tipo; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}