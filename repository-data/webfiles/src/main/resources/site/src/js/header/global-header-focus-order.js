const headerSelector = '#nhsd-global-header';
const menuButtonSelector = '#nhsd-global-header__menu-button';
const menuSelector = '#nhsd-global-header__menu';
const menuCloseButtonSelector = '#nhsd-global-header__menu-close-button';

const focusableSelector = [
    'a[href]',
    'button:not([disabled])',
    'input:not([disabled])',
    'select:not([disabled])',
    'textarea:not([disabled])',
    '[tabindex]',
].join(',');

const originalTabIndex = new WeakMap();

function rememberTabIndex(element) {
    if (!originalTabIndex.has(element)) {
        originalTabIndex.set(element, element.getAttribute('tabindex'));
    }
}

function restoreTabIndex(element) {
    rememberTabIndex(element);

    const tabIndex = originalTabIndex.get(element);

    if (tabIndex === null) {
        element.removeAttribute('tabindex');
        return;
    }

    element.setAttribute('tabindex', tabIndex);
}

function isHidden(element) {
    return !element.getClientRects().length
        || window.getComputedStyle(element).visibility === 'hidden';
}

function isTabbable(element) {
    const tabIndex = element.getAttribute('tabindex');

    return tabIndex === null || Number(tabIndex) >= 0;
}

function getFocusableCandidates(root) {
    return Array.from(root.querySelectorAll(focusableSelector))
        .filter((element) => !element.hasAttribute('disabled') && !isHidden(element));
}

function getFocusableElements(root) {
    return getFocusableCandidates(root).filter(isTabbable);
}

function setTabIndex(element, value) {
    rememberTabIndex(element);
    element.setAttribute('tabindex', value);
}

function isMenuButtonVisible(menuButton) {
    return !isHidden(menuButton);
}

function isMenuOpen(menuButton) {
    return menuButton.getAttribute('aria-expanded') === 'true';
}

function toggleMenu(menuButton) {
    menuButton.click();
}

function getNextFocusableAfterContainer(container) {
    const focusableElements = getFocusableElements(document);
    const containerFocusableElements = focusableElements
        .filter((element) => container.contains(element));
    const lastContainerElement = containerFocusableElements[containerFocusableElements.length - 1];
    const lastContainerElementIndex = focusableElements.indexOf(lastContainerElement);

    if (lastContainerElementIndex === -1) {
        return null;
    }

    return focusableElements[lastContainerElementIndex + 1] || null;
}

function initGlobalHeader(header) {
    const menuButton = header.querySelector(menuButtonSelector);
    const menu = header.querySelector(menuSelector);
    const menuCloseButton = header.querySelector(menuCloseButtonSelector);
    const pageHeader = header.closest('header') || header;

    if (!menuButton || !menu || !menuCloseButton) {
        return;
    }

    let closeButtonActivated = false;
    let menuOpenedByKeyboard = false;

    Array.from(header.querySelectorAll(focusableSelector))
        .forEach(rememberTabIndex);

    const getMenuFocusableElements = () => getFocusableElements(menu);

    const applyState = () => {
        const menuButtonIsVisible = isMenuButtonVisible(menuButton);
        const menuIsOpen = isMenuOpen(menuButton);

        getFocusableCandidates(header).forEach(restoreTabIndex);

        if (menuButtonIsVisible && !menuIsOpen) {
            getFocusableCandidates(menu).forEach((element) => setTabIndex(element, '-1'));
        }
    };

    const scheduleApplyState = () => {
        window.setTimeout(applyState, 0);
    };

    menuButton.addEventListener('keydown', (event) => {
        if ((event.key === 'Enter' || event.key === ' ') && !isMenuOpen(menuButton)) {
            menuOpenedByKeyboard = true;
            return;
        }

        if (event.key !== 'Tab' || !isMenuOpen(menuButton)) {
            return;
        }

        if (event.shiftKey) {
            toggleMenu(menuButton);
            scheduleApplyState();
            return;
        }

        const firstMenuItem = getMenuFocusableElements()[0];

        if (firstMenuItem) {
            event.preventDefault();
            firstMenuItem.focus();
        }
    });

    menu.addEventListener('keydown', (event) => {
        if ((event.key === 'Enter' || event.key === ' ') && event.target === menuCloseButton) {
            closeButtonActivated = true;
            scheduleApplyState();
            return;
        }

        if (event.key !== 'Tab' || !isMenuOpen(menuButton)) {
            return;
        }

        const menuFocusableElements = getMenuFocusableElements();
        const firstMenuItem = menuFocusableElements[0];
        const lastMenuItem = menuFocusableElements[menuFocusableElements.length - 1];

        if (event.shiftKey && event.target === firstMenuItem) {
            event.preventDefault();
            menuButton.focus();
            return;
        }

        if (!event.shiftKey && event.target === lastMenuItem) {
            const nextFocusable = getNextFocusableAfterContainer(pageHeader);

            toggleMenu(menuButton);
            scheduleApplyState();

            if (nextFocusable) {
                event.preventDefault();
                window.setTimeout(() => nextFocusable.focus(), 0);
            }
        }
    });

    menuCloseButton.addEventListener('click', () => {
        closeButtonActivated = true;
        scheduleApplyState();
    });

    const observer = new MutationObserver(() => {
        const menuIsOpen = isMenuOpen(menuButton);

        scheduleApplyState();

        if (menuIsOpen && menuOpenedByKeyboard) {
            menuOpenedByKeyboard = false;
            window.setTimeout(() => menuButton.focus(), 0);
        }

        if (!menuIsOpen && closeButtonActivated) {
            closeButtonActivated = false;
            window.setTimeout(() => menuButton.focus(), 0);
        }
    });

    observer.observe(menuButton, {
        attributes: true,
        attributeFilter: ['aria-expanded'],
    });

    window.addEventListener('resize', scheduleApplyState);
    scheduleApplyState();
}

export default function initGlobalHeaderFocusOrder() {
    const header = document.querySelector(headerSelector);

    if (header) {
        initGlobalHeader(header);
    }
}
