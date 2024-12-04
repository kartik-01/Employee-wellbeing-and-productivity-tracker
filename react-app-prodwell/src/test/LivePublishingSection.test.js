import { render, screen } from "@testing-library/react";
import LivePublishingSection from "../components/LivePublishingSection";  

describe("LivePublishingSection", () => {
  test("renders Live Publishing heading", () => {
    render(<LivePublishingSection />);
    
    const heading = screen.getByText(/Live Publishing/i);
    expect(heading).toBeInTheDocument();
  });

  test("renders description text", () => {
    render(<LivePublishingSection />);
    
    const description = screen.getByText(/Publish live with a single click/i);
    expect(description).toBeInTheDocument();
  });

  test("ensures text is correctly aligned on large screens", () => {
    render(<LivePublishingSection />);
    
    const textParentElement = screen.getByText(/Live Publishing/i).closest("div");
    expect(textParentElement).toHaveClass("md:text-left");
  });
});
