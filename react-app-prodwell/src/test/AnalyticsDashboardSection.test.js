import React from "react";
import { render, screen } from "@testing-library/react";
import AnalyticsDashboardSection from "../components/AnalyticsDashboardSection";

describe("AnalyticsDashboardSection", () => {
  it("renders the 'Analytics Dashboard' header", () => {
    render(<AnalyticsDashboardSection />);
    
    const headingElement = screen.getByText(/Analytics Dashboard/i);
    expect(headingElement).toBeInTheDocument();
  });

  it("renders the description text correctly", () => {
    render(<AnalyticsDashboardSection />);
    
    const descriptionElement = screen.getByText(
      /Gain valuable insights into client interactions including views, engagement, and conversion rates./i
    );
    expect(descriptionElement).toBeInTheDocument();
  });

  it("renders the document image container", () => {
    render(<AnalyticsDashboardSection />);
    
    const imageContainer = document.querySelector(".bg-gray-200");
    expect(imageContainer).toBeInTheDocument();
  });
});
