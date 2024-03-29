var tryEndpointNowWindow = null;


function tryEndpointNow(endpointPathInSpec) { // method invocation is rendered by Codegen's index.mustache template

    const targetWindowUrl = tryEndpointNowBaseUrl + '.trythisapi#' + endpointPathInSpec;

    const targetWindowName = 'TryItNow';

    const targetWindowFeatures = 'menubar=no,toolbar=no,location=no,status=no,width=1000,height=1000,left=100,top=100';

    if (tryEndpointNowWindow == null || tryEndpointNowWindow.closed) {

        tryEndpointNowWindow = window.open(targetWindowUrl, targetWindowName, targetWindowFeatures)

    } else {

        tryEndpointNowWindow.location = targetWindowUrl;
        tryEndpointNowWindow.location.reload()
        tryEndpointNowWindow.focus();
    }
}

function expandChildren(schemaUUID) {
    const parentRow = document.querySelector(`tr[data-schema-uuid="${schemaUUID}"]`);
    const parentIndentation = parseInt(parentRow.getAttribute('data-indentation'));
    let row = parentRow.nextElementSibling;
    let rowIndentation = parseInt(row.getAttribute('data-indentation'));

    const button = document.querySelector(`a[data-schema-uuid="${schemaUUID}"]`);
    button.onclick = () => collapseChildren(schemaUUID);
    button.classList.replace('nhsd-o-schema__expander', 'nhsd-o-schema__collapser');

    while (row && rowIndentation > parentIndentation) {
        if (rowIndentation === parentIndentation + 1) {
            let classes = row.classList;
            classes.replace('nhsd-o-schema__collapsed', 'nhsd-o-schema__expanded');

            // make sure any nhsd-o-schema__expanded rows containing buttons are set to expand their own children
            const rowId = row.getAttribute('data-schema-uuid')
            const button = row.querySelector(`a.nhsd-o-schema__collapser[data-schema-uuid="${rowId}"]`);
            if (button) {
                button.onclick = () => expandChildren(rowId);
                button.classList.replace('nhsd-o-schema__collapser', 'nhsd-o-schema__expander');
            }
        }

        row = row.nextElementSibling;
        rowIndentation = row && parseInt(row.getAttribute('data-indentation'));
    }
}

function collapseChildren(schemaUUID) {
    const parentRow = document.querySelector(`tr[data-schema-uuid="${schemaUUID}"]`);
    const parentIndentation = parseInt(parentRow.getAttribute('data-indentation'));
    let row = parentRow.nextElementSibling;
    let rowIndentation = parseInt(row.getAttribute('data-indentation'));

    const button = document.querySelector(`a[data-schema-uuid="${schemaUUID}"]`);
    button.onclick = () => expandChildren(schemaUUID);
    button.classList.replace('nhsd-o-schema__collapser', 'nhsd-o-schema__expander');

    while (row && rowIndentation > parentIndentation) {
        let classes = row.classList;
        classes.replace('nhsd-o-schema__expanded', 'nhsd-o-schema__collapsed');

        row = row.nextElementSibling;
        rowIndentation = row && parseInt(row.getAttribute('data-indentation'));
    }
}

function expandAll(tableId) {
    const rows = document.querySelectorAll(`div[data-schema-uuid="${tableId}"] tr`);
    rows.forEach((row) => {
        row.classList.replace('nhsd-o-schema__collapsed', 'nhsd-o-schema__expanded');
    })
    const buttons = document.querySelectorAll(`div[data-schema-uuid="${tableId}"] a.nhsd-o-schema__expander`);
    buttons.forEach((button) => {
        const buttonID = button.getAttribute('data-schema-uuid');
        button.onclick = () => collapseChildren(buttonID);
        button.classList.replace('nhsd-o-schema__expander', 'nhsd-o-schema__collapser');
    })
}

function collapseAll(tableId) {
     const rows = [...document.querySelectorAll(`div[data-schema-uuid="${tableId}"] tr`)];
     rows.filter((row) => row.getAttribute('data-indentation') === '1')
         .filter((row) => row.querySelector('a.nhsd-o-schema__collapser'))
         .forEach((row) => collapseChildren(row.getAttribute('data-schema-uuid')));
}

// collapse all schema on page load

document.querySelectorAll('.nhsd-o-schema').forEach(schema => {
    collapseAll(schema.getAttribute('data-schema-uuid'));
})

// set up listeners for Ctrl + F / Cmd + F
document.addEventListener('keydown', (event) => {
    if(event.key === "f" && (event.metaKey || event.ctrlKey)) {
         const schemas = document.querySelectorAll('.nhsd-o-schema');
         schemas.forEach(schema => {
            expandAll(schema.getAttribute('data-schema-uuid'));
         });
   }
});

// make controls visible
document.querySelectorAll('.nhsd-o-schema__header-button').forEach(element => element.classList.remove('nhsd-!t-display-hide'));
document.querySelectorAll('.nhsd-o-schema__button').forEach(element => element.classList.remove('nhsd-!t-display-hide'));
document.querySelectorAll('.nhsd-api-spec__try-this-api__button button').forEach(element => {
    element.removeAttribute('disabled');
    element.querySelector('.nhsd-a-button__label').textContent = 'Try this API';
    element.querySelector('.nhsd-a-icon').classList.remove('nhsd-!t-display-hide');
});

