let resizeTimer;

function checkHero(hero, index) {
    const heading = hero.querySelector('h1.nhsd-t-heading-xxl');
    if (!heading) return;

    const headingId = `hero-title-${index}`;

    let btn = hero.querySelector('.nhsd-hero-title-btn-show-more');

    const isExpanded = heading.classList.contains('expanded');

    if (isExpanded) heading.classList.remove('expanded');
    const clamped = heading.scrollHeight > heading.clientHeight;
    if (isExpanded) heading.classList.add('expanded');

    if (!clamped && !isExpanded) {
        if (btn) btn.classList.remove('visible');
        return;
    }

    if (clamped && !btn) {
        // First time clamped — create the button
        btn = document.createElement('button');
        btn.classList.add('nhsd-hero-title-btn-show-more');
        btn.setAttribute('aria-expanded', 'false');
        btn.setAttribute('aria-controls', headingId);
        btn.textContent = 'Show full title';

        // Create wrapper for header and button
        heading.setAttribute('id', headingId);
        const wrapper = document.createElement('div');
        wrapper.classList.add('nhsd-t-heading-wrapper');
        heading.parentElement.insertBefore(wrapper, heading);
        wrapper.appendChild(heading);
        wrapper.appendChild(btn);

        btn.addEventListener('click', () => {
            const expanded = btn.getAttribute('aria-expanded') === 'true';

            if (expanded) {
                heading.classList.remove('expanded');
                btn.setAttribute('aria-expanded', 'false');
                btn.textContent = 'Show full title';
                heading.focus();
            } else {
                heading.classList.add('expanded');
                btn.setAttribute('aria-expanded', 'true');
                btn.textContent = 'Show less';
                heading.setAttribute('tabindex', '-1');
                heading.focus();
            }
        });
    }

    btn.setAttribute('aria-expanded', String(isExpanded));
    btn.textContent = isExpanded ? 'Show less' : 'Show full title';
    btn.classList.add('visible');
}

export default function initHeroTitles() {
    const heroes = document.querySelectorAll('.nhsd-o-hero');
    if (!heroes.length) return;

    heroes.forEach((hero, index) => checkHero(hero, index));

    window.addEventListener('resize', () => {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(() => {
            heroes.forEach((hero, index) => checkHero(hero, index));
        }, 150);
    });
}
