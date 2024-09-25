import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // Adjust to your Spring Boot API URL

const userService = {
  checkAndCreateUser: (userData) => axios.post(`${API_BASE_URL}/api/users/check-and-create`, userData),
};

export default userService;