package com.example.gps_g11.DB;

import com.example.gps_g11.GardenPlant.Plant;

import java.sql.*;

public class PlantDatabase {
    private static String url = "jdbc:sqlite:plant_database.db";


    public static Plant getPlantByName(String name) {
        String query = "SELECT * FROM plants WHERE common_name = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Plant(rs.getString("common_name"),
                           null, null, true, null, null, true, null,null,null);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving plant: " + e.getMessage());
        }
        return null;
    }
    public static String getPlantsName() {
        StringBuilder plantNames = new StringBuilder();
        String query = "SELECT common_name FROM plants";

        // Establish a connection to the SQLite database
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Retrieve and concatenate plant names
            while (rs.next()) {
                plantNames.append(rs.getString("common_name")).append(", ");
            }

            // Remove the trailing comma and space, if any
            if (plantNames.length() > 0) {
                plantNames.setLength(plantNames.length() - 2);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving plant names: " + e.getMessage());
        }

        return plantNames.toString();
    }



    public static String getImage(String plantsName) {
        // SQL query to retrieve the image path
        String query = "SELECT image_path FROM plants WHERE common_name = ?";
        String imagePath = null;

        // Establish a connection to the SQLite database
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the value of the parameter in the query
            pstmt.setString(1, plantsName);
            // Execute the query and process the result
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the image path
                    imagePath = rs.getString("image_path");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving image path: " + e.getMessage());
        }

        return imagePath;
    }


    public static void main(String[] args) {
        // URL do banco de dados SQLite


        // Cria uma conexão com o banco de dados
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                // Cria a tabela de plantas
                String createTableSQL = "CREATE TABLE IF NOT EXISTS plants ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "common_name TEXT,"
                        + "scientific_name TEXT,"
                        + "environment TEXT,"
                        + "leaf_color TEXT,"
                        + "blooms TEXT,"
                        + "growth_season TEXT,"
                        + "maintenance TEXT,"
                        + "pet_safe TEXT,"
                        + "allergenic TEXT,"
                        + "size TEXT,"
                        + "humidity_requirement TEXT,"
                        + "image_path TEXT,"
                        + "light TEXT,"
                        + "water TEXT,"
                        + "temperature TEXT,"
                        + "soil TEXT"
                        + ");";

                try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
                    pstmt.execute();
                }

                // Dados das 50 plantas
                String insertSQL = "INSERT INTO plants "
                        + "(common_name, scientific_name, environment, leaf_color, blooms, "
                        + "growth_season, maintenance, pet_safe, allergenic, size, "
                        + "humidity_requirement, image_path, light, water, temperature, soil) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    // Adicione os dados das plantas aqui
                    String[][] plantsData = {
                            {"Pothos", "Epipremnum aureum", "Indoor", "Green, variegated", "No", "All seasons", "Low", "No", "Low", "Medium", "Medium", "images/pothos.jpg", "Bright, indirect light", "Water when soil is dry", "15-30°C", "Well-draining"},
                            {"Spider Plant", "Chlorophytum comosum", "Indoor", "Green, striped", "Yes, small white flowers", "Spring, summer", "Low", "Yes", "Low", "Medium", "Medium", "images/spider_plant.jpg", "Indirect light", "Moderate, water when soil is dry", "15-25°C", "Well-draining, nutrient-rich"},
                            {"ZZ Plant", "Zamioculcas zamiifolia", "Indoor", "Dark green", "Rarely", "Spring, summer", "Low", "No", "Low", "Medium", "Low to medium", "images/zz_plant.jpg", "Low to bright, indirect light", "Minimal watering", "18-26°C", "Well-draining"},
                            {"Snake Plant", "Sansevieria trifasciata", "Indoor", "Green with yellow edges", "Rarely, small white flowers", "All seasons", "Low", "No", "Low", "Small to medium", "Low", "images/snake_plant.jpg", "Low to bright, indirect light", "Minimal, water when completely dry", "15-29°C", "Sandy, well-draining"},
                            {"Aloe Vera", "Aloe vera", "Indoor, outdoor", "Light green", "Rarely, yellow flowers", "Spring, summer", "Low", "No", "Low", "Small to medium", "Low", "images/aloe_vera.jpg", "Bright, indirect to direct light", "Minimal, drought-tolerant", "15-25°C", "Sandy, well-draining"},
                            {"Cactus", "Cactaceae", "Indoor, outdoor", "Green", "Yes, various colors", "Spring", "Low", "Yes", "Low", "Small to large", "Low", "images/cactus.jpg", "Full sunlight", "Minimal watering", "20-35°C", "Sandy, well-draining"},
                            {"Lavender", "Lavandula angustifolia", "Outdoor", "Gray-green", "Yes, purple flowers", "Summer", "Low", "Yes", "Low", "Small to medium", "Low", "images/lavender.jpg", "Full sunlight", "Minimal, drought-tolerant", "10-30°C", "Well-draining, slightly sandy"},
                            {"Peace Lily", "Spathiphyllum", "Indoor", "Dark green", "Yes, white flowers", "Spring, summer", "Low", "No", "Low", "Medium", "High", "images/peace_lily.jpg", "Low to medium, indirect light", "Moderate, keep soil moist", "18-27°C", "Well-draining"},
                            {"Rubber Plant", "Ficus elastica", "Indoor", "Dark green", "Rarely", "Spring, summer", "Medium", "No", "Low", "Medium to large", "Medium", "images/rubber_plant.jpg", "Bright, indirect light", "Water when soil is dry", "15-25°C", "Well-draining"},
                            {"Bird of Paradise", "Strelitzia reginae", "Indoor, outdoor", "Dark green", "Yes, orange and blue flowers", "Spring, summer", "Medium", "No", "Low", "Large", "Medium to high", "images/bird_of_paradise.jpg", "Bright, indirect light", "Moderate, keep soil slightly moist", "18-30°C", "Rich, well-draining"},
                            {"Monstera", "Monstera deliciosa", "Indoor", "Dark green", "Rarely", "All seasons", "Medium", "No", "Low", "Medium to large", "High", "images/monstera.jpg", "Bright, indirect light", "Moderate, water when soil is partially dry", "18-27°C", "Nutrient-rich, well-draining"},
                            {"Fiddle Leaf Fig", "Ficus lyrata", "Indoor", "Dark green", "Rarely", "Spring, summer", "Medium", "No", "Low", "Large", "Medium to high", "images/fiddle_leaf_fig.jpg", "Bright, indirect light", "Moderate, keep soil moist", "18-24°C", "Well-draining, nutrient-rich"},
                            {"Hibiscus", "Hibiscus rosa-sinensis", "Outdoor", "Green", "Yes, large flowers", "Spring, summer", "Medium", "Yes", "Low", "Medium to large", "High", "images/hibiscus.jpg", "Full sunlight", "Moderate, keep soil moist", "15-30°C", "Well-draining, nutrient-rich"},
                            {"Orchid", "Orchidaceae", "Indoor", "Green", "Yes, various colors", "Spring, summer", "Medium", "Yes", "Low", "Small to medium", "High", "images/orchid.jpg", "Bright, indirect light", "Moderate, keep slightly moist", "18-24°C", "Well-draining"},
                            {"Chinese Evergreen", "Aglaonema", "Indoor", "Green, variegated", "Rarely", "All seasons", "Low", "No", "Low", "Medium", "Medium", "images/chinese_evergreen.jpg", "Low to medium light", "Moderate, water when soil is dry", "15-25°C", "Well-draining"},
                            {"Geranium", "Pelargonium", "Outdoor", "Green", "Yes, various colors", "Spring, summer", "Medium", "Yes", "Low", "Medium", "Low", "images/geranium.jpg", "Full sunlight", "Moderate, keep soil slightly moist", "15-25°C", "Well-draining"},
                            {"Camellia", "Camellia japonica", "Outdoor", "Dark green", "Yes, various colors", "Winter, spring", "Medium", "Yes", "Low", "Medium to large", "Medium", "images/camellia.jpg", "Partial shade", "Moderate, keep soil moist", "10-25°C", "Well-draining, acidic"},
                            {"Maranta", "Maranta leuconeura", "Indoor", "Green, variegated", "Rarely", "Spring, summer", "Medium", "Yes", "Low", "Small to medium", "High", "images/maranta.jpg", "Low to medium, indirect light", "Moderate, keep soil moist", "18-24°C", "Well-draining"},
                            {"Hoya", "Hoya carnosa", "Indoor", "Green", "Yes, star-shaped flowers", "Spring, summer", "Low", "Yes", "Low", "Medium", "Medium", "images/hoya.jpg", "Bright, indirect light", "Water when soil is dry", "15-27°C", "Well-draining"},
                            {"Philodendron", "Philodendron spp.", "Indoor", "Green", "Rarely", "All seasons", "Low", "No", "Low", "Medium to large", "Medium", "images/philodendron.jpg", "Low to bright, indirect light", "Moderate, water when soil is dry", "18-27°C", "Well-draining"},
                            {"Chrysanthemum", "Chrysanthemum morifolium", "Outdoor", "Green", "Yes, various colors", "Fall", "Medium", "Yes", "Low", "Medium", "Medium", "images/chrysanthemum.jpg", "Full sunlight", "Moderate, keep soil moist", "15-20°C", "Well-draining"},
                            {"Fern", "Nephrolepis exaltata", "Indoor", "Green", "No", "All seasons", "Medium", "Yes", "Low", "Medium", "High", "images/fern.jpg", "Low to medium, indirect light", "Moderate, keep soil moist", "18-24°C", "Well-draining"},
                            {"Bougainvillea", "Bougainvillea spp.", "Outdoor", "Green", "Yes, various colors", "Spring, summer", "Medium", "Yes", "Low", "Large", "Low", "images/bougainvillea.jpg", "Full sunlight", "Moderate, keep soil slightly dry", "15-30°C", "Well-draining"},
                            {"Snapdragon", "Antirrhinum majus", "Outdoor", "Green", "Yes, various colors", "Spring, summer", "Medium", "Yes", "Low", "Medium", "Medium", "images/snapdragon.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Zinnia", "Zinnia elegans", "Outdoor", "Green", "Yes, various colors", "Summer", "Low", "Yes", "Low", "Medium", "Low", "images/zinnia.jpg", "Full sunlight", "Moderate, keep soil moist", "20-30°C", "Well-draining"},
                            {"Petunia", "Petunia hybrida", "Outdoor", "Green", "Yes, various colors", "Spring, summer", "Medium", "Yes", "Low", "Medium", "Medium", "images/petunia.jpg", "Full sunlight", "Moderate, keep soil moist", "15-30°C", "Well-draining"},
                            {"Foxglove", "Digitalis purpurea", "Outdoor", "Green", "Yes, purple flowers", "Summer", "Medium", "No", "Low", "Medium", "Medium", "images/foxglove.jpg", "Partial shade to full sunlight", "Moderate, keep soil moist", "10-20°C", "Well-draining"},
                            {"Amaryllis", "Hippeastrum", "Indoor, outdoor", "Green", "Yes, large flowers", "Winter, spring", "Medium", "No", "Low", "Medium", "Medium", "images/amaryllis.jpg", "Bright, indirect light", "Moderate, keep soil slightly moist", "15-25°C", "Well-draining"},
                            {"Nasturtium", "Tropaeolum majus", "Outdoor", "Green", "Yes, various colors", "Summer", "Low", "Yes", "Low", "Medium", "Medium", "images/nasturtium.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Begonia", "Begonia spp.", "Indoor, outdoor", "Green, various colors", "Yes, various colors", "Spring, summer", "Medium", "No", "Low", "Medium", "Medium to high", "images/begonia.jpg", "Bright, indirect light", "Moderate, keep soil moist", "18-24°C", "Well-draining"},
                            {"Kalanchoe", "Kalanchoe blossfeldiana", "Indoor", "Green", "Yes, various colors", "Spring, summer", "Low", "No", "Low", "Small", "Low", "images/kalanchoe.jpg", "Bright, indirect light", "Minimal, drought-tolerant", "15-25°C", "Well-draining"},
                            {"Coleus", "Plectranthus scutellarioides", "Indoor, outdoor", "Various colors", "Rarely", "Spring, summer", "Medium", "Yes", "Low", "Medium", "Medium", "images/coleus.jpg", "Bright, indirect light", "Moderate, keep soil moist", "18-24°C", "Well-draining"},
                            {"Sweet Potato Vine", "Ipomoea batatas", "Outdoor", "Green, purple", "Rarely", "Summer", "Low", "Yes", "Low", "Medium", "Medium", "images/sweet_potato_vine.jpg", "Full sunlight", "Moderate, keep soil moist", "20-30°C", "Well-draining"},
                            {"Cilantro", "Coriandrum sativum", "Indoor, outdoor", "Green", "Yes, white flowers", "Spring, fall", "Low", "Yes", "Low", "Small", "Medium", "images/cilantro.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Thyme", "Thymus vulgaris", "Indoor, outdoor", "Green", "Yes, small purple flowers", "Spring, summer", "Low", "Yes", "Low", "Small", "Low", "images/thyme.jpg", "Full sunlight", "Minimal, drought-tolerant", "15-30°C", "Well-draining"},
                            {"Rosemary", "Rosmarinus officinalis", "Indoor, outdoor", "Green", "Yes, blue flowers", "Spring, summer", "Low", "Yes", "Low", "Medium", "Low", "images/rosemary.jpg", "Full sunlight", "Minimal, drought-tolerant", "15-25°C", "Well-draining"},
                            {"Chives", "Allium schoenoprasum", "Indoor, outdoor", "Green", "Yes, purple flowers", "Spring, summer", "Low", "Yes", "Low", "Small", "Medium", "images/chives.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Sage", "Salvia officinalis", "Indoor, outdoor", "Green", "Yes, purple flowers", "Spring, summer", "Low", "Yes", "Low", "Medium", "Low", "images/sage.jpg", "Full sunlight", "Minimal, drought-tolerant", "15-25°C", "Well-draining"},
                            {"Basil", "Ocimum basilicum", "Indoor, outdoor", "Green", "Yes, white flowers", "Spring, summer", "Low", "Yes", "Low", "Small", "Medium", "images/basil.jpg", "Full sunlight", "Moderate, keep soil moist", "15-30°C", "Well-draining"},
                            {"Mint", "Mentha", "Indoor, outdoor", "Green", "Yes, purple flowers", "Spring, summer", "Low", "Yes", "Low", "Medium", "Medium", "images/mint.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Cilantro", "Coriandrum sativum", "Indoor, outdoor", "Green", "Yes, white flowers", "Spring, fall", "Low", "Yes", "Low", "Small", "Medium", "images/cilantro.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Dill", "Anethum graveolens", "Indoor, outdoor", "Green", "Yes, yellow flowers", "Spring, summer", "Low", "Yes", "Low", "Small", "Medium", "images/dill.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Lavender", "Lavandula", "Indoor, outdoor", "Green", "Yes, purple flowers", "Spring, summer", "Low", "Yes", "Low", "Medium", "Low", "images/lavender.jpg", "Full sunlight", "Minimal, drought-tolerant", "15-30°C", "Well-draining"},
                            {"Oregano", "Origanum vulgare", "Indoor, outdoor", "Green", "Yes, white flowers", "Spring, summer", "Low", "Yes", "Low", "Medium", "Low", "images/oregano.jpg", "Full sunlight", "Minimal, drought-tolerant", "15-30°C", "Well-draining"},
                            {"Fuchsia", "Fuchsia magellanica", "Outdoor", "Green", "Yes, various colors", "Spring, summer", "Medium", "Yes", "Low", "Medium", "Medium", "images/fuchsia.jpg", "Partial shade", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Cabbage", "Brassica oleracea", "Outdoor", "Green, purple", "Yes, yellow flowers", "Spring, fall", "Low", "Yes", "Low", "Medium", "Medium", "images/cabbage.jpg", "Full sunlight", "Moderate, keep soil moist", "15-20°C", "Well-draining"},
                            {"Arugula", "Eruca sativa", "Indoor, outdoor", "Green", "Yes, white flowers", "Spring, fall", "Low", "Yes", "Low", "Small", "Medium", "images/arugula.jpg", "Full sunlight", "Moderate, keep soil moist", "15-25°C", "Well-draining"},
                            {"Lettuce", "Lactuca sativa", "Outdoor", "Green, red", "Yes, yellow flowers", "Spring, fall", "Low", "Yes", "Low", "Small", "Medium", "images/lettuce.jpg", "Full sunlight", "Moderate, keep soil moist", "15-20°C", "Well-draining"},
                            {"Spinach", "Spinacia oleracea", "Outdoor", "Green", "Yes, yellow flowers", "Spring, fall", "Low", "Yes", "Low", "Small", "Medium", "images/spinach.jpg", "Full sunlight", "Moderate, keep soil moist", "15-20°C", "Well-draining"},
                            {"Kale", "Brassica oleracea", "Outdoor", "Green", "Yes, yellow flowers", "Spring, fall", "Low", "Yes", "Low", "Medium", "Medium", "images/kale.jpg", "Full sunlight", "Moderate, keep soil moist", "15-20°C", "Well-draining"}
                    };
                    // Verifica se o número de colunas e o número de dados correspondem
                    final int expectedColumnCount = 16; // Número de colunas na tabela

                    for (String[] plant : plantsData) {
                        if (plant.length != expectedColumnCount) {
                            System.out.println("Número de colunas não corresponde para a planta: " + plant[0]);
                            continue; // Pula para a próxima planta
                        }
                        for (int i = 1; i <= plant.length; i++) {
                            pstmt.setString(i, plant[i - 1]);
                        }
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }

                System.out.println("Tabela criada e dados inseridos com sucesso.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}
