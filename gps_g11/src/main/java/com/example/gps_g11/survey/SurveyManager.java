package com.example.gps_g11.survey;

import java.sql.*;

import com.example.gps_g11.DB.GardenDatabase;
import com.example.gps_g11.DB.PlantDatabase;
import com.example.gps_g11.GardenPlant.Plant;
import com.example.gps_g11.llm.LLMIntegration;
import org.json.JSONObject;

public class SurveyManager {

    public static String returnPlantSuggestion(Plant plant, String repeatedPlant) {

        String allPlantsFromDB = PlantDatabase.getPlantsName();
        System.out.println(allPlantsFromDB);
        // Construct the prompt using the Plant object's attributes
        String prompt = "You are a plant specialist. Based on the following attributes, suggest a suitable plant and provide a JSON response with the recommendation. Please include only the JSON response, without any additional commentary or explanation.\n\n" +
                "Plant Attributes:\n" +
                "Indoor/Outdoor: " + plant.getIndoorOutdoor() + "\n" +
                "Color: " + plant.getColor() + "\n" +
                "Has Flowers: " + (plant.isFlowers() ? "Yes" : "No") + "\n" +
                "Season: " + plant.getSeason() + "\n" +
                "Maintenance Level: " + plant.getMaintenance() + "\n" +
                "Pet Friendly: " + (plant.isPetFriendly() ? "Yes" : "No") + "\n" +
                "Allergies: " + plant.getAllergies() + "\n" +
                "Size: " + plant.getSize() + "\n" +
                "Preferred Environment: " + plant.gethumidity_requirement() + "\n\n" +
                "Considerations:\n" +
                "1. You must strictly choose from the following plants: " + allPlantsFromDB + ".\n" +
                "2. You are forbidden to recommend any plant not listed in the database.\n" +
                "3. Exclude these plants from consideration: " + repeatedPlant + ".\n\n" +
                "Recommendation format:\n" +
                "{\n" +
                "  \"plantSuggestion\": \"<common_name>\",\n" +
                "  \"reason\": \"<reason for suggestion>\"\n" +
                "}\n\n" +
                "Important: Your recommendation must strictly adhere to the list of allowed plants and avoid all excluded plants. If no suitable plant exists, respond with:\n" +
                "{\n" +
                "  \"plantSuggestion\": null,\n" +
                "  \"reason\": \"No suitable plant matches the given attributes.\"\n" +
                "}";


        JSONObject response = LLMIntegration.promptGPT(prompt);
      //  System.out.println(response);
        if (response.has("plantSuggestion")) {
            return response.getString("plantSuggestion");
        } else {
            throw new RuntimeException("Field 'plantSuggestion' not found in the response");
        }
    }

    public static void addPlantToGardenWithDetails(String plantName) {
        String urlPlantDB = "jdbc:sqlite:plant_database.db"; // Caminho para o plant_database
        String urlGardenDB = "jdbc:sqlite:garden_database.db"; // Caminho para o garden_database

        // Verificar se a planta já existe no garden_plants
        String checkQuery = "SELECT COUNT(*) FROM garden_plants WHERE common_name = ?";

        try (Connection gardenConn = DriverManager.getConnection(urlGardenDB);
             PreparedStatement checkStmt = gardenConn.prepareStatement(checkQuery)) {

            checkStmt.setString(1, plantName);
            ResultSet rs = checkStmt.executeQuery();

            // Se a planta já existe, não faz nada
            if (rs.getInt(1) > 0) {
                System.out.println("A planta já foi adicionada ao jardim: " + plantName);
                return; // Sai da função sem fazer nada
            }

            // Caso a planta não exista, recupera os dados dela no plant_database
            String selectQuery = "SELECT * FROM plants WHERE common_name = ?";
            try (Connection plantConn = DriverManager.getConnection(urlPlantDB);
                 PreparedStatement selectStmt = plantConn.prepareStatement(selectQuery)) {

                selectStmt.setString(1, plantName);
                ResultSet rsPlant = selectStmt.executeQuery();

                if (rsPlant.next()) {
                    // Recuperar os dados da planta
                    String light = rsPlant.getString("light");
                    String water = rsPlant.getString("water");
                    String temperature = rsPlant.getString("temperature");
                    String soil = rsPlant.getString("soil");
                    String imagePath = rsPlant.getString("image_path");

                    // Log dos dados recuperados
                   /* System.out.println("Light: " + light);
                    System.out.println("Water: " + water);
                    System.out.println("Temperature: " + temperature);
                    System.out.println("Soil: " + soil);
                    System.out.println("Image Path: " + imagePath); */

                    // Adicionar os dados ao garden_database
                    String insertQuery = "INSERT INTO garden_plants (common_name, light, water, temperature, soil, image_path) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = gardenConn.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, plantName);
                        insertStmt.setString(2, light); // Preencher com dados recuperados
                        insertStmt.setString(3, water); // Preencher com dados recuperados
                        insertStmt.setString(4, temperature); // Preencher com dados recuperados
                        insertStmt.setString(5, soil); // Preencher com dados recuperados
                        insertStmt.setString(6, imagePath); // Preencher com dados recuperados

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Planta adicionada ao garden_database: " + plantName);

                        } else {
                            System.err.println("Falha ao adicionar planta ao garden_database.");
                        }
                    }

                } else {
                    System.err.println("Planta não encontrada no plant_database: " + plantName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao processar a planta: " + plantName);
        }
    }




    public static String SurveyReply(Plant sData, String dontRepeatPlant) {
        StringBuilder repeatedPlants = new StringBuilder();
        if (dontRepeatPlant != null) {
            repeatedPlants.append(dontRepeatPlant);
        }

        String res = returnPlantSuggestion(sData, repeatedPlants.toString());
        repeatedPlants.append(res).append(" ");
        String queryBD = GardenDatabase.checkPlantByName(res);

        while (queryBD != null) {
            System.out.println("Repeated plant suggestion: " + res);
            res = returnPlantSuggestion(sData, repeatedPlants.toString());
            repeatedPlants.append(res).append(" ");
            queryBD = GardenDatabase.checkPlantByName(res);
        }

        // Adicionar planta com detalhes ao garden_database
        addPlantToGardenWithDetails(res);

        // Retornar a sugestão final
        return res;
    }


    public static void main(String[] args) {
        System.out.println(PlantDatabase.getPlantsName());
        System.out.println(GardenDatabase.returnAllPlantsNames());
    }
}

