package com.example.sigmastore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticarUsuario(String nome, String cpf, String senha) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND cpf = ? AND senha = ?";

        try (Connection conn = Conn_Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, senha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setAdmin(rs.getBoolean("is_admin"));
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao autenticar: " + e.getMessage());
        }

        return usuario;
    }

    public boolean cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, cpf, senha, is_admin) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conn_Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getSenha());
            stmt.setBoolean(4, usuario.isAdmin());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
            return false;
        }
    }
}