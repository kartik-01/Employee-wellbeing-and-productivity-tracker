import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // Adjust to your Spring Boot API URL

const taskService = {
  getAllTasks: () => axios.get(`${API_BASE_URL}/tasks`),
  createTask: (task) => axios.post(`${API_BASE_URL}/tasks`, task),
  getTaskById: (taskId) => axios.get(`${API_BASE_URL}/tasks/${taskId}`),
  getTasksBySeverity: (severity) => axios.get(`${API_BASE_URL}/tasks/severity/${severity}`),
  getTasksByAssignee: (assignee) => axios.get(`${API_BASE_URL}/tasks/assignee/${assignee}`),
  updateTask: (task) => axios.put(`${API_BASE_URL}/tasks`, task),
  deleteTask: (taskId) => axios.delete(`${API_BASE_URL}/tasks/${taskId}`),
};

export default taskService;