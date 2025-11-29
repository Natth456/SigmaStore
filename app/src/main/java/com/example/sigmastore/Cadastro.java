package com.example.sigmastore;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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


    DatabaseHelper dbHelper;


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

        dbHelper = new DatabaseHelper(this);

        btnVoltar.setOnClickListener(v -> startActivity(new Intent(Cadastro.this, MainActivity.class)));

        btnCadastrar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String cpf = edtCpf.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();
            int isAdm = swAdm.isChecked() ? 1 : 0;


            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }


            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("cpf", cpf);
            values.put("senha", senha);
            values.put("isAdm", isAdm);


            long id = db.insert("usuarios", null, values);
            if (id == -1) {
                Toast.makeText(this, "Erro ao cadastrar (CPF pode já existir)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}