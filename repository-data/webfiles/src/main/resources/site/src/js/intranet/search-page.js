import loadPage from "../ajax-page-loader";

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
    const filterMenuInputs = filterMenuForm.querySelectorAll('#nhsd-filter-menu input, #searchQuery, #searchSortBy');
    filterMenuInputs.forEach(input => {
        nhsd(input).unbind('change.search-page').on('change.search-page', () => performSearch());
    });
}

function loadUrl(url) {
    const contentEl = document.querySelector('#search-content');
    if (contentEl && contentEl.getBoundingClientRect().y < 0) {
        contentEl.scrollIntoView();
    }

    const loadingEl = document.querySelector('.js-search-loading');
    loadingEl.classList.remove('nhsd-!t-display-hide');
    loadPage(url, ['#search-results']).finally(() => {
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