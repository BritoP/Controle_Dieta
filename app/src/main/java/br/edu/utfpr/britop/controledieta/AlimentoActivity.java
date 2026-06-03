package br.edu.utfpr.britop.controledieta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlimentoActivity extends AppCompatActivity {

    public static final String EXTRA_NOME           = "nome";
    public static final String EXTRA_QUANTIDADE     = "quantidade";
    public static final String EXTRA_CALORIAS       = "calorias";
    public static final String EXTRA_CASEIRA        = "caseira";
    public static final String EXTRA_TIPO_NUTRIENTE = "tipoNutriente";
    public static final String EXTRA_TIPO_REFEICAO  = "tipoRefeicao";
    public static final String EXTRA_POSICAO        = "posicao";

    private EditText editTextNomeAlimento, editTextQuantidade, editTextCalorias;
    private CheckBox checkBoxCaseira;
    private RadioGroup radioGroupNutriente;
    private Spinner spinnerTipoRefeicao;

    // Posição do item sendo editado (-1 = novo cadastro)
    private int posicaoEdicao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimento);

        // Habilitar botão Up na Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextNomeAlimento = findViewById(R.id.editTextNomeAlimento);
        editTextQuantidade   = findViewById(R.id.editTextQuantidade);
        editTextCalorias     = findViewById(R.id.editTextCalorias);
        checkBoxCaseira      = findViewById(R.id.checkBoxCaseira);
        radioGroupNutriente  = findViewById(R.id.radioGroupNutriente);
        spinnerTipoRefeicao  = findViewById(R.id.spinnerTipoRefeicao);

        // Verificar se veio em modo edição
        Intent intent = getIntent();
        posicaoEdicao = intent.getIntExtra(EXTRA_POSICAO, -1);

        if (posicaoEdicao >= 0) {
            // Preencher campos com dados do item a editar
            editTextNomeAlimento.setText(intent.getStringExtra(EXTRA_NOME));
            editTextQuantidade.setText(String.valueOf(intent.getIntExtra(EXTRA_QUANTIDADE, 0)));
            editTextCalorias.setText(String.valueOf(intent.getIntExtra(EXTRA_CALORIAS, 0)));
            checkBoxCaseira.setChecked(intent.getBooleanExtra(EXTRA_CASEIRA, false));
            spinnerTipoRefeicao.setSelection(intent.getIntExtra(EXTRA_TIPO_REFEICAO, 0));

            int tipoNutriente = intent.getIntExtra(EXTRA_TIPO_NUTRIENTE, 0);
            switch (TipoNutriente.values()[tipoNutriente]) {
                case Proteina:
                    radioGroupNutriente.check(R.id.radioButtonProteina);
                    break;
                case Gordura:
                    radioGroupNutriente.check(R.id.radioButtonGordura);
                    break;
                case Carboidrato:
                    radioGroupNutriente.check(R.id.radioButtonCarboidrato);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_alimento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Botão Up: cancela e volta para listagem
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        } else if (id == R.id.menuItemSalvar) {
            salvarValores();
            return true;
        } else if (id == R.id.menuItemLimpar) {
            limparCampos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void limparCampos() {
        editTextCalorias.setText(null);
        editTextNomeAlimento.setText(null);
        editTextQuantidade.setText(null);
        checkBoxCaseira.setChecked(false);
        radioGroupNutriente.clearCheck();
        spinnerTipoRefeicao.setSelection(0);
        editTextNomeAlimento.requestFocus();
        Toast.makeText(this, R.string.as_entradas_foram_apagadas, Toast.LENGTH_LONG).show();
    }

    private void salvarValores() {
        // Validar nome
        String nomeAlimento = editTextNomeAlimento.getText().toString();
        if (nomeAlimento.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_o_nome_do_alimento, Toast.LENGTH_LONG).show();
            editTextNomeAlimento.requestFocus();
            editTextNomeAlimento.setSelection(0, editTextNomeAlimento.getText().toString().length());
            return;
        }

        // Validar quantidade
        String quantidadeStr = editTextQuantidade.getText().toString();
        if (quantidadeStr.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_a_quantidade, Toast.LENGTH_LONG).show();
            editTextQuantidade.requestFocus();
            editTextQuantidade.setSelection(0, editTextQuantidade.getText().toString().length());
            return;
        }

        // Validar calorias
        String caloriasStr = editTextCalorias.getText().toString();
        if (caloriasStr.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_as_calorias, Toast.LENGTH_LONG).show();
            editTextCalorias.requestFocus();
            editTextCalorias.setSelection(0, editTextCalorias.getText().toString().length());
            return;
        }

        // Validar nutriente
        int radioButtonID = radioGroupNutriente.getCheckedRadioButtonId();
        if (radioButtonID == -1) {
            Toast.makeText(this, R.string.faltou_preencher_o_tipo_do_nutriente, Toast.LENGTH_LONG).show();
            return;
        }

        int tipoNutriente;
        if (radioButtonID == R.id.radioButtonProteina) {
            tipoNutriente = TipoNutriente.Proteina.ordinal();
        } else if (radioButtonID == R.id.radioButtonGordura) {
            tipoNutriente = TipoNutriente.Gordura.ordinal();
        } else {
            tipoNutriente = TipoNutriente.Carboidrato.ordinal();
        }

        boolean caseira      = checkBoxCaseira.isChecked();
        int tipoRefeicao     = spinnerTipoRefeicao.getSelectedItemPosition();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_NOME, nomeAlimento);
        resultIntent.putExtra(EXTRA_QUANTIDADE, Integer.parseInt(quantidadeStr));
        resultIntent.putExtra(EXTRA_CALORIAS, Integer.parseInt(caloriasStr));
        resultIntent.putExtra(EXTRA_CASEIRA, caseira);
        resultIntent.putExtra(EXTRA_TIPO_NUTRIENTE, tipoNutriente);
        resultIntent.putExtra(EXTRA_TIPO_REFEICAO, tipoRefeicao);
        resultIntent.putExtra(EXTRA_POSICAO, posicaoEdicao);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}