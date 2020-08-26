import {
	ResizeSensor
}
from "css-element-queries";

export function initScrollSpy() {

	// lets make sure the page loads as the offsettop might not be accurate
	window.addEventListener("DOMContentLoaded", function() {
    var section = document.querySelectorAll(".article-section:not(.article-section--introduction):not(.article-section--highlighted):not(.article-section--summary), .article-section-separator, .article-section--summary-separator, .sticky-nav-spy-item");
    var sections = {};
    var i = 0;

		Array.prototype.forEach.call(section, function(e) {
			sections[e.id] = e.offsetTop;
		});

        document.addEventListener("click", function() {
            var navLinks = document.querySelectorAll(".article-section-nav__list a");
            var j;
            for (j = 0; j < navLinks.length; j++) {
                if (navLinks[j] == event.target) {
                    navLinks[j].classList.add("active");
                } else {
                    navLinks[j].classList.remove("active");
                }
            }
        });

		document.addEventListener("wheel", function() {
			var scrollPosition = document.documentElement.scrollTop || document.body.scrollTop;

			for (i in sections) {
				if (sections[i] <= scrollPosition) {
					if (document.querySelector('.active') !== null) {
						document.querySelector('.active').setAttribute('class', ' ');
					}

					if (document.querySelector('a[href\*=' + i + ']') !== null) {
						document.querySelector('a[href\*=' + i + ']').setAttribute('class', 'active');
					}
				}
			}
		});
	});
}
