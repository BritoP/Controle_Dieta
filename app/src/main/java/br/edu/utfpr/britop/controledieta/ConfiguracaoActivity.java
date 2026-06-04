package br.edu.utfpr.britop.controledieta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracaoActivity extends AppCompatActivity {

    public static final String PREFS_NAME        = "ControleDietaPrefs";
    public static final String PREF_ORDENACAO    = "ordenacao";
    public static final int    ORDENAR_POR_NOME      = 0;
    public static final int    ORDENAR_POR_CALORIAS  = 1;
    public static final int    ORDENAR_POR_QUANTIDADE = 2;

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

        // Carregar preferência salva
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int ordenacaoSalva = prefs.getInt(PREF_ORDENACAO, ORDENAR_POR_NOME);
        spinnerOrdenacao.setSelection(ordenacaoSalva);
    }

    public void salvarConfiguracoes(View view) {
        int ordenacaoEscolhida = spinnerOrdenacao.getSelectedItemPosition();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_ORDENACAO, ordenacaoEscolhida);
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