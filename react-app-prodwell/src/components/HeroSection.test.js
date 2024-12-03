import React from "react";
import { render, screen } from "@testing-library/react";
import HeroSection from "./HeroSection";
import "@testing-library/jest-dom"; 

describe("HeroSection Component", () => {
  test("renders HeroSection component correctly", () => {
    render(<HeroSection />);

    expect(screen.getByText("Enhance Employee")).toBeInTheDocument();
    expect(screen.getByText("Wellness & Productivity")).toBeInTheDocument();
    expect(screen.getByText("with ProdWell")).toBeInTheDocument();

    const description = screen.getByText(/Leverage AI to track productivity metrics and wellness data/);
    expect(description).toBeInTheDocument();
    
    const button = screen.getByRole("button", { name: /Get started/i });
    expect(button).toBeInTheDocument();
  });

  test("checks button hover class", () => {
    render(<HeroSection />);

    const button = screen.getByRole("button", { name: /Get started/i });
    expect(button).toHaveClass("hover:bg-purple-700");
  });

  test("checks the button click (optional)", () => {
    render(<HeroSection />);
    
    const button = screen.getByRole("button", { name: /Get started/i });
    button.click(); 
  });
});
