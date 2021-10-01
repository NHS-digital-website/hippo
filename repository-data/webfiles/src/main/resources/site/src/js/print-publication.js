const printPreviewSelector = 'js-print-preview';

function printPDF() {

    // Log the event
    if (typeof logGoogleAnalyticsEvent === "function") {
        logGoogleAnalyticsEvent('Button click','Download as PDF', window.location.href);
    }

    // Create iframe to build printable document
    let printPreview = document.querySelector(`.${printPreviewSelector}`);
    if (printPreview) printPreview.remove();

    printPreview = document.createElement("iframe");
    printPreview.classList.add(printPreviewSelector);
    printPreview.setAttribute('style', 'display: none;');
    printPreview.setAttribute('src', window.location.href);
    document.querySelector('body').appendChild(printPreview);

    new Promise(res => {
        // The iframe seems to initalise with a readyState == 'complete'
        // before changing to 'loading'. A timeout fixes this.
        setTimeout(() => {
            if (printPreview.contentWindow.document.readyState == 'complete') return res(true);
            printPreview.contentWindow.document.addEventListener('DOMContentLoaded', () => res(true));
        }, 500);
    }).then(() => {
        // Get other publication pages
        const printArticle = Array.from(document.querySelectorAll('[data-print-article]'));

        const articleRequests = [];
        printArticle.forEach(url => {

            // Create handle in the DOM that matches the order of Array
            let identity = window.crypto.getRandomValues(new Uint32Array(1))[0].toString(16);
            let handle = document.createElement('div');
            handle.setAttribute('id', identity);
            printPreview.contentWindow.document.body.appendChild(handle);

            // Download and append publication to iframe
            const articleRequest = fetch(url)
                .then(res => res.text())
                .then(content => {
                    const printableDoc = new DOMParser().parseFromString(content, "text/html");
                    const mainContent = printableDoc.querySelector('.js-publication-body');
                    printPreview.contentWindow.document.getElementById(identity).appendChild(mainContent);
                });
            articleRequests.push(articleRequest);
        });

        // When all publications have been downloaded open print dialog
        Promise.all(articleRequests).then(() => printPreview.contentWindow.print());
    });
}

// Bind print pdf button
let printPDFButtons = Array.from(document.querySelectorAll('.js-print-pdf-button'));
printPDFButtons.forEach(printPDFButton => printPDFButton.addEventListener('click', printPDF));
