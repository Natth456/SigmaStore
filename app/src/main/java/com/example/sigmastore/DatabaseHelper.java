package com.example.sigmastore;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "app.db";
    public static final int DB_VERSION = 2;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
// Tabela de usuários
        db.execSQL(
                "CREATE TABLE usuarios (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL," +
                        "cpf TEXT UNIQUE NOT NULL," +
                        "senha TEXT NOT NULL," +
                        "isAdm INTEGER NOT NULL DEFAULT 0)"
        );


// Tabela de produtos
        db.execSQL(
                "CREATE TABLE produtos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL," +
                        "preco REAL NOT NULL," +
                        "estoque INTEGER NOT NULL)"
        );


// Tabela de compras
        db.execSQL(
                "CREATE TABLE compras (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idUsuario INTEGER NOT NULL," +
                        "data TEXT NOT NULL," +
                        "total REAL NOT NULL," +
                        "FOREIGN KEY(idUsuario) REFERENCES usuarios(id))"
        );


// Itens da compra
        db.execSQL(
                "CREATE TABLE compra_itens (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "idCompra INTEGER NOT NULL," +
                        "idProduto INTEGER NOT NULL," +
                        "quantidade INTEGER NOT NULL," +
                        "preco REAL NOT NULL," +
                        "FOREIGN KEY(idCompra) REFERENCES compras(id)," +
                        "FOREIGN KEY(idProduto) REFERENCES produtos(id))"
        );


// Inserir um usuário admin padrão (senha: admin)
        db.execSQL("INSERT INTO usuarios (nome, cpf, senha, isAdm) VALUES ('Administrador','00000000000','admin',1)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS compra_itens");
        db.execSQL("DROP TABLE IF EXISTS compras");
        db.execSQL("DROP TABLE IF EXISTS produtos");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
}