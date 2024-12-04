import React, { useState, useEffect, useRef } from 'react';
import { MsalAuthenticationTemplate, useMsal} from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { FaPlus, FaTrash, FaEdit } from 'react-icons/fa';
import { Pie, Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, Title, Tooltip, Legend, ArcElement, LinearScale, PointElement, LineElement } from 'chart.js';
import dayjs from 'dayjs';
import { ClipLoader } from 'react-spinners'; // Import spinner component
import userService from '../services/userService';
import { FaMagic } from 'react-icons/fa';
ChartJS.register(CategoryScale, Title, Tooltip, Legend, ArcElement, LinearScale, PointElement, LineElement);

export const DashboardPage = ({ userId, setUserId }) => {
  const authRequest = {
    ...loginRequest,
  };

  return (
    <MsalAuthenticationTemplate
      interactionType={InteractionType.Redirect}
      authenticationRequest={authRequest}
    >
      <DashboardPageContent userId={userId} setUserId={setUserId} />
    </MsalAuthenticationTemplate>
  );
};

export const DashboardPageContent = ({ userId, setUserId }) => {
  const [tasks, setTasks] = useState([]);
  const [taskName, setTaskName] = useState("");
  const [assignedDate, setAssignedDate] = useState("");
  const [deadlineDate, setDeadlineDate] = useState("");
  const [taskStartDate, setTaskStartDate] = useState("");
  const [taskEndDate, setTaskEndDate] = useState("");
  const [dailyHours, setDailyHours] = useState({});
  const [errors, setErrors] = useState({});
  const [taskStatusCounts, setTaskStatusCounts] = useState({ beforeTime: 0, onTime: 0, late: 0 });
  const [dailyStressLevels, setDailyStressLevels] = useState([]);
  const [editMode, setEditMode] = useState(false);
  const [selectedTaskId, setSelectedTaskId] = useState(null);
  const [originalTask, setOriginalTask] = useState(null);
  const [isChanged, setIsChanged] = useState(false);
  const leftCardRef = useRef(null);
  const rightCardRef = useRef(null);
  const [showModal, setShowModal] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const { accounts } = useMsal();
  const claims = accounts[0]?.idTokenClaims || {};
  const [showAnalysis, setShowAnalysis] = useState(false); 
  const [accordData, setAccordData] = useState({});
  const [averageStressLevel, setAverageStressLevel] = useState(null);

  const fetchAIInsights = async () => {
    try {
      setIsLoading(true);
  
      // Hardcoded data
      const hardcodedResponse = {
        name: "Vyshnavi",
        dailyStressLevels: [
          { date: "2024-12-11", stressLevel: 7.5 },
          { date: "2024-12-12", stressLevel: 8.5 },
          { date: "2024-12-13", stressLevel: 7 },
          { date: "2024-12-14", stressLevel: 8.5 },
          { date: "2024-12-15", stressLevel: 5.5 },
          { date: "2024-12-16", stressLevel: 6 },
          { date: "2024-12-17", stressLevel: 6 },
          { date: "2024-12-18", stressLevel: 10 },
          { date: "2024-12-19", stressLevel: 5.5 },
          { date: "2024-12-20", stressLevel: 7 },
          { date: "2024-12-21", stressLevel: 7 },
        ],
        averageStressLevel: 7.18,
        analysis: {
          overview:
            "Vyshnavi, your personality suggests that you are highly motivated by helping others and prefer to recharge through engaging in hobbies. You tend to stick to your original plans and have a moderate comfort level with unexpected changes. Your instinct is to act immediately when faced with challenging situations, and you value having a structured routine. Here’s how these traits reflect in your work pattern:",
          workloadAnalysis:
            "You have a varied workload with multiple tasks assigned over different dates. On days like December 18, where you have 12 hours allocated for 'Tasku,' your stress level peaks due to the long working hours and the task's deadline. Similarly, tasks like 'AnotherOne' and 'Inkokati' overlap, adding to your stress. It’s important to manage your time effectively to avoid overlapping tasks and long working hours.",
          suggestions: {
            taskManagement: [
              "Prioritize tasks based on their deadlines and complexity to avoid last-minute rushes.",
              "Break down larger tasks into smaller, manageable chunks to distribute the workload evenly.",
              "Communicate with your team or manager about any overlapping tasks to adjust deadlines or allocate additional resources.",
            ],
            personalWellbeing: [
              "Ensure you take regular breaks during long working hours to maintain your energy and focus.",
              "Engage in your hobbies or relaxation techniques during your free time to recharge.",
              "Maintain a balanced diet and exercise routine to support your overall wellbeing.",
            ],
            counselling: [
              "Given your average stress level is above 7, it might be beneficial to seek counselling to manage stress more effectively.",
              "Discuss your workload and stress levels with a counsellor to find personalized strategies for coping.",
              "Explore stress management techniques such as mindfulness, meditation, or deep breathing exercises.",
            ],
          },
        },
      };
  
      // Set the hardcoded data into the state
      setDailyStressLevels(hardcodedResponse.dailyStressLevels || []);
      setAccordData({
        overview: hardcodedResponse.analysis.overview || "",
        workloadAnalysis: hardcodedResponse.analysis.workloadAnalysis || "",
        suggestions: hardcodedResponse.analysis.suggestions || {
          taskManagement: [],
          personalWellbeing: [],
          counselling: [],
        },
      });
      setAverageStressLevel(hardcodedResponse.averageStressLevel);
    } catch (error) {
      console.error("Failed to fetch AI insights", error);
    } finally {
      setIsLoading(false);
    }
  };
  
  useEffect(() => {
    if (userId) {
      fetchTasks();
      fetchDailyStressLevels(); // Fetch daily stress levels data when the component loads
    }
  }, [userId]);

  useEffect(() => {
    adjustCardHeights();
  }, [tasks, dailyStressLevels]);

  useEffect(() => {
    if (taskStartDate && taskEndDate) {
      generateDailyHoursFields(taskStartDate, taskEndDate);
    }
  }, [taskStartDate, taskEndDate]);
  const handleLogHours = () => {
    setShowModal(true);
  };

  const handleModalClose = () => {
    setShowModal(false);
  };

  const handleSaveLog = () => {
    const dailyHoursValues = Object.values(dailyHours);
    if (dailyHoursValues.some(hour => isNaN(hour) || hour <= 0 || hour > 12)) {
      setErrors({ dailyHours: "Each day's hours must be a number between 1 and 12." });
      return;
    }

    // Perform any additional save logic here
    setErrors({});
    setShowModal(false);
  };

  const adjustCardHeights = () => {
    if (leftCardRef.current && rightCardRef.current) {
      const leftCardHeight = leftCardRef.current.scrollHeight;
      rightCardRef.current.style.height = `${leftCardHeight}px`;
    }
  };

  const fetchTasks = async () => {
    if (!userId) {
      console.error("User ID is not available. Please ensure you are logged in.");
      return;
    }
    
    setIsLoading(true);
    
    try {
      const response = await userService.getUserTasks(userId);
      // Map the response to format the date fields correctly
      const formattedTasks = response.data.map((task) => {
        return {
          ...task,
          assignedDate: Array.isArray(task.assignedDate)
            ? dayjs(`${task.assignedDate[0]}-${task.assignedDate[1]}-${task.assignedDate[2]}`).format('YYYY-MM-DD')
            : task.assignedDate,
          deadlineDate: Array.isArray(task.deadlineDate)
            ? dayjs(`${task.deadlineDate[0]}-${task.deadlineDate[1]}-${task.deadlineDate[2]}`).format('YYYY-MM-DD')
            : task.deadlineDate,
          taskStartDate: Array.isArray(task.taskStartDate)
            ? dayjs(`${task.taskStartDate[0]}-${task.taskStartDate[1]}-${task.taskStartDate[2]}`).format('YYYY-MM-DD')
            : task.taskStartDate,
          taskEndDate: Array.isArray(task.taskEndDate)
            ? dayjs(`${task.taskEndDate[0]}-${task.taskEndDate[1]}-${task.taskEndDate[2]}`).format('YYYY-MM-DD')
            : task.taskEndDate,
        };
      });
  
      setTasks(formattedTasks);
      updateTaskStatusCounts(formattedTasks);
    } catch (error) {
      console.error("Network error while fetching tasks:", error);
    } finally {
      setIsLoading(false); // Set loading state to false after fetching
    }
  };
  
  const loaderStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100%',
    flexDirection: 'column',
    gap: '1rem',
  };
  const fetchDailyStressLevels = async () => {
    // Simulate a response from a backend for daily stress levels
    const response = {
      dailyStressLevels: [
        { date: "2024-12-06", stressLevel: 5 },
        { date: "2024-12-07", stressLevel: 5 },
        { date: "2024-12-08", stressLevel: 5 },
        { date: "2024-12-11", stressLevel: 7.5 },
        { date: "2024-12-12", stressLevel: 8.5 },
        { date: "2024-12-19", stressLevel: 10 },
        { date: "2024-12-20", stressLevel: 9.5 },
        { date: "2024-12-27", stressLevel: 10 }
      ]
    };

    // Set the daily stress levels data from the simulated response
    setDailyStressLevels(response.dailyStressLevels);
  };

  const handleAddOrUpdateTask = async () => {
    const validationErrors = {};

    const taskNamePattern = /^[a-zA-Z\s\-]+$/;
    if (!taskName.trim()) {
      validationErrors.taskName = "Task name is required.";
    } else if (!taskNamePattern.test(taskName)) {
      validationErrors.taskName = "Task name can only contain letters, hyphens, and spaces.";
    }

    if (!assignedDate) {
      validationErrors.assignedDate = "Assigned date is required.";
    }

    if (!deadlineDate) {
      validationErrors.deadlineDate = "Deadline date is required.";
    } else if (assignedDate && new Date(deadlineDate) < new Date(assignedDate)) {
      validationErrors.deadlineDate = "Deadline date cannot be before the assigned date.";
    }

    if (!taskStartDate) {
      validationErrors.taskStartDate = "Task start date is required.";
    } else if (assignedDate && new Date(taskStartDate) < new Date(assignedDate)) {
      validationErrors.taskStartDate = "Task start date cannot be before the assigned date.";
    }

    if (!taskEndDate) {
      validationErrors.taskEndDate = "Task end date is required.";
    } else if (taskStartDate && new Date(taskEndDate) < new Date(taskStartDate)) {
      validationErrors.taskEndDate = "Task end date cannot be before the start date.";
    }

    const dailyHoursValues = Object.values(dailyHours);
    if (dailyHoursValues.some(hour => isNaN(hour) || hour <= 0 || hour > 12)) {
      validationErrors.dailyHours = "Each day's hours must be a number between 1 and 12.";
    }

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const dailyHoursArray = Object.entries(dailyHours).map(([date, hours]) => ({
      [date]: hours
    }));

    const taskPayload = {
      taskName,
      assignedDate,
      deadlineDate,
      taskStartDate,
      taskEndDate,
      dailyHours: dailyHoursArray,
      userId,
      projectCode: claims.extension_ProjectCode, // Add project code from Azure claims
      ...(editMode && { taskId: selectedTaskId })
  };

  console.log("PAYLOAD", taskPayload)

  try {
      setIsLoading(true);
      if (editMode) {
          await userService.updateTask(selectedTaskId, taskPayload);
      } else {
          await userService.addTask(taskPayload);
      }
      await fetchTasks();
      
      // Clear form and reset edit mode
      setTaskName("");
      setAssignedDate("");
      setDeadlineDate("");
      setTaskStartDate("");
      setTaskEndDate("");
      setDailyHours({});
      setErrors({});
      setEditMode(false);
      setSelectedTaskId(null);
      setIsChanged(false);
      setOriginalTask(null);
  } catch (error) {
      console.error(`Error ${editMode ? "updating" : "adding"} task:`, error);
  }
};
  const handleDeleteTask = async (taskId) => {
    try {
      await userService.deleteTask(taskId);

      await fetchTasks();
    } catch (error) {
      console.error("Network error while deleting task:", error);
    }
  };

  const generateDailyHoursFields = (start, end) => {
    const startDay = dayjs(start);
    const endDay = dayjs(end);
    let currentDate = startDay;
    const hours = {};

    while (currentDate.isBefore(endDay) || currentDate.isSame(endDay)) {
      const formattedDate = currentDate.format('YYYY-MM-DD');
      hours[formattedDate] = dailyHours[formattedDate] || '';
      currentDate = currentDate.add(1, 'day');
    }

    setDailyHours(hours);
  };

  const handleEditTask = (task) => {
    setTaskName(task.taskName);
    setAssignedDate(task.assignedDate);
    setDeadlineDate(task.deadlineDate);
    setTaskStartDate(task.taskStartDate);
    setTaskEndDate(task.taskEndDate);
    setDailyHours(
      task.dailyHours.reduce((acc, obj) => {
        const [date, hours] = Object.entries(obj)[0];
        acc[date] = hours;
        return acc;
      }, {})
    );
    setOriginalTask(task);
    setEditMode(true);
    setSelectedTaskId(task.taskId);
    setIsChanged(false);
  };

  const handleFieldChange = (field, value) => {
    if (field === "taskName") setTaskName(value);
    else if (field === "assignedDate") setAssignedDate(value);
    else if (field === "deadlineDate") setDeadlineDate(value);
    else if (field === "taskStartDate") setTaskStartDate(value);
    else if (field === "taskEndDate") setTaskEndDate(value);

    if (originalTask) {
      setIsChanged(
        value !== originalTask[field] ||
        JSON.stringify(dailyHours) !== JSON.stringify(originalTask.dailyHours.reduce((acc, obj) => {
          const [date, hours] = Object.entries(obj)[0];
          acc[date] = hours;
          return acc;
        }, {}))
      );
    }
  };
  const handleAnalyseAI = async () => {
    setShowAnalysis(true); 
    await fetchAIInsights();
  };
  const handleDailyHoursChange = (date, value) => {
    setDailyHours((prev) => {
      const updatedHours = {
        ...prev,
        [date]: value,
      };

      if (originalTask) {
        const originalDailyHours = originalTask.dailyHours.reduce((acc, obj) => {
          const [originalDate, originalHours] = Object.entries(obj)[0];
          acc[originalDate] = originalHours;
          return acc;
        }, {});
        setIsChanged(JSON.stringify(updatedHours) !== JSON.stringify(originalDailyHours));
      }

      return updatedHours;
    });
  };

  const calculateStatus = (endDate, deadline) => {
    const end = new Date(endDate);
    const deadlineDate = new Date(deadline);
    if (end < deadlineDate) {
      return 0;
    } else if (end > deadlineDate) {
      return -1;
    } else {
      return 1;
    }
  };

  const updateTaskStatusCounts = (tasks) => {
    const counts = { beforeTime: 0, onTime: 0, late: 0 };
    tasks.forEach((task) => {
      const status = calculateStatus(task.taskEndDate, task.deadlineDate);
      if (status === 0) counts.beforeTime++;
      else if (status === 1) counts.onTime++;
      else if (status === -1) counts.late++;
    });
    setTaskStatusCounts(counts);
  };

  const pieData = {
    labels: ['Completed Before Time', 'Completed On Time', 'Completed Late'],
    datasets: [
      {
        label: 'Tasks',
        data: [taskStatusCounts.beforeTime, taskStatusCounts.onTime, taskStatusCounts.late],
        backgroundColor: ['#4CAF50', '#FFC107', '#F44336'],
      },
    ],
  };

  const lineData = {
    labels: dailyStressLevels.map((entry) => entry.date),
    datasets: [
      {
        label: 'Stress Levels Over Time',
        data: dailyStressLevels.map((entry) => entry.stressLevel),
        borderColor: '#4CAF50',
        fill: false,
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          font: {
            size: 14,
          },
          color: '#333', // Text color
        },
      },
      tooltip: {
        callbacks: {
          label: (tooltipItem) => {
            const value = tooltipItem.raw;
            const percentage = ((value / taskStatusCounts.total) * 100).toFixed(2);
            return `${tooltipItem.label}: ${value} (${percentage}%)`;
          },
        },
      },
      title: {
        display: true,
        text: 'Task Completion Percentage',
        font: {
          size: 18,
        },
        color: '#333',
      },
    },
    animation: {
      animateScale: true,
      animateRotate: true,
    },
  };
  
  const lineOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Stress Levels Over Time',
      },
    },
  };
  return ( 
    <>  
    <div className="flex flex-col lg:flex-row items-start justify-center min-h-screen p-4 bg-gray-100 gap-4">
      {isLoading ? (
      <div style={loaderStyle}>
        <ClipLoader size={50} color="#4CAF50" /> {/* Circular loader */}
        <p>Loading ...</p>
      </div>
    ) : (
      <>
      {/* Left Card */}
      <div
        ref={leftCardRef}
        className="w-full lg:w-2/4 p-8 bg-white rounded-lg shadow-md flex flex-col overflow-auto"
        style={{ minHeight: '500px' }}
      >
        <h2 className="text-xl font-semibold mb-4">
          {editMode ? 'Edit Task' : 'Add Tasks for This Week'}
        </h2>
        <div className="flex flex-col gap-4">
          <div className="flex flex-col gap-2">
            <label className="font-semibold">Task Name</label>
            <input
              type="text"
              value={taskName}
              onChange={(e) => handleFieldChange('taskName', e.target.value)}
              placeholder="Task Name"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            {errors.taskName && <p className="text-red-500">{errors.taskName}</p>}
  
            <label className="font-semibold">Assigned Date</label>
<input
  type="date"
  value={assignedDate}
  onChange={(e) => {
    handleFieldChange('assignedDate', e.target.value);
    setDeadlineDate(''); // Clear deadline date when assigned date changes
    setTaskStartDate(''); // Clear start date when assigned date changes
    setTaskEndDate('');   // Clear end date when assigned date changes
  }}
  placeholder="Assigned Date"
  className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
/>
{errors.assignedDate && <p className="text-red-500">{errors.assignedDate}</p>}

<label className="font-semibold">Deadline Date</label>
<input
  type="date"
  value={deadlineDate}
  disabled={!assignedDate} // Disable if assign date is not selected
  min={assignedDate} // Minimum is the assigned date
  max={assignedDate ? new Date(new Date(assignedDate).getTime() + 15 * 24 * 60 * 60 * 1000).toISOString().split('T')[0] : ''} // Maximum is 15 days from assign date
  onChange={(e) => {
    handleFieldChange('deadlineDate', e.target.value);
    setTaskStartDate(''); // Clear start date when deadline date changes
    setTaskEndDate('');   // Clear end date when deadline date changes
  }}
  placeholder="Deadline Date"
  className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
/>
{errors.deadlineDate && <p className="text-red-500">{errors.deadlineDate}</p>}

<label className="font-semibold">Task Start Date</label>
<input
  type="date"
  value={taskStartDate}
  disabled={!deadlineDate} // Disable if deadline date is not selected
  min={assignedDate} // Minimum is the assigned date
  max={deadlineDate} // Maximum is the deadline date
  onChange={(e) => handleFieldChange('taskStartDate', e.target.value)}
  placeholder="Task Start Date"
  className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
/>
{errors.taskStartDate && <p className="text-red-500">{errors.taskStartDate}</p>}

<label className="font-semibold">Task End Date</label>
<input
  type="date"
  value={taskEndDate}
  disabled={!deadlineDate} // Disable if deadline date is not selected
  min={taskStartDate || assignedDate} // Minimum is the task start date or assigned date
  max={deadlineDate} // Maximum is the deadline date
  onChange={(e) => handleFieldChange('taskEndDate', e.target.value)}
  placeholder="Task End Date"
  className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
/>
{errors.taskEndDate && <p className="text-red-500">{errors.taskEndDate}</p>}
     {/* Log Hour Button */}
            <button
  onClick={handleLogHours}
  disabled={!taskStartDate || !taskEndDate} // Disable button if either start or end date is not selected
  className={`px-4 py-2 rounded mt-4 text-white transition-all ${
    !taskStartDate || !taskEndDate
      ? 'bg-gray-400 cursor-not-allowed' // Disabled button styling
      : 'bg-blue-500 hover:bg-blue-600'  // Active button styling
  }`}
>
  Log Hour
</button>

        {/* Modal Window */}
        {showModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-1/3">
              <h2 className="text-xl font-semibold mb-4">Log Hours</h2>

              {/* Daily Hours Logging */}
              {(taskStartDate && taskEndDate) && (
                <div
                  className="daily-hours-container overflow-y-auto"
                  style={{
                    maxHeight: '200px',
                    padding: '8px',
                    border: '1px solid #ccc',
                    borderRadius: '8px',
                  }}
                >
                  {Object.keys(dailyHours).map((date, index) => (
                    <div key={index} className="flex flex-col gap-2">
                      <label className="font-semibold">{`Time Spent ${date}`}</label>
                      <input
                        type="number"
                        value={dailyHours[date]}
                        onChange={(e) => handleDailyHoursChange(date, e.target.value)}
                        placeholder="No. of Hours"
                        className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                  ))}
                </div>
              )}
              {errors.dailyHours && <p className="text-red-500">{errors.dailyHours}</p>}

              {/* Modal Buttons */}
              <div className="flex justify-end gap-4 mt-6">
                <button
                  onClick={handleModalClose}
                  className="bg-gray-300 px-4 py-2 rounded hover:bg-gray-400"
                >
                  Cancel
                </button>
                <button
                  onClick={handleSaveLog}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  Save
                </button>
              </div>
            </div>
          </div>
        )}
  
            <button
              onClick={handleAddOrUpdateTask}
              disabled={editMode && (!isChanged || Object.keys(errors).length > 0)}
              className={`flex items-center gap-2 px-4 py-2 ${
                editMode && !isChanged
                  ? 'bg-gray-400'
                  : 'bg-blue-500 hover:bg-blue-600'
              } text-white rounded transition-all mt-4`}
            >
              <FaPlus /> {editMode ? 'Update Task' : 'Add Task'}
            </button>
          </div>
        </div>
  
        {tasks.length > 0 && (
  <div className="mt-8 overflow-auto">
    <h2 className="text-xl font-semibold mb-4">Tasks Overview</h2>
    <div className="overflow-x-auto">
      <table className="min-w-full bg-white border rounded shadow-sm">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-3 py-2 border text-left text-sm font-medium text-gray-700">Task Name</th>
            <th className="px-3 py-2 border text-left text-sm font-medium text-gray-700">Status</th>
            <th className="px-3 py-2 border text-center text-sm font-medium text-gray-700">Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((task, index) => (
            <tr
              key={index}
              className={`${
                calculateStatus(task.taskEndDate, task.deadlineDate) === 0
                  ? 'bg-green-50'
                  : calculateStatus(task.taskEndDate, task.deadlineDate) === 1
                  ? 'bg-yellow-50'
                  : 'bg-red-50'
              } hover:bg-gray-50`}
            >
              <td className="px-3 py-1 border text-sm text-gray-800 h-10">{task.taskName}</td>
              <td className="px-3 py-1 border text-sm text-gray-800 h-10">
                {calculateStatus(task.taskEndDate, task.deadlineDate) === 0
                  ? 'Completed Before Time'
                  : calculateStatus(task.taskEndDate, task.deadlineDate) === 1
                  ? 'Completed On Time'
                  : 'Completed Late'}
              </td>
              <td className="px-3 py-1 border text-center h-10">
                <div className="flex justify-center gap-2">
                  <button
                    onClick={() => handleEditTask(task)}
                    className="bg-blue-500 text-white px-2 py-1 rounded text-xs inline-flex items-center gap-1 shadow hover:bg-blue-600 transition-all"
                  >
                    <FaEdit className="text-sm" />
                    Edit
                  </button>
                  <button
                    onClick={() => handleDeleteTask(task.taskId)}
                    className="bg-red-500 text-white px-2 py-1 rounded text-xs inline-flex items-center gap-1 shadow hover:bg-red-600 transition-all"
                  >
                    <FaTrash className="text-sm" />
                    Delete
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
)}

      </div>
  
      {/* Right Card */}
      {tasks.length > 0 && (
        <div ref={rightCardRef} className="w-full lg:w-2/4 flex flex-col gap-4">
          <div className="flex-1 p-8 bg-white rounded-lg shadow-md flex flex-col justify-center items-center overflow-auto">
            <div
              className="w-full h-full flex items-center justify-center"
              style={{ height: '80%' }}
            >
              <Pie data={pieData} options={pieOptions} />
            </div>
          </div>
          <div className="relative flex-1 p-8 bg-white rounded-lg shadow-md flex flex-col justify-center items-center overflow-auto">
  {/* Analyse AI Magic Button */}
  <button
  onClick={handleAnalyseAI} // Add your toggle logic
  className="absolute top-4 right-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 shadow-md transition-all flex items-center gap-2"
>
  Analyse AI <FaMagic size={16} /> {/* Magic Icon */}
</button>

  {/* Line Chart */}
  <div
  className="w-full h-full flex flex-col items-center justify-center text-center px-4"
  style={{ height: "60%", position: "relative" }}
>
  {showAnalysis ? (
    <Line data={lineData} options={lineOptions} />
  ) : (
    <div className="text-gray-600">
      <h3 className="text-xl font-bold mb-4 text-blue-600">
        Unlock Your Path to a Healthier and Happier Workplace!
      </h3>
      <p className="mb-4">
        Imagine a workplace where stress is understood, managed, and reduced. 
        With our AI-Driven Employee Wellbeing and Productivity Tracker, 
        you have the power to achieve this. 
      </p>
      <p className="mb-4">
        This cutting-edge tool leverages Azure AI Services to analyze stress patterns, uncover meaningful insights, 
        and provide personalized strategies that help you thrive. 
      </p>
      <p className="mb-6">
        Take the first step toward enhancing your productivity and finding balance in your work-life journey. 
        The solution is here—tailored for your success and happiness.
      </p>
      <p className="font-semibold text-lg">
        Click <span className="text-blue-600 font-bold">"Analyse AI Magic"</span> to explore your stress insights and unlock actionable recommendations!
      </p>
    </div>
  )}
</div>

</div>
        </div>
      )}
      </>
      )}
    </div>

      {/* Down Section */}
<div> 
{showAnalysis && (
  <div className="w-full p-4 bg-gray-100">
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-semibold text-center mb-4">Overview</h2>
      <div className="mb-6">
        <h3 className="text-xl font-semibold mb-2 text-gray-800">Summary</h3>
        <p className="text-gray-600">{accordData.overview || "No data available"}</p>
      </div>
      <div className="mb-6">
        <h3 className="text-xl font-semibold mb-2 text-gray-800">Workload Analysis</h3>
        <p className="text-gray-600">{accordData.workloadAnalysis || "No data available"}</p>
      </div>
      <div>
        <h3 className="text-xl font-semibold mb-2 text-gray-800">Suggestions</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {Object.entries(accordData.suggestions || {}).map(([category, suggestions], index) => (
            <div key={index} className="bg-gray-50 p-4 rounded-lg shadow-sm">
              <h4 className="font-semibold text-blue-500 mb-2">{category.replace(/([A-Z])/g, " $1")}</h4>
              <ul className="list-disc list-inside text-gray-600">
                {suggestions.map((suggestion, idx) => (
                  <li key={idx}>{suggestion}</li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </div>
    </div>
  </div>
)}

      </div>

    </> 
  );
};
