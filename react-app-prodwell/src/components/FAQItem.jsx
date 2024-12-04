import React from "react";

const FAQItem = ({ question, answer }) => (
  <details className="bg-white p-4 rounded-lg shadow-md">
    <summary className="font-semibold text-gray-800 cursor-pointer">{question}</summary>
    <p className="mt-2 text-gray-600">{answer}</p>
  </details>
);

export default FAQItem;