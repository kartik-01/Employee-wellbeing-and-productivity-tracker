import React from "react";

const Footer = () => {
  return (
    <div className="bg-gray-800 text-gray-300 py-6">
      <div className="container mx-auto px-4">
        <div className="flex flex-col md:flex-row justify-between items-center">
          {/* Left Section */}
          <div className="text-center md:text-left mb-4 md:mb-0">
            <p>&copy; {new Date().getFullYear()} ProdWell. All Rights Reserved.</p>
          </div>
          {/* Right Section */}
          <div className="flex space-x-4 text-sm">
            <a href="/privacy" className="hover:text-white transition">
              Privacy Policy
            </a>
            <span>|</span>
            <a href="/terms" className="hover:text-white transition">
              Terms of Service
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Footer;