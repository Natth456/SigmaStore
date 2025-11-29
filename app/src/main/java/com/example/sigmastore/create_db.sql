-- create_db.sql
-- Script SQL para criar o banco SQLite usado pelo app SigmaStore

BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    cpf TEXT UNIQUE NOT NULL,
    senha TEXT NOT NULL,
    is_admin INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS produtos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    qtd INTEGER DEFAULT 0,
    preco REAL DEFAULT 0.0
);

-- Inserir usuário administrador padrão (cpf 00000000000, senha admin)
INSERT OR IGNORE INTO usuarios (nome, cpf, senha, is_admin) VALUES ('Administrador', '00000000000', 'admin', 1);

COMMIT;
