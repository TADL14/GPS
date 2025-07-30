package com.example.gps_g11.DB;

public class EnviaPedidosDB {

    private DataBase db;

    public EnviaPedidosDB(DataBase db) {
        this.db = db;
    }

    public boolean login(String username, String password) {
        int userId = db.verificarCredenciais(username, password);
        return userId != -1; // Retorna verdadeiro se as credenciais forem válidas
    }
    public boolean registrarUser(String firstName, String lastName, String username, String email, String password) {
        try {
            db.adicionarUser(firstName, lastName, username, email, password);
            return true; // Retorna verdadeiro se o registro foi bem-sucedido
        } catch (Exception e) {
            System.out.println("Erro ao registrar utilizador: " + e.getMessage());
            return false; // Retorna falso se o registro falhar
        }
    }


}
