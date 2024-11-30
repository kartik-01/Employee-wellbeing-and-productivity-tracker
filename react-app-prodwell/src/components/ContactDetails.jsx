import React from "react";
import FeatureCard from "./FeatureCard";

const ContactDetails = () => (
  <section id="features" className="w-full bg-white py-16 px-8 text-center">
    <div className="flex flex-col md:flex-row md:space-x-8 space-y-8 md:space-y-0">
      <FeatureCard
        imageSrc="/photos/male.png"
        title="Kenil Gopani"
        description="SJSU ID: 017992624"
      />
      <FeatureCard
        imageSrc="/photos/yash.jpeg"
        title="Yash Savani"
        description="SJSU ID: 017581122"
      />
      <FeatureCard
        imageSrc="/photos/male.png"
        title="Kartik Chindarkar"
        description="SJSU ID: 017583878"
      />
      <FeatureCard
        imageSrc="/photos/female.jpg"
        title="Vyshnavi D P"
        description="SJSU ID: 018178979"
      />
    </div>
  </section>
);

export default ContactDetails;