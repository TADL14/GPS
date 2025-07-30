package com.example.gps_g11.DB;

import com.example.gps_g11.LoginSignUp.LoginControl;
import com.example.gps_g11.GardenPlant.Plant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private Connection conn = null;
    private String dbPath;

    public DataBase(String dbPath) {
        this.dbPath = dbPath;
    }

    // Método para obter uma conexão à base de dados
    public Connection connect() {
        if (conn == null) {
            try {
                String url = "jdbc:sqlite:" + dbPath;
                conn = DriverManager.getConnection(url);
                System.out.println("Conexão com a base de dados estabelecida.");
            } catch (SQLException e) {
                System.out.println("Erro ao conectar à base de dados: " + e.getMessage());
            }
        }
        return conn;
    }
    public void criarTabelas() {

        String sqlCreateUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "username TEXT UNIQUE NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        String sqlCreatePlantsGarden = "CREATE TABLE IF NOT EXISTS plantsGarden (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "common_name TEXT," +
                "image_path TEXT" +
                ");";

        String sqlCreateUsersPlantsGarden = "CREATE TABLE IF NOT EXISTS usersPlantsGarden (" +
                "user_id INTEGER NOT NULL," +              // Foreign key to users
                "plant_id INTEGER NOT NULL," +             // Foreign key to plantsGarden
                "PRIMARY KEY (user_id, plant_id)," +       // Composite primary key for uniqueness
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                "FOREIGN KEY (plant_id) REFERENCES plantsGarden(id) ON DELETE CASCADE" +
                ");";

        try (Statement stmt = connect().createStatement()) {
            stmt.execute(sqlCreateUsers);
            stmt.execute(sqlCreatePlantsGarden);
            stmt.execute(sqlCreateUsersPlantsGarden);
            System.out.println("Tabelas criadas com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public static Integer getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = getStaticConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                System.out.println("Utilizador não encontrado: " + username);
                return -1;  // Retorna null se o utilizador não for encontrado
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ID do utilizador: " + e.getMessage());
            return -1;
        }
    }
    public static List<String> getNamesPlantsByUserId() {
        int userId = getUserIdByUsername(LoginControl.user);  // Assuming `LoginControl.user` holds the logged-in username.

        if (userId == -1) {  // If the user is not found, return null or an empty list.
            System.out.println("User not found.");
            return null;
        }

        String sql = "SELECT pg.common_name " +
                "FROM plantsGarden pg " +
                "JOIN usersPlantsGarden upg ON pg.id = upg.plant_id " +
                "WHERE upg.user_id = ?";

        List<String> plantList = new ArrayList<>();

        try (Connection conn = getStaticConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String commonName = rs.getString("common_name");
                plantList.add(commonName);
            }

            if (plantList.isEmpty()) {
                System.out.println("No plants found for user ID: " + userId);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving plants: " + e.getMessage());
            return null;
        }

        return plantList;
    }

    public static void savePlantForUser(String plantname) {
        Plant plant = PlantDatabase.getPlantByName(plantname);
        int userId = getUserIdByUsername(LoginControl.user);
        if (userId==-1){
            System.out.println( "Error: User not found.");
            return;
        }

        if( plant==null){
            System.out.println( "Error: Plant not found.");
            return;
        }

        String insertPlantSQL = "INSERT INTO plantsGarden (common_name, image_path) VALUES (?, ?) RETURNING id;";
        String linkPlantToUserSQL = "INSERT INTO usersPlantsGarden (user_id, plant_id) VALUES (?, ?);";

        try (Connection conn = getStaticConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            int plantId = 0;

            // Step 1: Insert plant into plantsGarden and get the generated plant ID
            try (PreparedStatement pstmtPlant = conn.prepareStatement(insertPlantSQL)) {
                pstmtPlant.setString(1, plant.getName());
                pstmtPlant.setString(2, plant.getImagePath());
                ResultSet rs = pstmtPlant.executeQuery();

                if (rs.next()) {
                    plantId = rs.getInt("id");
                } else {
                    conn.rollback();
                    System.out.println( "Error: Unable to save the plant.");
                    return;
                }
            }

            // Step 2: Link the plant to the user in usersPlantsGarden
            try (PreparedStatement pstmtLink = conn.prepareStatement(linkPlantToUserSQL)) {
                pstmtLink.setInt(1, userId);
                pstmtLink.setInt(2, plantId);
                pstmtLink.executeUpdate();
            }

            conn.commit();  // Commit transaction
            System.out.println( "Plant successfully saved and linked to user.");
            return;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving plant for user: " + e.getMessage());
            return;
        }
    }

    public Connection getConnection() {
        return connect() ;
    }

    public static Connection getStaticConnection() {
        String  dbPath = "SproutSmart.db";
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(url);
            System.out.println("Conexão com a base de dados estabelecida.");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar à base de dados: " + e.getMessage());
        }
        return connection;
    }

    public int verificarCredenciais(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";


        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id"); // Retorna o id do usuário
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar credenciais: " + e.getMessage());
        }
        return -1; // Retorna -1 se as credenciais não foram verificadas
    }


    public void adicionarUser(String firstName, String lastName, String username, String email, String password) {
        String sql = "INSERT INTO users (first_name, last_name, username, email, password) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, firstName); // Nome
            pstmt.setString(2, lastName);  // Sobrenome
            pstmt.setString(3, username);  // Nome de utilizador
            pstmt.setString(4, email);     // E-mail
            pstmt.setString(5, password);   // Senha

            pstmt.executeUpdate();
            System.out.println("User adicionado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar user: " + e.getMessage());
        }
    }


}
