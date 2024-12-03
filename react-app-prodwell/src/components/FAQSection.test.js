import React from "react";
import { render, screen } from "@testing-library/react";
import FAQSection from "./FAQSection";

jest.mock("./FAQItem", () => ({ question }) => (
  <div>{question}</div>
));

describe("FAQSection", () => {
  it("renders the FAQ section with correct heading", () => {
    render(<FAQSection />);

    const heading = screen.getByText(/FAQ/i);
    expect(heading).toBeInTheDocument();
    expect(heading).toHaveClass("text-3xl font-semibold text-gray-800 text-center mb-8");
  });

  it("renders the correct FAQ items", () => {
    render(<FAQSection />);

    expect(screen.getByText(/What is Framer\?/i)).toBeInTheDocument();
    expect(screen.getByText(/Is it easy to learn\?/i)).toBeInTheDocument();
    expect(screen.getByText(/Do I need to code\?/i)).toBeInTheDocument();
  });

  it("renders the correct number of FAQ items", () => {
    render(<FAQSection />);

    const faqItems = screen.getAllByText(/What is Framer\?|Is it easy to learn\?|Do I need to code\?/i);
    expect(faqItems.length).toBe(3);
  });
});
