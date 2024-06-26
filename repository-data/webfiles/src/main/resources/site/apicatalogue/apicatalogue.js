const nhsdHiddenClass = 'nhsd-!t-display-hide';

function getAllSearchResults() {
    return [...document.querySelectorAll('[data-api-catalogue-entry]')];
}

function getAllSections() {
    return [...document.querySelectorAll('[data-letter-section]')];
}

function containsMatchingText(result, searchTerm) {
    const searchTermEscape = searchTerm.replace(/[!"£%&_#:<>.*+?^${}()|[\]\\]/g, '\\$&');
    const regex = new RegExp(`\\b${searchTermEscape.trim()}`, 'gi');
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
    const highlightContent = text
        .replaceAll('<span class="filter-tag-yellow-highlight">', '')
        .replaceAll('</span>', '')
        .replaceAll('&amp;', '&');
    let highlightContentFinal = '';
    if (searchTerm.length !== 0) {
        highlightContentFinal = highlightContent.replaceAll(new RegExp(searchTerm, 'gi'), (match) => {
            const returnString = `<span class="filter-tag-yellow-highlight">${match}</span>`;
            return returnString;
        });
    }
    if (element === 'p') {
        const resultNew = result;
        if (searchTerm.length !== 0) {
            resultNew.querySelector(element).innerHTML = highlightContentFinal;
        } else {
            resultNew.querySelector(element).innerHTML = highlightContent;
        }
    } else {
        const htmlElementNew = htmlElement;
        if (searchTerm.length !== 0) {
            htmlElementNew.innerHTML = highlightContentFinal;
        } else {
            htmlElementNew.innerHTML = highlightContent;
        }
    }
}

function highlightSearchContent(searchTerm) {
    const searchEscape = searchTerm.replace(/[!"£%&_#:<>.*+?^${}()|[\]\\]/g, '\\$&');
    const allResults = getAllSearchResults();
    const htmlElements = ['a', 'p'];
    let htmlData = '';
    allResults.forEach((result) => {
        htmlElements.forEach((element) => {
            if (element === 'a') {
                result.querySelectorAll('a')
                    .forEach((htmlElement) => {
                        htmlData = findHTMLElementData(result, element, htmlElement);
                        replaceHTMLContent(result, element, htmlData, searchEscape, htmlElement);
                    });
            }
            if (element === 'p') {
                htmlData = findHTMLElementData(result, element, null);
                replaceHTMLContent(result, element, htmlData, searchEscape, null);
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

function hide(element) {
    element.classList.add(nhsdHiddenClass);
}

function showFilterSubsections(element) {
    [...element.parentElement.children]
        .forEach((sibling) => {
            sibling.classList.remove(nhsdHiddenClass);
            [...sibling.children]
                .forEach((siblingChild) => siblingChild.classList.remove(nhsdHiddenClass));
        });
}

function searchApiCatalogue() {
    const searchInput = document.querySelector('#catalogue-search-bar-input');
    const searchTerm = searchInput.value.trim();
    const currentUrl = window.location.href;
    const requestUrl = new URL(currentUrl);
    requestUrl.searchParams.set('query', searchTerm);
    window.location.href = requestUrl.toString();
}

document.querySelector('#catalogue-search-bar').classList.remove(nhsdHiddenClass);
document.querySelector('#catalogue-search-bar-input').addEventListener('input', (ev) => updateSearchResults(ev));
document.querySelectorAll('#show-more').forEach((element) => element.addEventListener('click', () => {
    showFilterSubsections(element);
    hide(element);
}));
document.querySelector('#catalogue-search-bar-input').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        searchApiCatalogue();
    }
});
