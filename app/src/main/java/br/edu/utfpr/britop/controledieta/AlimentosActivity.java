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
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AlimentosActivity extends AppCompatActivity {

    private ListView listViewAlimentos;
    private List<Alimento> listaAlimentos;
    private AlimentoAdapter alimentoAdapter;

    // Guarda posição do item selecionado no menu contextual
    private int posicaoSelecionada = -1;

    private ActivityResultLauncher<Intent> cadastroLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentos);

        listViewAlimentos = findViewById(R.id.listViewAlimentos);

        listaAlimentos = new ArrayList<>();
        alimentoAdapter = new AlimentoAdapter(this, listaAlimentos);
        listViewAlimentos.setAdapter(alimentoAdapter);

        // Registrar launcher para resultado do cadastro/edição
        cadastroLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();

                            String nome       = data.getStringExtra(AlimentoActivity.EXTRA_NOME);
                            int quantidade    = data.getIntExtra(AlimentoActivity.EXTRA_QUANTIDADE, 0);
                            int calorias      = data.getIntExtra(AlimentoActivity.EXTRA_CALORIAS, 0);
                            boolean caseira   = data.getBooleanExtra(AlimentoActivity.EXTRA_CASEIRA, false);
                            int tipoNutriente = data.getIntExtra(AlimentoActivity.EXTRA_TIPO_NUTRIENTE, 0);
                            int tipoRefeicao  = data.getIntExtra(AlimentoActivity.EXTRA_TIPO_REFEICAO, 0);
                            int posicao       = data.getIntExtra(AlimentoActivity.EXTRA_POSICAO, -1);

                            TipoNutriente nutriente = TipoNutriente.values()[tipoNutriente];

                            if (posicao >= 0) {
                                // Modo edição: atualiza item existente
                                Alimento alimento = listaAlimentos.get(posicao);
                                alimento.setNome(nome);
                                alimento.setQuantidade(quantidade);
                                alimento.setCalorias(calorias);
                                alimento.setCaseira(caseira);
                                alimento.setTipoNutriente(nutriente);
                                alimento.setTipoRefeicao(tipoRefeicao);
                            } else {
                                // Modo inclusão: adiciona novo item
                                Alimento alimento = new Alimento(nome, quantidade, calorias, caseira, nutriente, tipoRefeicao);
                                listaAlimentos.add(alimento);
                            }

                            alimentoAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        // Clique simples: Toast
        listViewAlimentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alimento alimento = (Alimento) listViewAlimentos.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.alimento_de_nome) + alimento.getNome() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }
        });

        // Menu de ação contextual (long press)
        listViewAlimentos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewAlimentos.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                posicaoSelecionada = position;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_contextual, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItemEditar) {
                    Alimento alimento = listaAlimentos.get(posicaoSelecionada);
                    Intent intent = new Intent(AlimentosActivity.this, AlimentoActivity.class);
                    intent.putExtra(AlimentoActivity.EXTRA_NOME, alimento.getNome());
                    intent.putExtra(AlimentoActivity.EXTRA_QUANTIDADE, alimento.getQuantidade());
                    intent.putExtra(AlimentoActivity.EXTRA_CALORIAS, alimento.getCalorias());
                    intent.putExtra(AlimentoActivity.EXTRA_CASEIRA, alimento.isCaseira());
                    intent.putExtra(AlimentoActivity.EXTRA_TIPO_NUTRIENTE, alimento.getTipoNutriente().ordinal());
                    intent.putExtra(AlimentoActivity.EXTRA_TIPO_REFEICAO, alimento.getTipoRefeicao());
                    intent.putExtra(AlimentoActivity.EXTRA_POSICAO, posicaoSelecionada);
                    cadastroLauncher.launch(intent);
                    mode.finish();
                    return true;
                } else if (id == R.id.menuItemExcluir) {
                    listaAlimentos.remove(posicaoSelecionada);
                    alimentoAdapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                posicaoSelecionada = -1;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_alimentos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuItemAdicionar) {
            Intent intent = new Intent(this, AlimentoActivity.class);
            cadastroLauncher.launch(intent);
            return true;
        } else if (id == R.id.menuItemSobre) {
            Intent intent = new Intent(this, AutoriaActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}