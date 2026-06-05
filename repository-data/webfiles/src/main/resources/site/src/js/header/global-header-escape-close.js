const menuButtonSelector = '#nhsd-global-header__menu-button';
const searchButtonSelector = '#nhsd-global-header__search-button';
const searchCloseButtonSelector = '#nhsd-global-header__search-close-button';

function isExpanded(button) {
    return button.getAttribute('aria-expanded') === 'true';
}

function closeIfOpen(button, closeButton = button) {
    if (!button || !closeButton || !isExpanded(button)) {
        return;
    }

    closeButton.click();
    button.focus();
}

export default function initGlobalHeaderEscapeClose() {
    document.addEventListener('keydown', (event) => {
        if (event.key !== 'Escape') {
            return;
        }

        closeIfOpen(
            document.querySelector(menuButtonSelector),
        );

        closeIfOpen(
            document.querySelector(searchButtonSelector),
            document.querySelector(searchCloseButtonSelector),
        );
    });
}
