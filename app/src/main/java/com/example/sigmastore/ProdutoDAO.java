package com.example.sigmastore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class ProdutoDAO {

    private DatabaseHelper dbHelper;

    // Recebe o contexto da Activity
    public ProdutoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ==============================
    // CADASTRAR PRODUTO
    // ==============================
    public boolean cadastrarProduto(Produto produto) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("nome", produto.getNome());
            cv.put("preco", produto.getPreco());
            cv.put("estoque", produto.getQuantidade());

            long result = db.insert("produtos", null, cv);

            return result != -1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }

    // ==============================
    // LISTAR PRODUTOS
    // ==============================
    public ArrayList<Produto> listarProdutos() {
        ArrayList<Produto> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = dbHelper.getReadableDatabase();
            c = db.rawQuery("SELECT id, nome, preco, estoque FROM produtos", null);

            while (c.moveToNext()) {
                Produto p = new Produto();
                p.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                p.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
                p.setPreco(c.getDouble(c.getColumnIndexOrThrow("preco")));
                p.setQuantidade(c.getInt(c.getColumnIndexOrThrow("estoque")));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (c != null) c.close();
            if (db != null && db.isOpen()) db.close();
        }

        return lista;
    }
    public boolean atualizarEstoque(int id, int novoEstoque) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("estoque", novoEstoque);

            int updated = db.update("produtos", cv, "id = ?", new String[]{String.valueOf(id)});
            return updated > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }
    public boolean excluirProduto(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int linhas = db.delete("produtos", "id = ?", new String[]{String.valueOf(id)});
        return linhas > 0;
    }
}