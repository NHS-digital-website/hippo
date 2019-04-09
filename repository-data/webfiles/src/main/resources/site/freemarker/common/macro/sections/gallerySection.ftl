<#ftl output_format="HTML">

<#include "../fileIcon.ftl">

<#macro gallerySection section>
    <div class="article-section">

        <#if section.headingLevel == 'Main heading'>
            <h2 data-uipath="website.contentblock.gallerysection.title">${section.heading}</h2>
        <#else>
            <h3 data-uipath="website.contentblock.gallerysection.title">${section.heading}</h3>
        </#if>

        <div data-uipath="website.contentblock.gallerysection.description">
            <@hst.html hippohtml=section.description contentRewriter=gaContentRewriter />
        </div>

        <div class="grid-row galleryItems">
            <#list section.galleryItems as galleryItem>
                <div class="column column--one-half galleryItems__item">
                    <h3 class="galleryItems__heading">${galleryItem.title}</h3>

                    <div class="galleryItems__card">
                        <#if galleryItem.imageWarning != ''>
                            <div class="galleryItems__warning">${galleryItem.imageWarning}</div>
                        </#if>

                        <@hst.link hippobean=galleryItem.image.original fullyQualified=true var="image" />

                        <img src="${image}" alt="${galleryItem.imageAlt}" />

                        <div class="galleryItems__description">
                            <@hst.html hippohtml=galleryItem.description contentRewriter=gaContentRewriter />
                        </div>

                    </div>

                    <ol class="list list--reset">
                        <#list galleryItem.relatedFiles as attachment>
                            <li class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                                <a href="<@hst.link hippobean=attachment.link />" class="block-link" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">
                                    <div class="block-link__header">
                                        <@fileIcon attachment.link.asset.mimeType></@fileIcon>
                                    </div>
                                    <div class="block-link__body">
                                        <span class="block-link__title">${attachment.title}</span>
                                        <@fileMetaAppendix attachment.link.asset.getLength(), attachment.link.asset.mimeType></@fileMetaAppendix>
                                    </div>
                                </a>
                            </li>
                        </#list>
                    </ol>

                </div>
                <#if galleryItem?is_even_item>
                    <div class="clearfix"></div>
                </#if>
            </#list>
        </div>
    </div>
</#macro>
