package com.sjsu.cmpe272.prodwell.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.dto.LlmRequest;
import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.entity.UserTaskData;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import com.sjsu.cmpe272.prodwell.repository.UserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final TaskRepository taskRepository;
    private final WebClient webClient; // Use the injected WebClient
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserTaskService.class);

    // Constructor to handle both default WebClient and WebClient with base URL
    public UserTaskService(UserTaskRepository userTaskRepository, TaskRepository taskRepository,
                           WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.userTaskRepository = userTaskRepository;
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;

        // Use the builder to create a WebClient with a base URL if provided
        this.webClient = (webClientBuilder != null)
                ? webClientBuilder.baseUrl("https://api.perplexity.ai").build() // Initialize WebClient with base URL
                : WebClient.create(); // Default WebClient if no builder is provided
    }

    public UserTaskData processTaskData(UserTaskData taskData) throws Exception {
        // Step 1: Fetch tasks associated with the user
        ObjectId userId = new ObjectId(taskData.getUserId());
        List<Task> userTasks = taskRepository.findByUserId(userId);

        // Step 2: Aggregate task data
        int totalTasksAssigned = userTasks.size();
        int totalHoursSpent = userTasks.stream()
                .mapToInt(Task::getTotalNoHours)
                .sum();

        // Additional logic for submission time, hobbies, etc., if required
        String submissionTime = "onTime"; // Placeholder; implement logic as needed

        // Step 3: Populate taskData with aggregated metrics
        taskData.setTaskAssigned(totalTasksAssigned);
        taskData.setHoursSpent(totalHoursSpent);
        taskData.setSubmissionTime(submissionTime);

        // Step 4: Construct LLM payload
        LlmRequest llmRequest = new LlmRequest();
        llmRequest.setModel("llama-3.1-sonar-large-128k-online");

        LlmRequest.Message systemMessage = new LlmRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent("You are an AI assistant that evaluates employee stress levels based on metrics like HoursSpent where 8 is ideal. Always respond in a JSON format with two fields: 'stresslevel' (a number between 0 and 10, where 0 is the lowest stress and 5 is neutral) and 'suggestion' (a concise sentence providing feedback or advice based on the users Personality and likes - any one which seems appropriate, don't mention personality type in final output). Do not include any additional text or explanation outside of the JSON object.");

        LlmRequest.Message userMessage = new LlmRequest.Message();
        userMessage.setRole("user");

        // Populate the content based on aggregated task data
        String userContent = objectMapper.writeValueAsString(taskData);
        userMessage.setContent(userContent);

        llmRequest.setMessages(new LlmRequest.Message[]{systemMessage, userMessage});

        // Step 5: Call LLM API
        String response = fetchApiResponse(llmRequest);

        // Step 6: Parse LLM response
        JsonNode jsonResponse = objectMapper.readTree(response);
        String content = jsonResponse
                .path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();

        JsonNode resultNode = objectMapper.readTree(content);
        int stressLevel = resultNode.get("stresslevel").asInt();
        String suggestion = resultNode.get("suggestion").asText();

        // Step 7: Update task data with LLM response
        taskData.setStressLevel(stressLevel);
        taskData.setSuggestion(suggestion);

        // Step 8: Save updated task data to MongoDB
        return userTaskRepository.save(taskData);
    }

    // Method to handle API response fetching with error handling
    private String fetchApiResponse(LlmRequest llmRequest) throws Exception {
        try {
            logger.info("Calling LLM API with task data: {}", llmRequest);
            return webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer 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")  // Replace with the actual Bearer token
                    .bodyValue(llmRequest)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        logger.error("API error: {}", response.statusCode());
                        return Mono.error(new RuntimeException("API error: " + response.statusCode()));
                    })
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(10)); // Timeout to avoid indefinite waiting
        } catch (WebClientResponseException e) {
            logger.error("Error calling API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new Exception("Error calling LLM API: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }
}
