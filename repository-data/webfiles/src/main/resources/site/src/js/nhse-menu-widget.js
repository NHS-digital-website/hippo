(() => {
    function defineProperties(t, n) {
        for (let o = 0; o < n.length; o += 1) {
            const i = n[o];
            i.enumerable = i.enumerable || false;
            i.configurable = true;
            if ('value' in i) {
                i.writable = true;
            }
            Object.defineProperty(t, i.key, i);
        }
    }
    const obj = {};
    const propertiesArray = [
        {
            key: 'propertyName1',
            value: 'propertyValue1',
            enumerable: true,
        },
        {
            key: 'propertyName2',
            value: 'propertyValue2',
            enumerable: false,
        },
        // add more property descriptor objects here if needed
    ];

    defineProperties(obj, propertiesArray);

    document.addEventListener('DOMContentLoaded', () => {
        const menu = document.getElementById('nhse-global-menu');
        if (menu === null) {
            throw new Error('#nhse-global-menu container element does not exists, cannot initialise menu.');
        }

        const MenuInit = (function NHSEMenu() {
            function MenuStarter(t) {
                if (!(this instanceof MenuStarter)) {
                    throw new TypeError('Cannot call a class as a function');
                }
                this.toggleBtn = t.querySelector('.nhse-global-menu__btn');
                this.navigation = t.querySelector('.nhse-global-menu__dropdown');
                this.stateOpen = false;
                const buttonIsPresent = this.toggleBtn !== null;
                const navigationIsPresent = this.navigation !== null;
                if (buttonIsPresent && navigationIsPresent) {
                    this.addEventListeners();
                }
            }

            MenuStarter.prototype.addEventListeners = function addEvent() {
                const e = this;
                this.toggleBtn.addEventListener('click', () => {
                    e.toggleMenu();
                });
                this.toggleBtn.addEventListener('keydown', (t) => {
                    if (t.keyCode === 13 || t.keyCode === 32) {
                        t.preventDefault();
                        e.toggleMenu();
                    }
                });
            };

            MenuStarter.prototype.toggleMenu = function toggle() {
                if (this.stateOpen) {
                    this.menuClose();
                } else {
                    this.menuOpen();
                }
            };

            MenuStarter.prototype.menuOpen = function openMenu() {
                this.toggleBtn.setAttribute('aria-expanded', 'true');
                this.toggleBtn.classList.add('active');
                this.navigation.classList.add('expanded');
                this.stateOpen = true;
            };

            MenuStarter.prototype.menuClose = function closeMenu() {
                this.toggleBtn.setAttribute('aria-expanded', 'false');
                this.toggleBtn.classList.remove('active');
                this.navigation.classList.remove('expanded');
                this.stateOpen = false;
            };

            return MenuStarter;
        }());

        MenuInit(document.getElementById('nhse-global-menu'));
    });
})();
