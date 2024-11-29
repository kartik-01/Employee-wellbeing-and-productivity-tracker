import React from "react";
import FeaturesSection from "../components/FeaturesSection";
import RealTimeEditingSection from "../components/RealTimeEditingSection";
import AnalyticsDashboardSection from "../components/AnalyticsDashboardSection";
import LivePublishingSection from "../components/LivePublishingSection";
import AdvancedDocumentsSection from "../components/AdvancedDocumentsSection";
import FAQSection from "../components/FAQSection";
import Features from "../components/Features";
import "./../index.css"
import AboutSection from "../components/AboutSection";
import ContactDetails from "../components/ContactDetails";


export const AboutUs = () => (
  <div className="flex flex-col items-center bg-white-30">
      <AboutSection />
      <ContactDetails />
  </div>
);