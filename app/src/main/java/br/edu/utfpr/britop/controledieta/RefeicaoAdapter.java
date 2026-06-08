package br.edu.utfpr.britop.controledieta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RefeicaoAdapter extends BaseAdapter {

    private Context context;
    private List<Refeicao> listaRefeicoes;
    private String[] tiposRefeicao;

    private static class RefeicaoHolder {
        TextView textViewTipo;
        TextView textViewData;
    }

    public RefeicaoAdapter(Context context, List<Refeicao> listaRefeicoes) {
        this.context = context;
        this.listaRefeicoes = listaRefeicoes;
        this.tiposRefeicao = context.getResources().getStringArray(R.array.tipoRefeicaoSimples);
    }

    @Override
    public int getCount() { return listaRefeicoes.size(); }

    @Override
    public Object getItem(int position) { return listaRefeicoes.get(position); }

    @Override
    public long getItemId(int position) { return listaRefeicoes.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RefeicaoHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_refeicoes, parent, false);
            holder = new RefeicaoHolder();
            holder.textViewTipo = convertView.findViewById(R.id.textViewTipoRefeicao);
            holder.textViewData = convertView.findViewById(R.id.textViewDataRefeicao);
            convertView.setTag(holder);
        } else {
            holder = (RefeicaoHolder) convertView.getTag();
        }

        Refeicao refeicao = listaRefeicoes.get(position);
        holder.textViewTipo.setText(tiposRefeicao[refeicao.getTipo()]);

        // Converter de YYYY-MM-DD para DD/MM/YYYY para exibição
        String dataISO = refeicao.getData();
        if (dataISO != null && dataISO.length() == 10) {
            String[] partes = dataISO.split("-");
            holder.textViewData.setText(partes[2] + "/" + partes[1] + "/" + partes[0]);
        } else {
            holder.textViewData.setText(dataISO);
        }

        return convertView;
    }
}