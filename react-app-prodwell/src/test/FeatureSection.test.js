import React from 'react';
import { render, screen } from '@testing-library/react';
import FeatureSection from '../components/Features';

describe('FeatureSection Component', () => {
  test('renders the title text correctly', () => {
    render(<FeatureSection />);
    
    const heading = screen.getByText(/Discover ProdWell's Powerful Features/i);
    expect(heading).toBeInTheDocument();
  });

  test('renders the description text correctly', () => {
    render(<FeatureSection />);
    
    const description = screen.getByText(/Discover ProdWell's suite of powerful tools designed to enhance well-being/i);
    expect(description).toBeInTheDocument();
  });

  test('renders the subheading text correctly', () => {
    render(<FeatureSection />);
    
    const subheadings = screen.getAllByText(/Features/i);
    expect(subheadings[1]).toBeInTheDocument();

  });

  test('renders the paragraph with line break correctly', () => {
    render(<FeatureSection />);
    
    const paragraph = screen.getByText(/boost productivity, and unlock a world of possibilities for success./i);
    expect(paragraph).toBeInTheDocument();
  });
});
