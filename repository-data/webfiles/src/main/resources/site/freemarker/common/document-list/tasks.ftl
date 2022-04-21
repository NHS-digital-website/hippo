<#ftl output_format="HTML">
<#include "../../common/macro/component/actionLink.ftl">
<#include "../../common/macro/svgIcons.ftl">
<#include "../../include/imports.ftl">
<#include "../../common/macro/svgMacro.ftl">

<#-- @ftlvariable name="wrappingDocument" type="uk.nhs.digital.website.beans.Calltoaction" -->
<#-- @ftlvariable name="task" type="uk.nhs.digital.intranet.beans.Task" -->

<div class="intra-box intra-box--nested">

    <#if wrappingDocument.title?? && wrappingDocument.title?has_content>
        <h2 class="intra-box__title"> ${wrappingDocument.title}</h2>
    </#if>

    <#if pageable?? && pageable.items?has_content>
        <ul class="intra-task-grid">
            <#list pageable.items as task>

                <@hst.link hippobean=task.bannercontrols.icon fullyQualified=true var="image" />

                <li class="intra-task-grid-item">
                    <article class="intra-task-grid-task">
                        <div style="position:relative">
                            <@hst.manageContent hippobean=task />
                        </div>
                        <a href="<@hst.link hippobean=task/>"
                           class="intra-task-grid-task__link">
                            <div class="intra-task-grid-task__icon">
                            <#if image?ends_with("svg")>
                                <#if task.title?? && task.title?has_content>
                                    <@svgWithAltText svgString=task.svgXmlFromRepository altText=task.title/>
                                <#else>
                                    <@svgWithoutAltText svgString=task.svgXmlFromRepository/>
                                </#if>
                            </#if>
                            </div>
                            <h1 class="intra-task-grid-task__title">${task.title}</h1>
                        </a>
                    </article>
                </li>
            </#list>
        </ul>
    </#if>

    <#if wrappingDocument.internal?has_content>
        <@hst.link var="link" hippobean=wrappingDocument.internal/>
    <#else>
        <#assign link=wrappingDocument.external/>
    </#if>
    <@actionLink title="${wrappingDocument.getLabel()}" link="${link}" />

</div>
