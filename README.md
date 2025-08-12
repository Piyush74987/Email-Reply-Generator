
   <h1>Email Reply Generator</h1>
    <p class="lead">AI-powered email reply generator — two delivery modes: Web app (React + Vite + MUI) and Gmail Chrome Extension (injects into Gmail UI).</p>
  </header>

  <section>
    <h2>Project Overview</h2>
    <p>
      This repo contains an Email Reply Generator that uses an AI model Google Gemini  to produce context-aware replies.
      There are two compatible frontends backed by the same Spring Boot API:
    </p>
    <ul>
      <li><strong>Web app</strong> — React + Vite + MUI. Users paste email content, choose tone, and get AI-generated replies.</li>
      <li><strong>Gmail Chrome Extension</strong> — Injects an "AI Reply" UI directly into Gmail for one-click reply generation.</li>
    </ul>
  </section>

  <section>
    <h2>Features</h2>
    <ul>
      <li>Paste original email content and generate a reply</li>
      <li>Tone selection (Casual, Professional, Friendly, etc.)</li>
      <li>Copy to clipboard & quick edit</li>
      <li>Chrome extension integrates directly with Gmail compose UI</li>
      <li>Shared backend logic (Spring Boot) for both frontends</li>
    </ul>
  </section>

  <section>
    <h2>Repository Structure (example)</h2>
    <pre>
/Email-Reply-Generator
├─ backend/                 # Spring Boot service
│  ├─ src/main/java/...
│  ├─ src/main/resources/application.properties
│  └─ pom.xml
├─ web/                     # React + Vite + MUI app
│  ├─ src/
│  ├─ package.json
│  └─ vite.config.js
├─ extension/               # Chrome extension code (manifest, content script, popup)
│  ├─ manifest.json
│  ├─ content.js
│  └─ popup.html
├─ README.html              # (this file)
└─ LICENSE
    </pre>
  </section>

  <section>
    <h2>Prerequisites</h2>
    <ul>
      <li>Java 17+ and Maven (for backend)</li>
      <li>Node.js 16+ and npm/yarn (for frontend)</li>
      <li>Chrome (for testing extension) or Chromium-based browser</li>
      <li>AI API key Google Gemini — placed in backend config</li>
    </ul>
  </section>

  <section>
    <h2>Backend — Spring Boot (Quick Setup)</h2>
    <p>
      The Spring Boot service exposes a simple endpoint like <code>POST /api/email/generate</code> that accepts JSON:
    </p>
    <pre>{
  "emailContent": "Original email text here...",
  "tone": "formal"
}
    </pre>

  <h3>Important config</h3>
    <p>Edit <code>backend/src/main/resources/application.properties</code> (or environment variables):</p>
    <pre>
server.port=8080

# Example for Google Gemini endpoint (update accordingly)
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent
gemini.api.key=YOUR_GEMINI_API_KEY
    </pre>

   <h3>Run backend</h3>
    <pre>
cd backend
mvn clean package
mvn spring-boot:run
    </pre>

   <div class="note">
      <strong>Note:</strong> If your frontend runs on another port (e.g. Vite on 5173), ensure CORS is enabled on the backend (e.g. <code>@CrossOrigin(origins="*")</code> on controller for dev).
    </div>
  </section>

  <section>
    <h2>Web Frontend — React + Vite + MUI</h2>
    <h3>Install & Run (development)</h3>
    <pre>
cd web
npm install
npm run dev
    </pre>

   <p>The app expects the backend at <code>http://localhost:8080/api/email/generate</code>. If you need to change that, update the API base URL in <code>web/src/config</code> or where Axios is configured.</p>

 <h3>Build (production)</h3>
    <pre>
