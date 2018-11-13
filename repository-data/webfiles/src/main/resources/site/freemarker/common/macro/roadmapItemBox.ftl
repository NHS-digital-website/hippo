<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro roadmapItemBox options>
    <#if options??>
        <article class="roadmapitem-box ${(options.light??)?then('hub-box--light', '')}">

            <div class="hub-box__contents">
                <#if options.title??>
                    <h3 class="hub-box__title">
                        <#if options.link??>
                            <a href="${options.link}">
                        </#if>
                        ${options.title}
                        <#if options.link??>
                            </a>
                        </#if>
                    </h3>
                </#if>

                <#if options.text??>
                    <p class="hub-box__text">${options.text}</p>
                </#if>

                <table>
                    <tbody>
                        <#if options.status??>
                            <tr>
                                <td class="strong">Status</td>
                                <td><span class="hub-box__meta">${options.status}</span></td>
                            </tr>
                        </#if>

                        <#if options.date??>
                            <tr>
                                <td class="strong">Effective</td>
                                <td><span class="hub-box__meta">${options.date}</span></td>
                            </tr>
                        </#if>

                        <#if options.standards??>
                            <tr>
                                <td class="strong">Standards</td>
                                <td>
                                    <#list options.standards as standard>
                                        <span class="hub-box__meta"><a href="${standard.webLink}">${standard.referenceNumber}</a></span><#sep>,
                                    </#list>
                                </td>
                            </tr>
                        </#if>
                    </tbody>
                </table>

                <#if options.markers??>
                    <ul class="tag-list">
                        <#list options.markers as marker>
                            <li class="tag">${marker}</li>
                        </#list>
                    </ul>
                </#if>
            </div>
        </article>
    </#if>
</#macro>
