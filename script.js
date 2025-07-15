document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault(); // stop normal form action
    // Optional: Add login validation here
    window.location.href = "vault.html"; // redirect to vault
});
