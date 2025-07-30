package com.example.gps_g11.DB;

import com.example.gps_g11.GardenPlant.Plant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GardenDatabase {
    private static String url = "jdbc:sqlite:garden_database.db";
    public static void addPlantToGarden(String plantName) {

        // Obtém a informação da planta da classe PlantDatabase
        Plant plantInfo = PlantDatabase.getPlantByName(plantName);

        // Verifica se a informação obtida corresponde ao nome da planta
     /*   if (plantInfo != null) {

            String insertSQL = "INSERT INTO garden_plants (common_name, light, water, temperature, soil, image_path) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

                // Configura os parâmetros no PreparedStatement com os dados obtidos de plantInfo
               // pstmt.setString(1, plantName);                // Nome da planta
                pstmt.setString(2, null);      // Luz necessária
                pstmt.setString(3,null);      // Necessidade de água
                pstmt.setString(4, plantInfo.getTemperature()); // Temperatura ideal
                pstmt.setString(5, plantInfo.getSoil());       // Tipo de solo
                pstmt.setString(6, plantInfo.getImagePath());  // Caminho da imagem

                // Executa a inserção no banco de dados
                pstmt.executeUpdate();
                System.out.println("Planta '" + plantName + "' adicionada ao jardim com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao adicionar planta ao jardim: " + e.getMessage());
            }
        } else {
            System.out.println("Planta '" + plantName + "' não encontrada na base de dados.");
        } */
    }

    public static String checkPlantByName(String plantName) {
        List<String> plantNames = new ArrayList<>();
        String selectSQL = "SELECT common_name FROM garden_plants";  // SQL query to fetch plant names

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            // Iterate over the results and add plant names to the list
            while (rs.next()) {
                plantNames.add(rs.getString("common_name"));
            }

            // Check if the provided plantName exists in the list
            if (plantNames.contains(plantName)) {
                return plantName;  // Return the plant name if found
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving plant names: " + e.getMessage());
        }

        return null;  // Return null if plant name does not exist
    }
    public static List<String> returnAllPlantsNames() {
        List<String> plantNames = new ArrayList<>();
        String selectSQL = "SELECT common_name FROM garden_plants";  // Consulta para buscar os nomes das plantas

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            // Itera sobre os resultados e adiciona os nomes à lista
            while (rs.next()) {
                plantNames.add(rs.getString("common_name"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao recuperar os nomes das plantas: " + e.getMessage());
        }

        return plantNames;
    }

    public static void clearGardenTable() {
        String deleteSQL = "DELETE FROM garden_plants";  // Deleta todos os registros da tabela

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            // Executa a deleção dos registros
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " registro(s) apagado(s) da tabela garden_plants.");

        } catch (SQLException e) {
            System.out.println("Erro ao apagar registros da tabela garden_plants: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        // URL do banco de dados SQLite

        // Cria uma conexão com o banco de dados
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                // Cria a tabela garden_plants
                String createTableSQL = "CREATE TABLE IF NOT EXISTS garden_plants ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "common_name TEXT,"
                        + "light TEXT,"
                        + "water TEXT,"
                        + "temperature TEXT,"
                        + "soil TEXT,"
                        + "image_path TEXT"
                        + ");";

                try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
                    pstmt.execute();
                }

                // Exemplo de dados das plantas
                String insertSQL = "INSERT INTO garden_plants (common_name, light, water, temperature, soil, image_path) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

                    String[][] plantsData = {
                            // Exemplo
                            {"Aloe Vera", "Bright, indirect to direct light", "Minimal, drought-tolerant", "15-25°C", "Sandy, well-draining", "images/aloe_vera.jpg"},
                            {"Spider Plant", "Indirect light", "Moderate, water when soil is dry", "15-25°C", "Well-draining, nutrient-rich", "images/spider_plant.jpg"}
                    };

                    for (String[] plant : plantsData) {
                        for (int i = 1; i <= plant.length; i++) {
                            pstmt.setString(i, plant[i - 1]);
                        }
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }

                System.out.println("Tabela garden_plants criada e dados inseridos com sucesso.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
