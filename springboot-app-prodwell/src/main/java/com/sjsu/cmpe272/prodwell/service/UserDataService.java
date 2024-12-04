package com.sjsu.cmpe272.prodwell.service;

import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer;
import com.sjsu.cmpe272.prodwell.entity.PersonalityQuestion;
import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO.PersonalityData;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO.TaskData;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;
import com.sjsu.cmpe272.prodwell.repository.PersonalityAnswerRepository;
import com.sjsu.cmpe272.prodwell.repository.PersonalityQuestionRepository;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UserDataService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PersonalityAnswerRepository personalityAnswerRepository;
    
    @Autowired
    private PersonalityQuestionRepository personalityQuestionRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    public UserDataDTO getUserData(String oid) {
        UserDataDTO userData = new UserDataDTO();
        
        Optional<User> user = userRepository.findByOid(oid);
        if (user.isPresent()) {
            userData.setUser(user.get());
            
            // Get and transform personality answers
            Optional<PersonalityAnswer> answers = personalityAnswerRepository.findById(oid);
            if (answers.isPresent()) {
                List<PersonalityData> personalityDataList = answers.get().getAnswers().stream()
                    .map(qa -> {
                        PersonalityQuestion question = personalityQuestionRepository.findByQuestionId(qa.getQuestionId()).orElse(null);
                        return new PersonalityData(
                            question != null ? question.getQuestion() : "Unknown Question",
                            qa.getAnswer()
                        );
                    })
                    .collect(Collectors.toList());
                userData.setPersonalityData(personalityDataList);
            }
            
            // Get and transform tasks
            List<TaskData> taskDataList = taskRepository.findByUserId(oid).stream()
                .map(task -> new TaskData(
                    task.getTaskName(),
                    task.getAssignedDate(),
                    task.getDeadlineDate(),
                    task.getTaskStartDate(),
                    task.getTaskEndDate(),
                    task.getTotalNoHours(),
                    task.getProjectCode()
                ))
                .collect(Collectors.toList());
            userData.setTasks(taskDataList);
        }
        
        return userData;
    }

    // Method to check if user exists
    public boolean userExists(String oid) {
        return userRepository.existsByOid(oid);
    }

    // Method to get user details only
    public Optional<User> getUserDetails(String oid) {
        return userRepository.findByOid(oid);
    }

    // Method to get personality answers only
    public List<PersonalityData> getUserPersonalityAnswers(String oid) {
        Optional<PersonalityAnswer> answers = personalityAnswerRepository.findById(oid);
        if (!answers.isPresent()) {
            return new ArrayList<>();
        }
        
        return answers.get().getAnswers().stream()
            .map(qa -> {
                PersonalityQuestion question = personalityQuestionRepository.findByQuestionId(qa.getQuestionId()).orElse(null);
                return new PersonalityData(
                    question != null ? question.getQuestion() : "Unknown Question",
                    qa.getAnswer()
                );
            })
            .collect(Collectors.toList());
    }

    // Method to get user tasks only
    public List<Task> getUserTasks(String oid) {
        return taskRepository.findByUserId(oid);
    }
}