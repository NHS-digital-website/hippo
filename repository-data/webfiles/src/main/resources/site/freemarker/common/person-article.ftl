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

<#assign notSuppress = !(document.lawfulbasises?has_content && document.lawfulbasises.suppressdata?has_content && suppressdata[document.lawfulbasises.suppressdata] == suppressdata['suppress-data']) />

<article class="article article--publication" itemscope itemtype="http://schema.org/Person">

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

                  <@personrole document.roles idsuffix></@personrole>

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
                                      <a href="${document.socialmedias.linkedinlink}" onClick="logGoogleAnalyticsEvent('Link click','Person','${document.socialmedias.linkedinlink}');" onKeyUp="return vjsu.onKeyUp(event)"  title="${document.socialmedias.linkedinlink}">${document.socialmedias.linkedinlink}</a>
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

                                      <a href="${twitterlink}" onClick="logGoogleAnalyticsEvent('Link click','Person','${twitterlink}');" onKeyUp="return vjsu.onKeyUp(event)"  title="${document.socialmedias.twitteruser}">${document.socialmedias.twitteruser}</a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
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

                      <#if document.relatedNews?has_content >
                            <#assign links += [{ "url": "#related-articles-news-${idsuffix}", "title": 'News articles' }] />
                      </#if>
                      <#if document.relatedEvents?has_content >
                            <#assign links += [{ "url": "#related-articles-events-${idsuffix}", "title": 'Forthcoming events' }] />
                      </#if>
                      <#if document.relatedBlogs?has_content >
                            <#assign links += [{ "url": "#related-articles-blogs-${idsuffix}", "title": 'Blogs' }] />
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
              </#if>

              <@responsibility document.responsibilities idsuffix personName document.manages document.managedby ></@responsibility>

              <#if notSuppress>
                    <@award document.awards idsuffix personName></@award>
                    <@qualification document.qualifications idsuffix personName></@qualification>
                    <#if document.roles?has_content>
                      <@contactdetail document.roles.contactdetails idsuffix personMainNameAndPostnominals></@contactdetail>
                    </#if>
              </#if>

              <@relatedarticles document.relatedNews 'Person' true 'news-' + idsuffix 'News articles'></@relatedarticles>
              <@relatedarticles document.relatedEvents 'Person' true 'events-' + idsuffix 'Forthcoming events'></@relatedarticles>
              <@latestblogs document.relatedBlogs 'blogs-' + idsuffix 'Blogs'></@latestblogs>

            </div>
        </div>

    </div>

</article>
