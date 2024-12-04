import React from "react";
import yash from "../assets/images/realTime.jpg"

const RealTimeEditingSection = () => (
  <section className="w-full bg-white-50 py-16 px-16 pl-64 flex flex-col md:flex-row items-center">
    <div className="md:w-1/2 text-left md:text-left">
      <h2 className="text-3xl font-semibold text-gray-800">Real-Time Editing</h2>
      <p className="text-lg text-gray-600 mt-4">Make edits in real-time, ensuring presentations are always <br></br>updated & reflective of latest work.</p>
    </div>
    <div className="md:w-1/2 mt-8 md:mt-0">
    <img src={yash} />
    </div>
  </section>
);

export default RealTimeEditingSection;