import { render, screen } from '@testing-library/react';
import FeaturesSection from './FeaturesSection';

describe('FeaturesSection Component', () => {
  beforeEach(() => {
    render(<FeaturesSection />);
  });

  test('renders the correct number of FeatureCards', () => {
    expect(screen.getByText('AI-Driven Insights')).toBeInTheDocument();
    expect(screen.getByText('Personalized Interventions')).toBeInTheDocument();
    expect(screen.getByText('Real-Time Tracking')).toBeInTheDocument();
  });

  test('renders FeatureCard with correct title and description', () => {
    expect(screen.getByText('Analyze productivity and wellness data to provide actionable insights.')).toBeInTheDocument();
    expect(screen.getByText('Receive tailored suggestions like break times and counseling.')).toBeInTheDocument();
    expect(screen.getByText('Monitor employee metrics in real-time for up-to-date insights.')).toBeInTheDocument();
  });
});
