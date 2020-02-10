<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Person" -->

<#include "../include/imports.ftl">
<#include "macro/postnominal.ftl">
<#include "macro/qualification.ftl">
<#include "macro/award.ftl">
<#include "macro/contactdetail.ftl">
<#include "macro/socialmedia.ftl">
<#include "macro/biography.ftl">
<#include "macro/responsibility.ftl">
<#include "macro/personalinfo.ftl">
<#include "macro/lawfulbasis.ftl">
<#include "macro/personimage.ftl">
<#include "macro/role.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/relatedarticles.ftl">
<#include "macro/latestblogs.ftl">
<#include "macro/component/downloadBlock.ftl">

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign personName  = document.personalinfos.preferredname?has_content?then(document.personalinfos.preferredname, document.personalinfos.firstname) />
<#assign personMainName  = document.personalinfos.preferredname?has_content?then(document.personalinfos.preferredname, document.personalinfos.firstname + " " + document.personalinfos.lastname) />
<#assign postnominals = "" />
<#assign personMainNameAndPostnominals = personMainName />
<#assign renderNav = document.biographies?has_content || document.roles?has_content || document.responsibilities?has_content  />
<#assign idsuffix = slugify(personMainName) />
<#assign hasBusinessUnits = document.businessUnits?? && document.businessUnits?has_content/>

<#assign notSuppress = !(document.lawfulbasises?has_content && document.lawfulbasises.suppressdata?has_content && suppressdata[document.lawfulbasises.suppressdata] == suppressdata['suppress-data']) />

