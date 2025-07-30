package com.example.gps_g11.llm;
import com.example.gps_g11.Tasks.PlantTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class LLMIntegration {
    private static final String key = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    static final Map<String, List<PlantTask>> plantTasksCache = new HashMap<>();

    public static JSONObject promptGPT(String prompt) {
        OkHttpClient client = new OkHttpClient();

        // Create the JSON request body using JSONObject to avoid formatting issues
        JSONObject jsonRequestBody = new JSONObject();
        jsonRequestBody.put("model", "gpt-4");
        JSONArray messages = new JSONArray();
        JSONObject messageObject = new JSONObject();
        messageObject.put("role", "user");
        messageObject.put("content", prompt);
        messages.put(messageObject);
        jsonRequestBody.put("messages", messages);
        jsonRequestBody.put("temperature", 0.0);

        RequestBody requestBody = RequestBody.create(
                jsonRequestBody.toString(), MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + key)
                .build();

        // Execute the request and process the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + ", Body: " + (response.body() != null ? response.body().string() : "null"));
            }

            // Parse the response body into a JSONObject
            if (response.body() != null) {
                // Convert the response body to a JSONObject
                JSONObject jsonResponse = new JSONObject(response.body().string());

                // Access the "choices" array and get the "content" field
                JSONArray choices = jsonResponse.getJSONArray("choices");
                JSONObject choice = choices.getJSONObject(0);
                JSONObject message = choice.getJSONObject("message");
                String content = message.getString("content");

                // Convert the content (which is a JSON string) to a JSONObject
                JSONObject contentJson = new JSONObject(content);

                // Return the JSON object
                return contentJson;
            } else {
                throw new IOException("Received empty response body");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error calling OpenAI API", e);
        }
    }



    public List<PlantTask> callTaskAPI(String plantType) throws IOException {

        // Verifica se as tarefas para o tipo de planta já estão em cache
        if (plantTasksCache.containsKey(plantType)) {
            return plantTasksCache.get(plantType);
        }

        OkHttpClient client = new OkHttpClient();
        String prompt = "Provide a concise list of 3 essential care tasks for a plant of type: "+ plantType + ".List each task under the categories: Watering, Soil, and Light, with a few words for each, without any introductory text.";
        String jsonRequestBody = "{"
                + "\"model\": \"gpt-4o\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}],"
                + "\"temperature\": 0.0"
                + "}";

        RequestBody requestBody = RequestBody.create(
                MediaType.get("application/json"), jsonRequestBody
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + key)
                .build();

      /*  try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + ", Body: " + response.body().string());
            }

            String jsonResponse = response.body().string();
            System.out.println("Response: " + jsonResponse);

            List<PlantTask> tasks = parseTasksFromJson(jsonResponse);

            plantTasksCache.put(plantType, tasks);
            return tasks;
        } */
        long startTime = System.currentTimeMillis(); // Marca o início do tempo

        try (Response response = client.newCall(request).execute()) {
            long endTime = System.currentTimeMillis(); // Marca o fim do tempo
            long duration = endTime - startTime; // Calcula a duração

            System.out.println("Tempo de resposta com o modelo: " + duration + "ms");

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + ", Body: " + response.body().string());
            }

            String jsonResponse = response.body().string();
            List<PlantTask> tasks = parseTasksFromJson(jsonResponse);

            plantTasksCache.put(plantType, tasks); // Cache as tarefas
            return tasks;
        }
    }

    List<PlantTask> parseTasksFromJson(String jsonResponse) {
        List<PlantTask> tasks = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray choices = jsonObject.getAsJsonArray("choices");

            if (choices != null && choices.size() > 0) {
                String taskContent = choices.get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();

                String[] taskDescriptions = taskContent.split("\n");
                for (String description : taskDescriptions) {
                    if (!description.trim().isEmpty() && description.length() < 50) {
                        String cleanDescription = description.replaceFirst("^\\d+\\.\\s*", "").trim();
                        String recurrence = inferRecurrence(cleanDescription);
                        tasks.add(new PlantTask(cleanDescription,recurrence));
                        System.out.println("Tarefa: " + cleanDescription + " - Recorrência: " + recurrence);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }



    String inferRecurrence(String description) {
        description = description.toLowerCase();

        // Identificar recorrência com base em palavras-chave para tarefas diárias
        if (description.contains("moisture") ||
                description.contains("water") ||
                description.contains("irrigate") ||
                description.contains("shade") ||
                description.contains("spray") ||
                description.contains("daily") ||
                description.contains("light") ||
                description.contains("sun") ||
                description.contains("humidity")) {
            return "daily";
        }

        // Identificar recorrência com base em palavras-chave para tarefas semanais
        if (description.contains("soil") ||
                description.contains("fertilizer") ||
                description.contains("nutrients") ||
                description.contains("prune") ||
                description.contains("trim") ||
                description.contains("clean") ||
                description.contains("weeks") ||
                description.contains("weekly")) {
            return "weekly";
        }

        // Identificar recorrência com base em palavras-chave para tarefas mensais
        if (description.contains("repot") ||
                description.contains("re-pot") ||
                description.contains("inspect") ||
                description.contains("check pests") ||
                description.contains("deep clean") ||
                description.contains("monthly")) {
            return "monthly";
        }

        // Identificar recorrência com base em palavras-chave para tarefas anuais
        if (description.contains("replant") ||
                description.contains("replace soil") ||
                description.contains("seasonal") ||
                description.contains("annual pruning") ||
                description.contains("annual")) {
            return "annual";
        }

        // Caso nenhuma palavra-chave seja encontrada, retorne um padrão genérico
        return "Sem recorrência definida";
    }


}