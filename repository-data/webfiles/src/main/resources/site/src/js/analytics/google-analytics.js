window.dataLayer = window.dataLayer || [];

window.gtag = function(...args) {
    dataLayer.push(args);
}

gtag('js', new Date());
gtag('config', 'UA-76954916-2', {'anonymize_ip': true});

window.logGoogleAnalyticsEvent = function(action, category, label) {
    gtag('event', action, {
        'event_category': category,
        'event_label': label
    });
}
