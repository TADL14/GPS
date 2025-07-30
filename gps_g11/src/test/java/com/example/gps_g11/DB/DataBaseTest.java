package com.example.gps_g11.DB;

import org.junit.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseTest {
    @Test
    public void testConnect() {
        DataBase testDb = new DataBase(":memory:");
        Connection connection = testDb.connect();
        assertNotNull(connection, "A conexão deve ser estabelecida.");
    }

    @Test
    public void testCriarTabelas() {
        DataBase testDb = new DataBase(":memory:");
        testDb.criarTabelas();

        try (Statement stmt = testDb.connect().createStatement()) {
            // Verifica se a tabela "users" foi criada
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users';");
            assertTrue(rs.next(), "Tabela 'users' deve ser criada.");
        } catch (SQLException e) {
            fail("Erro ao verificar a tabela: " + e.getMessage());


        }
    }


    @Test
    public void testAdicionarUser() {
        DataBase testDb = new DataBase(":memory:");
        testDb.criarTabelas();

        testDb.adicionarUser("Joao", "Silva", "joao", "john@example.com", "password123");

        try (PreparedStatement pstmt = testDb.connect().prepareStatement("SELECT * FROM users WHERE username = ?")) {
            pstmt.setString(1, "joao");
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Usuário 'johndoe' deve ser encontrado no banco.");
            assertEquals("Joao", rs.getString("first_name"), "O nome do utilizador deve ser 'Joao'.");
        } catch (SQLException e) {
            fail("Erro ao verificar usuário: " + e.getMessage());
        }
    }

    @Test
    public void testVerificarCredenciais() {
        DataBase testDb = new DataBase(":memory:");
        testDb.criarTabelas();

        testDb.adicionarUser("Maria", "Silva", "maria", "maria@example.com", "password123");

        int userId = testDb.verificarCredenciais("maria", "password123");
        assertTrue(userId > 0, "ID do utilizador deve ser retornado para credenciais válidas.");

        int invalidId = testDb.verificarCredenciais("maria", "wrongpassword");
        assertEquals(-1, invalidId, "Deve retornar -1 para credenciais inválidas.");
    }




}