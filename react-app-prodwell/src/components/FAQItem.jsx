import React from "react";

const FAQItem = ({ question }) => (
  <details className="bg-white p-4 rounded-lg shadow-md">
    <summary className="font-semibold text-gray-800 cursor-pointer">{question}</summary>
    <p className="mt-2 text-gray-600">This is a sample answer. Add your specific answer here.</p>
  </details>
);

export default FAQItem;