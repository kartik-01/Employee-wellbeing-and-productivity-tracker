// src/pages/LandingPage.jsx

import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import './styles/LandingPage.css';

export const LandingPage = () => {
    return (
        <>
            <div className="main-section">
                <Container>
                    <Row className="justify-content-center text-center">
                        <Col md={6}>
                            <h1 className="main-heading">Enhance Employee Wellness & Productivity with ProdWell</h1>
                            <p className="lead main-subtext">
                                Leverage AI to track productivity metrics and wellness data, providing personalized insights and interventions for your team.
                            </p>
                            <Button variant="primary" size="l" className="cta-button">Get started</Button>
                        </Col>
                    </Row>
                </Container>
            </div>

            <Container className="features-section mt-5">
                <Row>
                    <Col md={4} className="feature">
                        <h3>AI-Driven Insights</h3>
                        <p>Analyze productivity and wellness data to provide actionable insights.</p>
                    </Col>
                    <Col md={4} className="feature">
                        <h3>Personalized Interventions</h3>
                        <p>Receive tailored suggestions like break times and counseling.</p>
                    </Col>
                    <Col md={4} className="feature">
                        <h3>Real-Time Tracking</h3>
                        <p>Monitor employee metrics in real-time for up-to-date insights.</p>
                    </Col>
                </Row>
            </Container>
        </>
    );
};