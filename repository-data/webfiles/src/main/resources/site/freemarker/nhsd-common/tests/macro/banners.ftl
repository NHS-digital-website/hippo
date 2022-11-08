<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">
<#include "../../macro/toolkit/organisms/banner.ftl">

<@hst.link hippobean=imageBean.newsPostImageLarge fullyQualified=true var="leadImage" />
<#assign imageBannerOptions = {
    "title": "Image banner",
    "summary": "NHS Digital collects data from GP practices to help support care and research. Find out about our new way of collecting this data, and what your choices are.",
    "image": {
        "src": leadImage,
        "alt": "Image banner image"
    },
    "colour": "Blue grey",
    "buttons": [{
        "text": "Call to action",
        "src": "#"
    }]
} />

<div class="nhsd-!t-margin-top-6">
    <div id="ImageBanner" data-variant="image banner">
        <@nhsdBanner imageBannerOptions />
    </div>
</div>
<div class="nhsd-!t-margin-top-6">
    <div id="ImageBannerMirrored" data-variant="image banner mirrored">
        <#assign imageBannerMirroredOptions = imageBannerOptions />
        <#assign imageBannerMirroredOptions += {
            "title": "Image banner mirrored",
            "image": {
                "src": leadImage,
                "alt": "Mirrored image banner image"
            }
        } />
        <@nhsdBanner imageBannerMirroredOptions true />
    </div>
</div>
<div class="nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-6">
    <div id="VideoBanner" data-variant="video banner" data-youtube="video banner">
        <#assign videoBannerOptions = imageBannerOptions />
        <#assign videoBannerOptions += {
            "title": "Video banner",
            "image": "",
            "video": {
                "src": "https://www.youtube.com/embed/PRVd30lUeAw?enablejsapi=1"
            }
        } />
        <@nhsdBanner videoBannerOptions />
    </div>
</div>
