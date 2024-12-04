import React from "react";
import { render, screen } from "@testing-library/react";
import Footer from "../components/Footer"; 
import "@testing-library/jest-dom"; 

describe("Footer Component", () => {
  test("renders the Footer component correctly", () => {
    render(<Footer />);

    const currentYear = new Date().getFullYear();
    expect(screen.getByText(`© ${currentYear} ProdWell. All Rights Reserved.`)).toBeInTheDocument();

    const cmpe272Link = screen.getByText("CMPE272");
    expect(cmpe272Link).toBeInTheDocument();
    expect(cmpe272Link).toHaveAttribute("href", "#");

    const softwarePlatformsLink = screen.getByText("Enterprise Software Platforms");
    expect(softwarePlatformsLink).toBeInTheDocument();
    expect(softwarePlatformsLink).toHaveAttribute("href", "#");

    const logoImage = screen.getByAltText("ProdWell logo");
    expect(logoImage).toBeInTheDocument();
    expect(logoImage).toHaveAttribute("src", expect.stringContaining("prodwell.png"));
  });

  test("contains correct link structure and styling", () => {
    render(<Footer />);

    const cmpe272Link = screen.getByText("CMPE272");
    expect(cmpe272Link).toHaveClass("hover:text-white");
    
    const softwarePlatformsLink = screen.getByText("Enterprise Software Platforms");
    expect(softwarePlatformsLink).toHaveClass("hover:text-white");
  });
});
