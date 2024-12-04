import React from "react";
import "./../index.css"
import { useMsal } from "@azure/msal-react";
import { b2cPolicies } from '../authConfig';
import { MsalAuthenticationTemplate } from '@azure/msal-react';
import { InteractionType } from '@azure/msal-browser';
import { loginRequest } from "../authConfig";
import { useNavigate } from "react-router-dom";

export const ProfilePage = () => {
    const authRequest = {
        ...loginRequest,
    };

    return (
        <MsalAuthenticationTemplate
            interactionType={InteractionType.Redirect}
            authenticationRequest={authRequest}
        >
            <ProfilePageContent />
        </MsalAuthenticationTemplate>
    );
};

const ProfilePageContent = () => {
    const { accounts, instance } = useMsal();
    const claims = accounts[0]?.idTokenClaims || {};
    const navigate = useNavigate();

    const handleEditProfile = () => {
        instance.loginRedirect({
            authority: b2cPolicies.authorities.editProfile.authority,
            scopes: []
        }).catch((error) => console.log(error));
    };

    const handleResetPassword = () => {
        instance.loginRedirect({
            authority: b2cPolicies.authorities.resetPassword.authority,
            scopes: []
        }).catch((error) => console.log(error));
    };

    const handleFillSurvey = () => {
        navigate("/survey");
    };

    return (
        <div className="flex flex-col items-center bg-white-30">
            <section className="w-full bg-green-50 py-20 text-center">
                <h1 className="text-5xl font-bold text-gray-800 mb-4">Profile</h1> 
            </section>
            
            <div className="max-w-3xl w-full px-4 py-8">
                <div className="bg-white rounded-lg shadow-sm p-8">
                    <div className="grid grid-cols-2 gap-8 mb-6">
                        <div className="flex flex-col">
                            <label className="text-base font-bold text-gray-800 mb-1">First Name</label>
                            <p className="text-xl text-gray-600">{claims.given_name}</p>
                        </div>
                        <div className="flex flex-col">
                            <label className="text-base font-bold text-gray-800 mb-1">Last Name</label>
                            <p className="text-xl text-gray-600">{claims.family_name}</p>
                        </div>
                    </div>

                    <div className="grid grid-cols-2 gap-8 mb-6">
                        <div className="flex flex-col">
                            <label className="text-base font-bold text-gray-800 mb-1">Job Role</label>
                            <p className="text-xl text-gray-600">{claims.extension_JobRole}</p>
                        </div>
                        <div className="flex flex-col">
                            <label className="text-base font-bold text-gray-800 mb-1">Job Level</label>
                            <p className="text-xl text-gray-600">{claims.extension_JobLevel}</p>
                        </div>
                    </div>

                    <div className="mb-6">
                        <label className="text-base font-bold text-gray-800 mb-1">Project</label>
                        <p className="text-xl text-gray-600">{claims.extension_ProjectCode}</p>
                    </div>
                    
                    <div className="mb-6">
                        <label className="text-base font-bold text-gray-800 mb-1">Email</label>
                        <p className="text-xl text-gray-600">{claims.emails?.[0]}</p>
                    </div>

                    {/* Buttons Section */}
                    <div className="flex justify-center gap-4 mt-8">
                        {/* Fill Survey Button */}
                        <button 
                            onClick={handleFillSurvey}
                            className="px-4 py-2 bg-gray-100 text-gray-800 rounded shadow-md hover:bg-gray-200 hover:text-[#0078D4] transition-all text-sm font-medium"
                        >
                            Fill Survey
                        </button>

                        {/* Edit Profile Button */}
                        <button 
                            onClick={handleEditProfile}
                            className="px-4 py-2 bg-gray-100 text-gray-800 rounded shadow-md hover:bg-gray-200 hover:text-[#0078D4] transition-all text-sm font-medium"
                        >
                            Edit Profile
                        </button>

                        {/* Reset Password Button */}
                        <button 
                            onClick={handleResetPassword}
                            className="px-4 py-2 bg-gray-100 text-gray-800 rounded shadow-md hover:bg-gray-200 hover:text-[#0078D4] transition-all text-sm font-medium"
                        >
                            Reset Password
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};