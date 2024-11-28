import React, { useState } from 'react';
import { Nav, Navbar, Button } from 'react-bootstrap';
import { useMsal } from '@azure/msal-react';
import { loginRequest } from '../authConfig';
import { useNavigate } from 'react-router-dom';
import './styles/NavigationBar.css';
import logo from '../logo.svg';

export const NavigationBar = () => {
    const { instance } = useMsal();
    const activeAccount = instance.getActiveAccount();
    const [isLoggingOut, setIsLoggingOut] = useState(false);
    const navigate = useNavigate();

    const handleLoginSignUp = () => {
        instance.loginRedirect(loginRequest).catch((error) => console.log(error));
    };

    const handleLogout = async () => {
        try {
            await instance.logoutRedirect();
            sessionStorage.removeItem('msal.id.token'); // Ensure session storage is cleared
            setIsLoggingOut(true);
        } catch (error) {
            console.log(error);
        }
        setIsLoggingOut(false);
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
                <Nav className="mr-auto">
                    <Nav.Link onClick={() => navigate('/dashboard')}>Dashboard</Nav.Link>
                    <Nav.Link href="#about">About Us</Nav.Link>
                    <Nav.Link href="#contact">Contact Us</Nav.Link>
                    <Nav.Link onClick={()=>navigate('/survey')}   href="#survey">Survey</Nav.Link>
                </Nav>
                {activeAccount ? (
                    <Button
                        variant="primary"
                        onClick={handleLogout}
                        className="ml-auto small-btn"
                        disabled={isLoggingOut}
                    >
                        {isLoggingOut ? 'Logging out...' : 'Logout'}
                    </Button>
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