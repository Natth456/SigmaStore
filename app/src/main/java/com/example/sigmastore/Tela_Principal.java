package com.example.sigmastore;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Tela_Principal extends AppCompatActivity {

    ListView listViewProdutos;
    Button btnCarrinho, btnVoltar;

    ArrayList<Produto> listaProdutos = new ArrayList<>();
    ArrayList<Produto> carrinho = new ArrayList<>();
    ProdutoDAO produtoDAO = new ProdutoDAO();

    ArrayAdapter<String> adapter;

    private String usuarioNome = "João da Silva";
    private String usuarioCpf = "123.456.789-00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        listViewProdutos = findViewById(R.id.Produtos);
        btnCarrinho = findViewById(R.id.carrinho);
        btnVoltar = findViewById(R.id.voltar_tela);

        Intent it = getIntent();
        if (it != null) {
            String n = it.getStringExtra("usuarioNome");
            String c = it.getStringExtra("usuarioCpf");
            if (n != null && !n.isEmpty()) usuarioNome = n;
            if (c != null && !c.isEmpty()) usuarioCpf = c;
        }

        carregarProdutos();

        btnCarrinho.setOnClickListener(v -> emitirNotaFiscal());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void carregarProdutos() {
        listaProdutos = produtoDAO.listarProdutos();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getNomesProdutos()
        );
        listViewProdutos.setAdapter(adapter);

        listViewProdutos.setOnItemClickListener((parent, view, position, id) -> {
            Produto produto = listaProdutos.get(position);
            mostrarDetalhesProduto(produto);
        });

        if (listaProdutos.isEmpty()) {
            Toast.makeText(this, "Nenhum produto cadastrado", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> getNomesProdutos() {
        ArrayList<String> nomes = new ArrayList<>();
        for (Produto p : listaProdutos) {
            nomes.add(p.getNome() + " - R$ " + String.format("%.2f", p.getPreco()) + " (" + p.getQuantidade() + " un.)");
        }
        return nomes;
    }

    private void mostrarDetalhesProduto(Produto produto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(produto.getNome());
        builder.setMessage("Preço: R$ " + String.format("%.2f", produto.getPreco()) +
                "\nEstoque: " + produto.getQuantidade() +
                "\n\nDeseja adicionar ao carrinho?");

        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            if (produto.getQuantidade() > 0) {
                carrinho.add(produto);
                int novoEstoque = produto.getQuantidade() - 1;
                produto.setQuantidade(novoEstoque);
                boolean ok = atualizarEstoqueBanco(produto);

                if (!ok) {
                    produto.setQuantidade(novoEstoque + 1);
                    carrinho.remove(produto);
                    Toast.makeText(this, "Erro ao atualizar estoque no banco", Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter.clear();
                adapter.addAll(getNomesProdutos());
                adapter.notifyDataSetChanged();

                Toast.makeText(this, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Produto esgotado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private boolean atualizarEstoqueBanco(Produto produto) {
        String sql = "UPDATE produtos SET estoque = ? WHERE id = ?";
        try (Connection conn = Conn_Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produto.getQuantidade());
            stmt.setInt(2, produto.getId());
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void emitirNotaFiscal() {
        if (carrinho.isEmpty()) {
            Toast.makeText(this, "Carrinho vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = 0;
        StringBuilder nota = new StringBuilder();
        nota.append("🧾  NOTA FISCAL\n");
        nota.append("------------------------------\n");
        nota.append("Nome: ").append(usuarioNome).append("\n");
        nota.append("CPF: ").append(usuarioCpf).append("\n\n");
        nota.append("Produtos:\n");

        for (Produto p : carrinho) {
            nota.append("• ").append(p.getNome())
                    .append("  -  R$ ").append(String.format("%.2f", p.getPreco()))
                    .append("\n");
            total += p.getPreco();
        }

        nota.append("------------------------------\n");
        nota.append("Total: R$ ").append(String.format("%.2f", total)).append("\n");
        nota.append("------------------------------");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nota Fiscal");
        builder.setMessage(nota.toString());
        builder.setPositiveButton("OK", (dialog, which) -> {
            carrinho.clear();
            Toast.makeText(this, "Compra finalizada", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}