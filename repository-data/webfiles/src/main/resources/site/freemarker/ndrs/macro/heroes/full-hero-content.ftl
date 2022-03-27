<#ftl output_format="HTML">

<#include "./simple-hero-content.ftl">
<#include "./quote-hero-content.ftl">

<#macro fullHeroContent options>
    <#assign uiPath = options.uiPath?has_content?then(options.uiPath, "website.hero") />
    <#if options.introTags?has_content>
        <div class="nhsd-!t-margin-bottom-4">
            <#list options.introTags as introTag>
                <span class="nhsd-a-tag nhsd-a-tag--meta nhsd-a-tag--meta-light nhsd-!t-bg-blue nhsd-!t-col-white">${introTag}</span>
            </#list>
        </div>
    </#if>
    <#if options.quote?has_content>
       <@quoteHeroContent options/>
    </#if>
    <@simpleHeroContent options>
        <#nested />
        <#if options["metaData"]?has_content && options["metaData"]?size gt 0>
            <div class="nhsd-!t-margin-top-6">
                <dl class="nhsd-o-hero__meta-data">
                    <#list options["metaData"] as metaDataItem>
                        <#assign schemaOrgTagAttr = data.schemaOrgTag?has_content?then('itemprop=${metaDataItem.schemaOrgTag}','') />
                        <div class="nhsd-o-hero__meta-data-item">
                            <dt class="nhsd-o-hero__meta-data-item-title">${metaDataItem.title?ends_with(":")?then(metaDataItem.title, metaDataItem.title + ":")}</dt>
                            <dd class="nhsd-o-hero__meta-data-item-description" ${schemaOrgTagAttr} data-uipath="${uiPath}.${slugify(metaDataItem["title"]?trim?replace(':', ''))}">
                                <#if metaDataItem.value?is_sequence>
                                    <#list metaDataItem.value as val>${val?no_esc}<#sep>, </#list>
                                <#else>
                                    ${metaDataItem.value?no_esc}
                                </#if>
                            </dd>
                        </div>
                    </#list>
                </dl>
            </div>
        </#if>
    </@simpleHeroContent>
</#macro>

