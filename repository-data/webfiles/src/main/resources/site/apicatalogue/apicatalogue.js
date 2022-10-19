const nhsdHiddenClass = 'nhsd-!t-display-hide';

function getAllSearchResults() {
    return [...document.querySelectorAll('[data-api-catalogue-entry]')];
}

function getAllSections() {
    return [...document.querySelectorAll('#list-page-results-list > div')];
}

function containsMatchingText(result, searchTerm) {
    const regex = new RegExp(`\\b${searchTerm}`, 'gi');
    const headingContainsTerm = result.querySelector('h2').textContent.match(regex);
    const summaryContainsTerm = result.querySelector('p').textContent.match(regex);
    return headingContainsTerm || summaryContainsTerm;
}

function updateSections() {
    const sections = getAllSections();
    sections.forEach((section) => {
        const visibleResults = [...section.querySelectorAll('[data-api-catalogue-entry]')]
            .filter((result) => !result.classList.contains(nhsdHiddenClass));
        if (!visibleResults.length) {
            section.classList?.add(nhsdHiddenClass);
        } else {
            section.classList?.remove(nhsdHiddenClass);
            visibleResults.forEach((result) => {
                result.querySelector('hr')?.classList?.remove(nhsdHiddenClass)
            });
            visibleResults.pop().querySelector('hr')?.classList?.add(nhsdHiddenClass);
        }
    });
}

function updateSearchResults(ev) {
    const searchTerm = ev.target.value;
    const allResults = getAllSearchResults();
    allResults.forEach((result) => {
        if (!containsMatchingText(result, searchTerm)) {
            result?.classList?.add(nhsdHiddenClass);
            const rule = result.querySelector('hr');
            rule?.classList?.remove(nhsdHiddenClass);
        } else {
            result?.classList?.remove(nhsdHiddenClass);
            const rule = result.querySelector('hr');
            rule?.classList?.remove(nhsdHiddenClass);
        }
    });
    updateSections();
}

document.querySelector('#catalogue-search-bar').addEventListener('input', (ev) => updateSearchResults(ev));
