package br.edu.utfpr.britop.controledieta;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlimentoActivity extends AppCompatActivity {

    private EditText editTextNomeAlimento, editTextQuantidade, editTextCalorias;
    private CheckBox checkBoxCaseira;
    private RadioGroup radioGroupNutriente;
    private Spinner spinnerTipoRefeicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimento);

        editTextNomeAlimento = findViewById(R.id.editTextNomeAlimento);
        editTextQuantidade = findViewById(R.id.editTextQuantidade);
        editTextCalorias = findViewById(R.id.editTextCalorias);
        checkBoxCaseira = findViewById(R.id.checkBoxCaseira);
        radioGroupNutriente = findViewById(R.id.radioGroupNutriente);
        spinnerTipoRefeicao = findViewById(R.id.spinnerTipoRefeicao);
//        popularSpinner();
    }

//    private void popularSpinner() {
//        ArrayList<String> lista = new ArrayList<>();
//
//        lista.add(getString(R.string.cafe_da_manha));
//        lista.add(getString(R.string.almoco));
//        lista.add(getString(R.string.cafe_da_tarde));
//        lista.add(getString(R.string.janta));
//        lista.add(getString(R.string.outro));
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
//
//        spinnerTipoRefeicao.setAdapter(adapter);
//    }

    public void limparCampos(View view) {
        editTextCalorias.setText(null);
        editTextNomeAlimento.setText(null);
        editTextQuantidade.setText(null);
        checkBoxCaseira.setChecked(false);
        radioGroupNutriente.clearCheck();
        spinnerTipoRefeicao.setSelection(0);
        editTextNomeAlimento.requestFocus();
        Toast.makeText(this, R.string.as_entradas_foram_apagadas, Toast.LENGTH_LONG).show();
    }

    public void salvarValores(View view) {
        String nomeAlimento = editTextNomeAlimento.getText().toString();
        if (nomeAlimento == null || nomeAlimento.trim().isEmpty()) {

            Toast.makeText(this, R.string.faltou_entrar_com_o_nome_do_alimento, Toast.LENGTH_LONG).show();
            editTextNomeAlimento.requestFocus();
            editTextNomeAlimento.setSelection(0, editTextNomeAlimento.getText().toString().length());
            return;
        }
        String quantidade = editTextQuantidade.getText().toString();

        if (quantidade == null || quantidade.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_a_quantidade, Toast.LENGTH_LONG).show();
            editTextQuantidade.requestFocus();
            editTextQuantidade.setSelection(0, editTextQuantidade.getText().toString().length());
            return;

        }

        String calorias = editTextCalorias.getText().toString();
        if (calorias == null || calorias.trim().isEmpty()) {
            Toast.makeText(this, R.string.faltou_entrar_com_as_calorias, Toast.LENGTH_LONG).show();
            editTextCalorias.requestFocus();
            editTextCalorias.setSelection(0, editTextCalorias.getText().toString().length());
            return;
        }
        boolean caseira = checkBoxCaseira.isChecked();

        int radioButtonID = radioGroupNutriente.getCheckedRadioButtonId();
        String nutriente = "";

        if (radioButtonID == R.id.radioButtonProteina) {
            nutriente = getString(R.string.proteina);
        } else if (radioButtonID == R.id.radioButtonGordura) {
            nutriente = getString(R.string.gordura);
        } else if (radioButtonID == R.id.radioButtonCarboidrato) {
            nutriente = getString(R.string.carboidrato);
        } else {
            Toast.makeText(this, R.string.faltou_preencher_o_tipo_do_nutriente, Toast.LENGTH_LONG).show();
        }

        String tipoRefeicao = (String) spinnerTipoRefeicao.getSelectedItem();

        if(tipoRefeicao == null){
            Toast.makeText(this, R.string.o_spinner_tipo_nao_possui_valor,Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this,
                        "Alimento: "+ nomeAlimento + "\n"+
                              "Quantidade: "+quantidade + "\n" +
                                (caseira ? "É caseira" : "Não é caseira") + "\n" +
                                "Tipo de Nutriente: " + nutriente +"\n"+
                                "Tipo de Refeição: " + tipoRefeicao,Toast.LENGTH_LONG).show();
    }

}



