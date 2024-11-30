import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaArrowRight, FaArrowLeft } from 'react-icons/fa';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { useNavigate, useLocation } from 'react-router-dom';

export const SurveyPage = ({ userId, setUserId }) => {
  const authRequest = {
    ...loginRequest,
  };
  return (
    <MsalAuthenticationTemplate 
      interactionType={InteractionType.Redirect} 
      authenticationRequest={authRequest}
    >
      <SurveyPageContent userId={userId} setUserId={setUserId} />
    </MsalAuthenticationTemplate>
  );
};

const SurveyPageContent = ({ userId, setUserId }) => {
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState({});
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const response = await axios.get('http://localhost:8080/personalityQuestions/');
        const validQuestions = response.data.filter(
          (item) => item.question && item.type && item.options
        );
        const formattedQuestions = validQuestions.map((item) => ({
          questionId: item.id.timestamp,
          category: "Personality",
          question: item.question,
          type: item.type,
          options: item.options,
        }));
        setQuestions(formattedQuestions);
      } catch (error) {
        console.error("Error fetching questions:", error);
      }
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
    if (isAnswerValid(currentQuestionIndex)) {
      const currentQuestion = questions[currentQuestionIndex];

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
        userId: userId, // Replace with actual user ID if available
        questionId: currentQuestion.questionId,
        answer: selectedAnswers[currentQuestion.questionId],
      };

      try {
        await axios.post('http://localhost:8080/personalityAnswers/', answerPayload);
      } catch (error) {
        console.error('Error saving answer:', error);
      }

      if (currentQuestionIndex < questions.length - 1) {
        setCurrentQuestionIndex(currentQuestionIndex + 1);
      }
    }
  };

  const handlePrevious = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(currentQuestionIndex - 1);
    }
  };

  const handleSubmit = () => {
    console.log("Survey submitted, navigating to dashboard...");
    console.log("Selected Answers:", selectedAnswers);
    navigate('/dashboard');
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
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-2xl p-10 bg-white rounded-lg shadow-md">
        <h1 className="text-3xl font-bold text-center mb-10">Let Us Know About You</h1>
        <h2 className="mb-6 text-xl font-semibold text-gray-700">{currentQuestion.question}</h2>
        <div className="mb-8">
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
            className="px-6 py-3 text-white bg-[#6200ea] rounded-lg hover:bg-[#4a00b4] disabled:opacity-50"
          >
            <FaArrowLeft />
          </button>
          {currentQuestionIndex < questions.length - 1 ? (
            <button
              onClick={handleNext}
              disabled={!isAnswerValid(currentQuestionIndex)}
              className="px-6 py-3 text-white bg-[#6200ea] rounded-lg hover:bg-[#4a00b4] disabled:opacity-50"
            >
              <FaArrowRight />
            </button>
          ) : (
            <button
              onClick={handleSubmit}
              disabled={!isAnswerValid(currentQuestionIndex)}
              className="px-6 py-3 text-white bg-[#6200ea] rounded-lg hover:bg-[#4a00b4] disabled:opacity-50"
            >
              Submit
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
