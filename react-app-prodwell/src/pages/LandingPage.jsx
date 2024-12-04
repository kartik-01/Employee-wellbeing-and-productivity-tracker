import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import userService from "../services/userService";
import { useMsal } from "@azure/msal-react";
import { EventType } from "@azure/msal-browser";
import HeroSection from "../components/HeroSection";
import FeaturesSection from "../components/FeaturesSection";
import RealTimeEditingSection from "../components/RealTimeEditingSection";
import AnalyticsDashboardSection from "../components/AnalyticsDashboardSection";
import LivePublishingSection from "../components/LivePublishingSection";
import AdvancedDocumentsSection from "../components/AdvancedDocumentsSection";
import FAQSection from "../components/FAQSection";
import Features from "../components/Features";
import "./../index.css"

export const LandingPage = () => {
  const { instance } = useMsal();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const callbackId = instance.addEventCallback((event) => {
      if (
        (event.eventType === EventType.LOGIN_SUCCESS || 
         event.eventType === EventType.ACQUIRE_TOKEN_SUCCESS) &&
        event.payload.account
      ) {
        checkPersonalityAnswer(event.payload.idTokenClaims.oid);
      }
    });

    return () => {
      if (callbackId) {
        instance.removeEventCallback(callbackId);
      }
    };
  }, [instance, navigate]);

  const checkPersonalityAnswer = async (userId) => {
    setIsLoading(true);
    try {
      const response = await userService.checkPersonalityAnswerExists(userId);
      if (!response.data) {
        navigate("/survey");
      }
    } catch (error) {
      console.error("Error checking personality answer:", error);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="flex flex-col items-center bg-white-50">
      <HeroSection />
      <FeaturesSection />
      <Features />
      <RealTimeEditingSection />
      <AnalyticsDashboardSection />
      <FAQSection />
    </div>
  );
};