/**
 * This JavaScript module facilitate dynamic highlighting of hyperlinks in the side
 * nav. To show the corresponding section of the main content that's in view until
 * it's very bottom element is scrolled out of view.
 *
 * 'Comes to view' means its boundaries start crossing the top edge of the DOM's
 * Window.
 *
 * Side nav hyperlinks is the 'top level' heading of a section. The nested ones (or
 * logically nested ones) are displayed are associated with their relevant section
 * parent.
 *
 * 'logically nested ones' means sibling elements that are not truly nested within
 * it's 'top level' heading's section in the DOM, but are nonetheless associated.
 *
 * To make use of this feature:
 * - Annotate 'container' element of each section in the main content with CSS
 * class 'navigationMarker',
 * - Annotate 'container' element of each sub-section that is logically nested in
 * the main content with CSS class 'navigationMarker-sub'.
 *
 * Indicative example:
 *
 * <-- nested -->
 * <section class="navigationMarker">
 *     <heading>Nested</heading> <-- In the sticky nav -->
 *     <element>Example...</element>
 *     <sub-heading>Nested</sub-heading>
 *     <element>Example...</element>
 * </section>
 *
 * <-- logically nested -->
 * <section class="navigationMarker">
 *     <heading>Nested</heading> <-- In the sticky nav -->
 *     <element>Example...</element>
 * </section>
 * <section class="navigationMarker-sub">
 *     <sub-heading>Nested</sub-heading>
 *     <element>Example...</element>
 * </section>
 *
 *
 * The 'highlighting' of the hyperlinks in the side nav is achieved by CSS.
 * A data attribute is dynamically added to appropriate hyperlink items as the
 * corresponding section come to view, and removed as the section moves out of view.
 * By design more than one sticky nav item can have the data attribute. The
 * responsibility of how it's displayed is delegates to the CSS stylesheet.
 *
 * Example SCSS:
 *
 * [data-nav-marker^="|"] {
 *   border-left: solid #CCC;
 *   & ~ & {
 *       border-left: none;
 *   }
 * }
 */


export const INDICATOR  = "navigationMarker";
export const NAV_MARKER = "data-nav-marker";

function addMarker(elm) {
    let elementById = document.getElementById(`#${elm.id}_list`)
    if (elementById !== null) {
        let markers = elementById.getAttribute(NAV_MARKER);
        if (markers !== null) {
            elementById.setAttribute(NAV_MARKER, markers + "|");
        } else {
            elementById.setAttribute(NAV_MARKER, "|");
        }
    }
}

function removeMarker(elm) {
    let elementById = document.getElementById(`#${elm.id}_list`)
    if (elementById !== null) {
        let markers = elementById.getAttribute(NAV_MARKER);
        if (markers !== null && markers.length > 1) {
            elementById.setAttribute(NAV_MARKER, markers.substring(1));
        } else {
            elementById.removeAttribute(NAV_MARKER);
        }

    }
}

/**
 * Finds any elements marked with the CSS class 'navigationMarker' and calculates
 * whether it in or out of view - then marks/unmasks the corresponding sticky nav
 * list items appropriately.
 */
export function initStickyNav() {
    const io = new IntersectionObserver(entries => {
        for (const entry of entries) {
            if (entry.isIntersecting) {
                addMarker(entry.target);
            } else {
                removeMarker(entry.target);
            }
        }
    }, {threshold: 0})

    document.querySelectorAll(`.${INDICATOR}`)
        .forEach(el => io.observe(el))
}


/**
 * Finds logically nested elements that are marked with the CSS class
 * 'navigationMarker-sub' and calculates whether it's relevant logical parent is in
 * or out of view - then marks/unmasks the corresponding sticky nav list items
 * appropriately.
 *
 * See above for details on 'logically nested' elements.
 */
export function initStickyNavSubItems() {
    const io = new IntersectionObserver(entries => {
        const getPreviousRelevantSibling = (elem, selector) => {
            let sibling = elem.previousElementSibling
            while (sibling) {
                if (sibling.matches(selector)) return sibling
                sibling = sibling.previousElementSibling
            }
        };
        for (const entry of entries) {
            if (entry.isIntersecting) {
                let relevant = getPreviousRelevantSibling(entry.target, `.${INDICATOR}`);
                if(relevant) {
                    addMarker(relevant)
                }
            } else {
                let relevant = getPreviousRelevantSibling(entry.target, `.${INDICATOR}`);
                if (relevant) {
                    removeMarker(relevant)
                }
            }
        }
    }, {threshold: 0})
    document.querySelectorAll(`.${INDICATOR}-sub`)
        .forEach(el => io.observe(el))
}
