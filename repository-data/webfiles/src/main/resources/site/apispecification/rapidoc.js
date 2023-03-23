window.addEventListener('load', () => {
    let rapiDocEl = document.getElementById("rapi-doc-spec").shadowRoot;
    makeRapiDocMarkdownTablesResponsive(rapiDocEl);
    addHorizontalRuleBetweenHeadings(rapiDocEl);

    let nav = rapiDocEl.querySelector('nav.nav-scroll');
    nav.addEventListener('click', () => {
        // TODO added multiple times if click on the main content multiple times
        makeRapiDocMarkdownTablesResponsive(rapiDocEl);
        addHorizontalRuleBetweenHeadings(rapiDocEl);
    })
})


// Stick nav highlight active section possible?

// responsive table js taken from nhsd frontend
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

function makeRapiDocMarkdownTablesResponsive(rapiDocEl) {
    const tables = rapiDocEl.querySelectorAll('table');
    Array.from(tables).forEach((table) => {
        makeResponsive(table);
    });
}

function addHorizontalRuleBetweenHeadings(rapiDocEl) {
    const headings = rapiDocEl.querySelectorAll('h2');
    for(let i = 1; i < headings.length; i++) {
        const horizontalRuleEl = document.createElement('hr');
        horizontalRuleEl.classList.add('nhsd-a-horizontal-rule');
        headings[i].before(horizontalRuleEl);
    }
}
