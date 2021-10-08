import loadPage from "../ajax-page-loader";
import parseQueryString from "../utils/parse-query-string";
import debounce from "../utils/debounce";

let filterMenuForm = document.querySelector('#nhsd-filter-menu-form');

export default function() {
    filterMenuForm = document.querySelector('#nhsd-filter-menu-form');
    if (!filterMenuForm) return;

    bindFormEvents();
    bindFilterDropdown();
    bindTags();
    bindClearFilterButton();
    bindLinks();

    nhsd('.js-search-submit').on('click', performSearch);

    window.onpopstate = function(e) {
        if (e.state.page !== 'search-page') return;
        loadUrl(document.location);
    }
}

function bindLinks() {
    nhsd('.js-pagination a').on('click', t => {
        const url = t.currentTarget.href;
        loadUrl(url);
        history.pushState({ page: 'search-page' }, document.title, url);
        return false;
    });
}

function bindClearFilterButton() {
    nhsd('.js-clear-filters').on('click', () => {
        const searchFilterInputs = document.querySelectorAll('.js-search-filter input');
        if (searchFilterInputs.length === 0) return;
        searchFilterInputs.forEach(sfi => sfi.checked = false);
        nhsd(searchFilterInputs[0]).trigger('change');
    });
}

function bindTags() {
    nhsd('.js-filter-tag').unbind('click.search-page').on('click.search-page', e => {
        const filterInput = document.querySelector(`#${e.target.dataset.filterId}`);
        if (!filterInput) return;

        filterInput.checked = false;
        nhsd(filterInput).trigger('change');

        return false;
    });
}

function bindFormEvents() {
    const filterMenuInputs = filterMenuForm.querySelectorAll('#nhsd-filter-menu input, #searchSortBy');
    filterMenuInputs.forEach(input => {
        nhsd(input).unbind('change.search-page').on('change.search-page', () => performSearch());
    });

    nhsd(filterMenuForm).on('submit', () => performSearch());
}

function loadUrl(url) {
    const contentEl = document.querySelector('#search-content');
    if (contentEl && contentEl.getBoundingClientRect().y < 0) {
        contentEl.scrollIntoView();
    }

    document.querySelectorAll('.nhsd-o-dropdown').forEach(dropdown => nhsd(dropdown).trigger('dropdown-close'));
    const loadingEl = document.querySelector('.js-search-loading');
    loadingEl.classList.remove('nhsd-!t-display-hide');
    loadPage(url, ['#search-results', '#people-search-nav']).finally(() => {
        loadingEl.classList.add('nhsd-!t-display-hide');
        bindFormEvents();
        bindTags();
        bindClearFilterButton();
        bindLinks();
    });
}

function performSearch() {
    const formData = new FormData(filterMenuForm);
    const url = window.location.pathname + "?" + new URLSearchParams(formData).toString();

    if (url === document.location.pathname + document.location.search) return false;

    loadUrl(url);
    history.pushState({ page: 'search-page' }, document.title, url);

    return false;
}

function bindFilterDropdown() {
    nhsd('#searchFilterInput').on(['input', 'click'], e => {
        var searchText = document.querySelector('#searchFilterInput').value;

        const filters = createFilterDropdown(searchText);
        nhsd('#searchFilterDropdown').trigger('dropdown-set-items', filters);

        if (searchText.length > 0 && filters.length > 0) {
            nhsd('#searchFilterDropdown').trigger('dropdown-open');
        } else {
            nhsd('#searchFilterDropdown').trigger('dropdown-close');
        }
    });

    nhsd('#searchFilterDropdown').on('dropdown-selection', (e, selected) => {
        const filterInput = document.querySelector(`#${selected.dataset.id}`);
        if (!filterInput) return;

        filterInput.checked = selected.checked;
        nhsd(filterInput).trigger('change');

        const menu = filterInput.closest('.nhsd-m-filter-menu-section');
        if (selected.checked) {
            nhsd(menu).trigger('filter-menu-open');
        }
    });
}

function createFilterDropdown(searchText) {
    const filters = [];
    const searchFilters = document.querySelectorAll('.js-search-filter');
    searchFilters.forEach(searchFilter => {
        const filterInput = searchFilter.querySelector('input');
        const filterKeywords = searchFilter.dataset.keywords;
        const filterName = searchFilter.querySelector('.js-filter-name');

        if (filterKeywords.match(new RegExp(searchText, 'i'))) {
            filters.push({
                text: filterName.innerText,
                name: filterInput.id,
                data: {
                    id: filterInput.id,
                },
                checked: filterInput.checked,
                checkbox: true,
            });
        }
    });

    return filters;
}

