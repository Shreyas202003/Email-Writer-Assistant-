# Email-Assistant
<h3>Project Structure</h3>
The project consists of two main components:
1.<h4>Backend (Spring Boot)</h4>
Purpose: Handles API requests and communicates with the AI service.
Tech Stack: Java, Spring Boot, WebClient, Jackson.
Key Files:
EmailGeneratorController.java: API endpoint (/api/email/generate).
EmailGeneratorService.java: Logic for AI interaction and response processing.
EmailRequest.java: Data model for email content and tone.
application.properties: Configuration for API keys and URLs.
2. Chrome Extension
Purpose: Integrates with Gmail to provide a user-friendly interface.
Tech Stack: JavaScript, HTML/CSS (via content scripts), Manifest V3.
Key Files:
manifest.json: Extension configuration.
content.js: Injects the "Generate Reply" button and handles API calls.
content.css: Styles the button.
icons/: Contains extension icons.
Prerequisites
Java 17+: For the Spring Boot backend.
Maven: To build and run the backend.
Google Chrome: For the extension.
API Key: A valid Gemini API key (or equivalent AI service key).
Node.js: Optional, only if you plan to extend the frontend further.
Setup Instructions
Backend Setup
Clone the Repository:
git clone https://github.com/Shreyas202003/Email-Writer-Assistant.git
cd email-writer-assistant

Configure Environment:
Open backend/src/main/resources/application.properties.

Set your API key and URL:
gemini.api.key=YOUR_GEMINI_API_KEY
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent

Build and Run:
cd backend
mvn clean install
mvn spring-boot:run
The backend will start at http://localhost:8080.
Chrome Extension Setup
Navigate to Extension Folder:
cd extension
Load in Chrome:
Open Chrome and go to chrome://extensions/.
Enable "Developer mode" (top right).
Click "Load unpacked" and select the extension folder.
Verify Icons:
Ensure the icons/ folder contains icon16.png, icon48.png, and icon128.png.
Usage
Start the Backend:
Ensure the Spring Boot app is running (http://localhost:8080).
Open Gmail:
Go to mail.google.com in Chrome.
Open a new compose window (click "Compose").
Generate a Reply:
Type or paste email content into the compose window.
Click the "Generate Reply" button (appears at the bottom of the compose area).
The AI-generated reply will replace the content in the text box, styled according to the default tone ("professional").
Customize Tone (Optional)**:
Modify the content.js file to change the tone parameter in the fetch request (e.g., "friendly", "casual").
Example API Request
The extension sends a POST request to the backend:

{
    "emailContent": "Hi, can you help me with this?",
    "tone": "professional"
}

Response (example):
Dear Sender,

Thank you for reaching out. I’d be happy to assist you with your request. Please let me know how I can help.

Best regards,
[Your Name]
