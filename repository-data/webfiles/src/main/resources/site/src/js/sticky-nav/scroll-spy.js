import {
	ResizeSensor
}
from "css-element-queries";

export function initScrollSpy() {

	// lets make sure the page loads as the offsettop might not be accurate
	window.addEventListener("DOMContentLoaded", function() {
		var section = document.querySelectorAll(".article-section-nav, .article-section, .article-section-separator, .article-section--summary-separator, .sticky-nav-spy-item");
		var sections = {};
		var i = 0;

		Array.prototype.forEach.call(section, function(e) {
			sections[e.id] = e.offsetTop;
		});

		document.addEventListener("scroll", function() {
				var scrollPosition = document.documentElement.scrollTop || document.body.scrollTop;

				for (i in sections) {
					if (sections[i] <= scrollPosition) {
						if (document.querySelector('.active') !== null) {
							document.querySelector('.active').setAttribute('class', ' ');
						}

						if (document.querySelector('a[href*=' + i + ']') !== null) {
							document.querySelector('a[href*=' + i + ']').setAttribute('class', 'active');
						}
					}
				}
			});
		}
	);
}
