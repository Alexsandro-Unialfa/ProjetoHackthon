package com.example.projetohackthon;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.projetohackthon.Config;
import com.example.projetohackthon.DetalhesActivity;
import com.example.projetohackthon.R;
import com.example.projetohackthon.Rota;
import com.example.projetohackthon.datasource.BuscadadosApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CadastroActivity extends ListActivity {

    private ArrayList<Rota> listaDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        try {
            listaDados = new BuscadadosApi().execute(Config.linkServer).get();

            ListAdapter adapter = new SimpleAdapter(
                    this,
                    dadosToMap(listaDados),
                    R.layout.listview_modelo,
                    new String[]{"destino"},
                    new int[]{R.id.txtNome});
            setListAdapter(adapter);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private List<HashMap<String,String>> dadosToMap(ArrayList<Rota> listaDados) {
        List<HashMap<String,String>> lista = new ArrayList<>();

        for(int i = 0; i < listaDados.size(); i++) {
            HashMap<String,String> item = new HashMap<>();
//            item.put("id", String.valueOf(listaDados.get(i).id));
            item.put("destino", listaDados.get(i).destino);
            item.put("transportadora", listaDados.get(i).transportadora);
            item.put("horaSaida", listaDados.get(i).horaSaida);
            item.put("horaChegada", listaDados.get(i).horaChegada);
            item.put("localPartida", listaDados.get(i).localPartida);
            item.put("descricao", listaDados.get(i).descricao);
            lista.add(item);
        }
        return  lista;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Rota rota = listaDados.get(position);
        Intent tela = new Intent(CadastroActivity.this, DetalhesActivity.class);
        Bundle parametros = new Bundle();
        parametros.putString("transportadora", rota.transportadora);
        parametros.putString("descricao", rota.descricao);
        parametros.putString("localPartida", rota.localPartida);
        parametros.putString("destino", rota.destino);
        parametros.putString("saida", rota.horaSaida);
        parametros.putString("chegada", rota.horaChegada);
        tela.putExtras(parametros);
        startActivity(tela);
    }
}
