import { LogLevel } from "@azure/msal-browser";

/**
 * Enter here the user flows and custom policies for your B2C application.
 */
export const b2cPolicies = {
    names: {
        signUpSignIn: 'B2C_1_SignUpSignIn'
    },
    authorities: {
        signUpSignIn: {
            authority: 'https://prodwelltest.b2clogin.com/prodwelltest.onmicrosoft.com/B2C_1_SignUpSignIn',
        },
        // forgotPassword: {
        //     authority: 'https://prodwelltest.b2clogin.com/prodwelltest.onmicrosoft.com/B2C_1_reset_v3',
        // },
        // editProfile: {
        //     authority: 'https://prodwelltest.b2clogin.com/prodwelltest.onmicrosoft.com/b2c_1_edit_profile_v2',
        // },
    },
    authorityDomain: 'prodwelltest.b2clogin.com'
};

/**
 * Configuration object to be passed to MSAL instance on creation.
 */
export const msalConfig = {
    auth: {
        clientId: '1390abed-3d4d-497d-ab36-84a00ffad2bd', // Updated client ID
        authority: b2cPolicies.authorities.signUpSignIn.authority, // Updated authority
        knownAuthorities: [b2cPolicies.authorityDomain], // Updated known authorities
        redirectUri: 'http://localhost:3000', // Ensure this is registered in Azure Portal
        postLogoutRedirectUri: 'http://localhost:3000',
        navigateToLoginRequestUrl: false,
    },
    cache: {
        cacheLocation: 'sessionStorage',
        storeAuthStateInCookie: false,
    },
    system: {
        loggerOptions: {
            loggerCallback: (level, message, containsPii) => {
                if (containsPii) {
                    return;
                }
                switch (level) {
                    case LogLevel.Error:
                        console.error(message);
                        return;
                    case LogLevel.Info:
                        console.info(message);
                        return;
                    case LogLevel.Verbose:
                        console.debug(message);
                        return;
                    case LogLevel.Warning:
                        console.warn(message);
                        return;
                    default:
                        return;
                }
            },
        },
    },
};

/**
 * Add here the endpoints and scopes when obtaining an access token for protected web APIs.
 */
export const protectedResources = {
    apiTodoList: {
        endpoint: 'http://localhost:5000/api/todolist',
        scopes: {
            read: ['https://prodwelltest.onmicrosoft.com/TodoList/ToDoList.Read'],
            write: ['https://prodwelltest.onmicrosoft.com/TodoList/ToDoList.ReadWrite'],
        },
    },
};

/**
 * Scopes you add here will be prompted for user consent during sign-in.
 */
export const loginRequest = {
    scopes: [...protectedResources.apiTodoList.scopes.read, ...protectedResources.apiTodoList.scopes.write],
};