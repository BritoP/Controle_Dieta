package br.edu.utfpr.britop.controledieta;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static TipoNutriente fromInt(Integer value) {
        return value == null ? null : TipoNutriente.values()[value];
    }

    @TypeConverter
    public static Integer toInt(TipoNutriente tipoNutriente) {
        return tipoNutriente == null ? null : tipoNutriente.ordinal();
    }
}