createSearchDropdown('#heroSearchDropdown', '#heroSearchQuery');
createSearchDropdown('#heroSearchDropdown', '#query');
function createSearchDropdown(dropdownId, dropdownInputId) {
    const ajaxSearchUrl = document.querySelector(dropdownId).dataset.liveSearchUrl;
    const searchUrl = document.querySelector(dropdownId).dataset.searchUrl;
    if (!ajaxSearchUrl || !searchUrl) return;

    const debouncedRequest = debounce(() => {
        let searchText = document.querySelector(dropdownInputId).value;

        if (searchText.length < 3) {
            nhsd(dropdownId).trigger('dropdown-close');
            return;
        }

        nhsd(dropdownId).trigger('dropdown-set-content', '<div class="nhsd-!t-margin-2 nhsd-!t-margin-left-4 nhsd-t-heading-xs">Searching..</div>');
        nhsd(dropdownId).trigger('dropdown-open');

        const ajaxUrl = ajaxSearchUrl.split('?')[0];
        const ajxQueryParams = parseQueryString(ajaxSearchUrl)
        ajxQueryParams.query = searchText;

        fetch(ajaxUrl + '?' + (new URLSearchParams(ajxQueryParams)).toString(), {
            method: 'GET'
        }).then(results => results.json())
        .then(jsonRes => {
            const peopleResults = jsonRes.people || [];
            const documentResults  = jsonRes.content || [];

            if (peopleResults.length > 0 || documentResults.length > 0) {
                let dropdownContent = '';
                if (peopleResults.length > 0) {
                    dropdownContent += `<div class="nhsd-t-heading-xs nhsd-!t-padding-2 nhsd-!t-padding-left-4 nhsd-!t-margin-2 nhsd-!t-margin-top-0 nhsd-!t-bg-pale-grey nhsd-t-round nhsd-t-text-align-left">People</div>
                    <ul class="nhsd-t-text-align-left">
                      ${peopleResults.map(person => `<li>
                          <a class="nhsd-!t-flex" href="${person.url}">
                            <div class="nhsd-!t-margin-right-xs-1 nhsd-!t-margin-right-s-1 nhsd-!t-margin-right-1">
                              <span class="nhsd-a-icon nhsd-a-icon--size-xs nhsd-!t-margin-right-2">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16">
                                  <g><path d="M 16 11.199219 L 15.332031 16 L 0.667969 16 L 0 11.199219 L 3.867188 9.667969 C 2.867188 8.601562 2.265625 7.199219 2.265625 5.734375 C 2.265625 2.601562 4.867188 0 8 0 C 11.132812 0 13.734375 2.601562 13.734375 5.734375 C 13.734375 7.199219 13.132812 8.601562 12.132812 9.667969 Z M 13.601562 12.667969 L 9.464844 11.066406 L 9.464844 8.867188 C 10.667969 8.066406 11.464844 7.199219 11.464844 5.667969 C 11.464844 3.800781 9.933594 2.265625 8.066406 2.265625 C 6.199219 2.265625 4.667969 3.800781 4.667969 5.667969 C 4.667969 7.199219 5.464844 8.066406 6.667969 8.867188 L 6.667969 11.066406 L 2.398438 12.667969 L 2.601562 13.734375 L 13.398438 13.734375 Z M 13.601562 12.667969 "/></g>
                                </svg>
                              </span>
                            </div>
                            <div>
                              <div class="nhsd-t-heading-xs nhsd-!t-margin-0">${person.name}</div>
                              <div>${person.role}</div>
                            </div>
                          </a>
                          </li>`).reduce((v, acc) => acc + v, '')}
                      <li class="nhsd-!t-padding-4 nhsd-!t-padding-top-2 nhsd-!t-padding-bottom-2"><a class="nhsd-a-link" href="${searchUrl}?area=PEOPLE&query=${searchText}">View all people</a></li>
                    </ul>
                  </div>`;
                }

                if (documentResults.length > 0 && peopleResults.length > 0) {
                    dropdownContent += '<hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s nhsd-!t-margin-top-0" />';
                }

                if (documentResults.length > 0) {
                    dropdownContent += `<div class="nhsd-t-text-align-left nhsd-t-heading-xs nhsd-!t-padding-2 nhsd-!t-padding-left-4 nhsd-!t-margin-2 nhsd-!t-margin-top-0 nhsd-!t-bg-pale-grey nhsd-t-round">Documents</div>
                    <ul class="nhsd-t-text-align-left">
                      ${documentResults.map(doc => `<li>
                          <a class="nhsd-!t-flex" href="${doc.url}">
                            <div class="nhsd-!t-margin-right-xs-1 nhsd-!t-margin-right-s-1 nhsd-!t-margin-right-1">
                                <span class="nhsd-a-icon nhsd-a-icon--size-xs nhsd-!t-margin-right-2">
                                    <svg id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" viewBox="0 0 512 512" xml:space="preserve">
                                    <g><path d="M360.948,0H51.613v512h408.774V100.123L360.948,0z M392.258,443.871H119.742V68.129h202.323v70.194h70.194V443.871z"/></g>
                                    <g><rect x="187.871" y="171.355" width="136.258" height="68.129"/></g>
                                    <g><rect x="187.871" y="307.613" width="136.258" height="68.129"/></g>
                                    </svg>
                                </span>
                            </div>
                            <div>
                              <div class="nhsd-t-heading-xs nhsd-!t-margin-0">${doc.name}</div>
                              <div>${doc.year}</div>
                            </div>
                          </a>
                          </li>`).reduce((v, acc) => acc + v, '')}
                        <li class="nhsd-!t-padding-4 nhsd-!t-padding-top-2 nhsd-!t-padding-bottom-2"><a class="nhsd-a-link" href="${searchUrl}?query=${searchText}">View all documents</a></li>
                    </ul>
                  </div>`;
                }

                nhsd(dropdownId).trigger('dropdown-set-content', dropdownContent);
            } else {
                nhsd(dropdownId).trigger('dropdown-set-content', '<div class="nhsd-!t-margin-2 nhsd-!t-margin-left-4 nhsd-t-heading-xs">No results found...</div>');
            }
        });
    }, 200);

    nhsd(dropdownInputId).on(['input', 'click'], () => debouncedRequest());

    nhsd(dropdownId).on('dropdown-selection', () => {
        return false;
    });
}