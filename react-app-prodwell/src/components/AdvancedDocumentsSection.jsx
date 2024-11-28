// src/components/AdvancedDocumentsSection.jsx
import React from "react";

const AdvancedDocumentsSection = () => (
  <section className="w-full bg-white py-16 px-8 flex flex-col md:flex-row items-center">
    <div className="md:w-1/2">
      <div className="bg-gray-200 h-48 w-full rounded-lg"></div>
    </div>
    <div className="md:w-1/2 mt-8 md:mt-0 text-center md:text-left">
      <h2 className="text-3xl font-semibold text-gray-800 ml-4">Advanced Documents</h2>
      <p className="text-lg text-gray-600 mt-4 ml-4">Know when someone reads your documents, how long for, and if they've downloaded it.</p>
    </div>
  </section>
);

export default AdvancedDocumentsSection;
