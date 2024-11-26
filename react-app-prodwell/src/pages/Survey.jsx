import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaArrowRight, FaArrowLeft } from 'react-icons/fa';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";

export const SurveyPage = () => {
  const authRequest = {
    ...loginRequest,
  };

  return (
    <MsalAuthenticationTemplate 
      interactionType={InteractionType.Redirect} 
      authenticationRequest={authRequest}
    >
      <SurveyPageContent />
    </MsalAuthenticationTemplate>
  );
};

const SurveyPageContent = () => {
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState({});
  const [activeTab, setActiveTab] = useState('Personality');

  useEffect(() => {
    // Fetch questions from the backend
    const fetchQuestions = async () => {
      // Hardcoded response for questions
      const response = {
        data: [
          {
            "questionId": 1,
            "category": "Personality",
            "question": "Do you consider yourself more introverted or extroverted in a work environment?",
            "type": "select",
            "options": ["Introverted", "Extroverted", "A mix of both"]
          },
          {
            "questionId": 2,
            "category": "Personality",
            "question": "What activities do you enjoy outside of work?",
            "type": "multiSelect",
            "options": ["Physical exercise (e.g., running, gym)", "Creative arts (e.g., painting, writing)", "Socializing with friends or family", "Learning new things (e.g., courses, reading)", "Other (Please specify)"]
          },
          {
            "questionId": 3,
            "category": "Personality",
            "question": "How often do you engage in activities that help you recharge (e.g., hobbies, exercise, meditation)?",
            "type": "select",
            "options": ["Daily", "Weekly", "Monthly", "Rarely"]
          },
          {
            "questionId": 4,
            "category": "Personality",
            "question": "What time of day do you feel most productive?",
            "type": "select",
            "options": ["Morning", "Afternoon", "Evening", "Late Night"]
          },
          {
            "questionId": 5,
            "category": "Personality",
            "question": "When facing a difficult task, what strategies help you the most?",
            "type": "select",
            "options": ["Breaking it into smaller parts", "Seeking advice or feedback", "Researching possible solutions", "Taking a step back before tackling it again"]
          },
          {
            "questionId": 6,
            "category": "Personality",
            "question": "On a scale of 1 to 10, how well do you feel you balance multiple responsibilities at once?",
            "type": "range",
            "options": {}
          },
          {
            "questionId": 7,
            "category": "Personality",
            "question": "Do you enjoy working under deadlines?",
            "type": "select",
            "options": ["Yes, it helps me stay focused.", "Sometimes, depending on the task.", "No, I prefer flexible timelines."]
          },
          {
            "questionId": 8,
            "category": "Personality",
            "question": "Which of these values are important to you in the workplace?",
            "type": "multiSelect",
            "options": ["Integrity and honesty", "Respect for others", "Creativity and innovation", "Accountability", "Continuous learning and growth"]
          },
          {
            "questionId": 9,
            "category": "Personality",
            "question": "What motivates you the most at work?",
            "type": "multiSelect",
            "options": ["Achieving goals", "Recognition and rewards", "Learning and growth opportunities", "Helping others", "Being part of a team"]
          },
          {
            "questionId": 10,
            "category": "Personality",
            "question": "Do you prefer working on a single task at a time or multitasking?",
            "type": "select",
            "options": ["Single task", "Multitasking", "No preference"]
          },
          {
            "questionId": 11,
            "category": "Productivity",
            "question": "How do you make decisions at work?",
            "type": "select",
            "options": ["Based on careful analysis", "Based on intuition and experience", "A combination of both"]
          },
          {
            "questionId": 12,
            "category": "Productivity",
            "question": "Do you enjoy working under deadlines?",
            "type": "select",
            "options": ["Yes, it helps me stay focused.", "Sometimes, depending on the task.", "No, I prefer flexible timelines."]
          },
          {
            "questionId": 13,
            "category": "Productivity",
            "question": "How comfortable are you discussing your stress levels with your manager or team?",
            "type": "select",
            "options": ["Very comfortable", "Somewhat comfortable", "Neutral", "Not comfortable"]
          },
          {
            "questionId": 14,
            "category": "Productivity",
            "question": "On a scale of 1 to 10, how manageable do you find your current workload?",
            "type": "range",
            "options": {}
          },
          {
            "questionId": 15,
            "category": "Productivity",
            "question": "How often do you need to work overtime to complete your tasks? (Rate from 1 - Never to 10 - Very Often)",
            "type": "range",
            "options": {}
          },
          {
            "questionId": 16,
            "category": "Productivity",
            "question": "How often do you feel distracted during work hours?",
            "type": "select",
            "options": ["Never", "Rarely", "Occasionally", "Often", "Always"]
          },
          {
            "questionId": 17,
            "category": "Productivity",
            "question": "What tools or techniques help you stay organized?",
            "type": "select",
            "options": ["Task management software", "To-do lists", "Calendar reminders", "Time-blocking", "Other (Please specify)"]
          },
          {
            "questionId": 18,
            "category": "Productivity",
            "question": "Do you feel that your work is affecting your mental well-being?",
            "type": "select",
            "options": ["Not at all", "Occasionally", "Often", "Always"]
          },
          {
            "questionId": 19,
            "category": "Productivity",
            "question": "What contributes most to your work-related stress?",
            "type": "multiSelect",
            "options": ["Heavy workload", "Lack of resources or tools", "Tight deadlines", "Poor work-life balance", "Lack of support from management", "Other (Please specify)"]
          },
          {
            "questionId": 20,
            "category": "Productivity",
            "question": "Do you enjoy working collaboratively with your team, or do you prefer independent work?",
            "type": "select",
            "options": ["Collaboration", "Independent work", "A mix of both"]
          },
          {
            "questionId": 21,
            "category": "Productivity",
            "question": "What two factors are important for you to maintain focus during work?",
            "type": "multiSelect",
            "options": ["Quiet environment", "Regular breaks", "Well-defined tasks", "Support from colleagues", "Clear goals", "Other (Please specify)"]
          },
          {
            "questionId": 22,
            "category": "Productivity",
            "question": "What aspects of your job do you enjoy the most?",
            "type": "multiSelect",
            "options": ["Solving complex problems", "Engaging with customers or stakeholders", "Designing or creating new things", "Supporting teammates", "Learning new technologies or methodologies"]
          }
        ]
      };
      setQuestions(response.data);
    };

    fetchQuestions();
  }, []);

  const handleAnswerChange = (questionId, answer) => {
    setSelectedAnswers({
      ...selectedAnswers,
      [questionId]: answer,
    });
  };

  const handleCheckboxChange = (questionId, answer) => {
    const currentAnswers = selectedAnswers[questionId] || [];
    const isSelected = currentAnswers.includes(answer);

    if (isSelected) {
      // Remove answer if it's already selected
      setSelectedAnswers({
        ...selectedAnswers,
        [questionId]: currentAnswers.filter((item) => item !== answer),
      });
    } else {
      setSelectedAnswers({
        ...selectedAnswers,
        [questionId]: [...currentAnswers, answer],
      });
    }
  };

  const handleOtherTextChange = (questionId, value) => {
    setSelectedAnswers({
      ...selectedAnswers,
      [`other-${questionId}`]: value,
    });
  };

  const handleNext = async () => {
    const currentQuestion = questions[currentQuestionIndex];
    if (isAnswerValid(currentQuestionIndex)) {
      // If "Other" is selected and has text, replace "Other (Please specify)" with the actual text
      if (currentQuestion.type === 'multiSelect' && selectedAnswers[`other-${currentQuestion.questionId}`]) {
        const updatedAnswers = selectedAnswers[currentQuestion.questionId].map((item) =>
          item === 'Other (Please specify)'
            ? selectedAnswers[`other-${currentQuestion.questionId}`]
            : item
        );
        setSelectedAnswers((prev) => {
          const { [`other-${currentQuestion.questionId}`]: removed, ...rest } = prev;
          return {
            ...rest,
            [currentQuestion.questionId]: updatedAnswers,
          };
        });
        
      }

      const answerPayload = {
        userId: "", // Replace with actual user ID if available
        questionId: currentQuestion.questionId,
        answer: selectedAnswers[currentQuestion.questionId],
      };

      try {
        await axios.post('/api/saveAnswer', answerPayload);
      } catch (error) {
        console.error('Error saving answer:', error);
      }

      if (currentQuestionIndex < questions.length - 1) {
        setCurrentQuestionIndex(currentQuestionIndex + 1);
        updateActiveTab();
      }
    }
  };

  const handlePrevious = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(currentQuestionIndex - 1);
      updateActiveTab();
    }
  };

  const handleTabChange = (tab) => {
    if (tab === 'Productivity' && activeTab === 'Personality') {
      const personalityQuestionsCompleted = questions.every(
        (q) => q.category !== 'Personality' || selectedAnswers[q.questionId]
      );
      if (!personalityQuestionsCompleted) {
        return;
      }
    }
    setActiveTab(tab);
    setCurrentQuestionIndex(questions.findIndex((q) => q.category === tab));
  };

  const updateActiveTab = () => {
    const currentCategory = questions[currentQuestionIndex].category;
    setActiveTab(currentCategory);
  };

  const isAnswerValid = (index) => {
    const question = questions[index];
    const answer = selectedAnswers[question.questionId];
    if (question.type === 'select' || question.type === 'range') {
      return !!answer;
    }
    if (question.type === 'multiSelect') {
      return Array.isArray(answer) && answer.length > 0;
    }
    return false;
  };

  if (questions.length === 0) {
    return <div className="text-center text-gray-500">Loading questions...</div>;
  }

  const currentQuestion = questions[currentQuestionIndex];

  return (
    <div className="flex flex-col items-center justify-center min-h-screen p-4 bg-gray-100">
      <div className="flex justify-center mb-6 space-x-4">
        <button
          onClick={() => handleTabChange('Personality')}
          className={`px-4 py-2 rounded ${activeTab === 'Personality' ? 'bg-[#eefdf1] text-black' : 'bg-gray-200 text-gray-700'}`}
        >
          Personality
        </button>
        <button
          onClick={() => handleTabChange('Productivity')}
          disabled={activeTab === 'Personality' && questions.some((q) => q.category === 'Personality' && !selectedAnswers[q.questionId])}
          className={`px-4 py-2 rounded ${activeTab === 'Productivity' ? 'bg-[#eefdf1] text-black' : 'bg-gray-200 text-gray-700'} ${activeTab === 'Personality' && questions.some((q) => q.category === 'Personality' && !selectedAnswers[q.questionId]) ? 'opacity-50 cursor-not-allowed' : ''}`}
          style={{ transition: 'background-color 0.3s ease' }}
        >
          Productivity
        </button>
      </div>

      <div className="relative w-full max-w-md p-8 bg-white rounded-lg shadow-md transform transition-transform duration-500 ease-in-out hover:scale-105">
        <h2 className="mb-4 text-lg font-semibold text-gray-700">{currentQuestion.question}</h2>
        <div className="mb-4">
          {currentQuestion.type === 'select' && (
            <div>
              {currentQuestion.options.map((option) => (
                <label key={option} className="flex items-center mb-2">
                  <input
                    type="radio"
                    name={`question-${currentQuestion.questionId}`}
                    value={option}
                    checked={selectedAnswers[currentQuestion.questionId] === option}
                    onChange={() => handleAnswerChange(currentQuestion.questionId, option)}
                    className="mr-2"
                  />
                  {option}
                </label>
              ))}
            </div>
          )}
          {currentQuestion.type === 'multiSelect' && (
            <div>
              {currentQuestion.options.map((option) => (
                <label key={option} className="flex items-center mb-2">
                  <input
                    type="checkbox"
                    name={`question-${currentQuestion.questionId}`}
                    value={option}
                    checked={selectedAnswers[currentQuestion.questionId]?.includes(option)}
                    onChange={() => handleCheckboxChange(currentQuestion.questionId, option)}
                    className="mr-2"
                  />
                  {option}
                  {option === 'Other (Please specify)' && selectedAnswers[currentQuestion.questionId]?.includes(option) && (
                    <input
                      type="text"
                      placeholder="Please specify"
                      value={selectedAnswers[`other-${currentQuestion.questionId}`] || ''}
                      onChange={(e) => handleOtherTextChange(currentQuestion.questionId, e.target.value)}
                      className="ml-2 p-1 border rounded w-full"
                      required
                    />
                  )}
                </label>
              ))}
            </div>
          )}
          {currentQuestion.type === 'range' && (
            <div>
              <input
                type="range"
                min="1"
                max="10"
                value={selectedAnswers[currentQuestion.questionId] || 1}
                onChange={(e) => handleAnswerChange(currentQuestion.questionId, e.target.value)}
                className="w-full"
              />
              <div className="text-center mt-2">{selectedAnswers[currentQuestion.questionId] || 1}</div>
            </div>
          )}
        </div>
        <div className="flex justify-between">
          <button
            onClick={handlePrevious}
            disabled={currentQuestionIndex === 0}
            className="px-4 py-2 text-white bg-[#6200ea] rounded hover:bg-[#4a00b4] disabled:opacity-50"
          >
            <FaArrowLeft />
          </button>
          <button
            onClick={handleNext}
            disabled={currentQuestionIndex === questions.length - 1 || !isAnswerValid(currentQuestionIndex)}
            className="px-4 py-2 text-white bg-[#6200ea] rounded hover:bg-[#4a00b4] disabled:opacity-50"
          >
            <FaArrowRight />
          </button>
        </div>
      </div>

      <div className="mt-6 w-full max-w-md p-4 bg-white rounded-lg shadow-md">
        <h3 className="text-lg font-semibold text-gray-700 mb-4">Saved Responses:</h3>
        <pre className="text-sm text-gray-600">
          {JSON.stringify(selectedAnswers, null, 2)}
        </pre>
      </div>
    </div>
  );
};

