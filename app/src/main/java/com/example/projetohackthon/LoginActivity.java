package com.example.projetohackthon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText editEmail, editSenha;
    private Button btEntrar;
    private ProgressBar progressBar;
    private CheckBox checkBoxRememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        btEntrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressbar);
        checkBoxRememberPassword = findViewById(R.id.lembrarsenha);

        IniciarComponentes();

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                // Adiciona a validação para campos vazios
                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    new AutenticacaoTask().execute(email, senha);

                    // Check if the "Remember Password" CheckBox is checked
                    if (checkBoxRememberPassword.isChecked()) {
                        // Save the password to SharedPreferences
                        savePasswordToPreferences(senha);
                    } else {
                        // Clear the saved password from SharedPreferences
                        clearPasswordFromPreferences();
                    }
                }
            }
        });
    }

    private void IniciarComponentes() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        btEntrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressbar);
        checkBoxRememberPassword = findViewById(R.id.lembrarsenha);

        // Load the saved state of the CheckBox
        checkBoxRememberPassword.setChecked(loadRememberPasswordState());
    }

    private void savePasswordToPreferences(String password) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("savedPassword", password);
        editor.putBoolean("rememberPassword", true);
        editor.apply();
    }

    private void clearPasswordFromPreferences() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("savedPassword");
        editor.putBoolean("rememberPassword", false);
        editor.apply();
    }

    private boolean loadRememberPasswordState() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        return preferences.getBoolean("rememberPassword", false);
    }

    private class AutenticacaoTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String login = params[0];
            String senha = params[1];

            try {
                URL url = new URL("http://10.0.61.140:3000/usuario?login=" + login + "&senha=" + senha);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();

                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String apiLogin = jsonObject.getString("login");
                    String apiSenha = jsonObject.getString("senha");

                    if (login.equals(apiLogin) && senha.equals(apiSenha)) {
                        startActivity(new Intent(LoginActivity.this, FuncoesActivity.class));
                        return "Login realizado com sucesso";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Não foi possível efetuar o login";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
