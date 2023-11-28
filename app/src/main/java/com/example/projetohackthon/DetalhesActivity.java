package com.example.projetohackthon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetalhesActivity extends AppCompatActivity {

    Button btnVolta;
    Button btnsair;
    TextView transportadora, descricao, partida, destino, saida, chegada;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        

        btnVolta = findViewById(R.id.btnVoltar);
        btnsair= findViewById(R.id.btnsair);

        transportadora = findViewById(R.id.txtTransportadora);
        descricao = findViewById(R.id.txtDescricao);
        partida = findViewById(R.id.txtPartida);
        destino = findViewById(R.id.txtDestino);
        saida = findViewById(R.id.txtSaida);
        chegada = findViewById(R.id.txtChegada);

        Intent caminhoRecebido = getIntent();

        if(caminhoRecebido != null){
            //captura os parametros recebidos no caminho de tela
            Bundle parametros = caminhoRecebido.getExtras();

            if(parametros != null){
                transportadora.setText("Transportadora: "+parametros.getString("transportadora"));
                descricao.setText("Descrição: "+parametros.getString("descricao"));
                partida.setText("Local de Partida: "+parametros.getString("localPartida"));
                destino.setText("Destino: "+parametros.getString("destino"));
                saida.setText("Saida: "+parametros.getString("saida"));
                chegada.setText("Chegada: "+parametros.getString("chegada"));


            }
        }

        btnVolta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetalhesActivity.this, CadastroActivity.class));
            }
        });

        btnsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetalhesActivity.this, LoginActivity.class));
            }
        });

    }
}