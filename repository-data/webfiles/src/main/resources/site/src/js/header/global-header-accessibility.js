let globalHeader;
let globalMenuBtn;
let globalSearchBtn;
let inertTargets;

function isMenuOpen() {
    return document.querySelector('.js-menu-active') !== null;
}

function isSearchOpen() {
    return document.querySelector('.js-search-active') !== null;
}

function setInert() {
    if (isMenuOpen() || isSearchOpen()) {
        inertTargets.forEach((el) => el.setAttribute('inert', ''));
    } else {
        inertTargets.forEach((el) => el.removeAttribute('inert'));
    }
}

function setFocus() {
    if (isMenuOpen()) {
        const firstFocusable = document.getElementById('nhsd-global-header__menu-close-button');
        firstFocusable?.focus();
    } else if (!isMenuOpen() && !isSearchOpen()) {
        // return focus only when both are closed
        if (document.activeElement === globalMenuBtn) {
            globalMenuBtn?.focus();
        } else {
            globalSearchBtn?.focus();
        }
    }
}

function observeMenuState() {
    if (!globalHeader) {
        console.log('globalHeader not found', document.getElementById('nhsd-global-header'));
        return;// bail out if it doesn't exist
    }

    console.log('observing:', globalHeader); // ← fires on init?
    const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
            console.log('mutation fired:', mutation.target.className);
            if (mutation.attributeName !== 'class') {
                return;
            }

            const { classList } = mutation.target;

            if (classList.contains('js-menu-active') || classList.contains('js-search-active')) {
                console.log('menu/search open — setting inert');
                setInert();
                setFocus();
            } else {
                console.log('menu/search closed — removing inert');
                setInert();
            }
        });
    });

    observer.observe(globalHeader, {
        attributes: true,
        attributeFilter: ['class'],
    });
}

export default function initGlobalHeader() {
    globalHeader = document.getElementById('nhsd-global-header');
    globalMenuBtn = document.getElementById('nhsd-global-header__menu-button');
    globalSearchBtn = document.getElementById('nhsd-global-header__search-button');

    inertTargets = [
        document.getElementById('main-content'),
        document.getElementById('footer'),
        document.querySelector('.nhsd-m-breadcrumbs'),
        document.querySelector('.nhsd-a-skip-link'),
    ].filter(Boolean);

    console.log('initGlobalHeader called');
    observeMenuState();
}
