import React from "react";
import logo from "../assets/images/prodwell.png";

const Footer = () => {
  return (
    <div className="bg-gray-800 text-gray-300 py-6">
      <div className="container mx-auto px-4">
        <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
          {/* Left Section */}
          <div className="text-center md:text-left">
            <p>&copy; {new Date().getFullYear()} ProdWell. All Rights Reserved.</p>
          </div>
          {/* Right Section */}
          <div className="flex flex-col md:flex-row items-center md:space-x-4 text-center md:text-left space-y-4 md:space-y-0">
            <div className="flex space-x-2">
              <a href="#" className="hover:text-white transition">
                CMPE272
              </a>
              <span>|</span>
              <a href="#" className="hover:text-white transition">
                Enterprise Software Platforms
              </a>
            </div>
            <a href="/" className="flex items-center justify-center">
              <img
                src={logo}
                alt="ProdWell logo"
                className="w-8 h-8 rounded-full"
              />
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Footer;
