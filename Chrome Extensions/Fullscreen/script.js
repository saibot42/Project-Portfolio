let active = null;

/* ===============================
   Global exit (ESC)
================================ */
document.addEventListener("keydown", e => {
  if (e.key === "Escape") exitFullscreen();
});

/* ===============================
   Detection
================================ */
function isVideoIframe(el) {
  if (el.tagName !== "IFRAME") return false;

  const src = el.src || "";
  return (
    el.allowFullscreen ||
    /youtube|vimeo|twitch|player/i.test(src)
  );
}

function isVideoElement(el) {
  return el.tagName === "VIDEO";
}

/* ===============================
   Button injection (initial)
================================ */
function addButton(playerEl) {
  if (playerEl.__hasCustomFs) return;
  playerEl.__hasCustomFs = true;

  const btn = document.createElement("button");
  btn.textContent = "⛶";

  Object.assign(btn.style, {
    position: "absolute",
    zIndex: "2147483649",
    padding: "6px 10px",
    background: "rgba(0,0,0,0.7)",
    color: "white",
    border: "none",
    cursor: "pointer",
    borderRadius: "4px"
  });

  document.body.appendChild(btn);

  function positionButton() {
    const r = playerEl.getBoundingClientRect();
    btn.style.top = `${r.top + 8 + window.scrollY}px`;
    btn.style.left = `${r.right - 40 + window.scrollX}px`;
  }

  positionButton();
  window.addEventListener("scroll", positionButton);
  window.addEventListener("resize", positionButton);

  btn.onclick = () => toggleWindowFullscreen(playerEl);

  playerEl.__floatingButton = btn;
}

/* ===============================
   Window fullscreen (overlay-based)
================================ */
function toggleWindowFullscreen(playerEl) {
  if (active) {
    exitFullscreen();
    return;
  }

  // Placeholder
  const placeholder = document.createElement("div");
  placeholder.style.width = playerEl.offsetWidth + "px";
  placeholder.style.height = playerEl.offsetHeight + "px";
  playerEl.parentNode.insertBefore(placeholder, playerEl);

  // Overlay
  const overlay = document.createElement("div");
  Object.assign(overlay.style, {
    position: "fixed",
    top: "0",
    left: "0",
    background: "black",
    zIndex: "2147483647",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    maxWidth: "100%",
    maxHeight: "100%",
    width: "auto",
    height: "auto",
    objectFit: "contain",
    pointerEvents: "auto"  // blocks page behind
  });

  // if (playerEl.tagName === "VIDEO") {
  //   Object.assign(playerEl.style, {
  //     maxWidth: "100%",
  //     maxHeight: "100%",
  //     width: "auto",
  //     height: "auto",
  //     objectFit: "contain"
  //   });
  // }

  // if (playerEl.tagName === "IFRAME") {
  //   Object.assign(playerEl.style, {
  //     width: "100%",
  //     height: "100%",
  //     maxWidth: "100%",
  //     maxHeight: "100%"
  //   });
  // }




  // Button
  const fsBtn = document.createElement("button");
  fsBtn.textContent = "⛶";
  Object.assign(fsBtn.style, {
    position: "absolute",
    top: "12px",
    right: "12px",
    padding: "6px 10px",
    background: "rgba(0,0,0,0.7)",
    color: "white",
    border: "none",
    cursor: "pointer",
    borderRadius: "4px",
    zIndex: "2147483648",
    pointerEvents: "auto"
  });

  fsBtn.onclick = exitFullscreen;

  // Put video/iframe inside overlay
  overlay.appendChild(playerEl);
  overlay.appendChild(fsBtn);

  // Make video ignore pointer events so button works
  playerEl.style.pointerEvents = "none";

  // Append overlay to page
  document.body.appendChild(overlay);
  document.body.style.overflow = "hidden";

  if (playerEl.__floatingButton) playerEl.__floatingButton.style.display = "none";

  playerEl.__fsState = {
    placeholder,
    overlay,
    style: playerEl.getAttribute("style") || ""
  };

  active = { playerEl };
}


/* ===============================
   Exit fullscreen
================================ */
function exitFullscreen() {
  if (!active) return;

  const { playerEl } = active;
  const { placeholder, overlay, style } = playerEl.__fsState;

  overlay.replaceWith(playerEl);
  placeholder.replaceWith(playerEl);

  playerEl.setAttribute("style", style);
  document.body.style.overflow = "";

  if (playerEl.__floatingButton) {
    playerEl.__floatingButton.style.display = "";
  }

  active = null;
}

/* ===============================
   Scan + observe
================================ */
function scan() {
  document.querySelectorAll("iframe, video").forEach(el => {
    if (isVideoIframe(el) || isVideoElement(el)) {
      addButton(el);
    }
  });
}

scan();

new MutationObserver(scan).observe(document.body, {
  childList: true,
  subtree: true
});
