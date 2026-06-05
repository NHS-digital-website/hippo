const menuButtonSelector = '#nhsd-global-header__menu-button';

function isMenuOpen(menuButton) {
    return menuButton.getAttribute('aria-expanded') === 'true';
}

export default function initGlobalHeaderEscapeClose() {
    document.addEventListener('keydown', (event) => {
        if (event.key !== 'Escape') {
            return;
        }

        const menuButton = document.querySelector(menuButtonSelector);

        if (menuButton && isMenuOpen(menuButton)) {
            menuButton.click();
            menuButton.focus();
        }
    });
}
