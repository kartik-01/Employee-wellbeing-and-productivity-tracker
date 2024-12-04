import React from "react";
import { WandSparkles } from 'lucide-react';
import { Atom } from 'lucide-react';
import { Gem } from 'lucide-react';

const FeaturesSection = () => (
  <section id="features" className="w-full bg-white py-16 px-8 text-center">
    <div className="flex flex-col md:flex-row md:space-x-8 space-y-8 md:space-y-0">
      {/* First Card */}
      <div className="flex flex-col items-center text-center bg-white p-6 rounded-lg shadow-md w-full md:w-1/3">
        <div className="flex items-center justify-center bg-[#EDFCF7] p-6 rounded-full mb-4">
          <WandSparkles className="text-[#18BA64] w-7 h-7" />
        </div>
        <h3 className="text-xl font-semibold text-gray-800 mb-2">AI-Driven Insights</h3>
        <p className="text-gray-600">Analyze productivity and wellness data to provide actionable insights.</p>
      </div>

      {/* Second Card */}
      <div className="flex flex-col items-center text-center bg-white p-6 rounded-lg shadow-md w-full md:w-1/3">
        <div className="flex items-center justify-center bg-[#EDFCF7] p-6 rounded-full mb-4">
          <Atom className="text-[#18BA64] w-7 h-7" />
        </div>
        <h3 className="text-xl font-semibold text-gray-800 mb-2">Personalized Interventions</h3>
        <p className="text-gray-600">Receive tailored suggestions like break times and counseling.</p>
      </div>

      {/* Third Card */}
      <div className="flex flex-col items-center text-center bg-white p-6 rounded-lg shadow-md w-full md:w-1/3">
        <div className="flex items-center justify-center bg-[#EDFCF7] p-6 rounded-full mb-4">
          <Gem className="text-[#18BA64] w-7 h-7" />
        </div>
        <h3 className="text-xl font-semibold text-gray-800 mb-2">Real-Time Tracking</h3>
        <p className="text-gray-600">Monitor employee metrics in real-time for up-to-date insights.</p>
      </div>
    </div>
  </section>
);

export default FeaturesSection;
