package br.edu.utfpr.britop.controledieta;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RefeicaoDao {

    @Insert
    long inserir(Refeicao refeicao);

    @Update
    void atualizar(Refeicao refeicao);

    @Delete
    void deletar(Refeicao refeicao);

    @Query("SELECT * FROM refeicao ORDER BY data DESC")
    List<Refeicao> listarTodas();

    @Query("SELECT * FROM refeicao WHERE id = :id")
    Refeicao buscarPorId(int id);
}