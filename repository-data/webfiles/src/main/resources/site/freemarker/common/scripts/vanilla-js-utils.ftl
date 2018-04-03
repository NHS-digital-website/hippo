<#ftl output_format="HTML">

<#-- Vanilla Javascript utils -->
<script>
    (function() {
        function pruneSpaces(el) {
            el.className = el.className.replace(/^\s+|\s+$/g, '');
        }

        function toggleClass(el, className) {
            if (hasClass(el, className)) {
                removeClass(el, className);
            } else {
                addClass(el, className);
            }
            
            pruneSpaces(el);
        }

        function addClass(el, className) {
            if (!hasClass(el, className)) {
                el.className += ' ' + className;
            }
            
            pruneSpaces(el);
        }

        function removeClass(el, className) {
            if (hasClass(el, className)) {
                el.className = el.className.replace(className, '');
            }

            pruneSpaces(el);
        }

        function hasClass(el, className) {
            return !!(el.className.indexOf(className) >= 0);
        }

        // Expose the util functions on the global namespace
        window.vanillaJSUtils = {
            toggleClass: toggleClass,
            addClass: addClass,
            removeClass: removeClass
        };
    })()
</script>