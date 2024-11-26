const nhsdHiddenClass = 'nhsd-!t-display-hide';

function showFilterSubsections(element) {
    [...element.parentElement.children]
        .forEach((sibling) => {
            sibling.classList.remove(nhsdHiddenClass);
            [...sibling.children]
                .forEach((siblingChild) => siblingChild.classList.remove(nhsdHiddenClass));
        });
}

function hide(element) {
    element.classList.add(nhsdHiddenClass);
}

document.querySelectorAll('.show-more').forEach((element) => element.addEventListener('click', () => {
    showFilterSubsections(element);
    hide(element);
}));
