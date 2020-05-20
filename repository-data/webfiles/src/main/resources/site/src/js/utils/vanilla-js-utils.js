(function () {
    function pruneSpaces(el) {
        el.className = el.className.replace(/^\s+|\s+$/g, '');
    }

    function hasClass(el, className) {
        return (el.className.indexOf(className) >= 0);
    }

    function addClass(el, className) {
        if (!hasClass(el, className)) {
            el.className += ` ${  className}`;
        }

        pruneSpaces(el);
    }

    function removeClass(el, className) {
        if (hasClass(el, className)) {
            el.className = el.className.replace(className, '');
        }

        pruneSpaces(el);
    }

    function toggleClass(el, className) {
        if (hasClass(el, className)) {
            removeClass(el, className);
        } else {
            addClass(el, className);
        }

        pruneSpaces(el);
    }

    function onKeyUp(event) {
        const keyCode = event.keyCode || event.which;
        const target = event.target || event.srcElement;

        if (event.preventDefault) {
            event.preventDefault()
        } else {
            event.returnValue = false;
        }

        if (keyCode === 13 || keyCode === 32) {
            target.click();
        }
    }


    // Expose the util functions on the global namespace
    window.vjsu = {
        toggleClass,
        addClass,
        hasClass,
        removeClass,
        onKeyUp
    };

    window.vanillaJSUtils = window.vjsu;
}())
