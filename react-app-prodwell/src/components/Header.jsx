import React from "react";

const Header = () => (
  <header className="w-full bg-white shadow-md fixed top-0 z-10 flex justify-between items-center px-6 py-4">
    <div className="text-xl font-bold">ProdWell</div>
    <nav className="space-x-6">
      <a href="" className="text-gray-700 hover:text-gray-900">Features</a>
      <a href="" className="text-gray-700 hover:text-gray-900">About Us</a>
      <a href="" className="text-gray-700 hover:text-gray-900">Contact Us</a>
    </nav>
    <button className="bg-purple-600 text-white py-2 px-4 rounded-lg hover:bg-purple-700">Login/Sign Up</button>
  </header>
);

export default Header;




