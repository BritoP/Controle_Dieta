package br.edu.utfpr.britop.controledieta;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AlimentosActivity extends AppCompatActivity {
    private ListView listViewAlimentos;
    private List<Alimento> listaAlimentos; //COISA PRA FAZER O BD DEPOIS
    private AlimentoAdapter alimentoAdapter; //COISA PRA CADASTRO NO BD ADAPTER PRA MOSTRAR TELA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentos);
        listViewAlimentos = findViewById(R.id.listViewAlimentos);

        listViewAlimentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alimento alimento = (Alimento) listViewAlimentos.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),getString(R.string.alimento_de_nome)+alimento.getNome()+getString(R.string.foi_clicado),Toast.LENGTH_LONG).show();

            }
        });

        popularListaAlimentos();
    }

    private void popularListaAlimentos() {
        String[] alimentos_nome = getResources().getStringArray(R.array.alimentos_nome);
        int[] alimentos_quantidade = getResources().getIntArray(R.array.alimentos_quantidade);
        int[] alimentos_calorias = getResources().getIntArray(R.array.alimentos_calorias);
        int[] alimentos_caseira = getResources().getIntArray(R.array.alimentos_caseira);
        int[] alimentos_tipos_nutrientes = getResources().getIntArray(R.array.alimentos_tipos_nutrientes);
        int[] alimentos_tipos_refeicao = getResources().getIntArray(R.array.alimentos_tipos_refeicao);

        listaAlimentos = new ArrayList<>();

        Alimento alimento;
        boolean caseira;
        TipoNutriente tipoNutriente;
        TipoNutriente[] tipos_nutrientes = TipoNutriente.values();
        for (int cont = 0; cont < alimentos_nome.length; cont++) {
            caseira = (alimentos_caseira[cont] == 1 ? true : false);
            tipoNutriente = tipos_nutrientes[alimentos_tipos_nutrientes[cont]];
            alimento = new Alimento(alimentos_nome[cont],
                    alimentos_quantidade[cont],
                    alimentos_calorias[cont],
                    caseira,
                    tipoNutriente,
                    alimentos_tipos_refeicao[cont]
            );

            listaAlimentos.add(alimento);

        }
        alimentoAdapter = new AlimentoAdapter(this,listaAlimentos);

        listViewAlimentos.setAdapter(alimentoAdapter);

    }
}