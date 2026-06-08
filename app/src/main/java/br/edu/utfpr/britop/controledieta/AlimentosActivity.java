package br.edu.utfpr.britop.controledieta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AlimentosActivity extends AppCompatActivity {

    public static final String EXTRA_REFEICAO_ID = "refeicaoId";

    private ListView listViewAlimentos;
    private List<Alimento> listaAlimentos;
    private AlimentoAdapter alimentoAdapter;
    private AppDatabase db;
    private int refeicaoId;
    private int posicaoSelecionada = -1;

    private ActivityResultLauncher<Intent> cadastroLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentos);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.titulo_alimentos);
        }

        db = AppDatabase.getInstancia(this);
        refeicaoId = getIntent().getIntExtra(EXTRA_REFEICAO_ID, -1);

        listViewAlimentos = findViewById(R.id.listViewAlimentos);

        cadastroLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                (ActivityResult result) -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        carregarAlimentos();
                    }
                }
        );

        listViewAlimentos.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Alimento alimento = listaAlimentos.get(position);
            Toast.makeText(this,
                    getString(R.string.alimento_de_nome) + alimento.getNome() + getString(R.string.foi_clicado),
                    Toast.LENGTH_SHORT).show();
        });

        listViewAlimentos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewAlimentos.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                posicaoSelecionada = position;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (posicaoSelecionada < 0) return false;
                int id = item.getItemId();
                if (id == R.id.menuItemEditar) {
                    Alimento alimento = listaAlimentos.get(posicaoSelecionada);
                    Intent intent = new Intent(AlimentosActivity.this, AlimentoActivity.class);
                    intent.putExtra(AlimentoActivity.EXTRA_ALIMENTO_ID, alimento.getId());
                    intent.putExtra(AlimentoActivity.EXTRA_REFEICAO_ID, refeicaoId);
                    cadastroLauncher.launch(intent);
                    mode.finish();
                    return true;
                } else if (id == R.id.menuItemExcluir) {
                    Alimento alimento = listaAlimentos.get(posicaoSelecionada);
                    confirmarExclusaoAlimento(alimento, mode);
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) { posicaoSelecionada = -1; }
        });

        carregarAlimentos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAlimentos();
    }

    private void carregarAlimentos() {
        listaAlimentos = db.alimentoDao().listarPorRefeicao(refeicaoId);
        if (alimentoAdapter == null) {
            alimentoAdapter = new AlimentoAdapter(this, listaAlimentos);
            listViewAlimentos.setAdapter(alimentoAdapter);
        } else {
            alimentoAdapter.notifyDataSetChanged();
        }
    }

    private void confirmarExclusaoAlimento(Alimento alimento, ActionMode mode) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmar_exclusao)
                .setMessage(R.string.mensagem_exclusao_alimento)
                .setPositiveButton(R.string.excluir, (dialog, which) -> {
                    db.alimentoDao().deletar(alimento);
                    carregarAlimentos();
                    mode.finish();
                    Toast.makeText(this, R.string.alimento_excluido, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancelar, (dialog, which) -> {
                    mode.finish();
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alimentos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.menuItemAdicionar) {
            Intent intent = new Intent(this, AlimentoActivity.class);
            intent.putExtra(AlimentoActivity.EXTRA_REFEICAO_ID, refeicaoId);
            cadastroLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}