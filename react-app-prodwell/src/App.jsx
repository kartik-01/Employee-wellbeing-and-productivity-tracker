import React, { useEffect } from 'react';
import { MsalProvider, useMsal } from '@azure/msal-react';
import { EventType } from '@azure/msal-browser';
import { Routes, Route } from "react-router-dom";

import { PageLayout } from './components/PageLayout';
import { TodoList } from './pages/TodoList';
import { Home } from './pages/Home';
import { b2cPolicies, protectedResources } from './authConfig';
import { compareIssuingPolicy } from './utils/claimUtils';
import { ChakraProvider } from '@chakra-ui/react'
import userService from './services/userService';

import './styles/App.css';

const Pages = () => {
    const { instance } = useMsal();

    useEffect(() => {
        const callbackId = instance.addEventCallback((event) => {
            if (
                (event.eventType === EventType.LOGIN_SUCCESS || event.eventType === EventType.ACQUIRE_TOKEN_SUCCESS) &&
                event.payload.account
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
    }, [instance]);

    return (
        <Routes>
            <Route path="/todolist" element={<TodoList />} />
            <Route path="/" element={<Home />} />
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
