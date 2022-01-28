<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "./responsiveImage.ftl">

  <#macro qualification quals idsuffix firstname>
    <#if quals?has_content>
    <div id="qualification-${slugify(idsuffix)}">
      <h2 class="nhsd-t-heading-xl">Qualifications</h2>
      <#if quals?has_content >
        <p class="nhsd-t-body">${firstname} is qualified for:</p>
          <#list quals as qual>
            <div class="nhsd-!t-bg-pale-grey-40-tint nhsd-!t-padding-4 nhsd-!t-margin-bottom-4 nhsd-t-round">
            <dl class="nhsd-a-summary-list">
            <div class="nhsd-a-summary-list__item">
              <dt>Qualification</dt>
              <dd>
                <span class="nhsd-a-summary-list__text">
                <#assign qualnameDisplay = qual.qualname />
                <#if qual.quallink?has_content>
                  <a href="${qual.quallink.link}" class="nhsd-a-link" onClick="logGoogleAnalyticsEvent('Link click','Person','${qual.quallink.link}');" onKeyUp="return vjsu.onKeyUp(event)" title="${qualnameDisplay}">${qualnameDisplay}</a>
                <#else>
                  ${qualnameDisplay}
                </#if>
                </span>
              </dd>
            </div>
              <#if qual.qualawardingbody?has_content>
                <div class="nhsd-a-summary-list__item">
                  <dt>Awarding Body</dt>
                  <dd>
                    <span class="nhsd-a-summary-list__text">
                    <#if qual.qualawardingbodylink?has_content>
                      <a href="${qual.qualawardingbodylink.link}" class="nhsd-a-link" onClick="logGoogleAnalyticsEvent('Link click','Person','${qual.qualawardingbodylink.link}');" onKeyUp="return vjsu.onKeyUp(event)"  title="${qual.qualawardingbody}">${qual.qualawardingbody}</a>
                    <#else>
                      ${qual.qualawardingbody}
                    </#if>
                    </span>
                  </dd>
                </div>
              </#if>
              <#if qual.qualificationlogo?has_content>
                <div class="nhsd-a-summary-list__item">
                  <dt>Qualification Logo</dt>
                  <dd>
                    <span class="nhsd-a-summary-list__text">
                        <@responsiveImage qual.qualificationlogo {'alt': qual.qualname} />
                    </span>
                  </dd>
                </div>
              </#if>
            </dl>
            </div>
          </#list>
      </#if>
    </div>
    </#if>
  </#macro>
