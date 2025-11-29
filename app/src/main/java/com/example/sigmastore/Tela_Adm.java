package com.example.sigmastore;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Tela_Adm extends AppCompatActivity {

    EditText edtNome, edtQtd, edtPreco;
    Button btnVoltar, btnAdicionar;
    ListView listaProdutos;

    ArrayList<Produto> produtos;
    ArrayAdapter<Produto> adapter;
    ProdutoDAO produtoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adm);

        edtNome = findViewById(R.id.Nome_Produto);
        edtQtd = findViewById(R.id.Quantidade_Produto);
        edtPreco = findViewById(R.id.Preco_Produto);
        btnVoltar = findViewById(R.id.voltar_adm);
        btnAdicionar = findViewById(R.id.adicionar_produto);
        listaProdutos = findViewById(R.id.lista_produtos_adm);
        produtoDAO = new ProdutoDAO(this);

        carregarProdutos();

        listaProdutos.setOnItemClickListener((parent, view, position, id) -> {
            Produto selecionado = produtos.get(position);
            mostrarDialogoExcluir(selecionado);
        });

        btnAdicionar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String qtdStr = edtQtd.getText().toString().trim();
            String precoStr = edtPreco.getText().toString().trim();

            if (nome.isEmpty() || qtdStr.isEmpty() || precoStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int qtd = Integer.parseInt(qtdStr);
                double preco = Double.parseDouble(precoStr);

                Produto p = new Produto();
                p.setNome(nome);
                p.setQuantidade(qtd);
                p.setPreco(preco);

                new Thread(() -> {
                    boolean sucesso = produtoDAO.cadastrarProduto(p);

                    runOnUiThread(() -> {
                        if (sucesso) {
                            limparCampos();
                            carregarProdutos();
                        } else {
                            Toast.makeText(this, "Erro ao cadastrar produto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantidade e preço devem ser numéricos", Toast.LENGTH_SHORT).show();
            }
        });

        btnVoltar.setOnClickListener(v -> startActivity(new Intent(Tela_Adm.this, MainActivity.class)));
    }

    private void carregarProdutos() {
        new Thread(() -> {
            produtos = produtoDAO.listarProdutos();

            runOnUiThread(() -> {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, produtos);
                listaProdutos.setAdapter(adapter);
            });
        }).start();
    }

    private void limparCampos() {
        edtNome.setText("");
        edtQtd.setText("");
        edtPreco.setText("");
    }
    private void mostrarDialogoExcluir(Produto produto) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Produto")
                .setMessage("Deseja realmente excluir o produto:\n\n" +
                        produto.getNome() + " ?")
                .setPositiveButton("Excluir", (dialog, which) -> excluirProduto(produto))
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void excluirProduto(Produto produto) {
        new Thread(() -> {
            boolean sucesso = produtoDAO.excluirProduto(produto.getId());

            runOnUiThread(() -> {
                if (sucesso) {
                    Toast.makeText(this, "Produto excluído", Toast.LENGTH_SHORT).show();
                    carregarProdutos();
                } else {
                    Toast.makeText(this, "Erro ao excluir produto", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}