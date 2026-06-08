package br.edu.utfpr.britop.controledieta;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlimentoDao {

    @Insert
    long inserir(Alimento alimento);

    @Update
    void atualizar(Alimento alimento);

    @Delete
    void deletar(Alimento alimento);

    @Query("SELECT * FROM alimento WHERE refeicaoId = :refeicaoId")
    List<Alimento> listarPorRefeicao(int refeicaoId);

    @Query("SELECT * FROM alimento WHERE id = :id")
    Alimento buscarPorId(int id);
}