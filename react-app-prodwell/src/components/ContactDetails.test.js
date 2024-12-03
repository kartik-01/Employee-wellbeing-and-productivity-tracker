import React from "react";
import { render, screen } from "@testing-library/react";
import ContactDetails from "./ContactDetails";


jest.mock("./FeatureCard", () => ({ image, title, description }) => (
  <div>
    <img src={image} alt={title} />
    <h3>{title}</h3>
    <p>{description}</p>
  </div>
));

describe("ContactDetails", () => {
  it("renders the contact details section", () => {
    render(<ContactDetails />);

    const sectionElement = screen.getByText("Kenil Gopani");
    expect(sectionElement).toBeInTheDocument();
  });

  it("renders the names and descriptions correctly", () => {
    render(<ContactDetails />);

    expect(screen.getByText(/Kenil Gopani/i)).toBeInTheDocument();
    expect(screen.getByText(/Yash Savani/i)).toBeInTheDocument();
    expect(screen.getByText(/Kartik Chindarkar/i)).toBeInTheDocument();
    expect(screen.getByText(/Vyshnavi D P/i)).toBeInTheDocument();

    expect(screen.getByText(/SJSU ID: 017992624/i)).toBeInTheDocument();
    expect(screen.getByText(/SJSU ID: 017581122/i)).toBeInTheDocument();
    expect(screen.getByText(/SJSU ID: 017583878/i)).toBeInTheDocument();
    expect(screen.getByText(/SJSU ID: 018178979/i)).toBeInTheDocument();
  });

  it("renders the correct images for each person", () => {
    render(<ContactDetails />);

    const images = screen.getAllByRole("img");
    expect(images).toHaveLength(4);
    expect(images[0].src).toContain("Kenil.jpeg");
    expect(images[1].src).toContain("Yash.jpeg");
    expect(images[2].src).toContain("Yash.jpeg");
    expect(images[3].src).toContain("Vyshnavi.jpeg");
  });
});
