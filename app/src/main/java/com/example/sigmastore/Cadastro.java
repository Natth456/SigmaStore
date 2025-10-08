package com.example.sigmastore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Cadastro extends AppCompatActivity {

    EditText edtNome, edtCpf, edtSenha;
    Switch swAdm;
    Button btnCadastrar, btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome = findViewById(R.id.nome_cadastro);
        edtCpf = findViewById(R.id.cpf_cadastro);
        edtSenha = findViewById(R.id.senha_cadastro);
        swAdm = findViewById(R.id.adm_switch);
        btnCadastrar = findViewById(R.id.cadastrar);
        btnVoltar = findViewById(R.id.voltar_cadastro);

        btnVoltar.setOnClickListener(v -> startActivity(new Intent(Cadastro.this, MainActivity.class)));

        btnCadastrar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String cpf = edtCpf.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();
            boolean isAdm = swAdm.isChecked();

            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setCpf(cpf);
            usuario.setSenha(senha);
            usuario.setAdmin(isAdm);

            new Thread(() -> {
                UsuarioDAO dao = new UsuarioDAO();
                boolean sucesso = dao.cadastrarUsuario(usuario);

                runOnUiThread(() -> {
                    if (sucesso) {
                        startActivity(new Intent(Cadastro.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}