<#ftl output_format="HTML">
<#-- @ftlvariable name="dateFm" type="uk.nhs.digital.website.beans.EffectiveDate" -->

<#include "../../include/imports.ftl">

<#assign inArray="uk.nhs.digital.freemarker.utils.InArray"?new() />

<#macro roadmapItemBox options>
    <#if options??>
        <div>
            <#if options.category?has_content>
                <div class="nhsd-!t-margin-bottom-1">
                    <span class="nhsd-a-tag nhsd-a-tag--bg-light-grey">${options.category}</span>
                </div>
            </#if>

            <h3 class="nhsd-t-heading-s">
                <#if options.link??>
                    <a href="${options.link}">
                </#if>
                <span data-uipath="website.roadmap.${options.title}">${options.title}</span>
                <#if options.link??>
                    </a>
                </#if>
            </h3>

            <#if options.text??>
                <p>${options.text}</p>
            </#if>

            <#if options.showDate>
                <p><time datetime="${options.datetime}">${options.datelabel}</time></p>
            </#if>

            <#if options.status??>
                <div class="nhsd-t-flex">
                    <span class="nhsd-a-icon nhsd-a-icon--size-l">
                        <#if inArray(options.status?lower_case, ['complete'])>
                            <img src="<@hst.webfile path="/images/icon/Icon-tick-v3_v2.svg"/>"
                                 alt="Tick" />
                        <#elseif inArray(options.status?lower_case, ['cancelled', 'superseded']) >
                            <img src="<@hst.webfile path="/images/icon/Icon-cross-v2_v2.svg"/>"
                                 alt="Cross" />
                        </#if>
                    </span>
                    <b class="nhsd-!t-font-weight-bold">${options.status?cap_first}</b>
                </div>
            </#if>
        </div>
    </#if>
</#macro>