<article class="article article--person" itemscope itemtype="http://schema.org/Person">

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">

        <#if notSuppress>
          <#if document.postnominals?? && document.postnominals?has_content>
              <#list document.postnominals as postnominal>
                <#if postnominal.letters == "PhD">
                  <#assign personMainName = "Dr " + personMainName />
                </#if>
                <#assign postnominals += postnominal.letters />
                <#if postnominal?has_next>
                  <#assign postnominals += ", " />
                </#if>
                <#assign personMainNameAndPostnominals = personMainName + " (" + postnominals + ")"/>
              </#list>
          </#if>
        </#if>

        <div class="grid-row">
            <div class="column column--reset">
                <div class="local-header article-header article-header--detailed">
                <div class="grid-wrapper">
                <div class="article-wrapper__inner">
                  <h1 id="top" class="local-header__title" data-uipath="document.personalinfos.name"><span itemprop="name">${personMainName}</span></h1>

                  <#if document.roles?has_content>
                      <@personrole document.roles idsuffix></@personrole>
                  </#if>

                  <#if notSuppress>
                    <#if postnominals?has_content>
                        <div class="article-header__label">
                          ${postnominals}
                        </div>
                    </#if>

                  <div class="detail-list-grid">
                      <#if document.socialmedias?? && document.socialmedias.linkedinlink?has_content>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key" id="linkedin-${slugify(idsuffix)}">Linkedin: </dt>
                                    <dd class="detail-list__value" data-uipath="person.socialmedia.linkedinlink">
                                      <a itemprop="sameAs" href="${document.socialmedias.linkedinlink}" onClick="logGoogleAnalyticsEvent('Link click','Person','${document.socialmedias.linkedinlink}');" onKeyUp="return vjsu.onKeyUp(event)"  title="${document.socialmedias.linkedinlink}">LinkedIn profile</a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                      </#if>

                      <#if document.socialmedias?? && document.socialmedias.twitteruser?has_content>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key" id="twitter-${slugify(idsuffix)}">Twitter: </dt>
                                    <dd class="detail-list__value" data-uipath="person.socialmedia.twitteruser">

                                      <#assign twitteruser = document.socialmedias.twitteruser />
                                      <#if twitteruser?substring(0, 1) == "@">
                                        <#assign twitteruser = document.socialmedias.twitteruser?substring(1) />
                                      </#if>
                                      <#assign twitterlink = "https://twitter.com/" + twitteruser />

                                      <a itemprop="sameAs" href="${twitterlink}" onClick="logGoogleAnalyticsEvent('Link click','Person','${twitterlink}');" onKeyUp="return vjsu.onKeyUp(event)"  title="${document.socialmedias.twitteruser}" target="_blank">@${twitteruser}</a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                      </#if>
                      <#if document.socialmedias?? && document.socialmedias.othersocialmedias?has_content>
                        <#list document.socialmedias.othersocialmedias as medium>
                          <div class="grid-row">
                              <div class="column column--reset">
                                  <dl class="detail-list">
                                    <dt class="detail-list__key" >${medium.title}: </dt>
                                    <dd class="detail-list__value" data-uipath="person.socialmedia.${slugify(medium.title)}">
                                        <a itemprop="sameAs" href="${medium.link}" onClick="logGoogleAnalyticsEvent('Link click','Person','${medium.link}');" onKeyUp="return vjsu.onKeyUp(event)"  title="${medium.title}">${medium.link}</a>
                                      </dd>
                                  </dl>
                              </div>
                          </div>
                        </#list>
                      </#if>

                    </div>
                    </#if>
                  </div>
                  </div>
                </div>
            </div>
        </div>
        </div>

        <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <#if renderNav>
              <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                  <!-- start sticky-nav -->
                  <div id="sticky-nav">
                      <#assign links = [{ "url": "#top", "title": 'Top of page' }] />

                      <#if notSuppress>
                          <#if document.biographies?has_content && document.biographies.profbiography.content?has_content >
                            <#assign links += [{ "url": "#biography-${idsuffix}", "title": 'Biography' }] />
                          </#if>
                          <#if hasBusinessUnits>
                            <#assign links += [{ "url": "#businessunits-${idsuffix}", "title": 'Responsible for' }] />
                          </#if>
                      </#if>

                      <#if
                       document.responsibilities?has_content && (document.responsibilities.responsible?has_content || document.responsibilities.responsibleforservice?has_content) ||
                       document.manages?has_content ||
                       document.managedby?has_content
                      >
                        <#assign links += [{ "url": "#responsibility-${idsuffix}", "title": 'Responsibilities' }] />
                      </#if>

                      <#if notSuppress>
                          <#if document.awards?has_content>
                            <#assign links += [{ "url": "#award-${idsuffix}", "title": 'Awards' }] />
                          </#if>

                          <#if document.qualifications?has_content>
                            <#assign links += [{ "url": "#qualification-${idsuffix}", "title": 'Qualifications' }] />
                          </#if>

                          <#if document.roles?has_content && document.roles.contactdetails?has_content && (document.roles.contactdetails.emailaddress?has_content || document.roles.contactdetails.phonenumber?has_content) >
                            <#assign links += [{ "url": "#contactdetail-${idsuffix}", "title": 'Contact details' }] />
                          </#if>
                      </#if>

                      <#if document.relatedBlogs?has_content >
                            <#assign links += [{ "url": "#related-articles-blogs-${idsuffix}", "title": 'Blogs' }] />
                      </#if>
                      <#if document.relatedNews?has_content >
                            <#assign links += [{ "url": "#related-articles-news-${idsuffix}", "title": 'News articles' }] />
                      </#if>
                      <#if document.relatedEvents?has_content >
                            <#assign links += [{ "url": "#related-articles-events-${idsuffix}", "title": 'Forthcoming events' }] />
                      </#if>


                      <@stickyNavSections links=links></@stickyNavSections>
                  </div>
                  <!-- end sticky-nav -->
              </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

              <#if notSuppress>
                  <#if document.personimages?has_content>
                    <@personimage document.personimages idsuffix></@personimage>
                  </#if>
                  <#if document.biographies?has_content>
                    <@biography document.biographies idsuffix></@biography>
                  </#if>

                  <#if document.businessUnits?has_content>
                    <div id="businessunits-${idsuffix}" class="article-section">
                        <h2>Responsible for</h2>
                        <#list document.businessUnits as businessunit>
                          <div>
                            <@downloadBlock businessunit />
                          </div>
                        </#list>
                    </div>
                  </#if>
              </#if>

              <#if document.responsibilities?has_content >
                  <@responsibility document.responsibilities idsuffix personName document.manages document.managedby ></@responsibility>
              </#if>

              <#if notSuppress>
                    <@award document.awards idsuffix personName></@award>
                    <@qualification document.qualifications idsuffix personName></@qualification>
                    <#if document.roles?has_content && document.roles.contactdetails?has_content>
                      <@contactdetail document.roles.contactdetails idsuffix personMainNameAndPostnominals></@contactdetail>
                    </#if>
              </#if>

              <@latestblogs document.relatedBlogs 'Person' 'blogs-' + idsuffix 'Blogs' />
              <@latestblogs document.relatedNews 'Person' 'news-' + idsuffix 'News articles' />
              <@latestblogs document.relatedEvents 'Person' 'events-' + idsuffix 'Forthcoming events' />

            </div>
        </div>

    </div>

</article>
