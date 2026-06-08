package br.edu.utfpr.britop.controledieta;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlimentoActivity extends AppCompatActivity {

    public static final String EXTRA_ALIMENTO_ID = "alimentoId";
    public static final String EXTRA_REFEICAO_ID = "refeicaoId";

    private EditText editTextNome, editTextQuantidade, editTextCalorias;
    private CheckBox checkBoxCaseira;
    private RadioGroup radioGroupNutriente;
    private Spinner spinnerTipoRefeicao;

    private AppDatabase db;
    private Alimento alimentoEmEdicao = null;
    private int refeicaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimento);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstancia(this);

        editTextNome      = findViewById(R.id.editTextNomeAlimento);
        editTextQuantidade = findViewById(R.id.editTextQuantidade);
        editTextCalorias  = findViewById(R.id.editTextCalorias);
        checkBoxCaseira   = findViewById(R.id.checkBoxCaseira);
        radioGroupNutriente = findViewById(R.id.radioGroupNutriente);
        spinnerTipoRefeicao = findViewById(R.id.spinnerTipoRefeicao);

        refeicaoId = getIntent().getIntExtra(EXTRA_REFEICAO_ID, -1);

        // Modo edição
        int alimentoId = getIntent().getIntExtra(EXTRA_ALIMENTO_ID, -1);
        if (alimentoId != -1) {
            alimentoEmEdicao = db.alimentoDao().buscarPorId(alimentoId);
            if (alimentoEmEdicao != null) {
                editTextNome.setText(alimentoEmEdicao.getNome());
                editTextQuantidade.setText(String.valueOf(alimentoEmEdicao.getQuantidade()));
                editTextCalorias.setText(String.valueOf(alimentoEmEdicao.getCalorias()));
                checkBoxCaseira.setChecked(alimentoEmEdicao.isCaseira());
                spinnerTipoRefeicao.setSelection(alimentoEmEdicao.getRefeicaoId());

                switch (alimentoEmEdicao.getTipoNutriente()) {
                    case Proteina:
                        radioGroupNutriente.check(R.id.radioButtonProteina); break;
                    case Gordura:
                        radioGroupNutriente.check(R.id.radioButtonGordura); break;
                    case Carboidrato:
                        radioGroupNutriente.check(R.id.radioButtonCarboidrato); break;
                }
            }
        }
    }

    private void salvar() {
        String nome = editTextNome.getText().toString();
        if (nome.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_o_nome_do_alimento, Toast.LENGTH_LONG).show();
            editTextNome.requestFocus();
            return;
        }

        String quantidadeStr = editTextQuantidade.getText().toString();
        if (quantidadeStr.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_a_quantidade, Toast.LENGTH_LONG).show();
            editTextQuantidade.requestFocus();
            return;
        }

        String caloriasStr = editTextCalorias.getText().toString();
        if (caloriasStr.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_as_calorias, Toast.LENGTH_LONG).show();
            editTextCalorias.requestFocus();
            return;
        }

        int radioId = radioGroupNutriente.getCheckedRadioButtonId();
        if (radioId == -1) {
            Toast.makeText(this, R.string.faltou_preencher_o_tipo_do_nutriente, Toast.LENGTH_LONG).show();
            return;
        }

        TipoNutriente tipoNutriente;
        if (radioId == R.id.radioButtonProteina) tipoNutriente = TipoNutriente.Proteina;
        else if (radioId == R.id.radioButtonGordura) tipoNutriente = TipoNutriente.Gordura;
        else tipoNutriente = TipoNutriente.Carboidrato;

        boolean caseira = checkBoxCaseira.isChecked();
        int quantidade  = Integer.parseInt(quantidadeStr);
        int calorias    = Integer.parseInt(caloriasStr);

        if (alimentoEmEdicao == null) {
            Alimento novo = new Alimento(nome, quantidade, calorias, caseira, tipoNutriente, refeicaoId);
            db.alimentoDao().inserir(novo);
            Toast.makeText(this, R.string.alimento_salvo, Toast.LENGTH_SHORT).show();
        } else {
            alimentoEmEdicao.setNome(nome);
            alimentoEmEdicao.setQuantidade(quantidade);
            alimentoEmEdicao.setCalorias(calorias);
            alimentoEmEdicao.setCaseira(caseira);
            alimentoEmEdicao.setTipoNutriente(tipoNutriente);
            db.alimentoDao().atualizar(alimentoEmEdicao);
            Toast.makeText(this, R.string.alimento_atualizado, Toast.LENGTH_SHORT).show();
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

    private void limpar() {
        editTextNome.setText(null);
        editTextQuantidade.setText(null);
        editTextCalorias.setText(null);
        checkBoxCaseira.setChecked(false);
        radioGroupNutriente.clearCheck();
        spinnerTipoRefeicao.setSelection(0);
        editTextNome.requestFocus();
        Toast.makeText(this, R.string.as_entradas_foram_apagadas, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alimento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        } else if (id == R.id.menuItemSalvar) {
            salvar();
            return true;
        } else if (id == R.id.menuItemLimpar) {
            limpar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}