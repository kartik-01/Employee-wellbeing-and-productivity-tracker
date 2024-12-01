import React, { useState, useEffect, useRef } from 'react';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { FaPlus, FaTrash } from 'react-icons/fa';
import { Bar, Pie } from 'react-chartjs-2';
import { Line } from 'react-chartjs-2';
// import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, LineElement, PointElement } from 'chart.js';

// Registering necessary elements
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement, // For Pie chart
  LineElement, // For Line chart
  PointElement // For Line chart
);

// ChartJS.register(BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend, ArcElement);

export const DashboardPage = () => {
  const authRequest = {
    ...loginRequest,
  };

  return (
    <MsalAuthenticationTemplate 
      interactionType={InteractionType.Redirect} 
      authenticationRequest={authRequest}
    >
      <DashboardPageContent />
    </MsalAuthenticationTemplate>
  );
};

export const DashboardPageContent = () => {
  const [tasks, setTasks] = useState([]);
  const [taskName, setTaskName] = useState("");
  const [assignedDate, setAssignedDate] = useState("");
  const [deadlineDate, setDeadlineDate] = useState("");
  const [taskStartDate, setTaskStartDate] = useState("");
  const [taskEndDate, setTaskEndDate] = useState("");
  const [totalHours, setTotalHours] = useState("");
  const [errors, setErrors] = useState({});
  const leftCardRef = useRef(null);
  const rightCardRef = useRef(null);

  useEffect(() => {
    if (leftCardRef.current && rightCardRef.current) {
      const leftCardHeight = leftCardRef.current.scrollHeight;
      const rightCardHeight = rightCardRef.current.scrollHeight;
      const maxHeight = Math.max(leftCardHeight, rightCardHeight);
      leftCardRef.current.style.height = `${maxHeight}px`;
      rightCardRef.current.style.height = `${maxHeight}px`;
    }
  }, [tasks]);

  const handleAddTask = () => {
    const validationErrors = {};

    if (taskName.trim() === "") {
      validationErrors.taskName = "Task name is required.";
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

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const newTask = {
      taskName,
      assignedDate,
      deadlineDate,
      taskStartDate,
      taskEndDate,
      totalHours,
      status: calculateStatus(taskEndDate, deadlineDate),
    };
    setTasks([...tasks, newTask]);
    setTaskName("");
    setAssignedDate("");
    setDeadlineDate("");
    setTaskStartDate("");
    setTaskEndDate("");
    setTotalHours("");
    setErrors({});
  };

  const handleSubmitTasks = () => {
    console.log("Tasks Data:", tasks);
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

  const handleDeleteTask = (index) => {
    const updatedTasks = tasks.filter((_, taskIndex) => taskIndex !== index);
    setTasks(updatedTasks);
  };

  const getTaskStatusCounts = () => {
    const counts = { beforeTime: 0, onTime: 0, late: 0 };
    tasks.forEach((task) => {
      if (task.status === 0) counts.beforeTime++;
      else if (task.status === 1) counts.onTime++;
      else if (task.status === -1) counts.late++;
    });
    return counts;
  };

  const taskStatusCounts = getTaskStatusCounts();

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
  const lineData = {
    labels: tasks.map((task) => task.taskName),
    datasets: [
      {
        label: 'Total Hours per Task',
        data: tasks.map((task) => task.totalHours),
        fill: false,
        borderColor: '#4CAF50',
        tension: 0.1,
      },
    ],
  };

  const lineOptions = {
    responsive: true,
    plugins: {
      title: {
        display: true,
        text: 'Total Hours per Task',
      },
    },
  };

  return (
    <div className="flex flex-row items-start justify-center min-h-screen p-4 bg-gray-100 gap-4">
      {/* Left side card for form and task table */}
      <div ref={leftCardRef} className="w-2/4 p-8 bg-white rounded-lg shadow-md flex flex-col overflow-auto" >
        <h2 className="text-xl font-semibold mb-4">Add Tasks for This Week</h2>
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
            {errors.deadlineDate && <p className="text-red-500">{errors.deadlineDate}</p>}

            <label className="font-semibold">Task Start Date</label>
            <input
              type="date"
              value={taskStartDate}
              min={assignedDate}
              onChange={(e) => setTaskStartDate(e.target.value)}
              placeholder="Task Start Date"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            {errors.taskStartDate && <p className="text-red-500">{errors.taskStartDate}</p>}

            <label className="font-semibold">Task End Date</label>
            <input
              type="date"
              value={taskEndDate}
              min={taskStartDate}
              onChange={(e) => setTaskEndDate(e.target.value)}
              placeholder="Task End Date"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            {errors.taskEndDate && <p className="text-red-500">{errors.taskEndDate}</p>}

            <label className="font-semibold">Total Number of Hours</label>
            <input
              type="number"
              value={totalHours}
              onChange={(e) => setTotalHours(e.target.value)}
              placeholder="Total Number of Hours"
              className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              onClick={handleAddTask}
              className="flex items-center gap-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-all mt-4"
            >
              <FaPlus /> Add Task
            </button>
            <button
              onClick={handleSubmitTasks}
              className="flex items-center gap-2 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition-all mt-4"
            >
              Submit Tasks
            </button>
          </div>
        </div>
        {/* Task table */}
        <div className="mt-8">
          <h2 className="text-xl font-semibold mb-4">Tasks Overview</h2>
          {tasks.length > 0 ? (
            <div className="">
              <table
                style={{
                  maxHeight: '12rem',
                  width: '100%',
                  overflowY: 'auto',
                }}
                className="bg-white border rounded"
              >
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
                    maxHeight: '12rem',
                    overflowY: 'auto',
                    display: 'block',
                  }}
                >
                  {tasks.map((task, index) => (
                    <tr
                      key={task.id}
                      className={`text-center ${task.status === 0 ? 'bg-green-100' : task.status === 1 ? 'bg-yellow-100' : 'bg-red-100'}`}
                      style={{
                        display: 'table',
                        width: '100%',
                        tableLayout: 'fixed',
                      }}
                    >
                      <td className="px-4 py-2 border">{task.taskName}</td>
                      <td className="px-4 py-2 border">
                        {task.status === 0
                          ? 'Completed Before Time'
                          : task.status === 1
                            ? 'Completed On Time'
                            : 'Completed Late'}
                      </td>
                      <td className="px-4 py-2 border">
                        <button
                          onClick={() => handleDeleteTask(index)}
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
          ) : (
            <p className="text-gray-500">No tasks added yet. Start by adding some tasks!</p>
          )}
        </div>


      </div>

      {/* Right side card for displaying bar chart and pie chart */}
      <div ref={rightCardRef} className="w-2/4 p-8 bg-white rounded-lg shadow-md flex flex-col overflow-auto" style={{ minHeight: '500px' }}>
        {/* <h2 className="text-2xl font-semibold mb-6 text-center">Task Completion Status</h2>
        <Bar data={data} options={options} /> */}
        <div className="mt-8" style={{ height: '400px' }}>
          <h2 className="text-2xl font-semibold mb-6 text-center">Task Completion Percentage</h2>
          <Pie data={pieData} options={pieOptions} />
        </div>
        <div className="mt-20" style={{ height: '400px' }}>
          <h2 className="text-2xl font-semibold mb-6 text-center">Line Graph</h2>
          <center><Line data={lineData} options={lineOptions} height={200}/></center>
        </div>
         
      </div>
    </div>
  );
};
