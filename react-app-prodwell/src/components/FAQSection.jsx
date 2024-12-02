import React from "react";
import FAQItem from "./FAQItem";

const FAQSection = () => (
  <section className="w-full bg-white-50 py-16 px-8">
    <h2 className="text-3xl font-semibold text-gray-800 text-center mb-8">FAQ</h2>
    <div className="space-y-4">
      <FAQItem question="What is Framer?" answer="All is well"/>
      <FAQItem question="Is it easy to learn?" answer="All is well" />
      <FAQItem question="Do I need to code?" answer="All is well"/>
    </div>
  </section>
);

export default FAQSection;