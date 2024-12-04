import React from "react";
import { render, screen } from "@testing-library/react";
import AdvancedDocumentsSection from "../components/AdvancedDocumentsSection";

describe("AdvancedDocumentsSection", () => {
  it("renders the 'Advanced Documents' header", () => {
    render(<AdvancedDocumentsSection />);
    
    const headingElement = screen.getByText(/Advanced Documents/i);
    expect(headingElement).toBeInTheDocument();
  });

  it("renders the description text correctly", () => {
    render(<AdvancedDocumentsSection />);
    const descriptionElement = screen.getByText(
      /Know when someone reads your documents, how long for, and if they've downloaded it./i
    );
    expect(descriptionElement).toBeInTheDocument();
  });

  it("renders the document image container", () => {
    render(<AdvancedDocumentsSection />);
    
    const imageContainer = document.querySelector(".bg-gray-200");
    expect(imageContainer).toBeInTheDocument();
  });
});


