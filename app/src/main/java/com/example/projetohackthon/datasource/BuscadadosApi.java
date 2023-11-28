package com.example.projetohackthon.datasource;

import android.os.AsyncTask;

import com.example.projetohackthon.Rota;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BuscadadosApi extends AsyncTask<String, Void, ArrayList<Rota>> {

    @Override
    protected ArrayList<Rota> doInBackground(String... strings) {
        ArrayList<Rota> listaDados = new ArrayList<>();

        try {
            //capturando a primeira posicao do vetor de strings
            String link = strings[0];

            //criando uma url valida apartir do link
            URL url = new URL(link);

            //criando uma conexao apartir da URL
            URLConnection connection = url.openConnection();

            //salvando na memoria o retorno da API
            InputStream stream = connection.getInputStream();

            //Pegando os dados de memoria e colocando num reader
            InputStreamReader inputStreamReader = new InputStreamReader(stream);

            //Pegando os dados do reader e carregando no espaço de momoria legivel(buffer) que pode ser lido
            BufferedReader reader = new BufferedReader(inputStreamReader);


            String dados = "";
            String linha;

            //Enquanto existir dado para ler no reader salva o valor nos dados
            while ((linha = reader.readLine()) != null) {
                dados = dados + linha; //dados +=linha;
            }

            //Transformando os dados de texto em Objeto JSON
            //JSONObject json = new JSONObject(dados);

            JSONArray lista = new JSONArray(dados);

            for (int i = 0; i < lista.length(); i++) {
                //Pega o item atual da lista e salva em ITEM
                JSONObject item = (JSONObject) lista.get(i);

                //mapeando os campos do JSON para a classe Pokemon
                Rota rota = new Rota();
                rota.destino = item.getString("destino");
                rota.transportadora = item.getString("transportadora");
                rota.horaChegada = item.getString("chegada");
                rota.horaSaida = item.getString("saida");
                rota.descricao = item.getString("descricao");
                rota.localPartida = item.getString("localPartida");





                listaDados.add(rota);
            }

        } catch (Exception ex) {

        }
        return listaDados;
    }
}
