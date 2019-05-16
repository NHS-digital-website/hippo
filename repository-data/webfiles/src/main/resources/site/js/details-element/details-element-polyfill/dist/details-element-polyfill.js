/*
Based on Details Element Polyfill 2.3.1 (Under The MIT License)
Copyright © 2019 Javan Makhmali

Modified by NHS Digital Developer Geoff Hayward.
 */
(function (document) {
    "use strict";

    if (!('open' in document.createElement('details'))) {
        document.getElementsByTagName('html')[0].classList.add('no-details');
    } else {
        return;
    }

    var _ref = [], forEach = _ref.forEach, slice = _ref.slice;

    polyfillProperties();
    polyfillToggle();
    polyfillAccessibility();

    function polyfillProperties() {
        var prototype = document.createElement("details").constructor.prototype;
        var setAttribute = prototype.setAttribute, removeAttribute = prototype.removeAttribute;
        var open = Object.getOwnPropertyDescriptor(prototype, "open");
        Object.defineProperties(prototype, {
            open: {
                get: function get() {
                    if (this.tagName == "DETAILS") {
                        return this.hasAttribute("open");
                    } else {
                        if (open && open.get) {
                            return open.get.call(this);
                        }
                    }
                },
                set: function set(value) {
                    if (this.tagName == "DETAILS") {
                        return value ? this.setAttribute("open", "") : this.removeAttribute("open");
                    } else {
                        if (open && open.set) {
                            return open.set.call(this, value);
                        }
                    }
                }
            },
            setAttribute: {
                value: function value(name, _value) {
                    var _this = this;
                    var call = function call() {
                        return setAttribute.call(_this, name, _value);
                    };
                    if (name == "open" && this.tagName == "DETAILS") {
                        var wasOpen = this.hasAttribute("open");
                        var result = call();
                        if (!wasOpen) {
                            var summary = this.querySelector("summary");
                            if (summary) summary.setAttribute("aria-expanded", true);
                            triggerToggle(this);
                        }
                        return result;
                    }
                    return call();
                }
            },
            removeAttribute: {
                value: function value(name) {
                    var _this2 = this;
                    var call = function call() {
                        return removeAttribute.call(_this2, name);
                    };
                    if (name == "open" && this.tagName == "DETAILS") {
                        var wasOpen = this.hasAttribute("open");
                        var result = call();
                        if (wasOpen) {
                            var summary = this.querySelector("summary");
                            if (summary) summary.setAttribute("aria-expanded", false);
                            triggerToggle(this);
                        }
                        return result;
                    }
                    return call();
                }
            }
        });
    }

    function polyfillToggle() {
        onTogglingTrigger(function (element) {
            element.hasAttribute("open") ? element.removeAttribute("open") : element.setAttribute("open", "");
        });
    }

    function polyfillAccessibility() {
        setAccessibilityAttributes(document);
        if (window.MutationObserver) {
            new MutationObserver(function (mutations) {
                forEach.call(mutations, function (mutation) {
                    forEach.call(mutation.addedNodes, setAccessibilityAttributes);
                });
            }).observe(document.documentElement, {
                subtree: true,
                childList: true
            });
        } else {
            document.addEventListener("DOMNodeInserted", function (event) {
                setAccessibilityAttributes(event.target);
            });
        }
    }

    function setAccessibilityAttributes(root) {
        findElementsWithTagName(root, "SUMMARY").forEach(function (summary) {
            var details = findClosestElementWithTagName(summary, "DETAILS");
            summary.setAttribute("aria-expanded", details.hasAttribute("open"));
            if (!summary.hasAttribute("tabindex")) summary.setAttribute("tabindex", "0");
            if (!summary.hasAttribute("role")) summary.setAttribute("role", "button");
        });
    }

    function eventIsSignificant(event) {
        return !(event.defaultPrevented || event.ctrlKey || event.metaKey || event.shiftKey || event.target.isContentEditable);
    }

    function onTogglingTrigger(callback) {
        addEventListener("click", function (event) {
            if (eventIsSignificant(event)) {
                if (event.which <= 1) {
                    var element = findClosestElementWithTagName(event.target, "SUMMARY");
                    if (element && element.parentNode && element.parentNode.tagName == "DETAILS") {
                        callback(element.parentNode);
                    }
                }
            }
        }, false);
        addEventListener("keydown", function (event) {
            if (eventIsSignificant(event)) {
                if (event.keyCode == 13 || event.keyCode == 32) {
                    var element = findClosestElementWithTagName(event.target, "SUMMARY");
                    if (element && element.parentNode && element.parentNode.tagName == "DETAILS") {
                        callback(element.parentNode);
                        event.preventDefault();
                    }
                }
            }
        }, false);
    }

    function triggerToggle(element) {
        var event = document.createEvent("Event");
        event.initEvent("toggle", false, false);
        element.dispatchEvent(event);
    }

    function findElementsWithTagName(root, tagName) {
        return (root.tagName == tagName ? [root] : []).concat(typeof root.getElementsByTagName == "function" ? slice.call(root.getElementsByTagName(tagName)) : []);
    }

    function findClosestElementWithTagName(element, tagName) {
        if (typeof element.closest == "function") {
            return element.closest(tagName);
        } else {
            while (element) {
                if (element.tagName == tagName) {
                    return element;
                } else {
                    element = element.parentNode;
                }
            }
        }
    }
})(document);
