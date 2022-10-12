<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">
<#include "../../macro/sections/gallerySection.ftl">
<#include "../../macro/component/downloadBlockAsset.ftl">

<#-- image bean -->
<@hst.link hippobean=imageBean.newsPostImageLarge fullyQualified=true var="newimage" />
<#-- end image bean -->

<#-- image only-->
<#assign
list_io = [
{ "title": "", "image": "newimage", "imageAlt": "Abstract lights", "description": ""},
{ "title": "", "image": "newimage", "imageAlt": "Abstract lights", "description": ""}
]
gallerySections_io = {
"heading": "Section heading only",
"description": "Section description only",
"galleryItems": list_io
}
/>
<div class="nhsd-!t-margin-top-6">
    <div id="gOio" data-variant="gallery organism image only">
        <@gallerySection gallerySections_io/>
    </div>
</div>
<#-- end image only-->

<#-- title only-->
<#assign
list_to = [
{ "title": "About NHS Digital", "image": "newimage", "imageAlt": "Abstract lights", "description": ""},
{ "title": "About NHS Digital", "image": "newimage", "imageAlt": "Abstract lights", "description": ""}
]
gallerySections_to = {
"heading": "Section heading only",
"description": "Section description only",
"galleryItems": list_to
}
/>
<div class="nhsd-!t-margin-top-6">
    <div id="gOto" data-variant="gallery organism title only">
        <@gallerySection gallerySections_to/>
    </div>
</div>
<#-- end title only-->

<#-- description only-->
<#assign
list_do = [
{ "title": "", "image": "newimage", "imageAlt": "Abstract lights", "description": "#AT_ONLY Items can include a description if needed, like this. AT_ONLY#"},
{ "title": "", "image": "newimage", "imageAlt": "Abstract lights", "description": "#AT_ONLY Items can include a description if needed, like this. AT_ONLY#"}
]
gallerySections_do = {
"heading": "Section heading only",
"description": "Section description only",
"galleryItems": list_do
}
/>
<div class="nhsd-!t-margin-top-6">
    <div id="gOdo" data-variant="gallery organism description only">
        <@gallerySection gallerySections_do/>
    </div>
</div>
<#-- end description only-->

<#-- download card only-->
<#assign
list_relfiles = [
{
"class": "General",
"link": "http://localhost:8080/site/binaries/content/assets/test-resources/nhsdigitalaboutus.pdf",
"title": "#AT_ONLY NHS Digital About Us AT_ONLY#",
"mimeType": "application/pdf",
"length": "818.97"
}]

list_dloado = [
{ "title": "", "image": "newimage", "imageAlt": "Abstract lights", "description": "", "relatedFiles": list_relfiles},
{ "title": "", "image": "newimage", "imageAlt": "Abstract lights", "description": "", "relatedFiles": list_relfiles}
]

gallerySections_dloado = {
"heading": "Section heading only",
"description": "Section description only",
"galleryItems": list_dloado
}
/>
<div class="nhsd-!t-margin-top-6">
    <div id="gOdloado" data-variant="gallery organism download card only">
        <@gallerySection gallerySections_dloado/>
    </div>
</div>
<#-- end download card only-->





