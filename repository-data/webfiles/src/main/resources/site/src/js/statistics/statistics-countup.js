import { CountUp } from "countup.js";

function checkIntersectionObserver() {
    if ('IntersectionObserver' in window &&
        'IntersectionObserverEntry' in window &&
        'intersectionRatio' in window.IntersectionObserverEntry.prototype) {

        // Minimal polyfill for Edge 15's lack of `isIntersecting`
        // See: https://github.com/w3c/IntersectionObserver/issues/211
        if (!('isIntersecting' in window.IntersectionObserverEntry.prototype)) {
            Object.defineProperty(window.IntersectionObserverEntry.prototype,
                'isIntersecting', {
                    get() {
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
            const thisCount = new CountUp(entry.target, entry.target.dataset.targetNum);
            if (!thisCount.error) {
                thisCount.start(function () {
                    observer.unobserve(entry.target);
                });
            } else {
                console.error(thisCount.error);
            }
        }
    });
}

export function initCountup() {
    if (!checkIntersectionObserver()) {
        return;
    }

    const countUpItems = [].slice.call(document.querySelectorAll('.statistics-section--animated .statistic--single-number .statistic__number'));
    if (countUpItems.length === 0) {
        return;
    }

    const options = {
        rootMargin: '0px',
        threshold: 1.0
    };

    const observer = new IntersectionObserver(handleScrollIntoView, options);
    countUpItems.forEach(function (item) {
        item.dataset.targetNum = Number(item.innerText.replace(/,/g, ''));
        observer.observe(item)
    });
}
