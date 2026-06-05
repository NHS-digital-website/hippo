const headerSelector = '#nhsd-global-header';
const menuButtonSelector = '#nhsd-global-header__menu-button';
const menuSelector = '#nhsd-global-header__menu';
const menuCloseButtonSelector = '#nhsd-global-header__menu-close-button';
const searchButtonSelector = '#nhsd-global-header__search-button';
const searchCloseButtonSelector = '#nhsd-global-header__search-close-button';
const searchSelector = '#nhsd-global-header__search';
const activationKeys = ['Enter', ' '];

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

function isActivationKey(event) {
    return activationKeys.includes(event.key);
}

function isBackwardTab(event) {
    return event.key === 'Tab' && event.shiftKey;
}

function focusLater(element) {
    window.setTimeout(() => element.focus(), 0);
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

function isExpanded(button) {
    return button.getAttribute('aria-expanded') === 'true';
}

function createPanelState(button) {
    return {
        wasExpanded: isExpanded(button),
        focusButtonAfterClose: false,
        openedByKeyboard: false,
        suppressCloseFocus: false,
    };
}

function createPanel(button, panel, closeButton) {
    return {
        button,
        panel,
        closeButton,
        state: createPanelState(button),

        isExpanded() {
            return isExpanded(button);
        },

        close() {
            closeButton.click();
        },

        closeWithoutReturningFocus() {
            this.state.suppressCloseFocus = true;
            this.close();
            this.state.suppressCloseFocus = false;
        },

        focusButtonLater() {
            focusLater(button);
        },

        getFocusableElements() {
            return getFocusableElements(panel);
        },

        getFocusableCandidates() {
            return getFocusableCandidates(panel);
        },
    };
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
    const menuElement = header.querySelector(menuSelector);
    const menuCloseButton = header.querySelector(menuCloseButtonSelector);
    const searchButton = header.querySelector(searchButtonSelector);
    const searchElement = header.querySelector(searchSelector);
    const searchCloseButton = header.querySelector(searchCloseButtonSelector);
    const pageHeader = header.closest('header') || header;

    if (!menuButton || !menuElement || !menuCloseButton
        || !searchButton || !searchElement || !searchCloseButton) {
        return;
    }

    const menu = createPanel(menuButton, menuElement, menuCloseButton);
    const search = createPanel(searchButton, searchElement, searchCloseButton);

    Array.from(header.querySelectorAll(focusableSelector))
        .forEach(rememberTabIndex);

    const applyState = () => {
        const menuButtonIsVisible = !isHidden(menu.button);
        const menuExpanded = menu.isExpanded();

        getFocusableCandidates(header).forEach(restoreTabIndex);

        if (menuButtonIsVisible && !menuExpanded) {
            menu.getFocusableCandidates()
                .forEach((element) => setTabIndex(element, '-1'));
        }
    };

    const scheduleApplyState = () => {
        window.setTimeout(applyState, 0);
    };

    const closeSearchWithoutReturningFocus = () => {
        search.closeWithoutReturningFocus();
    };

    const closeMenuWithoutReturningFocus = () => {
        menu.closeWithoutReturningFocus();
    };

    menu.button.addEventListener('keydown', (event) => {
        if (isActivationKey(event) && !menu.isExpanded()) {
            if (search.isExpanded()) {
                closeSearchWithoutReturningFocus();
            }

            menu.state.openedByKeyboard = true;
            return;
        }

        if (event.key !== 'Tab' || !menu.isExpanded()) {
            return;
        }

        if (event.shiftKey) {
            closeMenuWithoutReturningFocus();
            scheduleApplyState();
            return;
        }

        const firstMenuItem = menu.getFocusableElements()[0];

        if (firstMenuItem) {
            event.preventDefault();
            firstMenuItem.focus();
        }
    });

    menu.panel.addEventListener('keydown', (event) => {
        if (isActivationKey(event) && event.target === menu.closeButton) {
            menu.state.focusButtonAfterClose = true;
            scheduleApplyState();
            return;
        }

        if (event.key !== 'Tab' || !menu.isExpanded()) {
            return;
        }

        const menuFocusableElements = menu.getFocusableElements();
        const firstMenuItem = menuFocusableElements[0];
        const lastMenuItem = menuFocusableElements[menuFocusableElements.length - 1];

        if (event.shiftKey && event.target === firstMenuItem) {
            event.preventDefault();
            menu.button.focus();
            return;
        }

        if (!event.shiftKey && event.target === lastMenuItem) {
            const nextFocusable = getNextFocusableAfterContainer(pageHeader);

            closeMenuWithoutReturningFocus();
            scheduleApplyState();

            if (nextFocusable) {
                event.preventDefault();
                focusLater(nextFocusable);
            }
        }
    });

    search.panel.addEventListener('keydown', (event) => {
        if (isActivationKey(event) && event.target === search.closeButton) {
            search.state.focusButtonAfterClose = true;
            scheduleApplyState();
            return;
        }

        if (event.key !== 'Tab' || !search.isExpanded()) {
            return;
        }

        const searchFocusableElements = search.getFocusableElements();
        const lastSearchItem = searchFocusableElements[searchFocusableElements.length - 1];

        if (!event.shiftKey && event.target === lastSearchItem) {
            const nextFocusable = getNextFocusableAfterContainer(pageHeader);

            closeSearchWithoutReturningFocus();
            scheduleApplyState();

            if (nextFocusable) {
                event.preventDefault();
                focusLater(nextFocusable);
            }
        }
    });

    search.button.addEventListener('keydown', (event) => {
        if (isBackwardTab(event) && search.isExpanded()) {
            closeSearchWithoutReturningFocus();
            scheduleApplyState();
        }
    });

    search.button.addEventListener('click', () => {
        if (search.isExpanded()) {
            search.state.focusButtonAfterClose = true;
            scheduleApplyState();
        }
    }, true);

    menu.button.addEventListener('click', () => {
        if (search.isExpanded()) {
            closeSearchWithoutReturningFocus();
            scheduleApplyState();
        }
    }, true);

    menu.closeButton.addEventListener('click', () => {
        if (!menu.state.suppressCloseFocus) {
            menu.state.focusButtonAfterClose = true;
        }

        scheduleApplyState();
    }, true);

    search.closeButton.addEventListener('click', () => {
        if (!search.state.suppressCloseFocus) {
            search.state.focusButtonAfterClose = true;
        }

        scheduleApplyState();
    });

    const observer = new MutationObserver(() => {
        const menuExpanded = menu.isExpanded();
        const searchExpanded = search.isExpanded();
        const menuJustClosed = menu.state.wasExpanded && !menuExpanded;
        const searchJustClosed = search.state.wasExpanded && !searchExpanded;

        scheduleApplyState();

        if (menuExpanded && menu.state.openedByKeyboard) {
            menu.state.openedByKeyboard = false;
            menu.focusButtonLater();
        }

        if (menuJustClosed) {
            if (menu.state.focusButtonAfterClose) {
                menu.focusButtonLater();
            }

            menu.state.focusButtonAfterClose = false;
        }

        if (!menuJustClosed && !menuExpanded) {
            menu.state.focusButtonAfterClose = false;
        }

        if (searchJustClosed) {
            if (search.state.focusButtonAfterClose) {
                search.focusButtonLater();
            }

            search.state.focusButtonAfterClose = false;
        }

        if (!searchJustClosed && !searchExpanded) {
            search.state.focusButtonAfterClose = false;
        }

        menu.state.wasExpanded = menuExpanded;
        search.state.wasExpanded = searchExpanded;
    });

    observer.observe(menu.button, {
        attributes: true,
        attributeFilter: ['aria-expanded'],
    });

    observer.observe(search.button, {
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
