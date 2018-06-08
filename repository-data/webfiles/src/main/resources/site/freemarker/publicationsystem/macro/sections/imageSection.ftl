<#ftl output_format="HTML">

<#macro imageSection section>
    <div data-uipath="ps.publication.image-section">
        <#if section.link?has_content><a href="${section.link}" data-uipath="ps.publication.image-section.link"></#if>
            <figure>
                <img src="<@hst.link hippobean=section.image/>"
                     alt="${section.altText}"
                     data-uipath="ps.publication.image-section.image"
                     <#if section.size??>style="width:100%;"</#if>
                />
                <#if section.caption?has_content>
                    <figcaption data-uipath="ps.publication.image-section.caption">
                        ${section.caption}
                    </figcaption>
                </#if>
            </figure>
        <#if section.link?has_content></a></#if>
    </div>
</#macro>
