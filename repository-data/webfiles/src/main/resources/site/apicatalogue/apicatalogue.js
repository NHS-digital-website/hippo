const nhsdHiddenClass = 'nhsd-!t-display-hide';

function hide(element) {
    element.classList.add(nhsdHiddenClass);
}

function showFilterSubsections(element) {
    [...element.parentElement.children]
        .forEach((sibling) => {
            sibling.classList.remove(nhsdHiddenClass);
            [...sibling.children]
                .forEach((siblingChild) => siblingChild.classList.remove(nhsdHiddenClass));
        });
}

document.querySelectorAll('#show-more').forEach((element) => element.addEventListener('click', () => {
    showFilterSubsections(element);
    hide(element);
}));
