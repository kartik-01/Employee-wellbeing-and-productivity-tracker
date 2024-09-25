import React, { useEffect, useState } from 'react';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import taskService from '../services/taskService';
import {
  Box,
  VStack,
  Heading,
  Input,
  Button,
  List,
  ListItem,
  Text,
  HStack,
  useToast,
} from '@chakra-ui/react';

const TodoListContent = () => {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState({ description: '', assignee: '', severity: 1, storyPoint: 1 });
  const toast = useToast();

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const response = await taskService.getAllTasks();
      setTasks(response.data);
    } catch (error) {
      console.error('Error fetching tasks:', error);
      toast({
        title: "Error fetching tasks",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const handleInputChange = (e) => {
    setNewTask({ ...newTask, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await taskService.createTask(newTask);
      setNewTask({ description: '', assignee: '', severity: '', storyPoint: '' });
      fetchTasks();
      toast({
        title: "Task created",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Error creating task:', error);
      toast({
        title: "Error creating task",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const handleDelete = async (taskId) => {
    try {
      await taskService.deleteTask(taskId);
      fetchTasks();
      toast({
        title: "Task deleted",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
    } catch (error) {
      console.error('Error deleting task:', error);
      toast({
        title: "Error deleting task",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  return (
    <Box maxWidth="800px" margin="auto" mt={8}>
      <Heading mb={6}>Todo List</Heading>
      <form onSubmit={handleSubmit}>
        <VStack spacing={4} align="stretch" mb={8}>
          <Input
            name="description"
            value={newTask.description}
            onChange={handleInputChange}
            placeholder="Description"
            required
          />
          <Input
            name="assignee"
            value={newTask.assignee}
            onChange={handleInputChange}
            placeholder="Assignee"
            required
          />
          <Input
            type="number"
            name="severity"
            value={newTask.severity}
            onChange={handleInputChange}
            placeholder="Severity"
            required
          />
          <Input
            type="number"
            name="storyPoint"
            value={newTask.storyPoint}
            onChange={handleInputChange}
            placeholder="Story Point"
            required
          />
          <Button type="submit" colorScheme="blue">Add Task</Button>
        </VStack>
      </form>
      <List spacing={3}>
        {tasks.map((task) => (
          <ListItem key={task.taskId} p={3} shadow="md" borderWidth="1px">
            <HStack justifyContent="space-between">
              <VStack align="start" spacing={1}>
                <Text fontWeight="bold">{task.description}</Text>
                <Text fontSize="sm">Assignee: {task.assignee}</Text>
                <Text fontSize="sm">Severity: {task.severity}, Story Points: {task.storyPoint}</Text>
              </VStack>
              <Button onClick={() => handleDelete(task.taskId)} colorScheme="red" size="sm">
                Delete
              </Button>
            </HStack>
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

export const TodoList = () => {
  const authRequest = {
    ...loginRequest,
  };

  return (
    <MsalAuthenticationTemplate 
      interactionType={InteractionType.Redirect} 
      authenticationRequest={authRequest}
    >
      <TodoListContent />
    </MsalAuthenticationTemplate>
  );
};