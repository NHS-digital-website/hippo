<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="systems-and-services.title" var="title"/>
<@fmt.message key="systems-and-services.text" var="text"/>
<@fmt.message key="systems-and-services.subTitle" var="subTitle"/>
<@fmt.message key="systems-and-services.buttonLabel" var="buttonLabel"/>

<#-- @ftlvariable name="item" type="uk.nhs.digital.website.beans.Service" -->
<div class="article-section">
    <h2 class="article-section__title">${title}</h2>
    <p>${text}</p>

    <div class="article-section__box article-section__box--services">
        <div class="grid-row">
            <div class="column">
                <h3>${subTitle}</h3>
            </div>
        </div>
          <#if pageable?? && pageable.items?has_content>
            <ol class="grid-row list-of-articles list-of-articles--2-columns">
              <#list pageable.items as item>
                <li class="column column--one-half">
                    <article>
                        <h2><a href="<@hst.link hippobean=item/>">${item.title}</a></h2>
                        <p>${item.shortsummary}</p>
                    </article>
                </li>
              </#list>
            </ol>
          </#if>
    </div>

    <div class="grid-row">
        <a href="#" class="button">${buttonLabel}</a>
    </div>
</div>
