//Project fade block animation
document.addEventListener("DOMContentLoaded", () => {
    const blocks = document.querySelectorAll(".project-block");

    blocks.forEach((block, index) => {
      setTimeout(() => {
        block.classList.add("show");
      }, index * 800); // delay between each block
    });
  });
