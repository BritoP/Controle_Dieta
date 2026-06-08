package br.edu.utfpr.britop.controledieta;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Refeicao.class, Alimento.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;

    public abstract RefeicaoDao refeicaoDao();
    public abstract AlimentoDao alimentoDao();

    public static synchronized AppDatabase getInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "controle_dieta_db"
                    )
                    .allowMainThreadQueries() // permitido para fins didáticos
                    .build();
        }
        return instancia;
    }
}