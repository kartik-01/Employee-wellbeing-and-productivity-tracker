import React from "react";
import logo from '../assets/images/prodwell.png';
import { Nav, Navbar, Button, Dropdown } from 'react-bootstrap';

const Footer = () => {
  return (
    <div className="bg-gray-800 text-gray-300 py-6">
      <div className="container mx-auto px-4">
        <div className="flex flex-col md:flex-row justify-between items-center">
          {/* Left Section */}
          <div className="flex space-x-4 text-l">
            <p>&copy; {new Date().getFullYear()} ProdWell. All Rights Reserved.</p>
          </div>
          {/* Right Section */}
          <div className="flex space-x-4 text-l">
            <a href="" className="hover:text-white transition">
              CMPE272
            </a>
            <span>|</span>
            <a href="" className="hover:text-white transition">
              Enterprise Software Platforms
            </a>
            <Navbar.Brand href="/" className="mr-auto font-weight-bold">
                <img
                    src={logo}
                    width="30"
                    height="30"
                    className="d-inline-block align-top"
                    alt="ProdWell logo"
                    style={{ borderRadius: "50%" }} 
                />
            </Navbar.Brand>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Footer;