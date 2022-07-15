/*
 * Simple ajax page loader
 */

export default function (url, elementsToUpdate, data = null) {
    const options = {
        method: 'GET',
    };

    if (data !== null) {
        options.method = 'POST';
        options.body = data;
    }

    return fetch(url, options)
        .then((response) => response.text())
        .then((content) => {
            const doc = new DOMParser().parseFromString(content, 'text/html');

            if (!Array.isArray(elementsToUpdate)) {
                elementsToUpdate = [elementsToUpdate];
            }
            elementsToUpdate.forEach((classToUpdate) => {
                const newContent = doc.querySelector(classToUpdate);
                const contentEl = document.querySelector(classToUpdate);

                if (newContent && contentEl) {
                    contentEl.innerHTML = newContent.innerHTML;
                }
            });

            return Promise.resolve(doc);
        });
}
