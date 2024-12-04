import React, { useState, useEffect } from "react";
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { useMsal } from "@azure/msal-react";
import { Chart as ChartJS, ArcElement, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { Pie, Line } from 'react-chartjs-2';
import { ClipLoader } from 'react-spinners';
import userService from '../services/userService';

ChartJS.register(
    ArcElement,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

export const AnalyticsPage = () => {
    const authRequest = {
        ...loginRequest
    };

    return (
        <MsalAuthenticationTemplate
            interactionType={InteractionType.Redirect}
            authenticationRequest={authRequest}
        >
            <AnalyticsPageContent />
        </MsalAuthenticationTemplate>
    );
};

const AnalyticsPageContent = () => {
    const { accounts } = useMsal();
    const claims = accounts[0]?.idTokenClaims || {};
    const isManager = claims.extension_JobLevel === 'Manager';
    const isHR = claims.extension_JobLevel === 'HR';
    const [analyticsData, setAnalyticsData] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [insightsData, setInsightsData] = useState(null);
    const [isLoadingInsights, setIsLoadingInsights] = useState(true);

    useEffect(() => {
        const fetchAnalytics = async () => {
            try {
                setIsLoading(true);
                const response = isManager 
                    ? await userService.getManagerAnalytics(claims.extension_ProjectCode)
                    : await userService.getHRAnalytics();
                
                setAnalyticsData(response.data);
            } catch (error) {
                console.error('Error fetching analytics:', error);
            } finally {
                setIsLoading(false);
            }
        };

        if (isManager || isHR) {
            fetchAnalytics();
        }
    }, [isManager, isHR, claims.extension_ProjectCode]);

    useEffect(() => {
        const fetchInsights = async () => {
            try {
                const response = isManager 
                    ? await userService.getTeamInsights(claims.extension_ProjectCode)
                    : await userService.getHRInsights();
                setInsightsData(response.data);
            } catch (error) {
                console.error('Error fetching insights:', error);
            } finally {
                setIsLoadingInsights(false);
            }
        };

        if (isManager || isHR) {
            fetchInsights();
        }
    }, [isManager, isHR, claims.extension_ProjectCode]);

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-screen">
                <ClipLoader size={50} color="#4CAF50" />
            </div>
        );
    }

    const pieData = {
        labels: ['Completed Before Time', 'Completed On Time', 'Completed Late'],
        datasets: [{
            data: [
                analyticsData?.taskStats?.completedBeforeTime || 0,
                analyticsData?.taskStats?.completedOnTime || 0,
                analyticsData?.taskStats?.completedLate || 0
            ],
            backgroundColor: ['#4CAF50', '#FFC107', '#F44336'],
        }]
    };

    const lineData = {
        labels: analyticsData?.teamStressLevels?.map(item => item.date) || [],
        datasets: [{
            label: 'Average Stress Levels',
            data: analyticsData?.teamStressLevels?.map(item => item.averageStressLevel) || [],
            borderColor: '#4CAF50',
            fill: false
        }]
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
                text: 'Task Completion Distribution'
            }
        }
    };

    const lineOptions = {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: {
                min: 2,
                max: 12,
                ticks: {
                    stepSize: 1
                }
            }
        },
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Stress Level Trends'
            }
        }
    };

    return (
        <div className="flex flex-col items-center bg-white-30">
            <section className="w-full bg-green-50 py-20 text-center">
                <h1 className="text-5xl font-bold text-gray-800 mb-4">Analytics Dashboard</h1>
                <p className="text-xl text-gray-600">
                    {isManager && `Team Performance Overview - ${claims.extension_ProjectCode}`}
                    {isHR && "Organization-wide Performance Analytics"}
                </p>
                <p className="text-lg text-gray-600 mt-2">
                    Total Team Members: {analyticsData?.peopleCount || 0}
                </p>
            </section>

            <div className="max-w-7xl w-full px-4 py-8">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    <div className="bg-white rounded-lg shadow-sm p-8">
                        <h2 className="text-2xl font-bold text-gray-800 mb-6">
                            {isManager ? "Team Task Distribution" : "Organization Task Distribution"}
                        </h2>
                        <div className="h-[400px]">
                            <Pie data={pieData} options={pieOptions} />
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-sm p-8">
                        <h2 className="text-2xl font-bold text-gray-800 mb-6">
                            {isManager ? "Team Stress Level Trends" : "Organization Stress Level Trends"}
                        </h2>
                        <div className="h-[400px]">
                            <Line data={lineData} options={lineOptions} />
                        </div>
                    </div>
                </div>

                {/* Team Insights or Organization Insights Card */}
                <div className="mt-6 bg-white rounded-lg shadow-sm p-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">
                        {isManager ? "Team Insights" : "Organization Insights"}
                    </h2>
                    <div className="prose max-w-none">
                        {isLoadingInsights ? (
                            <div className="flex justify-center items-center h-24">
                                <ClipLoader size={30} color="#4CAF50" />
                            </div>
                        ) : (
                            <>
                                <p className="text-gray-600 mb-4">
                                    <strong>Stress Levels:</strong> {insightsData?.stressLevels}
                                </p>
                                <p className="text-gray-600">
                                    <strong>Recommendations:</strong> {insightsData?.recommendations}
                                </p>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AnalyticsPage;