package com.example.sigmastore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText edtNome, edtCpf, edtSenha;
    Button btnVoltar, btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtNome = findViewById(R.id.Login_Nome);
        edtCpf = findViewById(R.id.Login_CPF);
        edtSenha = findViewById(R.id.senha_login);
        btnVoltar = findViewById(R.id.voltar_login);
        btnEntrar = findViewById(R.id.login_confirm);

        btnVoltar.setOnClickListener(v -> startActivity(new Intent(Login.this, MainActivity.class)));

        btnEntrar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String cpf = edtCpf.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();

            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                UsuarioDAO dao = new UsuarioDAO();
                Usuario usuario = dao.autenticarUsuario(nome, cpf, senha);

                runOnUiThread(() -> {
                    if (usuario != null) {
                        if (usuario.isAdmin()) {
                            Intent intent = new Intent(this, Tela_Adm.class);
                            intent.putExtra("usuario_id", usuario.getId());
                            intent.putExtra("usuario_nome", usuario.getNome());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(this, Tela_Principal.class);
                            intent.putExtra("usuario_id", usuario.getId());
                            intent.putExtra("usuario_nome", usuario.getNome());
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        Toast.makeText(this, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}