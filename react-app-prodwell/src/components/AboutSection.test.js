import React from "react";
import { render, screen } from "@testing-library/react";
import AboutSection from "./AboutSection";

describe("AboutSection", () => {
  it("renders About Us header", () => {
    render(<AboutSection />);
    const headingElement = screen.getByText(/About Us/i);
    expect(headingElement).toBeInTheDocument();
    expect(headingElement).toHaveClass("text-5xl", "font-bold", "text-gray-800", "mb-4");
  });
});
