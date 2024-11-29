import React from "react";
import Footer from "./Footer";
import { NavigationBar } from "./NavigationBar";

export const PageLayout = (props) => {
  return (
    <div className="flex flex-col min-h-screen">
      {/* Navigation Bar */}
      <NavigationBar />

      {/* Page Content */}
      <main className="flex-grow">
        {props.children}
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
};
