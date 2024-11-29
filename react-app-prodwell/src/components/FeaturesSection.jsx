import React from "react";
import FeatureCard from "./FeatureCard";

const FeaturesSection = () => (
  <section id="features" className="w-full bg-white py-16 px-8 text-center">
    <div className="flex flex-col md:flex-row md:space-x-8 space-y-8 md:space-y-0">
      <FeatureCard
        title="AI-Driven Insights"
        description="Analyze productivity and wellness data to provide actionable insights."
      />
      <FeatureCard
        title="Personalized Interventions"
        description="Receive tailored suggestions like break times and counseling."
      />
      <FeatureCard
        title="Real-Time Tracking"
        description="Monitor employee metrics in real-time for up-to-date insights."
      />
    </div>
  </section>
);

export default FeaturesSection;