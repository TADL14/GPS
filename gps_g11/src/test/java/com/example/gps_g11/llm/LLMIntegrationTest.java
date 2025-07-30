package com.example.gps_g11.llm;

import com.example.gps_g11.Tasks.PlantTask;
import okhttp3.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LLMIntegrationTest {


    @Test
    public void testCacheFunctionality() throws IOException {
        LLMIntegration integration = new LLMIntegration();

        // Mock the response
        List<PlantTask> mockTasks = List.of(
                new PlantTask("Water daily", "daily"),
                new PlantTask("Check soil weekly", "weekly")
        );
        LLMIntegration.plantTasksCache.put("Aloe Vera", mockTasks);

        // Test cache retrieval
        List<PlantTask> cachedTasks = integration.callTaskAPI("Aloe Vera");
        assertEquals(mockTasks, cachedTasks, "Cache should return the expected tasks");
    }

    @Test
    public void testInferRecurrence() {
        LLMIntegration integration = new LLMIntegration();

        assertEquals("daily", integration.inferRecurrence("Water the plant daily"));
        assertEquals("weekly", integration.inferRecurrence("Fertilize the soil weekly"));
        assertEquals("monthly", integration.inferRecurrence("Inspect for pests monthly"));
        assertEquals("annual", integration.inferRecurrence("Replant annually"));
        assertEquals("Sem recorrência definida", integration.inferRecurrence("No specific frequency mentioned"));
    }

    @Test
    public void testParseTasksFromJson() {
        LLMIntegration integration = new LLMIntegration();
        String jsonResponse = """
        {
            "choices": [
                {
                    "message": {
                        "content": "1. Water daily\\n2. Fertilize weekly\\n3. Check for pests monthly"
                    }
                }
            ]
        }
        """;

        List<PlantTask> tasks = integration.parseTasksFromJson(jsonResponse);

        assertEquals(3, tasks.size());
        assertEquals("Water daily", tasks.get(0).getDescription());
        assertEquals("daily", tasks.get(0).getRecurrence());
        assertEquals("Fertilize weekly", tasks.get(1).getDescription());
        assertEquals("weekly", tasks.get(1).getRecurrence());
        assertEquals("Check for pests monthly", tasks.get(2).getDescription());
        assertEquals("monthly", tasks.get(2).getRecurrence());
    }



    @Test
    public void testIntegrationWithRealApi() {
        // Use sparingly, as it makes real API calls
        LLMIntegration integration = new LLMIntegration();
        try {
            List<PlantTask> tasks = integration.callTaskAPI("Spider Plant");

            assertNotNull(tasks, "Tasks should not be null");
            assertTrue(tasks.size() > 0, "Tasks should contain at least one task");
        } catch (IOException e) {
            fail("Integration test failed due to exception: " + e.getMessage());
        }
    }


}