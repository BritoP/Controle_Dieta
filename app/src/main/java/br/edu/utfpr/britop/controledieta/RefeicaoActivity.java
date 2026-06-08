package br.edu.utfpr.britop.controledieta;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RefeicaoActivity extends AppCompatActivity {

    public static final String EXTRA_REFEICAO_ID = "refeicaoId";

    private Spinner spinnerTipoRefeicao;
    private TextView textViewData;
    private Button buttonEscolherData;

    private int anoSelecionado, mesSelecionado, diaSelecionado;
    private AppDatabase db;
    private Refeicao refeicaoEmEdicao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstancia(this);

        spinnerTipoRefeicao = findViewById(R.id.spinnerTipoRefeicaoForm);
        textViewData        = findViewById(R.id.textViewDataSelecionada);
        buttonEscolherData  = findViewById(R.id.buttonEscolherData);

        // Data padrão: hoje
        Calendar hoje = Calendar.getInstance();
        anoSelecionado = hoje.get(Calendar.YEAR);
        mesSelecionado = hoje.get(Calendar.MONTH);
        diaSelecionado = hoje.get(Calendar.DAY_OF_MONTH);
        atualizarTextoData();

        buttonEscolherData.setOnClickListener(v -> abrirDatePicker());

        // Modo edição
        int refeicaoId = getIntent().getIntExtra(EXTRA_REFEICAO_ID, -1);
        if (refeicaoId != -1) {
            refeicaoEmEdicao = db.refeicaoDao().buscarPorId(refeicaoId);
            if (refeicaoEmEdicao != null) {
                spinnerTipoRefeicao.setSelection(refeicaoEmEdicao.getTipo());
                // Parsear string "YYYY-MM-DD"
                String[] partes = refeicaoEmEdicao.getData().split("-");
                anoSelecionado = Integer.parseInt(partes[0]);
                mesSelecionado = Integer.parseInt(partes[1]) - 1; // Calendar usa 0-indexed
                diaSelecionado = Integer.parseInt(partes[2]);
                atualizarTextoData();
            }
        }
    }

    private void abrirDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    anoSelecionado = year;
                    mesSelecionado = month;
                    diaSelecionado = dayOfMonth;
                    atualizarTextoData();
                }, anoSelecionado, mesSelecionado, diaSelecionado);
        dialog.show();
    }

    private void atualizarTextoData() {
        // Formata como DD/MM/YYYY para exibição
        textViewData.setText(String.format("%02d/%02d/%04d", diaSelecionado, mesSelecionado + 1, anoSelecionado));
    }

    private String getDataISO() {
        // Salva como YYYY-MM-DD no banco
        return String.format("%04d-%02d-%02d", anoSelecionado, mesSelecionado + 1, diaSelecionado);
    }

    private void salvar() {
        int tipo = spinnerTipoRefeicao.getSelectedItemPosition();
        String data = getDataISO();

        if (refeicaoEmEdicao == null) {
            Refeicao nova = new Refeicao(tipo, data);
            db.refeicaoDao().inserir(nova);
            Toast.makeText(this, R.string.refeicao_salva, Toast.LENGTH_SHORT).show();
        } else {
            refeicaoEmEdicao.setTipo(tipo);
            refeicaoEmEdicao.setData(data);
            db.refeicaoDao().atualizar(refeicaoEmEdicao);
            Toast.makeText(this, R.string.refeicao_atualizada, Toast.LENGTH_SHORT).show();
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refeicao, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }
}