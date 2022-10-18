const nhsdHiddenClass = 'nhsd-!t-display-hide';

function getAllSearchResults() {
    return [...document.querySelectorAll('#list-page-results-list > div')];
}

function containsMatchingText(result, searchTerm) {
    const regex = new RegExp(`\\b${searchTerm}`, 'gi');
    const headingContainsTerm = result.querySelector('h2').textContent.match(regex);
    const summaryContainsTerm = result.querySelector('p').textContent.match(regex);
    return headingContainsTerm || summaryContainsTerm;
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
    const visibleResults = [...getAllSearchResults()]
        .filter((result) => !result.classList.contains(nhsdHiddenClass));
    visibleResults.at(0)?.querySelector('hr')?.classList?.add(nhsdHiddenClass);
}

document.querySelector('#catalogue-search-bar').addEventListener('input', (ev) => updateSearchResults(ev));
