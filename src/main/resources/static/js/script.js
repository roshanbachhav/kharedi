const body = document.querySelector("body"),
        sidebar = body.querySelector(".sidebar"),
        search_button= body.querySelector(".search-box"),
        toggle = body.querySelector(".toggle");
        

        toggle.addEventListener("click", () => {
            sidebar.classList.toggle("close");
        });

