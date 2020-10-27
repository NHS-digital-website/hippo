export function initStickyNav() {
    let options = {
        root: document.main,
        rootMargin: '5% 0% -85% 0%',
        threshold: '1.0'
    }
    let markers = document.querySelectorAll(".navigationMarker")

    document.addEventListener("scroll", function () {
        const observer = new IntersectionObserver(callback, options);
        markers.forEach(target => {
            observer.observe(target);
        });
    });

    const callback = function (entries) {
        let getCurrentNavMarkers = document.querySelectorAll(".navMarker")
        if (getCurrentNavMarkers !== null) {
            var setCurrentNavMarkers = Array.from(getCurrentNavMarkers)
        }

        entries.forEach(entry => {
            if (entry.isIntersecting && entry.intersectionRatio === 1) {
                setCurrentNavMarkers.forEach(currentMarker => {
                    if (currentMarker.id !== document.getElementById('#' + entry.target.id + "_list")) {
                        if (document.getElementById(currentMarker.id) !== null) {
                            document.getElementById(currentMarker.id).setAttribute('class', 'nonNavMarker');
                        }
                    }
                });

                if (document.getElementById('#' + entry.target.id + "_list") !== null) {
                    document.getElementById('#' + entry.target.id + "_list").setAttribute('class', 'navMarker');
                }
            }
        });
    }
}
