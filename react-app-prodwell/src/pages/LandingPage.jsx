// // src/pages/LandingPage.jsx

// import React from 'react';
// import { Container, Row, Col, Button } from 'react-bootstrap';
// import './styles/LandingPage.css';

// export const LandingPage = () => {
//     return (
//         <>
//             <div className="main-section">
//                 <Container>
//                     <Row className="justify-content-center text-center">
//                         <Col md={6}>
//                             <h1 className="main-heading">Enhance Employee Wellness & Productivity with ProdWell</h1>
//                             <p className="lead main-subtext">
//                                 Leverage AI to track productivity metrics and wellness data, providing personalized insights and interventions for your team.
//                             </p>
//                             <Button variant="primary" size="l" className="cta-button">Get started</Button>
//                         </Col>
//                     </Row>
//                 </Container>
//             </div>

//             <Container className="features-section mt-5">
//                 <Row>
//                     <Col md={4} className="feature">
//                         <h3>AI-Driven Insights</h3>
//                         <p>Analyze productivity and wellness data to provide actionable insights.</p>
//                     </Col>
//                     <Col md={4} className="feature">
//                         <h3>Personalized Interventions</h3>
//                         <p>Receive tailored suggestions like break times and counseling.</p>
//                     </Col>
//                     <Col md={4} className="feature">
//                         <h3>Real-Time Tracking</h3>
//                         <p>Monitor employee metrics in real-time for up-to-date insights.</p>
//                     </Col>
//                 </Row>
//             </Container>
//         </>
//     );
// };

// src/LandingPage.jsx
import React from "react";
import HeroSection from "../components/HeroSection";
import FeaturesSection from "../components/FeaturesSection";
import RealTimeEditingSection from "../components/RealTimeEditingSection";
import AnalyticsDashboardSection from "../components/AnalyticsDashboardSection";
import LivePublishingSection from "../components/LivePublishingSection";
import AdvancedDocumentsSection from "../components/AdvancedDocumentsSection";
import FAQSection from "../components/FAQSection";
import Features from "../components/Features";
import "./../index.css"


export const LandingPage = () => (
  <div className="flex flex-col items-center bg-white-50">
    {/* <main className="mt-20 w-full flex flex-col items-center text-center space-y-20"> */}
      <HeroSection />
      <FeaturesSection />
      <Features />
      <RealTimeEditingSection />
      <AnalyticsDashboardSection />
      <LivePublishingSection />
      <AdvancedDocumentsSection />
      <FAQSection />
    {/* </main> */}
  </div>
);

