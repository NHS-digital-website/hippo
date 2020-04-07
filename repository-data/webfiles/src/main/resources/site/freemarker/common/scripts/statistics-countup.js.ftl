<#ftl output_format="HTML">

<#include "countup.min.js.ftl"/>

<script>
    /* exported statisticsCountup */
    var statisticsCountup = (function () {
        'use strict';

        function checkIntersectionObserver() {
            if ('IntersectionObserver' in window &&
                    'IntersectionObserverEntry' in window &&
                    'intersectionRatio' in window.IntersectionObserverEntry.prototype) {

                // Minimal polyfill for Edge 15's lack of `isIntersecting`
                // See: https://github.com/w3c/IntersectionObserver/issues/211
                if (!('isIntersecting' in window.IntersectionObserverEntry.prototype)) {
                    Object.defineProperty(window.IntersectionObserverEntry.prototype,
                            'isIntersecting', {
                                get: function () {
                                    return this.intersectionRatio > 0;
                                }
                            });
                }
                return true;
            }
            return false
        }

        function handleScrollIntoView(entries, observer) {
            entries.forEach(function (entry) {
                if (entry.intersectionRatio >= 1) {
                    var countup = new countUp.CountUp(entry.target, entry.target.dataset.targetNum);
                    if (!countup.error) {
                        countup.start(function(){
                            observer.unobserve(entry.target);
                        });
                    } else {
                        console.error(countup.error);
                    }
                }
            });
        }

        function init() {
            if (!checkIntersectionObserver()) {
                return;
            }

            var countUpItems = [].slice.call(document.querySelectorAll('.statistics-section--animated .statistic--single-number .statistic__number'));
            if (countUpItems.length === 0) {
                return;
            }

            var options = {
                rootMargin: '0px',
                threshold: 1.0
            };

            var observer = new IntersectionObserver(handleScrollIntoView, options);
            countUpItems.forEach(function (item) {
                item.dataset.targetNum = Number(item.innerText.replace(/,/g, ''));
                observer.observe(item)
            });
        }

        return {
            init: init
        };
    }());
    statisticsCountup.init();
</script>
