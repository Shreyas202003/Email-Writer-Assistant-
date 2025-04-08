console.log("Email Writer Extension - content script loaded");

function injectButton(composeElement) {
    // Check if button already exists to avoid duplicates
    if (composeElement.querySelector(".email-writer-btn")) return;

    const button = document.createElement("button");
    button.textContent = "Generate Reply";
    button.className = "email-writer-btn";
    button.style.margin = "10px";
    button.style.padding = "5px 10px";
    button.style.backgroundColor = "#4285f4";
    button.style.color = "white";
    button.style.border = "none";
    button.style.borderRadius = "4px";
    button.style.cursor = "pointer";

    button.addEventListener("click", async () => {
        const emailContent = composeElement.querySelector("div[role='textbox']")?.textContent || "";
        if (!emailContent) {
            alert("No email content found to generate a reply.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/email/generate", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    emailContent: emailContent,
                    tone: "professional" // Default tone, can be customized
                })
            });

            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            const reply = await response.text();
            const textBox = composeElement.querySelector("div[role='textbox']");
            if (textBox) textBox.textContent = reply;
        } catch (error) {
            console.error("Error generating email reply:", error);
            alert("Failed to generate reply. Check console for details.");
        }
    });

    // Inject button into the compose window
    const toolbar = composeElement.querySelector(".btC") || composeElement; // Gmail toolbar or fallback
    if (toolbar) toolbar.appendChild(button);
}

const observer = new MutationObserver((mutations) => {
    for (const mutation of mutations) {
        const addedNodes = Array.from(mutation.addedNodes);
        const hasComposeElement = addedNodes.some(node =>
            node.nodeType === Node.ELEMENT_NODE &&
            (node.matches(".aDh, .btC, [role='dialog']") || node.querySelector(".aDh, .btC, [role='dialog']"))
        );

        if (hasComposeElement) {
            console.log("Compose element detected, injecting button...");
            const composeElement = document.querySelector(".aDh, .btC, [role='dialog']");
            if (composeElement) injectButton(composeElement);
        }
    }
});

observer.observe(document.body, {
    childList: true,
    subtree: true
});