import React from "react";

const RealTimeEditingSection = () => (
  <section className="w-full bg-white-50 py-16 px-8 flex flex-col md:flex-row items-center">
    <div className="md:w-1/2 text-center md:text-left">
      <h2 className="text-3xl font-semibold text-gray-800 ml-4">Real-Time Editing</h2>
      <p className="text-lg text-gray-600 mt-4 ml-4">Make edits in real-time, ensuring presentations are always updated & reflective of latest work.</p>
    </div>
    <div className="md:w-1/2 mt-8 md:mt-0">
      <div className="bg-gray-200 h-48 w-full rounded-lg mr-4"></div>
    </div>
  </section>
);

export default RealTimeEditingSection;