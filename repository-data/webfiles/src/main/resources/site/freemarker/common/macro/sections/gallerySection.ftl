<#ftl output_format="HTML">

<#macro gallerySection section>
    ${section.heading}
    ${section.headingLevel}

    <@hst.html hippohtml=section.description contentRewriter=gaContentRewriter />

    <div class="grid-row galleryItems">
        <#list section.galleryItems as galleryItem>
            <div class="column column--one-half galleryItems__item">
                <h5 class="galleryItems__heading">${galleryItem.title}</h5>

                <div class="galleryItems__card">
                    <#if galleryItem.imageWarning != ''>
                        <div class="galleryItems__warning">${galleryItem.imageWarning}</div>
                    </#if>

                    <@hst.link hippobean=galleryItem.image.original fullyQualified=true var="image" />

                    <img src="${image}" alt="${galleryItem.title}" />

                    <div class="galleryItems__description">
                        <@hst.html hippohtml=galleryItem.description contentRewriter=gaContentRewriter />
                    </div>

                    <#list galleryItem.relatedFiles as file>
                        <a 
                            class="cta__title cta__button" 
                            href="<@hst.link hippobean=file.link/>" 
                            title="${file.filename}" 
                            data-uipath="ps.search-results.result.title"
                        >
                            ${file.filename}
                        </a>
                    </#list>
                </div>
            </div>
        </#list>
    </div>
</#macro>