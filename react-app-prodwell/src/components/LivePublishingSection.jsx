import React from "react";
import yash from "../assets/images/LivePublishing.jpeg"

const LivePublishingSection = () => (
  <section className="w-full bg-white-50 py-16 px-16 pl-28 flex flex-col md:flex-row items-center">
    <div className="md:w-1/2 text-left md:text-left">
      <h2 className="text-3xl font-semibold text-gray-800">Live Publishing</h2>
      <p className="text-lg text-gray-600 mt-4">Publish live with a single click, allowing clients to access <br></br>them anytime, anywhere.</p>
    </div>
    <div className="w-full md:w-1/2 mb-8 md:mb-0">
      <img src={yash} alt="Analytics Dashboard" className="w-full h-auto max-h-96 object-contain" />
    </div>
  </section>
);

export default LivePublishingSection;