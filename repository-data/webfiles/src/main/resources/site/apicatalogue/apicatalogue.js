const nhsdHiddenClass = 'nhsd-!t-display-hide';

function getAllSearchResults() {
    return [...document.querySelectorAll('[data-api-catalogue-entry]')];
}

function getAllSections() {
    return [...document.querySelectorAll('#list-page-results-list > div')];
}

function containsMatchingText(result, searchTerm) {
    const regex = new RegExp(`\\b${searchTerm.trim()}`, 'gi');
    const headingContainsTerm = result.querySelector('h2').textContent.match(regex);
    const summaryContainsTerm = result.querySelector('p').textContent.match(regex);
    return headingContainsTerm || summaryContainsTerm;
}

function isVisible(element) {
    return !element.classList.contains(nhsdHiddenClass);
}

/* Hides hr elements and alphabet blocks for sections without visible results */
function updateSections() {
    const sections = getAllSections();

    // restore section dividers
    document.querySelectorAll('#list-page-results-list > div > hr:first-child')
        .forEach((hr) => {
            hr.classList?.remove(nhsdHiddenClass);
        });

    sections.forEach((section) => {
        const visibleResults = [...section.querySelectorAll('[data-api-catalogue-entry]')]
            .filter(isVisible);
        if (!visibleResults.length) {
            section.classList?.add(nhsdHiddenClass);
        } else {
            section.classList?.remove(nhsdHiddenClass);
            visibleResults.forEach((result) => {
                result.querySelector('hr')?.classList?.remove(nhsdHiddenClass);
            });
            visibleResults.pop().querySelector('hr')?.classList?.add(nhsdHiddenClass);
        }
    });

    // hide top divider from first visible section
    sections.filter(isVisible).at(0)
        ?.querySelector('hr:first-child')
        ?.classList
        ?.add(nhsdHiddenClass);
}

function updateSearchResults(ev) {
    const searchTerm = ev.target.value;
    const allResults = getAllSearchResults();
    allResults.forEach((result) => {
        if (!containsMatchingText(result, searchTerm)) {
            result?.classList?.add(nhsdHiddenClass);
        } else {
            result?.classList?.remove(nhsdHiddenClass);
            const rule = result.querySelector('hr');
            rule?.classList?.remove(nhsdHiddenClass);
        }
    });
    updateSections();
    const countDisplay = document.querySelector('h6#search-results-count');
    const visibleCount = allResults.filter(isVisible).length;
    countDisplay.textContent = `${visibleCount} results`;
}

document.querySelector('#catalogue-search-bar').classList.remove(nhsdHiddenClass);
document.querySelector('#catalogue-search-bar-input').addEventListener('input', (ev) => updateSearchResults(ev));
