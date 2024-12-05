# CMPE-272-Team7-ProdWell

## AI-Driven Employee Wellness and Productivity Tracker
The AI-Driven Employee Wellbeing and Productivity Tracker is designed to help organizations monitor and improve the mental wellbeing and productivity of their employees. It aims to provide insights into stress levels and suggest personalized interventions to ensure that employees maintain a healthy work-life balance, enhancing their productivity and wellbeing. The system gathers data through surveys and analyzes the information using Azure AI Services to offer meaningful recommendations to employees.

## Architecture

Below is a block diagram of the architecture:

![image](https://github.com/user-attachments/assets/407a25fd-0a0e-431f-9e6d-c40f713ac2a1)

## Explanation
1. Landing Page (React): Users land on the landing page where they can sign up or sign in using Azure AD B2C.<br/>
2. Azure AD B2C: Handles the authentication process by providing a secure sign-up/sign-in experience.<br/>
3. Spring Boot Backend: Handles REST API calls, manages the flow between the frontend and mongoDB, and communicates with Azure AI services.<br/>
4. MongoDB: Stores user data and other data inputs like survey data related to personality and productivity.<br/>
5. Azure AI Services: Analyzes collected data to calculate stress levels and provides recommendations such as break times, counseling, and other personalized interventions.<br/>

## Use Cases
* Employee Wellbeing: Calculate stress levels and suggest personalized wellbeing interventions based on personality and productivity data.<br/>
* Productivity Tracking: Track work-related data such as weekly task completion to visualize productivity trends and identify areas for improvement.<br/>
* Personalized Recommendations: Provide personalized interventions, such as break time notifications, suggestions based on the user's hobbies, likes/dislikes, or counseling options based on survey inputs and analysis of stress levels.<br/>

## Persona Summary
* Employees: Individuals looking to monitor and improve their productivity and mental wellbeing.<br/>
* HR Teams: Human resource professionals interested in assessing employee stress levels and productivity trends to improve work culture.<br/>
* Managers: Team leaders who want to enhance productivity while ensuring their team’s wellbeing.<br/>

## Technologies Used
* Frontend: React for building the UI components.<br/>
* Azure AD B2C: For secure user authentication.<br/>
* Backend: Spring Boot for managing business logic and data flow.<br/>
* Database: MongoDB for storing user data, productivity metrics and survey data.<br/>
* AI Services: A learning and modeling algorithm, in the context of machine learning, is a set of instructions or procedures that analyzes data to identify patterns and relationships, allowing a computer to "learn" from the data and create a mathematical model that can then be used to make predictions on new, unseen data;<br/>

