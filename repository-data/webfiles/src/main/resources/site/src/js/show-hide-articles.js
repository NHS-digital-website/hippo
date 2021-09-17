hideArticles();

function hideArticles() {
    const showAllBtns = document.querySelectorAll('.js-show-all-articles-btn');
    showAllBtns.forEach(showAllBtn => showAllBtn.classList.remove('nhsd-!t-display-hide'));

    const showLessBtns = document.querySelectorAll('.js-show-less-articles-btn');
    showLessBtns.forEach(showLessBtn => showLessBtn.classList.add('nhsd-!t-display-hide'));

    const articlesToHide = document.querySelectorAll('.js-hide-article');
    articlesToHide.forEach(article => article.classList.add('nhsd-!t-display-hide'));
}

function showArticles() {
    const showAllBtns = document.querySelectorAll('.js-show-all-articles-btn');
    showAllBtns.forEach(showAllBtn => showAllBtn.classList.add('nhsd-!t-display-hide'));

    const showLessBtns = document.querySelectorAll('.js-show-less-articles-btn');
    showLessBtns.forEach(showLessBtn => showLessBtn.classList.remove('nhsd-!t-display-hide'));

    const articlesToHide = document.querySelectorAll('.js-hide-article');
    articlesToHide.forEach(article => article.classList.remove('nhsd-!t-display-hide'));
}

const showAllBtns = document.querySelectorAll('.js-show-less-articles-btn');
showAllBtns.forEach(showAllBtn => showAllBtn.addEventListener('click', hideArticles));
const showLessBtns = document.querySelectorAll('.js-show-all-articles-btn');
showLessBtns.forEach(showLessBtn => showLessBtn.addEventListener('click', showArticles));
