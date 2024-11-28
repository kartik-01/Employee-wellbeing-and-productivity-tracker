// src/components/AnalyticsDashboardSection.jsx
import React from "react";

const AnalyticsDashboardSection = () => (
  <section className="w-full bg-white py-16 px-8 flex flex-col md:flex-row items-center">
    <div className="md:w-1/2">
      <div className="bg-gray-200 h-48 w-full rounded-lg ml-4"></div>
    </div>
    <div className="md:w-1/2 mt-8 md:mt-0 text-center md:text-left">
      <h2 className="text-3xl font-semibold text-gray-800 ml-8 ">Analytics Dashboard</h2>
      <p className="text-lg text-gray-600 mt-4 ml-8">Gain valuable insights into client interactions including views, engagement, and conversion rates.</p>
    </div>
  </section>
);

export default AnalyticsDashboardSection;
