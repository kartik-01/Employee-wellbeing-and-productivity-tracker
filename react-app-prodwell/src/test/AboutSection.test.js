import React from "react";
import { render, screen } from "@testing-library/react";
import AboutSection from "../components/AboutSection";

describe("AboutSection", () => {
  it("renders Development Team header", () => {
    render(<AboutSection />);
    
    const headingElement = screen.getByText(/Development Team/i);
    expect(headingElement).toBeInTheDocument();

    expect(headingElement).toHaveClass("text-5xl", "font-bold", "text-gray-800", "mb-4");
  });
});