npm run build
# serve the build folder with any static hosting (or integrate with backend)
    </pre>
  </section>

  <section>
    <h2>Chrome Extension — install & test</h2>
    <p>The extension integrates a small UI into Gmail. It can call the same backend endpoint to generate replies.</p>

   <h3>Development / manual install</h3>
    <ol>
      <li>Open <span class="kbd">chrome://extensions/</span></li>
      <li>Enable <strong>Developer mode</strong></li>
      <li>Click <strong>Load unpacked</strong> and select the <code>/extension</code> folder in the repo</li>
      <li>Open Gmail and test — the extension injects an "AI Reply" button into the compose box</li>
    </ol>

   <h3>manifest.json notes</h3>
    <pre>
{
  "manifest_version": 3,
  "name": "Email Reply Generator",
  "version": "1.0",
  "content_scripts": [{
    "matches": ["https://mail.google.com/*"],
    "js": ["content.js"],
    "run_at": "document_idle"
  }],
  "permissions": ["storage", "activeTab", "scripting"],
  "host_permissions": ["http://localhost:8080/*"]
}
    </pre>

   <div class="note">
      <strong>Security:</strong> When publishing to the Chrome Web Store, remove any hard-coded API keys and never call your AI provider directly from the extension with a confidential key. Prefer calling your backend which stores the key securely.
    </div>
  </section>

  <section>
    <h2>How It Works — summary</h2>
    <ol>
      <li>Frontend (web or extension) sends <code>emailContent</code> and <code>tone</code> to backend.</li>
      <li>Backend crafts prompt and calls AI provider using server-side secret API key.</li>
      <li>AI responds with generated reply text; backend parses it and returns to frontend.</li>
      <li>Frontend displays reply and allows copy/edit/send.</li>
    </ol>
  </section>

  <section>
    <h2>Common Troubleshooting</h2>
    <ul>
      <li><strong>CORS errors:</strong> enable CORS on Spring Boot dev controller or configure a proxy in Vite.</li>
      <li><strong>Missing API key / 401 from AI provider:</strong> verify key and endpoint in <code>application.properties</code>.</li>
      <li><strong>Extension not injecting UI:</strong> ensure <code>matches</code> in <code>manifest.json</code> includes <code>https://mail.google.com/*</code> and reload the extension.</li>
      <li><strong>AI output unexpected:</strong> tweak the prompt template in the backend service to include clearer instructions and examples.</li>
    </ul>
  </section>

  <section>
    <h2>Suggested Improvements / Roadmap</h2>
    <ul>
      <li>Persist history of generated replies in a database (Postgres) with user accounts.</li>
      <li>OAuth integration (so the extension can send replies using the user's Gmail account directly).</li>
      <li>Template management (save custom reply templates & favorite tones).</li>
      <li>Rate limiting & billing-aware integration for production AI API usage.</li>
    </ul>
  </section>

  <section>
    <h2>Screenshots</h2>
    <h3>Postman</h3>
     <img width="1413" height="827" alt="Screenshot (59)" src="https://github.com/user-attachments/assets/46445019-59c0-4de9-9073-0c855281b52d" />
   <h3>Frontend</h3>
<img width="1920" height="899" alt="Screenshot (58)" src="https://github.com/user-attachments/assets/9b876743-eace-4dbe-a222-e5cb984d6701" />
       <h3>Gmail Extension</h3>
 <img width="1591" height="848" alt="Screenshot (64)" src="https://github.com/user-attachments/assets/33a74332-d72b-4c67-b231-0104d81d645a" />

  </section>

  <section>
    <h2>Contributing</h2>
    <p>Contributions are welcome — open an issue for bugs/ideas or send a pull request.</p>
    <p>Suggested workflow:</p>
    <ol>
      <li>Fork the repo</li>
      <li>Create a feature branch <code>feature/your-feature</code></li>
      <li>Open a PR describing your changes</li>
    </ol>
  </section>

  <section>
    <h2>Security & API Keys</h2>
    <p>Never commit your AI provider API key to version control. Use environment variables or secret manager in production. Example for local dev with Maven/Spring Boot:</p>
    <pre>
# in application.properties (not committed) or pass as environment variable
gemini.api.key=${GEMINI_API_KEY}
    </pre>
  </section>

   <h2>📄 License</h2>
    <p>This project is licensed under the MIT License.</p>


