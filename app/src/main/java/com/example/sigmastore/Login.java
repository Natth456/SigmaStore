package com.example.sigmastore;


import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {


    EditText edtCpf, edtSenha;
    Button btnEntrar, btnSair;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtCpf = findViewById(R.id.Login_CPF);
        edtSenha = findViewById(R.id.senha_login);
        btnEntrar = findViewById(R.id.login_confirm);
        btnSair = findViewById(R.id.voltar_login);

        btnSair.setOnClickListener(v -> startActivity(new Intent(Login.this, MainActivity.class)));

        dbHelper = new DatabaseHelper(this);


        btnEntrar.setOnClickListener(v -> {
            String cpf = edtCpf.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();

            if (cpf.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha CPF e senha", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = dbHelper.getReadableDatabase();


                c = db.rawQuery(
                        "SELECT id, nome, isAdm, cpf, senha FROM usuarios WHERE cpf = ? AND senha = ?",
                        new String[]{cpf, senha}
                );

                if (c != null && c.moveToFirst()) {
                    int userId = c.getInt(c.getColumnIndexOrThrow("id"));
                    String nome = c.getString(c.getColumnIndexOrThrow("nome"));
                    int isAdm = c.getInt(c.getColumnIndexOrThrow("isAdm"));

                    Toast.makeText(this, "Bem-vindo, " + nome, Toast.LENGTH_SHORT).show();

                    Intent it;
                    if (isAdm == 1) {
                        it = new Intent(this, Tela_Adm.class);
                    } else {
                        it = new Intent(this, Tela_Principal.class);
                    }
                    it.putExtra("userId", userId);
                    it.putExtra("userName", nome);
                    startActivity(it);
                    finish();
                } else {

                    Toast.makeText(this, "CPF ou senha incorretos", Toast.LENGTH_SHORT).show();

                    Cursor cCpf = db.rawQuery("SELECT id, nome, cpf, senha, isAdm FROM usuarios WHERE cpf = ?", new String[]{cpf});
                    if (cCpf != null && cCpf.moveToFirst()) {
                        String foundNome = cCpf.getString(cCpf.getColumnIndexOrThrow("nome"));
                        String foundSenha = cCpf.getString(cCpf.getColumnIndexOrThrow("senha"));
                        int foundId = cCpf.getInt(cCpf.getColumnIndexOrThrow("id"));
                        cCpf.close();

                        // Aviso ao usuário (não mostrar senha em produção; só para debug local)
                        new AlertDialog.Builder(this)
                                .setTitle("CPF encontrado")
                                .setMessage("Existe um usuário com esse CPF (id=" + foundId + ", nome=" + foundNome + ").\nVerifique a senha")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Não encontrado")
                                .setMessage("Não existe usuário com esse CPF registrado")
                                .setNegativeButton("OK", null)
                                .show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao acessar o banco: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                if (c != null) c.close();
                if (db != null && db.isOpen()) db.close();
            }
        });
    }
}