import React, { useState, useEffect, useRef } from 'react';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { FaPlus, FaTrash, FaMagic, FaEdit } from 'react-icons/fa';
import { Bar, Pie } from 'react-chartjs-2';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, LineElement, PointElement } from 'chart.js';
import AnalysisItem from '../components/AnalysisItem';
import { Accordion, Card } from "react-bootstrap";


ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  LineElement,
  PointElement
);

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
  const [showStress, setShowStress] = useState(false);
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
      const response = await fetch(`http://localhost:8080/tasks/user/${userId}`);
      if (!response.ok) {
        console.error("Failed to fetch tasks:", response.statusText);
        return;
      }
      const data = await response.json();
      setTasks(data);
      updateTaskStatusCounts(data);
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
      // totalNoHours: parseInt(totalHours, 10),
      totalNoHours: totalHoursArray,
      userId,
    };

    try {
      const url = editMode ? `http://localhost:8080/tasks/${selectedTaskId}` : "http://localhost:8080/tasks/";
      const method = editMode ? "PUT" : "POST";

      const response = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(taskPayload),
      });

      if (!response.ok) {
        console.error(`Failed to ${editMode ? "update" : "add"} task:`, response.statusText);
        return;
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
    } catch (error) {
      console.error(`Network error while ${editMode ? "updating" : "adding"} task:`, error);
    }
  };

  const handleMagicClick = () => {
    setShowGraphs(true);
  };

  const handleStressClick = () => {
    setShowStress(true);
  }

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
      const response = await fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        console.error("Failed to delete task:", response.statusText);
        return;
      }

      // Fetch tasks again to update the table after deletion
      await fetchTasks();
    } catch (error) {
      console.error("Network error while deleting task:", error);
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

  // Line chart data
  const taskData = {
    startDate: new Date(2024, 10, 21), // November 21, 2024
    endDate: new Date(2024, 10, 23), // November 23, 2024
    stressLevels: [7, 10, 9],
  };

  // Helper function to calculate all dates between startDate and endDate
  const calculateDateRange = (startDate, endDate) => {
    const dates = [];
    let currentDate = new Date(startDate);

    while (currentDate <= endDate) {
      dates.push(new Date(currentDate));
      currentDate.setDate(currentDate.getDate() + 1);
    }

    return dates.map((date) =>
      date.toISOString().split("T")[0] // Format date as YYYY-MM-DD
    );
  };

  // Extract dates for the X-axis
  const xAxisLabels = calculateDateRange(taskData.startDate, taskData.endDate);

  // Create data object for the line chart
  const lineData = {
    labels: xAxisLabels,
    datasets: [
      {
        label: "Stress Levels",
        data: taskData.stressLevels,
        fill: false,
        borderColor: "#4CAF50",
        tension: 0.1,
      },
    ],
  };

  // Line chart options
  const lineOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Stress Levels Over Time",
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        min: 2,
        max: 14,
      },
    },
  };

  const [totalHoursArray, setTotalHoursArray] = useState([]);

  // Function to calculate the difference in days between taskStartDate and taskEndDate
  const calculateDateDifference = (start, end) => {
    const startDate = new Date(start);
    const endDate = new Date(end);
    const timeDifference = endDate - startDate;
    return (timeDifference / (1000 * 3600 * 24)) + 1; // Difference in days
  };

  // Update the totalHoursArray when taskStartDate or taskEndDate changes
  useEffect(() => {
    if (taskStartDate && taskEndDate) {
      const daysDifference = calculateDateDifference(taskStartDate, taskEndDate);
      // const newTotalHoursArray = Array.from({ length: daysDifference }, () => '');
      const newTotalHoursArray = Array.from({ length: daysDifference }, () => 0);
      setTotalHoursArray(newTotalHoursArray);
    }
  }, [taskStartDate, taskEndDate]);

  // Handle the change in hours for each day
  const handleTotalHoursChange = (index, value) => {
    const updatedHours = [...totalHoursArray];
    // updatedHours[index] = value;
    updatedHours[index] = value === '' ? 0 : parseFloat(value);
    setTotalHoursArray(updatedHours);
  };

  // const calculateTotalHours = () => {
  //   return totalHoursArray.reduce((total, hours) => total + hours, 0);
  // };

  // const preparePayload = () => {
  //   const totalHours = calculateTotalHours();
  //   const payload = {
  //     taskStartDate,
  //     taskEndDate,
  //     totalHours, // Sum of all day's hours
  //     dailyHours: totalHoursArray, // Array of hours for each day
  //   };
  //   console.log('Payload:', payload); // You can send this payload to your API
  // };

  const accordData = {
    overview:
      "Hello Trump, your stress levels indicate that you are managing a significant workload, especially around the mid-to-end of the month. It's clear that you are diligent and committed to your tasks, but it's important to balance your work and personal life to avoid burnout.",
    workloadAnalysis:
      "You have multiple tasks assigned with varying deadlines and hours. The task 'DummyTask' and 'TaskDummy' overlap significantly, which adds to your stress. Ensure you prioritize tasks based on their deadlines and allocate your time efficiently to avoid last-minute rushes.",
    suggestions: {
      "Task Management": [
        "Prioritize tasks based on their deadlines and complexity to manage your workload better.",
        "Break down large tasks into smaller, manageable chunks to reduce stress and increase productivity.",
        "Use a calendar or planner to keep track of your tasks and deadlines visually.",
      ],
      "Personal Wellbeing": [
        "Make sure to take regular breaks throughout the day to reduce stress and maintain focus.",
        "Engage in activities that help you relax, such as meditation, reading, or a short walk.",
        "Ensure you get adequate sleep each night to help your body and mind recover from the day's stress.",
      ],
      "Routine Optimization": [
        "Optimize your daily routine to include time for both work and personal activities.",
        "Consider delegating tasks if possible to reduce your workload.",
        "Use technology to your advantage by setting reminders and using productivity apps to stay organized.",
      ],
    },
  };

  return (
    <>
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
              {/* <label className="font-semibold">Total Number of Hours</label>
            <input
              type="number"
              value={totalHours}
              onChange={(e) => setTotalHours(e.target.value)}
              placeholder="Total Number of Hours"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            /> */}


              {taskStartDate && taskEndDate && totalHoursArray.length > 0 && (
                <>
                  {/* <label className="font-semibold">Total Number of Hours</label> */}
                  {totalHoursArray.map((_, index) => (
                    <div key={index} className="mb-2">
                      <label className="block font-medium">Day {index + 1}</label>
                      <input
                        type="number"
                        value={totalHoursArray[index]}
                        onChange={(e) => handleTotalHoursChange(index, e.target.value)}
                        placeholder={`Total Hours for Day ${index + 1}`}
                        className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                  ))}
                </>
              )}
              {errors.totalHours && <p className="text-red-500">{errors.totalHours}</p>}
              <button
                onClick={handleAddOrUpdateTask}
                className="flex items-center gap-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-all mt-4"
              >
                <FaPlus /> {editMode ? "Update Task" : "Add Task"}
              </button>
              <button
                onClick={handleMagicClick}
                className="flex items-center gap-2 px-4 py-2 bg-[#6200ea] text-white rounded hover:bg-purple-600 transition-all mt-4"
              >
                <FaMagic /> Show Graphs
              </button>
            </div>
          </div>
          <div className="mt-8" >
            <h2 className="text-xl font-semibold mb-4">Tasks Overview</h2>
            {tasks.length > 0 && (
              <div className="">
                <table
                  style={{
                    maxHeight: '12rem',
                    width: '100%',
                    overflowY: 'auto',
                  }}
                  className="bg-white border rounded">
                  <thead style={{ position: 'sticky', top: 0, backgroundColor: 'white', zIndex: 1 }}>
                    <tr className="flex m-2">
                      <th
                        className="px-4 py-2"
                        style={{
                          display: 'table',
                          width: '33%',
                          tableLayout: 'fixed',
                          textAlign: 'center',
                          verticalAlign: 'middle',
                        }}
                      >
                        Task Name
                      </th>
                      <th
                        className="px-4 py-2"
                        style={{
                          display: 'table',
                          width: '33%',
                          tableLayout: 'fixed',
                          textAlign: 'center',
                          verticalAlign: 'middle',
                        }}
                      >
                        Status
                      </th>
                      <th
                        className="px-4 py-2"
                        style={{
                          display: 'table',
                          width: '33%',
                          tableLayout: 'fixed',
                          textAlign: 'center',
                          verticalAlign: 'middle',
                        }}
                      >
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody
                    style={{
                      maxHeight: '10rem',
                      overflowY: 'auto',
                      display: 'block',
                    }}
                  >
                    {tasks.map((task, index) => (
                      <tr
                        key={index}
                        className={`text-center ${calculateStatus(task.taskEndDate, task.deadlineDate) === 0 ? 'bg-green-100' : calculateStatus(task.taskEndDate, task.deadlineDate) === 1 ? 'bg-yellow-100' : 'bg-red-100'}`}
                        style={{
                          display: 'table',
                          width: '100%',
                          tableLayout: 'fixed',
                        }}
                      >
                        <td className="px-4 py-2 border">{task.taskName}</td>
                        <td className="px-4 py-2 border">
                          {calculateStatus(task.taskEndDate, task.deadlineDate) === 0 ? "Completed Before Time" : calculateStatus(task.taskEndDate, task.deadlineDate) === 1 ? "Completed On Time" : "Completed Late"}
                        </td>
                        <td className="px-4 py-2 border items-center justify-center gap-2">
                          <button
                            onClick={() => handleEditTask(task)}
                            className="text-blue-500 hover:text-blue-700 transition-all mr-5"
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
            )}
          </div>


        </div>
        {showGraphs && (
          <div ref={rightCardRef} className="w-2/4 p-8 bg-white rounded-lg shadow-md flex flex-col" style={{ minHeight: '500px' }}>
            {/* <h2 className="text-2xl font-semibold mb-6 text-center">Task Completion Status</h2>
          <Bar data={data} options={options} /> */}
            <div className="mt-8" style={{ height: '400px' }}>
              <h2 className="text-2xl font-semibold mb-6 text-center">Task Completion Percentage</h2>
              <Pie data={pieData} options={pieOptions} />
            </div>
            <center>
              <button
                onClick={handleStressClick}
                className="flex items-center gap-1 px-2 py-2 w-32 bg-[#6200ea] text-white rounded hover:bg-purple-600 transition-all mt-20"
              >
                <FaMagic /> Show Stress
              </button>
            </center>
            {showStress && (
              <div className="mt-2" style={{ height: '400px' }}>
                {/* <h2 className="text-xl font-semibold mb-2 text-center">Stress Analysis</h2> */}
                <center><Line data={lineData} options={lineOptions} height={200} /></center>
              </div>
            )}

          </div>
        )}

      </div>

      <div className="flex flex-row items-start justify-center p-1 bg-gray-100 gap-2">
        <div className="w-100 ml-4 mr-4 mb-2 bg-gray-100 rounded-lg shadow-md flex flex-col overflow-auto">
          {/* <h2 className="text-3xl font-semibold text-gray-800 text-center mb-8 mt-2">Overview</h2>
          <p className="ml-7 mr-7">Hello Trump, your stress levels indicate that you are managing a significant workload, especially around the mid-to-end of the month. It's clear that you are diligent and committed to your tasks, but it's important to balance your work and personal life to avoid burnout.</p> */}
          <Accordion>
          <Accordion.Item eventKey="0">
              <Accordion.Header>
              <div className="font-semibold text-gray-800 text-center w-full m-2">
                Overview
              </div>
              </Accordion.Header>
              <Accordion.Body>{accordData.overview}</Accordion.Body>
            </Accordion.Item>
            {/* Workload Analysis */}
            <Accordion.Item eventKey="1">
              <Accordion.Header>
              <div className="font-semibold text-gray-800 text-center w-full m-2">
                Workload Analysis
              </div>
              </Accordion.Header>
              <Accordion.Body>{accordData.workloadAnalysis}</Accordion.Body>
            </Accordion.Item>
            {/* className="font-semibold text-gray-800 cursor-pointer text-center" */}
            {/* Suggestions */}
            <Accordion.Item eventKey="2">
            <Accordion.Header>
              <div className="font-semibold text-gray-800 text-center w-full m-2">
                Suggestions
              </div>
            </Accordion.Header>
              <Accordion.Body>
                <Accordion>
                  {Object.entries(accordData.suggestions).map(([key, values], index) => (
                    <Accordion.Item eventKey={index.toString()} key={key}>
                      <Accordion.Header>{key.replace(/([A-Z])/g, " $1")}</Accordion.Header>
                      <Accordion.Body>
                        <ul className="list-disc pl-5">
                          {values.map((item, idx) => (
                            <li key={idx}>{item}</li>
                          ))}
                        </ul>
                      </Accordion.Body>
                    </Accordion.Item>
                  ))}
                </Accordion>
              </Accordion.Body>
            </Accordion.Item>
          </Accordion>
        </div>
      </div>



    </>
  );
};
