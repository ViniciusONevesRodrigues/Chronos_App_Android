package com.example.chronos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapImg extends BaseAdapter {

    private Context context;
    private ArrayList<Evento> listaEventos;
    private LayoutInflater inflater;

    public AdapImg(Context ctx, ArrayList<Evento> eventos) {
        this.context = ctx;
        this.listaEventos = eventos;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return listaEventos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaEventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.modelo_evento, null);
        }

        TextView evento = convertView.findViewById(R.id.textoNomeEvento);
        TextView horario = convertView.findViewById(R.id.textoHorario);
        ImageView img = convertView.findViewById(R.id.imageViewEvento);

        Evento itemEvento = listaEventos.get(position);

        evento.setText(itemEvento.getNome());
        horario.setText(itemEvento.getDataHora());


        if (itemEvento.getImagem() != null) {
            img.setImageBitmap(itemEvento.getImagem());
        } else {
            img.setImageResource(R.drawable.file);
        }

        return convertView;
    }

}
