import React, { useState, useEffect, useRef } from 'react';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { FaPlus, FaTrash, FaMagic, FaEdit } from 'react-icons/fa';
import { Bar, Pie } from 'react-chartjs-2';
import userService from '../services/userService';
import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend, ArcElement } from 'chart.js';

ChartJS.register(BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend, ArcElement);

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
  const [totalHours, setTotalHours] = useState("");
  const [errors, setErrors] = useState({});
  const [showGraphs, setShowGraphs] = useState(false);
  const [taskStatusCounts, setTaskStatusCounts] = useState({ beforeTime: 0, onTime: 0, late: 0 });
  const [editMode, setEditMode] = useState(false);
  const [selectedTaskId, setSelectedTaskId] = useState(null);
  const leftCardRef = useRef(null);
  const rightCardRef = useRef(null);

  useEffect(() => {
    if (userId) {
      fetchTasks();
    }
  }, [userId]);

  useEffect(() => {
    if (leftCardRef.current && rightCardRef.current) {
      const leftCardHeight = leftCardRef.current.scrollHeight;
      const rightCardHeight = rightCardRef.current.scrollHeight;
      const maxHeight = Math.max(leftCardHeight, rightCardHeight);
      leftCardRef.current.style.height = `${maxHeight}px`;
      rightCardRef.current.style.height = `${maxHeight}px`;
    }
  }, [tasks]);

  const fetchTasks = async () => {
    if (!userId) {
        console.error("User ID is not available. Please ensure you are logged in.");
        return;
    }

    try {
        const response = await userService.getUserTasks(userId);
        setTasks(response.data);
        updateTaskStatusCounts(response.data);
    } catch (error) {
        console.error("Network error while fetching tasks:", error);
    }
};

  const handleAddOrUpdateTask = async () => {
    const validationErrors = {};

    const taskNamePattern = /^[a-zA-Z\s\-]+$/;
    if (!taskName.trim()) {
      validationErrors.taskName = "Task name is required.";
    } else if (!taskNamePattern.test(taskName)) {
      validationErrors.taskName = "Task name can only contain letters, hyphens, and spaces.";
    }

    if (assignedDate && taskStartDate && new Date(taskStartDate) < new Date(assignedDate)) {
      validationErrors.taskStartDate = "Task start date cannot be before the assigned date.";
    }

    if (assignedDate && deadlineDate && new Date(deadlineDate) < new Date(assignedDate)) {
      validationErrors.deadlineDate = "Deadline date cannot be before the assigned date.";
    }

    if (taskStartDate && taskEndDate && new Date(taskEndDate) < new Date(taskStartDate)) {
      validationErrors.taskEndDate = "Task end date cannot be before the start date.";
    }

    if (totalHours && (isNaN(totalHours) || totalHours <= 0 || totalHours >= 45)) {
      validationErrors.totalHours = "Total number of hours must be a number greater than 0 and less than 45.";
    }

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const taskPayload = {
      taskName,
      assignedDate,
      deadlineDate,
      taskStartDate,
      taskEndDate,
      totalNoHours: parseInt(totalHours, 10),
      userId,
    };

    try {
      if (editMode) {
          await userService.updateTask(selectedTaskId, taskPayload);
      } else {
          await userService.addTask(taskPayload);
      }
  
      // Fetch tasks again to update the table
      await fetchTasks();
  
      // Clear form and reset edit mode
      setTaskName("");
      setAssignedDate("");
      setDeadlineDate("");
      setTaskStartDate("");
      setTaskEndDate("");
      setTotalHours("");
      setErrors({});
      setEditMode(false);
      setSelectedTaskId(null);
    }catch (error) {
      console.error(`Network error while ${editMode ? "updating" : "adding"} task:`, error);
    }
  };

  const handleMagicClick = () => {
    setShowGraphs(true);
  };

  const handleEditTask = (task) => {
    setTaskName(task.taskName);
    setAssignedDate(task.assignedDate);
    setDeadlineDate(task.deadlineDate);
    setTaskStartDate(task.taskStartDate);
    setTaskEndDate(task.taskEndDate);
    setTotalHours(task.totalNoHours);
    setEditMode(true);
    setSelectedTaskId(task.taskId);
  };

  const calculateStatus = (endDate, deadline) => {
    const end = new Date(endDate);
    const deadlineDate = new Date(deadline);
    if (end < deadlineDate) {
      return 0; // completed before time
    } else if (end > deadlineDate) {
      return -1; // completed late
    } else {
      return 1; // completed on time
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

  const handleDeleteTask = async (taskId) => {
    try {
      await userService.deleteTask(taskId);
      // Fetch tasks again to update the table after deletion
      await fetchTasks();
    } catch (error) {
      console.error("Error deleting task:", error);
    }
  };

  const data = {
    labels: ['Completed Before Time', 'Completed On Time', 'Completed Late'],
    datasets: [
      {
        label: 'Tasks',
        data: [taskStatusCounts.beforeTime, taskStatusCounts.onTime, taskStatusCounts.late],
        backgroundColor: ['#4CAF50', '#FFC107', '#F44336'],
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Task Completion Overview',
      },
    },
  };

  const pieData = {
    labels: ['Completed Before Time', 'Completed On Time', 'Completed Late'],
    datasets: [
      {
        data: [taskStatusCounts.beforeTime, taskStatusCounts.onTime, taskStatusCounts.late],
        backgroundColor: ['#4CAF50', '#FFC107', '#F44336'],
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Task Completion Percentage',
      },
    },
  };

  return (
    <div className="flex flex-row items-start justify-center min-h-screen p-4 bg-gray-100 gap-4">
      {/* Left side card for form and task table */}
      <div ref={leftCardRef} className="w-2/4 p-8 bg-white rounded-lg shadow-md flex flex-col overflow-auto" style={{ minHeight: '500px' }}>
        <h2 className="text-xl font-semibold mb-4">{editMode ? "Edit Task" : "Add Tasks for This Week"}</h2>
        <div className="flex flex-col gap-4">
          <div className="flex flex-col gap-2">
            <label className="font-semibold">Task Name</label>
            <input
              type="text"
              value={taskName}
              onChange={(e) => setTaskName(e.target.value)}
              placeholder="Task Name"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            {errors.taskName && <p className="text-red-500">{errors.taskName}</p>}
            <label className="font-semibold">Assigned Date</label>
            <input
              type="date"
              value={assignedDate}
              onChange={(e) => setAssignedDate(e.target.value)}
              placeholder="Assigned Date"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <label className="font-semibold">Deadline Date</label>
            <input
              type="date"
              value={deadlineDate}
              min={assignedDate}
              onChange={(e) => setDeadlineDate(e.target.value)}
              placeholder="Deadline Date"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <label className="font-semibold">Task Start Date</label>
            <input
              type="date"
              value={taskStartDate}
              min={assignedDate}
              onChange={(e) => setTaskStartDate(e.target.value)}
              placeholder="Task Start Date"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <label className="font-semibold">Task End Date</label>
            <input
              type="date"
              value={taskEndDate}
              min={taskStartDate}
              onChange={(e) => setTaskEndDate(e.target.value)}
              placeholder="Task End Date"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <label className="font-semibold">Total Number of Hours</label>
            <input
              type="number"
              value={totalHours}
              onChange={(e) => setTotalHours(e.target.value)}
              placeholder="Total Number of Hours"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            {errors.totalHours && <p className="text-red-500">{errors.totalHours}</p>}
            <button
              onClick={handleAddOrUpdateTask}
              className="flex items-center gap-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-all mt-4"
            >
              <FaPlus /> {editMode ? "Update Task" : "Add Task"}
            </button>
            <button
              onClick={handleMagicClick}
              className="flex items-center gap-2 px-4 py-2 bg-purple-500 text-white rounded hover:bg-purple-600 transition-all mt-4"
            >
              <FaMagic /> Show Graphs
            </button>
          </div>
        </div>
        {tasks.length > 0 && (
          <div className="mt-8 overflow-auto">
            <h2 className="text-xl font-semibold mb-4">Tasks Overview</h2>
            <div className="overflow-x-auto">
              <table className="min-w-full bg-white border rounded">
                <thead>
                  <tr>
                    <th className="px-4 py-2 border">Task Name</th>
                    <th className="px-4 py-2 border">Status</th>
                    <th className="px-4 py-2 border">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {tasks.map((task, index) => (
                    <tr key={index} className={`text-center ${calculateStatus(task.taskEndDate, task.deadlineDate) === 0 ? 'bg-green-100' : calculateStatus(task.taskEndDate, task.deadlineDate) === 1 ? 'bg-yellow-100' : 'bg-red-100'}`}>
                      <td className="px-4 py-2 border">{task.taskName}</td>
                      <td className="px-4 py-2 border">
                        {calculateStatus(task.taskEndDate, task.deadlineDate) === 0 ? "Completed Before Time" : calculateStatus(task.taskEndDate, task.deadlineDate) === 1 ? "Completed On Time" : "Completed Late"}
                      </td>
                      <td className="px-4 py-2 border flex items-center justify-center gap-2">
                        <button
                          onClick={() => handleEditTask(task)}
                          className="text-blue-500 hover:text-blue-700 transition-all"
                        >
                          <FaEdit />
                        </button>
                        <button
                          onClick={() => handleDeleteTask(task.taskId)}
                          className="text-red-500 hover:text-red-700 transition-all"
                        >
                          <FaTrash />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>
      {showGraphs && (
        <div ref={rightCardRef} className="w-2/4 p-8 bg-white rounded-lg shadow-md flex flex-col overflow-auto" style={{ minHeight: '500px' }}>
          <h2 className="text-2xl font-semibold mb-6 text-center">Task Completion Status</h2>
          <Bar data={data} options={options} />
          <div className="mt-8" style={{ height: '400px' }}>
            <h2 className="text-2xl font-semibold mb-6 text-center">Task Completion Percentage</h2>
            <Pie data={pieData} options={pieOptions} />
          </div>
        </div>
      )}
    </div>
  );
};
