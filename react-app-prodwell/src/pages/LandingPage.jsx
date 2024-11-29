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