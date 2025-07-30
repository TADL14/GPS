package com.example.gps_g11.DB;

import com.example.gps_g11.GardenPlant.Plant;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class PlantDatabaseTest {
    private static final String TEST_DB_URL = "jdbc:sqlite:test_plant_database.db";

    @BeforeAll
    static void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL)) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS plants (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    common_name TEXT,
                    scientific_name TEXT,
                    environment TEXT,
                    leaf_color TEXT,
                    blooms TEXT,
                    growth_season TEXT,
                    maintenance TEXT,
                    pet_safe TEXT,
                    allergenic TEXT,
                    size TEXT,
                    humidity_requirement TEXT,
                    image_path TEXT,
                    light TEXT,
                    water TEXT,
                    temperature TEXT,
                    soil TEXT
                );
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
                pstmt.execute();
            }

            String insertSQL = """
                INSERT INTO plants (common_name, scientific_name, environment, leaf_color,
                blooms, growth_season, maintenance, pet_safe, allergenic, size, humidity_requirement,
                image_path, light, water, temperature, soil)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "Spider Plant");
                pstmt.setString(2, "Chlorophytum comosum");
                pstmt.setString(3, "Indoor");
                pstmt.setString(4, "Green, striped");
                pstmt.setString(5, "Yes, small white flowers");
                pstmt.setString(6, "Spring, summer");
                pstmt.setString(7, "Low");
                pstmt.setString(8, "Yes");
                pstmt.setString(9, "Low");
                pstmt.setString(10, "Medium");
                pstmt.setString(11, "Medium");
                pstmt.setString(12, "images/spider_plant.jpg");
                pstmt.setString(13, "Indirect light");
                pstmt.setString(14, "Moderate, water when soil is dry");
                pstmt.setString(15, "15-25°C");
                pstmt.setString(16, "Well-draining, nutrient-rich");
                pstmt.execute();
            }
        } catch (Exception e) {
            fail("Failed to setup database: " + e.getMessage());
        }
    }

    @Test
    public void testGetPlantByName() {
        Plant result = PlantDatabase.getPlantByName("Spider Plant");
        assertNotNull(result, "Plant should be found");
        assertEquals("Spider Plant", result.getName(), "Plant name should match");
    }

    @Test
    public void testGetPlantByName_NotFound() {
        Plant result = PlantDatabase.getPlantByName("Nonexistent Plant");
        assertNull(result, "Should return null for non-existent plant");
    }

    @Test
    public void testGetPlantsName() {
        String names = PlantDatabase.getPlantsName();
        assertTrue(names.contains("Spider Plant"), "Plant name should be listed");
    }

    @Test
    public void testGetImage() {
        String imagePath = PlantDatabase.getImage("Spider Plant");
        assertEquals("images/spider_plant.jpg",
                imagePath.replaceFirst("^/", ""),
                "Image path should match");
    }

    @Test
    public void testGetImage_NotFound() {
        String imagePath = PlantDatabase.getImage("Nonexistent Plant");
        assertNull(imagePath, "Should return null for non-existent plant");
    }

    @AfterAll
    static void tearDownDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL)) {
            String dropTableSQL = "DROP TABLE IF EXISTS plants;";
            try (PreparedStatement pstmt = conn.prepareStatement(dropTableSQL)) {
                pstmt.execute();
            }
        } catch (Exception e) {
            System.out.println("Failed to clean up database: " + e.getMessage());
        }
    }

}