package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.entity.UserTaskData;
import com.sjsu.cmpe272.prodwell.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-tasks")
@RequiredArgsConstructor
public class UserTaskController {

    private final UserTaskService userTaskService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserTaskData> createOrUpdateTask(@PathVariable("userId") String userId) {
        try {
            // Validate userId from the path
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            // Create UserTaskData object
            UserTaskData taskData = new UserTaskData();
            taskData.setUserId(userId);

            // Call service to process task
            UserTaskData updatedData = userTaskService.processTaskData(taskData);
            return ResponseEntity.ok(updatedData);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // Log the exception (can use a logging framework)
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
