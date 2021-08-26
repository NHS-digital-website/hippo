import loadPage from "./ajax-page-loader";
import debounce from "./utils/debounce";

const elementsToUpdate = ['.js-feed-content', '.js-filter-tags', '.js-filter-list'];
const loadingTextEl = document.querySelector('.js-loading-text');
let focusElementId = null;
let filterForm = null;

function rebind() {
    filterForm = document.querySelector('#feed-list-filter');

    Array.from(document.querySelectorAll('.js-filter-checkbox')).forEach(checkbox => {
        checkbox.addEventListener('change', function (e) {
            loadFeedPage(filterForm, elementsToUpdate);
            focusElementId = e.target.dataset.inputId;
        });
    });

    const filterTags = Array.from(document.querySelectorAll('.js-filter-tag'));
    filterTags.forEach(filterTag => {
        filterTag.addEventListener('click', function (e) {
            const filterId = filterTag.dataset.filterId;
            const filterInput = document.querySelector('[data-input-id="' + filterId + '"]');
            if (!filterInput) return;
            filterInput.click();
            focusElementId = null;
            e.preventDefault();
        });
    });
}

if (document.querySelector('#feedhub-content')) {
    rebind();
}

const loadFeedPageDebounced = debounce(loadFeedPage, 500);
const queryInputs = Array.from(document.querySelectorAll(".js-feedhub-query"));
queryInputs.forEach(queryInput => queryInput.addEventListener('keyup', () => {
    queryInputs.forEach(input => input.value = queryInput.value);
    loadFeedPageDebounced(filterForm, elementsToUpdate);
}));

const sortInput = document.querySelector('#sort-by-input');
if (sortInput) {
    sortInput.addEventListener('change', function () {
        loadFeedPage(filterForm, elementsToUpdate);
    });
}

function getActiveFilters() {
    const activeFilterEls = Array.from(document.querySelectorAll('.nhsd-m-filter-menu-section--active'));
    return activeFilterEls.reduce((acc, activeFilter) => acc + encodeURI(activeFilter.dataset.activeMenu) + ',', '');
}

function loadFeedPage(filterForm, elementsToUpdate) {
    const activeFilters = getActiveFilters();
    const formData = new FormData(filterForm);
    loadingTextEl.classList.remove('nhsd-!t-display-hide');
    const url = window.location.pathname + "?active-filters=" + activeFilters + "&" + new URLSearchParams(formData).toString()
    loadPage(url, elementsToUpdate)
        .then(() => {
            nhsd.init();
            rebind();
            if (focusElementId) {
                const input = document.querySelector('[data-input-id="' + focusElementId + '"]');
                if (input) input.focus();
                focusElementId = null;
            }
            loadingTextEl.classList.add('nhsd-!t-display-hide');
            history.pushState({}, document.title, url);
        });
}
