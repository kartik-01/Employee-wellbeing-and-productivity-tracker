import React from "react";

const AnalysisItem = ({ question, l1, l2, l3 }) => (
    <details className="bg-white p-4 rounded-lg shadow-md">
        <summary className="font-semibold text-gray-800 cursor-pointer text-center">{question}</summary>
        <ul className="list-disc ml-1 text-gray-600">
            {l1 && (
                <li>{l1}</li>
            )}
            {l2 && (
                <li>{l2}</li>
            )}
            {l3 && (
                <li>{l3}</li>
            )}
        </ul>
    </details>
);

export default AnalysisItem;