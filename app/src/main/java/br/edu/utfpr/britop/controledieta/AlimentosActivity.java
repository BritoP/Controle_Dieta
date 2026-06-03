package br.edu.utfpr.britop.controledieta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    private ActivityResultLauncher<Intent> cadastroLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentos);

        listViewAlimentos = findViewById(R.id.listViewAlimentos);
        Button buttonAdicionar = findViewById(R.id.buttonAdicionar);
        Button buttonSobre = findViewById(R.id.buttonSobre);

        listaAlimentos = new ArrayList<>();
        alimentoAdapter = new AlimentoAdapter(this, listaAlimentos);
        listViewAlimentos.setAdapter(alimentoAdapter);


        cadastroLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();

                            String nome = data.getStringExtra(AlimentoActivity.EXTRA_NOME);
                            int quantidade = data.getIntExtra(AlimentoActivity.EXTRA_QUANTIDADE, 0);
                            int calorias = data.getIntExtra(AlimentoActivity.EXTRA_CALORIAS, 0);
                            boolean caseira = data.getBooleanExtra(AlimentoActivity.EXTRA_CASEIRA, false);
                            int tipoNutriente = data.getIntExtra(AlimentoActivity.EXTRA_TIPO_NUTRIENTE, 0);
                            int tipoRefeicao = data.getIntExtra(AlimentoActivity.EXTRA_TIPO_REFEICAO, 0);

                            TipoNutriente nutriente = TipoNutriente.values()[tipoNutriente];
                            Alimento alimento = new Alimento(nome, quantidade, calorias, caseira, nutriente, tipoRefeicao);

                            listaAlimentos.add(alimento);
                            alimentoAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        listViewAlimentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alimento alimento = (Alimento) listViewAlimentos.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.alimento_de_nome) + alimento.getNome() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlimentosActivity.this, AlimentoActivity.class);
                cadastroLauncher.launch(intent);
            }
        });

        buttonSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlimentosActivity.this, AutoriaActivity.class);
                startActivity(intent);
            }
        });
    }
}