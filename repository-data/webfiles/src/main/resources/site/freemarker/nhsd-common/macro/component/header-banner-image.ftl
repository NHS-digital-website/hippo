<#ftl output_format="HTML">

<#macro headerBannerImage banner bannerImage="" >

<#assign content="">
<#if banner.content??>
    <#assign content = banner.content>
<#elseif banner.summary??>
    <#assign content = banner.summary>
</#if>

<#if banner.title??>
    <#assign title = banner.title>
</#if>

<div class="nhsd-o-hero nhsd-!t-bg-grad-black nhsd-!t-col-white nhsd-o-hero--image-banner nhsd-!t-margin-bottom-6">
    <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
        <div class="nhsd-t-row ">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6">
                <div class="nhsd-o-hero__content-box">
                    <div class="nhsd-o-hero__content"><span class="nhsd-t-heading-xl nhsd-!t-col-white">${title}</span>
                        <p class="nhsd-t-body" data-uipath="document.summary"><@hst.html hippohtml=content contentRewriter=stripTagsContentRewriter/></p>
                    </div>
                </div>
            </div>
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-!t-no-gutters">
                <figure class="nhsd-a-image nhsd-a-image--contain" aria-hidden="true">
                    <picture class="nhsd-a-image__picture ">
                        <img src="${bannerImage}" alt="Abstract lights">
                    </picture>
                </figure>
            </div>
        </div>
    </div>
</div>
</#macro>
