import React, { useState, useEffect } from 'react';
import { Nav, Navbar, Button, Dropdown } from 'react-bootstrap';
import { useMsal } from '@azure/msal-react';
import { loginRequest, b2cPolicies } from '../authConfig';
import { useNavigate } from 'react-router-dom';
import './styles/NavigationBar.css';
import logo from '../logo.svg';
import { EventType } from '@azure/msal-browser';

export const NavigationBar = () => {
    const { instance } = useMsal();
    const [userName, setUserName] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        // Function to update userName based on active account
        const updateUserName = () => {
            const account = instance.getActiveAccount();
            if (account && account.idTokenClaims) {
                setUserName(account.idTokenClaims.given_name || 'User Account');
            } else {
                setUserName(''); // Clear username if no account is found
            }
        };

        // Initial call to set userName
        updateUserName();

        // Add event callback to listen for login success
        const callbackId = instance.addEventCallback((event) => {
            if (event.eventType === EventType.LOGIN_SUCCESS || 
                event.eventType === EventType.ACQUIRE_TOKEN_SUCCESS) {
                updateUserName(); // Update username on successful login
            }
        });

        return () => {
            // Clean up event callback on unmount
            if (callbackId) {
                instance.removeEventCallback(callbackId);
            }
        };
    }, [instance]);

    const handleLoginSignUp = () => {
        instance.loginRedirect(loginRequest).catch((error) => console.log(error));
    };

    const handleLogout = () => {
        instance.logoutRedirect().catch((error) => console.log(error));
    };

    const handleResetPassword = () => {
        instance.loginRedirect({
            authority: b2cPolicies.authorities.resetPassword.authority,
            scopes: []
        }).catch((error) => console.log(error));
    };

    const handleEditProfile = () => {
        instance.loginRedirect({
            authority: b2cPolicies.authorities.editProfile.authority,
            scopes: []
        }).catch((error) => console.log(error));
    };

    return (
<Navbar bg="light" expand="lg" className="px-3 custom-navbar">
    <Navbar.Brand href="/" className="mr-auto font-weight-bold">
        <img
            src={logo}
            width="30"
            height="30"
            className="d-inline-block align-top"
            alt="ProdWell logo"
        />
        <span className="ml-2">ProdWell</span>
    </Navbar.Brand>
    <Navbar.Toggle aria-controls="basic-navbar-nav" />
    <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="me-auto">
            <Nav.Link onClick={() => navigate('/dashboard')}>Dashboard</Nav.Link>
            <Nav.Link href="/aboutus">About Us</Nav.Link>
            <Nav.Link onClick={()=>navigate('/survey')}   href="#survey">Survey</Nav.Link>
        </Nav>
        {userName ? (
            <Dropdown align="end">
                <Dropdown.Toggle variant="light" id="dropdown-basic" className="user-dropdown">
                    {userName}
                </Dropdown.Toggle>

                <Dropdown.Menu>
                    <Dropdown.Item onClick={handleEditProfile}>Edit Profile</Dropdown.Item>
                    <Dropdown.Item onClick={handleResetPassword}>Reset Password</Dropdown.Item>
                    <Dropdown.Divider />
                    <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        ) : (
            <Button
                variant="primary"
                onClick={handleLoginSignUp}
                className="ml-auto small-btn"
            >
                Login/Sign Up
            </Button>
        )}
    </Navbar.Collapse>
</Navbar>
    );
};