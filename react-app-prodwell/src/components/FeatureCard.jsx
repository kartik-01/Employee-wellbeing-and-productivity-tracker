import React from "react";


const FeatureCard = ({ imageSrc, title, description }) => (
  <div className="flex flex-col items-center text-center bg-white-100 p-6 rounded-lg shadow-md w-full md:w-1/3">
    {/* <img src={imageSrc} alt="feature image" className="mb-4" /> */}
    <img src={imageSrc} alt="feature image" className="mb-4 w-64 h-72 object-cover" />
    <h3 className="text-xl font-semibold text-gray-800 mb-2">{title}</h3>
    <p className="text-gray-600">{description}</p>
  </div>
);

export default FeatureCard;