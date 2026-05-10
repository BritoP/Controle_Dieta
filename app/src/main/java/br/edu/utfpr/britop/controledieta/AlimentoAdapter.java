package br.edu.utfpr.britop.controledieta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AlimentoAdapter extends BaseAdapter{
    private Context context;
    private List<Alimento> listaAlimentos;
    private String[] tiposRefeicao;
    private static class AlimentoHolder{
        public TextView textViewValorNome;
        public TextView textViewValorQuantidade;
        public TextView textViewValorCalorias;
        public TextView textViewValorCaseira;
        public TextView textViewValorNutriente;
        public TextView textViewValorRefeicao;
    }
    public AlimentoAdapter(Context context, List<Alimento> listaAlimentos){
        this.context = context;
        this.listaAlimentos = listaAlimentos;
        this.tiposRefeicao = context.getResources().getStringArray(R.array.tipoRefeicao);
    }

    @Override
    public int getCount() {
        return listaAlimentos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaAlimentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlimentoHolder holder;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_alimentos, parent,false);

            holder = new AlimentoHolder();

            holder.textViewValorNome = convertView.findViewById(R.id.textViewValorNome);
            holder.textViewValorQuantidade = convertView.findViewById(R.id.textViewValorQuantidade);
            holder.textViewValorCalorias = convertView.findViewById(R.id.textViewValorCalorias);
            holder.textViewValorCaseira = convertView.findViewById(R.id.textViewValorCaseira);
            holder.textViewValorNutriente = convertView.findViewById(R.id.textViewValorNutriente);
            holder.textViewValorRefeicao = convertView.findViewById(R.id.textViewValorRefeicao);

            convertView.setTag(holder);
        }else{

            holder = (AlimentoHolder) convertView.getTag();
        }
        Alimento alimento = listaAlimentos.get(position);

        holder.textViewValorNome.setText(alimento.getNome());
        holder.textViewValorQuantidade.setText(String.valueOf(alimento.getQuantidade()));
        holder.textViewValorCalorias.setText(String.valueOf(alimento.getCalorias()));

        if(alimento.isCaseira()){
            holder.textViewValorCaseira.setText(R.string.receita_caseira);
        }else{
            holder.textViewValorCaseira.setText(R.string.receita_nao_caseira);
        }
        switch(alimento.getTipoNutriente()){
            case Proteina:
                holder.textViewValorNutriente.setText(R.string.proteina);
                break;
            case Gordura:
                holder.textViewValorNutriente.setText(R.string.gordura);
                break;
            case Carboidrato:
                holder.textViewValorNutriente.setText(R.string.carboidrato);
                break;
        }
        holder.textViewValorRefeicao.setText(tiposRefeicao[alimento.getTipoRefeicao()]);

        return convertView;
    }
}
