import React from "react";

const FeatureCard = ({ image, title, description, role }) => (
  <div className="flex flex-col items-center text-center bg-white-100 p-6 rounded-lg shadow-md w-full md:w-1/4 space-y-4">
    <img src={image} alt="Developer" className="w-32 h-32 mb-2 rounded-full" />
    <h3 className="text-xl font-semibold text-gray-800">{title}</h3>
    <p className="text-gray-600">{description}</p>
    <hr className="w-3/4 border-gray-200 my-2" />
    <h4 className="text-lg font-semibold text-gray-800">Role:</h4>
    <p className="text-gray-600 text-sm italic">{role}</p>
  </div>
);
export default FeatureCard;