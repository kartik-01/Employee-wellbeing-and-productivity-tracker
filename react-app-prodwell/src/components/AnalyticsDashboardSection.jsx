import React from "react";
import yash from "../assets/images/Dashboard.avif";

const AnalyticsDashboardSection = () => (
  <section className="w-full bg-white py-16 px-16 flex flex-col md:flex-row items-center justify-between">
    {/* Image Section */}
    <div className="w-full md:w-1/2 mb-8 md:mb-0">
      <img src={yash} alt="Analytics Dashboard" className="w-full h-auto max-h-96 object-contain" />
    </div>
    
    {/* Text Section */}
    <div className="w-full md:w-1/2 text-left md:text-left pl-12 p-6">
      <h2 className="text-3xl font-semibold text-gray-800">Analytics Dashboard</h2>
      <p className="text-lg text-gray-600 mt-4">
        Gain valuable insights into client interactions including <br />
        views, engagement, and conversion rates.
      </p>
    </div>
  </section>
);

export default AnalyticsDashboardSection;
