package com.example.gps_g11.DB;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GardenDatabaseTest {
    // URL do banco de dados existente
    private static final String TEST_DB_URL = "jdbc:sqlite:garden_database.db";  // Aponta para o banco de dados já existente

    // Certifica-se de que o banco de dados existe e está configurado corretamente antes de rodar os testes
    @BeforeAll
    static void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL)) {
            if (conn != null) {
                System.out.println("Conectado ao banco de dados existente com sucesso!");
            }
        } catch (SQLException e) {
            fail("Falha ao conectar ao banco de dados existente: " + e.getMessage());
        }
    }


    // Testa se a verificação de uma planta pelo nome funciona corretamente
    @Test
    public void testCheckPlantByName() {
        // Teste para uma planta existente
        String plantName = "Cactus"; // Verifique se esta planta já está no banco de dados
        String result = GardenDatabase.checkPlantByName(plantName);
        assertNotNull(result, "A planta '" + plantName + "' deveria ser encontrada.");
        assertEquals(plantName, result, "O nome da planta não corresponde.");

        // Teste para uma planta não existente
        String notFound = GardenDatabase.checkPlantByName("Nonexistent Plant");
        assertNull(notFound, "Planta não existente não deveria ser encontrada.");
    }

    // Testa se retorna todos os nomes das plantas corretamente
    @Test
    public void testReturnAllPlantsNames() {
        // Verifica se as plantas já presentes no banco de dados são retornadas corretamente
        List<String> plantNames = GardenDatabase.returnAllPlantsNames();
        assertTrue(plantNames.contains("Cactus"), "Planta 'Aloe Vera' deveria estar na lista.");
        assertTrue(plantNames.contains("Spider Plant"), "Planta 'Spider Plant' deveria estar na lista.");
    }


}