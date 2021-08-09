<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro headerMetadata metadataSet doctype>
    <#if metadataSet?? && metadataSet?has_content >

        <#list metadataSet as data>

            <#assign uipath = data.uipath?has_content?then('data-uipath=${data.uipath}','') />
            <#assign itempropProp = data.schemaOrgTag?has_content?then('itemprop=${data.schemaOrgTag}','') />

            <div class="nhsd-o-hero__meta-data-item">
                <div class="nhsd-o-hero__meta-data-item-title">${data.key}:</div>
                <div class="nhsd-o-hero__meta-data-item-description" ${uipath}>
                    <#if data.type?? >
                        <#if data.type == "date" >
                            <#assign datePattern = (doctype=="cyberalert")?then('d MMMM yyyy h:mm a','d MMMM yyyy')/>
                            <span ${itempropProp}></span>
                        <#elseif data.type == "list" >
                            <span ${itempropProp}><#list data.value as tag>${tag}<#sep>, </#list></span>
                        <#elseif data.type == "link" >
                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, data.value.title) />
                            <a class="nhsd-a-link nhsd-a-link--col-white" href="<@hst.link hippobean=data.value/>" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)"><span ${itempropProp}>${data.value.title}</span></a>
                        <#elseif data.type == "twitterHashtag">
                            <#list data.value as oldtag>
                                <#if ! oldtag?starts_with("#")>
                                    <#assign tag = "#" + oldtag>
                                <#else>
                                    <#assign tag = oldtag>
                                </#if>
                                <a class="nhsd-a-link nhsd-a-link--col-white" href="https://twitter.com/search?q=${tag?replace('#','%23')}" target="blank_">${tag}</a><#sep>,
                            </#list>
                        <#else>
                            <span ${itempropProp}>${data.value}</span>
                        </#if>
                    <#else>
                        <span ${itempropProp}>${data.value}</span>
                    </#if>
                </div>
            </div>
        </#list>
      </#if>
</#macro>
