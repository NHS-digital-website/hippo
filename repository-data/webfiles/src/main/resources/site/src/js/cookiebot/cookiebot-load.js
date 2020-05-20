window.dataLayer = window.dataLayer || [];

window.CookiebotCallback_OnAccept = function () {
    if (Cookiebot.consent.preferences)
        window.dataLayer.push({'event': 'cookieconsent_preferences'});
    if (Cookiebot.consent.statistics)
        window.dataLayer.push({'event': 'cookieconsent_statistics'});
    if (Cookiebot.consent.marketing)
        window.dataLayer.push({'event': 'cookieconsent_marketing'});

    const youtubeParents = document.getElementsByClassName("iframe-wrapper-no-consent");
    for (let i = 0; i < youtubeParents.length; i++) {
        youtubeParents[i].setAttribute("class", "iframe-wrapper iframe-wrapper-16-9");
    }

}

window.CookiebotCallback_OnDecline = function() {
    const youtubeParents = document.getElementsByClassName("iframe-wrapper");
    for (let i = 0; i < youtubeParents.length; i++) {
        youtubeParents[i].setAttribute("class", "iframe-wrapper-no-consent");
        const consentTexts = document.getElementsByClassName("cookieconsent-optout-marketing");
        if (consentTexts.length > 0) {
            consentTexts[0].innerHTML = "<div>There is a video here, which you can't see because cookies are disabled.</div><div>To view the video, <a href=\"javascript:Cookiebot.renew()\">enable your cookies</a></div>";
        }
    }
}
