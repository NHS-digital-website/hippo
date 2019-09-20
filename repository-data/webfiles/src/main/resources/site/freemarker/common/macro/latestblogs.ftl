<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro latestblogs blogs idsuffix='latest-blogs' title="Latest blogs">

  <#assign hasLatestBlogs = blogs?has_content />
  <#if hasLatestBlogs>
  <div id="related-articles-${slugify(idsuffix)}" class="latestBlog article-section related-articles--div">
        <h2>${title}</h2>

          <#list blogs as latest>
              <div class="latestBlog__item">
                  <div class="latestBlog__icon">
                      <#if latest.leadImage??>
                          <@hst.link hippobean=latest.leadImage.original fullyQualified=true var="leadImage" />
                          <img class="latestBlog__icon__img" src="${leadImage}" alt="<#if hasLeadImageAltText>${latest.leadImageAltText}</#if>" />
                      <#else>
                          <img class="latestBlog__icon__nhsimg" src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital blog" >
                      </#if>
                  </div>

                  <div class="latestBlog__content">
                      <@hst.link hippobean=latest var="link"/>
                      <div class="latestBlog__title"><a class="cta__title cta__button" href="${link}" onClick="logGoogleAnalyticsEvent('Link click','Blog','${link}');" onKeyUp="return vjsu.onKeyUp(event)" title="${latest.title}">${latest.title}</a></div>
                          <div class="latestBlog__author">
                              <#if latest.authors?? && latest.authors?has_content>
                                  By <#list latest.authors as author>${author.title}<#sep>, </#list>.
                              </#if>
                              <@fmt.formatDate value=latest.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                          </div>
                      <div class="latestBlog__body">${latest.shortsummary}</div>
                  </div>
              </div>
          </#list>
      </div>
  </#if>
</#macro>
