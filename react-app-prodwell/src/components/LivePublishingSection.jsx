// src/components/LivePublishingSection.jsx
import React from "react";

const LivePublishingSection = () => (
  <section className="w-full bg-white-50 py-16 px-8 flex flex-col md:flex-row items-center">
    <div className="md:w-1/2 text-center md:text-left">
      <h2 className="text-3xl font-semibold text-gray-800">Live Publishing</h2>
      <p className="text-lg text-gray-600 mt-4">Publish live with a single click, allowing clients to access them anytime, anywhere.</p>
    </div>
    <div className="md:w-1/2 mt-8 md:mt-0">
      <div className="bg-gray-200 h-48 w-full rounded-lg"></div>
    </div>
  </section>
);

export default LivePublishingSection;
