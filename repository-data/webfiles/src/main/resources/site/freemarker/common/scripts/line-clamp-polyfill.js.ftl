<#ftl output_format="HTML">

<script>
    "use strict";

    (function() {
        /**
         * @param {HTMLElement} element
         * @param {string} property
         * @return {string}
         */
        function getStyleValue(element, property) {
            return window.getComputedStyle(element, null).getPropertyValue(property);
        }

        /**
         * @param {HTMLElement} element
         * @return {number}
         */
        function getLineHeight(element) {
            var lineHeight = getStyleValue(element, 'line-height');

            if (lineHeight === 'normal') {
                return parseInt(getStyleValue(element, 'font-size'), 10) * 1.25;
            }

            return parseFloat(lineHeight);
        }

        /**
         * @param {HTMLElement} element
         */
        function setupLineHeight(element) {
            for (var index = 0; index < element.childNodes.length; index++) {
                var childNode = element.childNodes[index];

                if (childNode.nodeType === 1) {
                    childNode.inlineCssText = childNode.style.cssText;
                    childNode.style.lineHeight = '100%';
                }
            }
        }

        /**
         * @param {HTMLElement} element
         */
        function removeLineHeight(element) {
            for (var index = 0; index < element.childNodes.length; index++) {
                var childNode = element.childNodes[index];

                if (childNode.nodeType === 1) {
                    childNode.removeAttribute('style');

                    if (childNode.inlineCssText) {
                        childNode.style.cssText = childNode.inlineCssText;
                    }
                }
            }
        }

        /**
         * @param {HTMLElement} textNode
         * @param {HTMLElement} rootElement
         * @param {number} maxHeight
         * @return {boolean}
         */
        function truncateTextNode(textNode, rootElement, maxHeight) {
            var textContent = textNode.textContent;

            textNode.textContent = '';

            if (rootElement.clientHeight > maxHeight) return false;

            var chunks = textContent.split(' ');

            while (chunks.pop()) {
                textNode.textContent = chunks.join(' ');

                if (rootElement.clientHeight <= maxHeight) {
                    textNode.textContent = textContent;
                    break;
                }

                textContent = textNode.textContent;
            }

            var length = textContent.length;

            while (length > 2) {
                textContent = textContent.substring(0, --length);

                textNode.textContent = textContent + '…';

                if (rootElement.clientHeight <= maxHeight) return true;
            }

            return false;
        }

        /**
         * @param {HTMLElement} element
         * @param {HTMLElement} rootElement
         * @param {number} maxHeight
         * @param {number} lineHeight
         * @return {boolean}
         */
        function truncateElementNode(element, rootElement, maxHeight, lineHeight) {
            var childNodes = element.childNodes;
            var length = childNodes.length - 1;

            while (length > -1) {
                var childNode = childNodes[length--];
                var func = childNode.nodeType === 1 ? truncateElementNode : truncateTextNode;

                if (func(childNode, rootElement, maxHeight, lineHeight)) return true;

                element.removeChild(childNode);
            }

            return false;
        }

        /**
         * @param {HTMLElement} rootElement
         * @param {number} lineCount
         */
        function truncate(rootElement, lineCount) {
            var lineHeight = getLineHeight(rootElement);
            var maxHeight = Math.round(lineHeight * lineCount);

            if (rootElement.clientHeight <= maxHeight) return;

            setupLineHeight(rootElement);
            truncateElementNode(rootElement, rootElement, maxHeight, lineHeight);
            removeLineHeight(rootElement);
        }

        /**
         * @param {HTMLElement} rootElement
         * @param {number} lineCount
         * @param {string} colour
         */
        function native(rootElement, lineCount, colour) {
            rootElement.style.overflow = 'hidden';
            rootElement.style.textOverflow = 'ellipsis';
            rootElement.style.webkitBoxOrient = 'vertical';
            rootElement.style.display = '-webkit-box';
            rootElement.style.webkitLineClamp = lineCount;
            rootElement.style.color = colour;
        }

        /**
         * @param {HTMLElement} element
         * @param {number} lineCount
         * @param {string} colour
         */
        function webkitLineClamp(element, lineCount, colour) {
            if (!lineCount) return;

            (typeof element.style.webkitLineClamp === 'undefined' ? truncate : native)(element, lineCount, colour);
        }

        window.webkitLineClamp = webkitLineClamp;
    })();
</script>
