import React from "react";
import FAQItem from "./FAQItem";

const FAQSection = () => (
  <section className="w-full bg-white-50 py-16 px-8">
    <h2 className="text-3xl font-semibold text-gray-800 text-center mb-8">FAQ</h2>
    <div className="space-y-4">
      <FAQItem 
        question="How does ProdWell enhance employee productivity and wellness?" 
        answer="ProdWell uses AI to track productivity and wellness metrics, providing personalized insights and interventions that boost both employee well-being and performance."
      />
      <FAQItem question="What kind of personalized interventions does ProdWell offer?" 
        answer="ProdWell provides tailored suggestions such as optimal break times and counseling, ensuring that each employee receives support suited to their unique needs."
      />
      <FAQItem question="Can I monitor employee metrics in real-time with ProdWell?"
        answer="Yes, ProdWell offers real-time tracking of employee metrics, allowing you to stay updated on their productivity and wellness at any moment."
      />
    </div>
  </section>
);

export default FAQSection;