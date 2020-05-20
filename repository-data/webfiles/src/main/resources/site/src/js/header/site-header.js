const header = document.getElementById('header');
const navToggle = document.getElementById('navToggle');

function handleToggleClick() {
    header.classList.toggle('menu-open');
}


function onResize() {
    if (window.innerWidth > 1024 && header.classList.contains('menu-open')) {
        handleToggleClick();
    }
}

export function initSiteHeader() {
    navToggle.addEventListener('click', handleToggleClick);
    window.addEventListener('resize', onResize);
    onResize()
}
