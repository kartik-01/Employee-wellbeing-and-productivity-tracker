import React from "react";
import FeatureCard from "./FeatureCard";
import yash from "../assets/images/Yash.jpeg";
import kenil from "../assets/images/Kenil.jpeg"
import vyshnavi from "../assets/images/Vyshnavi.jpeg"
import kartik from "../assets/images/kartik.jpeg"

const ContactDetails = () => (
  <section id="features" className="w-full bg-white py-16 px-8 text-center">
    <div className="flex flex-col md:flex-row md:space-x-8 space-y-8 md:space-y-0">
    <FeatureCard
        image = {kartik}
        title="Kartik Chindarkar"
        description="SJSU ID: 017583878"
        role="Team Lead"
      />
      <FeatureCard
        image = {kenil}
        title="Kenil Gopani"
        description="SJSU ID: 017992624"
        role="Devops and Backend Developer"
      />
        <FeatureCard
        image = {vyshnavi}
        title="Vyshnavi D P"
        description="SJSU ID: 018178979"
        role="Full Stack Developer and Scrum Master"
      />
      <FeatureCard
        image = {yash}
        title="Yash Savani"
        description="SJSU ID: 017581122"
        role="Front End Developer"
      />
    </div>
  </section>
);

export default ContactDetails;