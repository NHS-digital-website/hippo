<#ftl output_format="HTML">

<#include "../fileIconByMimeType.ftl">
<#include "../component/downloadBlockAsset.ftl">

<#macro gallerySection section mainHeadingLevel=2 orgPrompt=false>
    <div id="${slugify(section.heading)}">
      <#if section.headingLevel?has_content && section.headingLevel == 'Main heading'>
        <h2 data-uipath="website.contentblock.gallerysection.title" class="nhsd-t-heading-xl">${section.title}</h2>
      <#else>
        <h3 data-uipath="website.contentblock.gallerysection.title" class="nhsd-t-heading-l">${section.title}</h3>
      </#if>

      <div data-uipath="website.contentblock.gallerysection.description">
        <@hst.html hippohtml=section.description contentRewriter=brContentRewriter />
      </div>
    </div>

    <div class="nhsd-o-gallery">
      <div class="nhsd-t-grid nhsd-t-grid--nested">
        <div class="nhsd-t-row nhsd-o-gallery__items">

          <#list section.galleryItems as galleryItem>
            <div class="nhsd-t-col-xs-12
              <#if section.galleryItems?size gt 1>
                nhsd-t-col-s-6
              </#if>
              <#if section.galleryItems?size gt 2>
                nhsd-t-col-l-4
              </#if>">
              <div class="nhsd-o-gallery__card-container">
                <article class="nhsd-m-card">
                  <div class="nhsd-a-box nhsd-a-box--border-grey">

                    <@hst.link hippobean=galleryItem.image.original fullyQualified=true var="newimage" />
                    <div class="nhsd-m-card__image_container">
                      <figure class="nhsd-a-image nhsd-a-image--round-top-corners nhsd-a-image--maintain-ratio">
                        <picture class="nhsd-a-image__picture ">
                          <img src="${newimage}" alt="${galleryItem.imageAlt}">
                        </picture>
                      </figure>
                    </div>

                    <div class="nhsd-m-card__content_container">
                      <div class="nhsd-m-card__content-box">
                        <#if galleryItem.title?has_content>
                          <h1 class="nhsd-t-heading-s">${galleryItem.title}</h1>
                        </#if>
                        <#if galleryItem.description.content?has_content>
                          <@hst.html hippohtml=galleryItem.description contentRewriter=brContentRewriter />
                        </#if>
                      </div>

                      <#if galleryItem.relatedFiles?has_content>
                          <#list galleryItem.relatedFiles as attachment>
                            <div class="nhsd-m-card__download-card nhsd-!t-margin-bottom-6">
                              <#if attachment.link.asset?has_content>
                                <@downloadBlockAsset document.class.name attachment.link "${attachment.title}" "" attachment.link.asset.mimeType attachment.link.asset.getLength() false true orgPrompt=orgPrompt attachment.link.archiveMaterial/>
                              <#else>
                                <@downloadBlockAsset document.class.name attachment.link "${attachment.title}" "" attachment.link.original.mimeType attachment.link.original.getLength() false true orgPrompt=orgPrompt/>
                              </#if>
                            </div>
                          </#list>
                      </#if>
                    </div>
                  </div>
                </article>
              </div>
            </div>
          </#list>

        </div>
      </div>
    </div>
</#macro>
