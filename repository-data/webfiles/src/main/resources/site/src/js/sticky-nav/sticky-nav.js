import "css-element-queries/src/ResizeSensor";
import StickySidebar from "sticky-sidebar";

import {
	initScrollSpy
}
from "./scroll-spy";

const stickyNav = document.querySelector('#sticky-nav');
const chapterNav = document.querySelector('.chapter-pagination-wrapper');

function getStickyOffset() {
	const CHAPTER_NAV_SPACING = 24;
	const SIDEBAR_SPACING = 20;

	return chapterNav ? (chapterNav.offsetHeight + (CHAPTER_NAV_SPACING + SIDEBAR_SPACING)) : SIDEBAR_SPACING;
}

export function initStickyNav() {
	if (stickyNav) {
		initScrollSpy();

		const sidebar = new StickySidebar('#sticky-nav', {
			topSpacing: getStickyOffset(),
			bottomSpacing: 40,
			resizeSensor: true,
			containerSelector: '.grid-row'
		});

		$(window).on('load resize', function() {
			if (window.matchMedia('(max-width: 924px)').matches) {
				sidebar.destroy();
			} else {
				sidebar.initialize();
			}
		});


		$('.article-section-nav__list a').click(function() {
			const match = jQuery(this).attr('href').match(/#\S+/);
			const url = match ? window.location.pathname + match[0] : window.location.pathname;
			logGoogleAnalyticsEvent('Link click', 'Section Nav', url);
		});
	}
}
