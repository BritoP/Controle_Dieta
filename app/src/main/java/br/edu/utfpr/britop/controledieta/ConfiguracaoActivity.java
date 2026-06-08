package br.edu.utfpr.britop.controledieta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracaoActivity extends AppCompatActivity {

    public static final String PREFS_NAME           = "ControleDietaPrefs";
    public static final String PREF_ORDENACAO        = "ordenacao";
    public static final int    ORDENAR_POR_DATA_DESC = 0;
    public static final int    ORDENAR_POR_DATA_ASC  = 1;
    public static final int    ORDENAR_POR_TIPO       = 2;

    private Spinner spinnerOrdenacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.titulo_configuracoes);
        }

        spinnerOrdenacao = findViewById(R.id.spinnerOrdenacao);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        spinnerOrdenacao.setSelection(prefs.getInt(PREF_ORDENACAO, ORDENAR_POR_DATA_DESC));
    }

    public void salvarConfiguracoes(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(PREF_ORDENACAO, spinnerOrdenacao.getSelectedItemPosition());
        editor.apply();
        Toast.makeText(this, R.string.configuracoes_salvas, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}