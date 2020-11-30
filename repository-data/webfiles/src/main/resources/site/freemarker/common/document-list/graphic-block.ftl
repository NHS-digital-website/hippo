<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<div class="nhsd-row nhsd-u-text-centre" style="background-color: transparent; margin: 20px 0">
    <#if pageable?? && pageable.items?has_content>
        <#list pageable.items as item>
            <div class="nhsd-col-des-3 nhsd-col-tab-3 nhsd-col-mob-1">
                <div class="nhsd-a-box">
                    <#if item.infographics.icon??>
                        <@hst.link hippobean=item.infographics.icon fullyQualified=true var="graphicImage" />
                        <picture class="nhsd-a-picture nhsd-a-picture--round-corners">
                            <img src="${graphicImage}" alt="2 scientists testing in a laboratory">
                        </picture>
                    </#if>

                    <#if item.infographics??>
                        <h2>${item.infographics.heading}</h2>
                    </#if>

                    <#if item.title??>
                        <p class="nhsd-u-fw-bold">${item.title}.</p>
                    </#if>
                    <p><@hst.html hippohtml=item.infographics.qualifyingInformation contentRewriter=gaContentRewriter/></p>
                </div>
            </div>
        </#list>
        <div class="nhsd-col-12">
            <hr class="nhsd-a-rule" />
        </div>
    </#if>
</div>
