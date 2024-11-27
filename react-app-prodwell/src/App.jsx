import React, { useEffect, useState } from 'react';
import { MsalProvider, useMsal } from '@azure/msal-react';
import { EventType } from '@azure/msal-browser';
import { Routes, Route, useLocation } from "react-router-dom";
import { PageLayout } from './components/PageLayout';
import { TodoList } from './pages/TodoList';
import { Home } from './pages/Home';
import { b2cPolicies, protectedResources } from './authConfig';
import { compareIssuingPolicy } from './utils/claimUtils';
import { ChakraProvider } from '@chakra-ui/react'
import userService from './services/userService';
import { LandingPage } from './pages/LandingPage';
import { SurveyPage } from './pages/Survey';
import { useNavigate } from "react-router-dom";
import { DashboardPage } from './pages/Dashboard';

import './styles/App.css';

const Pages = () => {
    const { instance, accounts } = useMsal();
    const navigate = useNavigate();
    const location = useLocation(); // Get current route information
    const [initialRedirectDone, setInitialRedirectDone] = useState(false); // Flag to track the initial redirect

    useEffect(() => {
        const callbackId = instance.addEventCallback((event) => {
            if (
                (event.eventType === EventType.LOGIN_SUCCESS || event.eventType === EventType.ACQUIRE_TOKEN_SUCCESS) &&
                event.payload.account &&
                !initialRedirectDone // Only redirect if initial redirect hasn't been done
            ) {
                // Store the ID token in session storage
                const idToken = event.payload.idToken;
                if (idToken) {
                    sessionStorage.setItem('msal.id.token', idToken);
                }

                // Extract required user data from idTokenClaims
                const userData = {
                    oid: event.payload.idTokenClaims.oid,
                    given_name: event.payload.idTokenClaims.given_name,
                    family_name: event.payload.idTokenClaims.family_name,
                    jobTitle: event.payload.idTokenClaims.jobTitle,
                    country: event.payload.idTokenClaims.country,
                    city: event.payload.idTokenClaims.city
                };

                // Call the backend to check and create user if not exists
                userService.checkAndCreateUser(userData)
                    .then(response => {
                        console.log('User check/create response:', response.data);
                    })
                    .catch(error => {
                        console.error('Error checking/creating user:', error);
                    });

                // Redirect to the Survey page, but only if not already on it
                if (location.pathname !== "/survey") {
                    navigate("/survey");
                }
                
                // Set the redirect done flag to true to prevent repeated redirects
                setInitialRedirectDone(true);

                if (compareIssuingPolicy(event.payload.idTokenClaims, b2cPolicies.names.editProfile)) {
                    const originalSignInAccount = instance
                        .getAllAccounts()
                        .find(
                            (account) =>
                                account.idTokenClaims.oid === event.payload.idTokenClaims.oid &&
                                account.idTokenClaims.sub === event.payload.idTokenClaims.sub && 
                                compareIssuingPolicy(account.idTokenClaims, b2cPolicies.names.signUpSignIn)        
                        );

                    let signUpSignInFlowRequest = {
                        authority: b2cPolicies.authorities.signUpSignIn.authority,
                        account: originalSignInAccount,
                    };

                    instance.ssoSilent(signUpSignInFlowRequest);
                }

                if (compareIssuingPolicy(event.payload.idTokenClaims, b2cPolicies.names.forgotPassword)) {
                    let signUpSignInFlowRequest = {
                        authority: b2cPolicies.authorities.signUpSignIn.authority,
                        scopes: [
                            ...protectedResources.apiTodoList.scopes.read,
                            ...protectedResources.apiTodoList.scopes.write,
                        ],
                    };
                    instance.loginRedirect(signUpSignInFlowRequest);
                }
            }

            if (event.eventType === EventType.LOGIN_FAILURE) {
                if (event.error && event.error.errorMessage.includes('AADB2C90118')) {
                    const resetPasswordRequest = {
                        authority: b2cPolicies.authorities.forgotPassword.authority,
                        scopes: [],
                    };
                    instance.loginRedirect(resetPasswordRequest);
                }
            }
        });

        return () => {
            if (callbackId) {
                instance.removeEventCallback(callbackId);
            }
        };
    }, [instance, navigate, location, initialRedirectDone]);

    return (
        <Routes>
            <Route path="/todolist" element={<TodoList />} />
            <Route path="/" element={<LandingPage />} />
            <Route path="/survey" element={<SurveyPage />} />
            <Route path="/dashboard" element={<DashboardPage />} />
        </Routes>
    );
};

const App = ({ instance }) => {
    return (
        <ChakraProvider>
            <MsalProvider instance={instance}>
                <PageLayout>
                    <Pages />
                </PageLayout>
            </MsalProvider>
        </ChakraProvider>
    );
};

export default App;
