<#ftl output_format="HTML">

<#macro gallerySection section>
    ${section.heading}
    ${section.headingLevel}
    <@hst.html hippohtml=section.description contentRewriter=gaContentRewriter />

    <#list section.galleryImages as galleryImage>
        ${galleryImage.title}
        ${galleryImage.imageWarning}
        <@hst.link hippobean=galleryImage.image.original fullyQualified=true var="image" />
        <img src="${image}" alt="${galleryImage.title}" />
        <@hst.html hippohtml=galleryImage.description contentRewriter=gaContentRewriter />

        <#list galleryImage.filePicker as file>
            <a class="cta__title cta__button" href="<@hst.link hippobean=file.link/>" title="${file.filename}" data-uipath="ps.search-results.result.title">
                ${file.filename}
            </a>
        </#list>


    </#list>


</#macro>
