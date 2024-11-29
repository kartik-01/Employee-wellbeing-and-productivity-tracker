import React from "react";

const ContactUs = () => {
  return (
    <div className="flex flex-col items-center bg-gray-50 min-h-screen">
      <section className="w-full max-w-4xl bg-white shadow-lg p-8 rounded-lg mt-8">
        <h2 className="text-2xl font-semibold text-gray-800 text-center mb-6">Send Us a Message</h2>
        <form className="space-y-6">
          <div className="flex flex-col">
            <label htmlFor="name" className="text-gray-700 font-medium">Name</label>
            <input
              type="text"
              id="name"
              className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Your Name"
            />
          </div>
          <div className="flex flex-col">
            <label htmlFor="email" className="text-gray-700 font-medium">Email</label>
            <input
              type="email"
              id="email"
              className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Your Email"
            />
          </div>
          <div className="flex flex-col">
            <label htmlFor="message" className="text-gray-700 font-medium">Message</label>
            <textarea
              id="message"
              rows="5"
              className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Your Message"
            ></textarea>
          </div>
          <button
            type="submit"
            className="w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600 transition duration-300"
          >
            Send Message
          </button>
        </form>
      </section>

    </div>
  );
};

export default ContactUs;
