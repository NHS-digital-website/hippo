import {ResizeSensor} from "css-element-queries";

const section = document.querySelectorAll(".article-section, .article-section-separator, .article-section--summary-separator, .sticky-nav-spy-item");
let topOffset = 0;

// Alter the "top" from the document top. Useful for sticky headers.
const chapterPagination = document.querySelector('.grid-wrapper--chapter-pagination');
if (chapterPagination) {
    topOffset = chapterPagination.getBoundingClientRect().height * -1;
}

const sections = {top: 0};
const pageBlocks = document.getElementsByClassName("page-block--main");
// Alter the "top" from the document top. Useful for sticky headers.

const MARGIN_OF_ERROR = 2; // used to 'adjust/falsify' element position for marking while click

function getPosition(element) {
    let distance = -MARGIN_OF_ERROR;
    let testElement = element;
    do {
        distance += element.offsetTop;
        testElement = testElement.offsetParent;
    } while (testElement);
    return distance;
}

function calculate() {
    Array.prototype.forEach.call(section, function (e) {
        if (e.id !== "") {
            sections[e.id] = getPosition(e);
        }
    });
}

function markStickyNavElem(navElem) {
    if (document.querySelector('.active') != null) {
        document.querySelector('.active').setAttribute('class', ' ');
    }
    const newActive = document.querySelector(`a[href="#${  navElem  }"]`);
    const idElement = document.getElementById(navElem);
    if (
        newActive != null &&
        idElement != null &&
        !idElement.classList.contains("sticky-nav-exclude-active")
    ) {
        newActive.setAttribute('class', 'active');
    }
}

function getBottomStickyNavElement(navSection) {
    // if no sections in sticky nav return immediately
    if (navSection == null || navSection.length === 0) {
        return null;
    }

    // sort sections descending by value / second element

    // create temp array to sort dict by second element
    const sectionsArray = Object.keys(navSection).map(function (key) {
        return [key, navSection[key]];
    });

    // Sort the array descending based on the second element
    sectionsArray.sort(function (first, second) {
        return second[1] - first[1];
    });

    // return first element after sorting by position / which is bottom
    // element of the the sticky nav
    return sectionsArray[0][0];
}

function marker() {
    let scrollPosition = document.documentElement.scrollTop || document.body.scrollTop;

    // Alter the "top" from the document top..
    scrollPosition -= topOffset;
    Object.keys(sections).forEach(navElem => {
        if (navElem != null && navElem !== "" && sections[navElem] <= scrollPosition) {
            markStickyNavElem(navElem);
        }
    })

    // if scrolled to the bottom (with margin of error) => mark last section
    if ($(window).scrollTop() + $(window).height() > $(document).height() - MARGIN_OF_ERROR) {

        // mark last element on the sticky nav
        const navElem = getBottomStickyNavElement(sections);
        if (navElem != null && navElem !== "") {
            markStickyNavElem(navElem);
        }
    }

}

export function initScrollSpy() {
    $(window).on('load resize scroll', function (e) {
        if (e.type === 'resize' || e.type === 'load') {
            calculate();
        }
        marker();
    });
    if (pageBlocks.length) {
        // eslint-disable-next-line no-new
        new ResizeSensor(pageBlocks[0], function () {
            calculate();
        });
    }
}
