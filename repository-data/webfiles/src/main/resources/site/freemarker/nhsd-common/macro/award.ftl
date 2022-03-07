<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "./responsiveImage.ftl">

<#macro award awards idsuffix firstname>
    <#if awards?has_content>
        <div id="award-${slugify(idsuffix)}">
          <h2 class="nhsd-t-heading-xl">Awards</h2>
          <#if awards?has_content>
              <p class="nhsd-t-body">${firstname} has won awards for:</p>
              <#list awards as personaward>
                  <div class="nhsd-!t-bg-pale-grey-40-tint nhsd-!t-padding-4 nhsd-!t-margin-bottom-4 nhsd-t-round">
                    <dl class="nhsd-a-summary-list">
                        <div class="nhsd-a-summary-list__item">
                            <dt>Award</dt>
                            <dd>
                              <span class="nhsd-a-summary-list__text">
                              <#assign awardnameDisplay = personaward.awardname />
                              <#if personaward.awardlink?has_content>
                                <a href="${personaward.awardlink.link}" class="nhsd-a-link" onClick="logGoogleAnalyticsEvent('Link click','Person','${personaward.awardlink.link}');" onKeyUp="return vjsu.onKeyUp(event)"><span itemprop="award">${awardnameDisplay}</span></a>
                              <#else>
                                ${awardnameDisplay}
                              </#if>
                              </span>
                            </dd>
                        </div>

                        <#if personaward.awardingbody?has_content>
                            <div class="nhsd-a-summary-list__item">
                              <dt>Awarding Body</dt>
                              <dd>
                                <span class="nhsd-a-summary-list__text">
                                <#if personaward.awardingbodylink?has_content>
                                  <a href="${personaward.awardingbodylink.link}" class="nhsd-a-link" onClick="logGoogleAnalyticsEvent('Link click','Person','${personaward.awardingbodylink.link}');" onKeyUp="return vjsu.onKeyUp(event)">${personaward.awardingbody}</a>
                                <#else>
                                  <span class="nhsd-t-heading-xs nhsd-!t-margin-0 nhsd-!t-margin-top-4">${personaward.awardingbody}</span>
                                </#if>
                                </span>
                              </dd>
                            </div>
                        </#if>

                        <#if personaward.awardpicture?has_content>
                            <div class="nhsd-a-summary-list__item">
                              <dt>Award Image</dt>
                              <dd>
                                <span class="nhsd-a-summary-list__text">
                                  <@responsiveImage personaward.awardpicture {'alt': personaward.awardname} />
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
