<#ftl output_format="HTML">

<#macro gallerySection section>

    <#-- TODO
    CREATE FRONTEND DISPLAY.
    SYNTAX FOR ACCESSING THE OBJECT PROPERTIES BELOW
    -->

    ${section.heading}
    ${section.headingLevel}
    <@hst.html hippohtml=section.description contentRewriter=gaContentRewriter />

    <#list section.galleryItems as galleryItem>
        ${galleryItem.title}
        ${galleryItem.imageWarning}
        <@hst.link hippobean=galleryItem.image.original fullyQualified=true var="image" />
        <img src="${image}" alt="${galleryItem.title}" />
        <@hst.html hippohtml=galleryItem.description contentRewriter=gaContentRewriter />

        <#list galleryItem.relatedFiles as file>
            <a class="cta__title cta__button" href="<@hst.link hippobean=file.link/>" title="${file.filename}" data-uipath="ps.search-results.result.title">
                ${file.filename}
            </a>
        </#list>


    </#list>


</#macro>
