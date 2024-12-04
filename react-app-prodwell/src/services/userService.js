import axios from './axiosConfig';

const API_BASE_URL = process.env.BACKEND_URL;

const userService = {
    checkAndCreateUser: (userData) => axios.post(`${API_BASE_URL}/api/users/check-and-create`, userData),

    getCompleteData: (oid) => axios.get(`${API_BASE_URL}/api/users/${oid}/complete-data`),

    getPersonalityQuestions: () => axios.get(`${API_BASE_URL}/personalityQuestions/`),
    
    getUserSurveyAnswers: (userId) => axios.get(`${API_BASE_URL}/personalityAnswers/${userId}`),

    submitPersonalityAnswers: (payload) => axios.post(`${API_BASE_URL}/personalityAnswers/`, payload),

    updatePersonalityAnswers: (payload) => axios.put(`${API_BASE_URL}/personalityAnswers/${payload.userId}`, payload),
    
    getAIInsights: (oid) => axios.get(`${API_BASE_URL}/api/users/${oid}/ai-insights`),

    getUserTasks: (userId) => axios.get(`${API_BASE_URL}/tasks/user/${userId}`),

    addTask: (taskPayload) => axios.post(`${API_BASE_URL}/tasks/`, taskPayload),
    
    updateTask: (taskId, taskPayload) => axios.put(`${API_BASE_URL}/tasks/${taskId}`, taskPayload),

    deleteTask: (taskId) => axios.delete(`${API_BASE_URL}/tasks/${taskId}`),

    getManagerAnalytics: (projectCode) =>  axios.get(`${API_BASE_URL}/api/analytics/manager/${projectCode}`),
    
    getHRAnalytics: () =>  axios.get(`${API_BASE_URL}/api/analytics/hr`)
};

export default userService;