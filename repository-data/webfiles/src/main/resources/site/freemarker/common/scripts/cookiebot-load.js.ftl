<#ftl output_format="HTML">

<script id="Cookiebot" src="https://consent.cookiebot.com/uc.js" data-cbid="8a16fbff-6ab2-4087-ae02-65267c376ba1" data-blockingmode="auto"></script>
<script>
    window.dataLayer = window.dataLayer || [];
    
    function CookiebotCallback_OnAccept() {
        if (Cookiebot.consent.preferences)
            window.dataLayer.push({'event':'cookieconsent_preferences'});
        if (Cookiebot.consent.statistics)
            window.dataLayer.push({'event':'cookieconsent_statistics'});
        if (Cookiebot.consent.marketing)
            window.dataLayer.push({'event':'cookieconsent_marketing'});

        var youtubeParents = document.getElementsByClassName("iframe-wrapper-no-consent");
        for(var i = 0; i < youtubeParents.length; i++){
            youtubeParents[i].setAttribute("class", "iframe-wrapper iframe-wrapper-16-9");
        }

    }

    function CookiebotCallback_OnDecline() {
        var youtubeParents = document.getElementsByClassName("iframe-wrapper");
        for(var i = 0; i < youtubeParents.length; i++){
            youtubeParents[i].setAttribute("class", "iframe-wrapper-no-consent");
            var consentTexts = document.getElementsByClassName("cookieconsent-optout-marketing");
            if (consentTexts.length > 0){
              consentTexts[0].innerHTML = "<div>There is a video here, which you can't see because cookies are disabled.</div><div>To view the video, <a href=\"javascript:Cookiebot.renew()\">enable your cookies</a></div>";
            }
        }
    }
</script>
