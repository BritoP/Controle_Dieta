package br.edu.utfpr.britop.controledieta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RefeicoesActivity extends AppCompatActivity {

    private ListView listViewRefeicoes;
    private List<Refeicao> listaRefeicoes;
    private RefeicaoAdapter refeicaoAdapter;
    private AppDatabase db;
    private int posicaoSelecionada = -1;

    private ActivityResultLauncher<Intent> cadastroLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicoes);

        db = AppDatabase.getInstancia(this);
        listViewRefeicoes = findViewById(R.id.listViewRefeicoes);

        cadastroLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                (ActivityResult result) -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        carregarRefeicoes();
                    }
                }
        );

        listViewRefeicoes.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Refeicao refeicao = listaRefeicoes.get(position);
            Intent intent = new Intent(RefeicoesActivity.this, AlimentosActivity.class);
            intent.putExtra(AlimentosActivity.EXTRA_REFEICAO_ID, refeicao.getId());
            startActivity(intent);
        });

        listViewRefeicoes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewRefeicoes.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                posicaoSelecionada = position;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_contextual_refeicoes, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (posicaoSelecionada < 0) return false;
                int id = item.getItemId();
                if (id == R.id.menuItemEditar) {
                    Refeicao refeicao = listaRefeicoes.get(posicaoSelecionada);
                    Intent intent = new Intent(RefeicoesActivity.this, RefeicaoActivity.class);
                    intent.putExtra(RefeicaoActivity.EXTRA_REFEICAO_ID, refeicao.getId());
                    cadastroLauncher.launch(intent);
                    mode.finish();
                    return true;
                } else if (id == R.id.menuItemExcluir) {
                    Refeicao refeicao = listaRefeicoes.get(posicaoSelecionada);
                    confirmarExclusao(refeicao, mode);
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) { posicaoSelecionada = -1; }
        });

        carregarRefeicoes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarRefeicoes();
    }

    private void carregarRefeicoes() {
        listaRefeicoes = db.refeicaoDao().listarTodas();
        ordenarRefeicoes();
        if (refeicaoAdapter == null) {
            refeicaoAdapter = new RefeicaoAdapter(this, listaRefeicoes);
            listViewRefeicoes.setAdapter(refeicaoAdapter);
        } else {
            refeicaoAdapter.notifyDataSetChanged();
        }
    }

    private void ordenarRefeicoes() {
        SharedPreferences prefs = getSharedPreferences(ConfiguracaoActivity.PREFS_NAME, MODE_PRIVATE);
        int ordenacao = prefs.getInt(ConfiguracaoActivity.PREF_ORDENACAO, ConfiguracaoActivity.ORDENAR_POR_DATA_DESC);
        switch (ordenacao) {
            case ConfiguracaoActivity.ORDENAR_POR_DATA_DESC:
                // String ISO ordena corretamente de forma lexicográfica
                Collections.sort(listaRefeicoes, (a, b) -> b.getData().compareTo(a.getData()));
                break;
            case ConfiguracaoActivity.ORDENAR_POR_DATA_ASC:
                Collections.sort(listaRefeicoes, (a, b) -> a.getData().compareTo(b.getData()));
                break;
            case ConfiguracaoActivity.ORDENAR_POR_TIPO:
                Collections.sort(listaRefeicoes, Comparator.comparingInt(Refeicao::getTipo));
                break;
        }
    }

    private void confirmarExclusao(Refeicao refeicao, ActionMode mode) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmar_exclusao)
                .setMessage(R.string.mensagem_exclusao_refeicao)
                .setPositiveButton(R.string.excluir, (dialog, which) -> {
                    db.refeicaoDao().deletar(refeicao);
                    carregarRefeicoes();
                    mode.finish();
                    Toast.makeText(this, R.string.refeicao_excluida, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancelar, (dialog, which) -> {
                    mode.finish();
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refeicoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuItemAdicionar) {
            cadastroLauncher.launch(new Intent(this, RefeicaoActivity.class));
            return true;
        } else if (id == R.id.menuItemSobre) {
            startActivity(new Intent(this, AutoriaActivity.class));
            return true;
        } else if (id == R.id.menuItemConfiguracoes) {
            startActivity(new Intent(this, ConfiguracaoActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}