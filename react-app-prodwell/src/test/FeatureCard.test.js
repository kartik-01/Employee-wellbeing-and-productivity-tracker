import React from "react";
import { render, screen } from "@testing-library/react";
import FeatureCard from "../components/FeatureCard";

const mockImage = "https://example.com/image.jpg";
const mockTitle = "John Doe";
const mockDescription = "Software Engineer at Example Corp.";

describe("FeatureCard", () => {
  it("renders the image with correct src and alt text", () => {
    render(<FeatureCard image={mockImage} title={mockTitle} description={mockDescription} />);
    
    const image = screen.getByAltText("Developer");
    expect(image).toHaveAttribute("src", mockImage);
    expect(image).toHaveAttribute("alt", "Developer");
  });

  it("renders the title correctly", () => {
    render(<FeatureCard image={mockImage} title={mockTitle} description={mockDescription} />);
    
    const title = screen.getByText(mockTitle);
    expect(title).toBeInTheDocument();
    expect(title).toHaveClass("text-xl font-semibold text-gray-800 mb-2");
  });

  it("renders the description correctly", () => {
    render(<FeatureCard image={mockImage} title={mockTitle} description={mockDescription} />);
    
    const description = screen.getByText(mockDescription);
    expect(description).toBeInTheDocument();
    expect(description).toHaveClass("text-gray-600");
  });

  it("applies the correct class names for styling", () => {
    render(<FeatureCard image={mockImage} title={mockTitle} description={mockDescription} />);
    
    const featureCardContainer = screen.getByText(mockTitle).closest("div");
    expect(featureCardContainer).toHaveClass("flex flex-col items-center text-center bg-white-100 p-6 rounded-lg shadow-md w-full md:w-1/3");
  });
});
