const nhsdHiddenClass = 'nhsd-!t-display-hide';

function getAllSearchResults() {
    return [...document.querySelectorAll('[data-api-catalogue-entry]')];
}

function getAllSections() {
    return [...document.querySelectorAll('[data-letter-section]')];
}

function containsMatchingText(result, searchTerm) {
    const regex = new RegExp(`\\b${searchTerm.trim()}`, 'gi');
    const headingContainsTerm = result.querySelector('h2').textContent.match(regex);
    const summaryContainsTerm = result.querySelector('p').textContent.match(regex);
    const tagsContainTerm = Array.from(result.querySelectorAll('.nhsd-a-tag'))
        .map((item) => item.textContent.match(regex) != null)
        .includes(true);
    return headingContainsTerm || summaryContainsTerm || tagsContainTerm;
}

function isVisible(element) {
    return !element.classList.contains(nhsdHiddenClass);
}

/* Hides hr elements and alphabet blocks for sections without visible results */
function updateSections() {
    const sections = getAllSections();

    // restore section dividers
    document.querySelectorAll('[data-letter-section] > hr:first-child')
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

function findHTMLElementData(result, element, htmlElement) {
    let htmlData = '';
    if (element === 'p') {
        htmlData = result.querySelector(element).innerHTML;
    } else {
        htmlData = htmlElement.innerHTML;
    }
    return htmlData;
}

function replaceHTMLContent(result, element, text, searchTerm, htmlElement) {
    const highlightContent1 = text.replaceAll('<span class="filter-tag-yellow-highlight">', '');
    const highlightContent2 = highlightContent1.replaceAll('</span>', '');
    let highlightContentFinal = '';
    if (searchTerm.length !== 0) {
        highlightContentFinal = highlightContent2.replaceAll(new RegExp(searchTerm, 'gi'), (match) => {
            const returnString = `<span class="filter-tag-yellow-highlight">${match}</span>`;
            return returnString;
        });
    }
    if (element === 'p') {
        const resultNew = result;
        if (searchTerm.length !== 0) {
            resultNew.querySelector(element).innerHTML = highlightContentFinal;
        } else {
            resultNew.querySelector(element).innerHTML = highlightContent2;
        }
    } else {
        const htmlElementNew = htmlElement;
        if (searchTerm.length !== 0) {
            htmlElementNew.innerHTML = highlightContentFinal;
        } else {
            htmlElementNew.innerHTML = highlightContent2;
        }
    }
}

function highlightSearchContent(searchTerm) {
    const allResults = getAllSearchResults();
    const htmlElementArray = ['a', 'p'];
    let htmlData = '';
    allResults.forEach((result) => {
        htmlElementArray.forEach((element) => {
            if (element === 'a') {
                result.querySelectorAll('a')
                    .forEach((htmlElement) => {
                        htmlData = findHTMLElementData(result, element, htmlElement);
                        replaceHTMLContent(result, element, htmlData, searchTerm, htmlElement);
                    });
            }
            if (element === 'p') {
                htmlData = findHTMLElementData(result, element, null);
                replaceHTMLContent(result, element, htmlData, searchTerm, null);
            }
        });
    });
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
    highlightSearchContent(searchTerm);
    const countDisplay = document.querySelector('h6#search-results-count');
    const visibleCount = allResults.filter(isVisible).length;
    countDisplay.textContent = `${visibleCount} results`;
    const listElement = document.querySelector('#list-page-results-list');
    if (visibleCount === 0) {
        listElement.classList?.remove('nhsd-!t-margin-bottom-9');
        listElement.classList?.add('nhsd-!t-margin-bottom-4');
    } else {
        listElement.classList?.remove('nhsd-!t-margin-bottom-4');
        listElement.classList?.add('nhsd-!t-margin-bottom-9');
    }
}

document.querySelector('#catalogue-search-bar').classList.remove(nhsdHiddenClass);
document.querySelector('#catalogue-search-bar-input').addEventListener('input', (ev) => updateSearchResults(ev));
