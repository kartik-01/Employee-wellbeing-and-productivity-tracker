import React from "react";
import FeatureCard from "./FeatureCard";
import yash from "../assets/images/Yash.jpeg";

const ContactDetails = () => (
  <section id="features" className="w-full bg-white py-16 px-8 text-center">
    <div className="flex flex-col md:flex-row md:space-x-8 space-y-8 md:space-y-0">
      <FeatureCard
        title="Kenil Gopani"
        description="SJSU ID: 017992624"
      />
      <FeatureCard
        image = {yash}
        title="Yash Savani"
        description="SJSU ID: 017581122"
      />
      <FeatureCard
        title="Kartik Chindarkar"
        description="SJSU ID: 017583878"
      />
      <FeatureCard
        title="Vyshnavi D P"
        description="SJSU ID: 018178979"
      />
    </div>
  </section>
);

export default ContactDetails;