// import debounce from "debounce";

// stick nav js adapted from NHSD frontend
let activeItem = null;
let overviewNavItems = [];
let allNavItems = [];

let windowTopPos = 0;
const TOP_THRESHOLD = 0.1;

// temp until figure out debounce import
function debounce(func, wait, immediate) {
    var timeout;

    return function executedFunction() {
        var context = this;
        var args = arguments;

        var later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        }

        var callNow = immediate && !timeout;

        clearTimeout(timeout);

        timeout = setTimeout(later, wait);

        if (callNow) func.apply(context, args);
    }
}

function updateActiveStates() {
    allNavItems.forEach((item) => item.classList.remove('active'));

    if (activeItem) activeItem.navEl.classList.add('active');
}

function findActiveItem() {
    if (window.location.href.indexOf("overview") === -1) return;

    let newActiveItem = null;
    let newItemTopPos = null;
    overviewNavItems.forEach((item) => {
        const itemTopPos = item.contentEl.getBoundingClientRect().top;
        if (windowTopPos < itemTopPos) return;
        if (newActiveItem && itemTopPos < newItemTopPos) return;

        newActiveItem = item;
        newItemTopPos = newActiveItem.contentEl.getBoundingClientRect().top;
    });

    if (activeItem !== newActiveItem) {
        activeItem = newActiveItem;
        updateActiveStates();
    }
}

function initItem(rapiDocEl, navEl) {
    const { contentId } = navEl.dataset;
    const contentEl = rapiDocEl.querySelector(`[id='${contentId}']`);
    if (!contentEl) return;

    const navItem = {
        navEl,
        contentEl,
    };

    overviewNavItems.push(navItem);
}

function highlightNavbarOnScroll(rapiDocEl) {
    allNavItems = rapiDocEl.querySelectorAll('.nav-bar-components, .nav-bar-h1, .nav-bar-h2, .nav-bar-info, .nav-bar-tag, .nav-bar-path');
    overviewNavItems = [];
    const items = rapiDocEl.querySelectorAll('.nav-bar-h1, .nav-bar-h2');
    items.forEach((item) => initItem(rapiDocEl, item));
    windowTopPos = window.innerHeight * TOP_THRESHOLD;

    window.addEventListener('resize', () => {
        windowTopPos = window.innerHeight * TOP_THRESHOLD;
        debounce(findActiveItem, 16);
    });

    window.addEventListener('scroll', debounce(findActiveItem, 16));
    findActiveItem();
}

// responsive table js adapted from NHSD frontend
function makeResponsive(table) {
    table.classList.add('nhsd-!t-display-hide');
    table.classList.add('nhsd-!t-display-s-show-table');

    const tableHeader = table.querySelector('thead tr');
    const tableHeadings = tableHeader.querySelectorAll('td, th');
    const tableBodys = table.querySelectorAll('tbody');

    let listElHtml = '<ul class="nhsd-!t-display-s-hide">';

    Array.from(tableBodys).forEach((tableBody) => {
        const tableRows = tableBody.querySelectorAll('tr');

        Array.from(tableRows).forEach((tableRow) => {
            listElHtml += '<li>';
            listElHtml += '<ul class="nhsd-m-table__mobile-list-item">';

            const tableCells = tableRow.querySelectorAll('td, th');
            Array.from(tableCells).forEach((tableCell, index) => {
                listElHtml += '<li><span><b>';
                if (tableHeadings[index]) {
                    listElHtml += tableHeadings[index].innerText;
                }
                listElHtml += '</b></span><span>';
                listElHtml += tableCell.innerHTML;
                listElHtml += '</span></li>';
            });
            listElHtml += '</ul></li>';
        });
    });

    listElHtml += '</ul>';

    const listEl = document.createElement('div');
    listEl.classList.add('nhsd-m-table__mobile-list');
    listEl.innerHTML = listElHtml;

    table.parentNode.insertBefore(listEl, table.nextSibling);
}

function makeTablesResponsive(element) {
    const tables = element.querySelectorAll('.m-markdown-small table, .m-markdown table');
    Array.from(tables).forEach((table) => {
        if (!table.classList.contains('nhsd-!t-display-hide')) {
            makeResponsive(table);
        }
    });
}

function addHorizontalRuleBetweenHeadings(element) {
    const headings = element.querySelectorAll('h2');
    for (let i = 1; i < headings.length; i += 1) {
        const horizontalRuleEl = document.createElement('hr');
        horizontalRuleEl.classList.add('nhsd-a-horizontal-rule');

        if (!headings[i].previousElementSibling
            || !headings[i].previousElementSibling.isEqualNode(horizontalRuleEl)) {
            headings[i].before(horizontalRuleEl);
        }
    }
}



window.addEventListener('load', () => {
    const rapiDocEl = document.getElementById('rapi-doc-spec').shadowRoot;
    makeTablesResponsive(rapiDocEl);
    addHorizontalRuleBetweenHeadings(rapiDocEl);
    highlightNavbarOnScroll(rapiDocEl)
});

window.navigation.addEventListener('currententrychange', () => {
    const rapiDocEl = document.getElementById('rapi-doc-spec').shadowRoot;
    makeTablesResponsive(rapiDocEl);
    addHorizontalRuleBetweenHeadings(rapiDocEl);
    highlightNavbarOnScroll(rapiDocEl)
});
