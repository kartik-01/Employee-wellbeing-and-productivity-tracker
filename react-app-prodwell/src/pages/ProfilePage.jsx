// src/pages/LandingPage.jsx

import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import './styles/ProfilePage.css';

export const ProfilePage = () => {
    return (
        <>
            <div className="main-section">
                <Container>
                    <Row className="justify-content-center text-center">
                        <Col md={6}>
                            <h1 className="main-heading">Profile</h1>
                        </Col>
                    </Row>
                </Container>
            </div>

            <Container className="features-section mt-5">
                <Row>
                    <Col md={4} className="feature">
                        <h3>First Name</h3>
                        <p>Test</p>
                    </Col>
                    <Col md={4} className="feature">
                        <h3>Last Name</h3>
                        <p>Test</p>
                    </Col>
                    <Col md={4} className="feature">
                        <h3>Job Role</h3>
                        <p>Software Engineering</p>
                    </Col>
                </Row>
            </Container>
        </>
    );
};