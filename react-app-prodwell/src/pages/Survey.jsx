import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaArrowRight, FaArrowLeft } from 'react-icons/fa';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { useNavigate } from 'react-router-dom';
import userService from '../services/userService';

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
  const [isNextDisabled, setIsNextDisabled] = useState(true);
  const [otherTextError, setOtherTextError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const response = await userService.getPersonalityQuestions();
        const validQuestions = response.data.filter(
          (item) => item.question && item.type && item.options
        );
        const formattedQuestions = validQuestions.map((item) => ({
          questionId: item.questionId,
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

  useEffect(() => {
    const currentQuestion = questions[currentQuestionIndex];
    if (currentQuestion) {
      validateAnswer(currentQuestion.questionId, selectedAnswers[currentQuestion.questionId] || []);
    }
  }, [currentQuestionIndex, questions]);

  const handleAnswerChange = (questionId, answer) => {
    setSelectedAnswers({
      ...selectedAnswers,
      [questionId]: answer,
    });

    if (answer === 'Other (Please specify)') {
      validateOtherText(questionId, selectedAnswers[`other-${questionId}`] || '');
    } else {
      setOtherTextError('');
      setIsNextDisabled(false); // Enable "Next" button if valid answer is selected
    }
  };

  const handleCheckboxChange = (questionId, answer) => {
    const currentAnswers = selectedAnswers[questionId] || [];
    const isSelected = currentAnswers.includes(answer);

    let updatedAnswers;
    if (isSelected) {
      updatedAnswers = currentAnswers.filter((item) => item !== answer);
    } else {
      updatedAnswers = [...currentAnswers, answer];
    }

    setSelectedAnswers({
      ...selectedAnswers,
      [questionId]: updatedAnswers,
    });

    validateAnswer(questionId, updatedAnswers);
  };

  const handleOtherTextChange = (questionId, value) => {
    setSelectedAnswers({
      ...selectedAnswers,
      [`other-${questionId}`]: value,
    });

    validateOtherText(questionId, value);
  };

  const validateOtherText = (questionId, value) => {
    const validChars = /^[A-Za-z\s]*$/;

    if (!validChars.test(value)) {
      setOtherTextError("Only letters are allowed.");
      setIsNextDisabled(true);
    } else if (value.length < 2 || value.length > 30) {
      setOtherTextError("Must be between 2-30 characters.");
      setIsNextDisabled(true);
    } else {
      setOtherTextError('');
      validateAnswer(questionId, selectedAnswers[questionId] || []);
    }
  };

  const validateAnswer = (questionId, answer) => {
    if (Array.isArray(answer)) {
      if (answer.includes('Other (Please specify)')) {
        const otherText = selectedAnswers[`other-${questionId}`];
        if (!otherText || otherText.length < 2 || otherText.length > 30 || !/^[A-Za-z\s]*$/.test(otherText)) {
          setIsNextDisabled(true);
          return;
        }
      }
      setIsNextDisabled(answer.length === 0);
    } else {
      if (answer === 'Other (Please specify)') {
        const otherText = selectedAnswers[`other-${questionId}`];
        if (!otherText || otherText.length < 2 || otherText.length > 30 || !/^[A-Za-z\s]*$/.test(otherText)) {
          setIsNextDisabled(true);
          return;
        }
      }
      setIsNextDisabled(!answer);
    }
  };

  const handleNext = () => {
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

      if (currentQuestionIndex < questions.length - 1) {
        setCurrentQuestionIndex(currentQuestionIndex + 1);
        setIsNextDisabled(true);
      }
    }
  };

  const handlePrevious = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(currentQuestionIndex - 1);
    }
  };

  const handleSubmit = async () => {
    // Prepare the payload by processing the selectedAnswers object.
    const processedAnswers = Object.entries(selectedAnswers).reduce((acc, [key, value]) => {
      // Check if the key contains the "other-" prefix and map it back to the original question ID.
      if (key.startsWith('other-')) {
        const originalQuestionId = key.replace('other-', '');
        acc[originalQuestionId] = value;
      } else {
        acc[key] = value;
      }
      return acc;
    }, {});
  
    const payload = {
      userId: userId,
      answers: Object.entries(processedAnswers).map(([questionId, answer]) => ({
        questionId,
        answer: Array.isArray(answer) ? answer : [answer],
      })),
    };
  
    try {
      await userService.submitPersonalityAnswers(payload);
      console.log("Survey submitted successfully.");
      navigate('/dashboard');
    } catch (error) {
      console.error('Error submitting survey:', error);
    }
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
                  {option === 'Other (Please specify)' && selectedAnswers[currentQuestion.questionId] === 'Other (Please specify)' && (
                    <div className="flex flex-col w-full ml-2 mt-1">
                      <input
                        type="text"
                        placeholder="Please specify"
                        value={selectedAnswers[`other-${currentQuestion.questionId}`] || ''}
                        onChange={(e) => handleOtherTextChange(currentQuestion.questionId, e.target.value)}
                        className="p-1 border rounded w-full"
                        required
                      />
                      {otherTextError && <p className="text-red-500 text-sm mt-1">{otherTextError}</p>}
                    </div>
                  )}
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
                    <div className="flex flex-col w-full ml-2 mt-1">
                      <input
                        type="text"
                        placeholder="Please specify"
                        value={selectedAnswers[`other-${currentQuestion.questionId}`] || ''}
                        onChange={(e) => handleOtherTextChange(currentQuestion.questionId, e.target.value)}
                        className="p-1 border rounded w-full"
                        required
                      />
                      {otherTextError && <p className="text-red-500 text-sm mt-1">{otherTextError}</p>}
                    </div>
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
              disabled={isNextDisabled || !isAnswerValid(currentQuestionIndex)}
              className="px-6 py-3 text-white bg-[#6200ea] rounded-lg hover:bg-[#4a00b4] disabled:opacity-50"
            >
              <FaArrowRight />
            </button>
          ) : (
            <button
              onClick={handleSubmit}
              disabled={isNextDisabled || !isAnswerValid(currentQuestionIndex)}
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
