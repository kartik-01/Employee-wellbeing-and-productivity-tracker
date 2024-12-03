import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import FAQItem from './FAQItem';

describe('FAQItem Component', () => {
    const sampleQuestion = "What is React?";

    test('renders the FAQItem question correctly', () => {
        render(<FAQItem question={sampleQuestion} />);

        const questionElement = screen.getByText(sampleQuestion);
        expect(questionElement).toBeInTheDocument();
    });

    test('toggles the answer visibility on click', () => {
        render(<FAQItem question={sampleQuestion} />);

        const answerElement = screen.queryByText(/this is a sample answer\. add your specific answer here\./i);
        expect(answerElement).not.toBeVisible();

        const summaryElement = screen.getByText(sampleQuestion);
        fireEvent.click(summaryElement);

        expect(screen.getByText(/this is a sample answer\. add your specific answer here\./i)).toBeVisible();

        fireEvent.click(summaryElement);

        expect(screen.queryByText(/this is a sample answer\. add your specific answer here\./i)).not.toBeVisible();
    });
});
