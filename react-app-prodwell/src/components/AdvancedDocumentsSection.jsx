import React from "react";
import yash from "../assets/images/advancedDocument.jpg"

const AdvancedDocumentsSection = () => (
  <section className="w-full bg-white py-16 px-16 flex flex-col md:flex-row items-center justify-between">
    <div className="md:w-1/2">
      <img src={yash} alt="Advanced Document" className="w-full h-auto max-h-96 object-contain" />
    </div>
    <div className="md:w-1/2 mt-8 pl-12 p-6  md:mt-0 text-left md:text-left">
      <h2 className="text-3xl font-semibold text-gray-800">Advanced Documents</h2>
      <p className="text-lg text-gray-600 mt-4">Know when someone reads your documents, how long for <br></br> and if they've downloaded it.</p>
    </div>
  </section>
);

export default AdvancedDocumentsSection;