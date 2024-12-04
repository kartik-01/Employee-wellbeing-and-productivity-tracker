import React, { useEffect, useState } from 'react';
import { MsalProvider, useMsal } from '@azure/msal-react';
import { EventType } from '@azure/msal-browser';
import { Routes, Route, useLocation, useNavigate } from "react-router-dom";
import { PageLayout } from './components/PageLayout';
import { TodoList } from './pages/TodoList';
import { Home } from './pages/Home';
import { b2cPolicies, protectedResources } from './authConfig';
import { compareIssuingPolicy } from './utils/claimUtils';
import { ChakraProvider } from '@chakra-ui/react';
import userService from './services/userService';
import { LandingPage } from './pages/LandingPage';
import { SurveyPage } from './pages/Survey';
import { DashboardPage } from './pages/Dashboard';
import './styles/App.css';
import { ProfilePage } from './pages/ProfilePage';
import { AboutUs } from './pages/AboutUs';
import { AnalyticsPage } from './pages/Analytics';

const Pages = ({ userId, setUserId }) => {
    const { instance } = useMsal();
    const navigate = useNavigate();
    const location = useLocation();
    const [initialRedirectDone, setInitialRedirectDone] = useState(false);

    useEffect(() => {
        const callbackId = instance.addEventCallback((event) => {
            if (
                (event.eventType === EventType.LOGIN_SUCCESS || event.eventType === EventType.ACQUIRE_TOKEN_SUCCESS) &&
                event.payload.account &&
                !initialRedirectDone
            ) {
                const idToken = event.payload.idToken;
                if (idToken) {
                    sessionStorage.setItem('msal.id.token', idToken);
                }

                const userData = {
                    oid: event.payload.idTokenClaims.oid,
                    email: event.payload.idTokenClaims.emails[0],
                    given_name: event.payload.idTokenClaims.given_name,
                    family_name: event.payload.idTokenClaims.family_name,
                    jobRole: event.payload.idTokenClaims.extension_JobRole,
                    jobLevel: event.payload.idTokenClaims.extension_JobLevel,
                    projectCode: event.payload.idTokenClaims.extension_ProjectCode,
                };

                userService.checkAndCreateUser(userData)
                    .then(response => {
                        console.log('User check/create response:', response.data);
                        setUserId(response.data.oid);
                    })
                    .catch(error => {
                        console.error('Error checking/creating user:', error);
                    });

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
    }, [instance, navigate, location, initialRedirectDone, setUserId]);

    return (
        <Routes>
            <Route path="/todolist" element={<TodoList />} />
            <Route path="/" element={<LandingPage />} />
            <Route path="/survey" element={<SurveyPage userId={userId} setUserId={setUserId} />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/dashboard" element={<DashboardPage userId={userId} setUserId={setUserId} />} />
            <Route path="/aboutus" element={<AboutUs />} />
            <Route path="/analytics" element={<AnalyticsPage />} />
        </Routes>
    );
};

const App = ({ instance }) => {
    const [userId, setUserId] = useState(null);

    return (
        <ChakraProvider>
            <MsalProvider instance={instance}>
                <PageLayout>
                    <Pages userId={userId} setUserId={setUserId} />
                </PageLayout>
            </MsalProvider>
        </ChakraProvider>
    );
};

export default App;
