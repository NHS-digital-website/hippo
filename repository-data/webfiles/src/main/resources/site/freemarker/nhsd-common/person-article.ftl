<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Person" -->

<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/postnominal.ftl">
<#include "../nhsd-common/macro/qualification.ftl">
<#include "../nhsd-common/macro/award.ftl">
<#include "../nhsd-common/macro/contactdetail.ftl">
<#include "../nhsd-common/macro/socialmedia.ftl">
<#include "../nhsd-common/macro/biography.ftl">
<#include "../nhsd-common/macro/responsibility.ftl">
<#include "../nhsd-common/macro/personalinfo.ftl">
<#include "../nhsd-common/macro/lawfulbasis.ftl">
<#include "../nhsd-common/macro/personimage.ftl">
<#include "../nhsd-common/macro/role.ftl">
<#include "../nhsd-common/macro/stickyNavSections.ftl">
<#include "../nhsd-common/macro/relatedarticles.ftl">
<#include "../nhsd-common/macro/latestblogs.ftl">
<#include "../nhsd-common/macro/component/downloadBlock.ftl">
<#include "../nhsd-common/macro/contentPixel.ftl">

<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">

<#-- Add meta tags -->
<#include "../nhsd-common/macro/metaTags.ftl">
<@metaTags></@metaTags>


<#assign personName  = document.personalinfos.preferredname?has_content?then(document.personalinfos.preferredname, document.personalinfos.firstname) />
<#assign personMainName  = document.personalinfos.preferredname?has_content?then(document.personalinfos.preferredname, document.personalinfos.firstname + " " + document.personalinfos.lastname) />
<#assign postnominals = "" />
<#assign personMainNameAndPostnominals = personMainName />
<#assign renderNav = document.biographies?has_content || document.roles?has_content || document.responsibilities?has_content  />
<#assign idsuffix = slugify(personMainName) />
<#assign hasBusinessUnits = document.businessUnits?? && document.businessUnits?has_content/>

<#assign notSuppress = !(document.lawfulbasises?has_content && document.lawfulbasises.suppressdata?has_content && suppressdata[document.lawfulbasises.suppressdata] == suppressdata['suppress-data']) />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article>
    <#assign heroOptions = getHeroOptions(document) />
    <#assign metadata = []/>

    <#if document.socialmedias?? && document.socialmedias.othersocialmedias?has_content>
        <#list document.socialmedias.othersocialmedias as medium>
                            <#assign metadata += [{
                                "title": "${medium.title}",
                                "value": "<a itemprop=\"sameAs\"
                               href=\"${medium.link}\"
                               onClick=\"logGoogleAnalyticsEvent('Link click','Person','${medium.link}');\"
                               onKeyUp=\"return vjsu.onKeyUp(event)\"
                               title=\"${medium.title}\">${medium.link}</a>"
                            }] />

        </#list>
    </#if>


    <#if document.socialmedias?? && document.socialmedias.linkedinlink?has_content>
        <#assign linkedInUrl =
        "<a itemprop=\"sameAs\"
               href=\"${document.socialmedias.linkedinlink}\"
                           onClick=\"logGoogleAnalyticsEvent('Link click','Person','${document.socialmedias.linkedinlink}');\"
                           onKeyUp=\"return vjsu.onKeyUp(event)\"
                           title=\"${document.socialmedias.linkedinlink}\">LinkedIn profile</a>"/>
        <#assign metadata += [{
        "title": "LinkedIn",
        "value": linkedInUrl
        }] />

    </#if>

    <#if document.socialmedias?? && document.socialmedias.twitteruser?has_content>
        <#assign twitteruser = document.socialmedias.twitteruser />
        <#if twitteruser?substring(0, 1) == "@">
            <#assign twitteruser = document.socialmedias.twitteruser?substring(1) />
        </#if>
        <#assign twitterlink = "https://twitter.com/" + twitteruser />
        <#assign twitterHashTags =
        "<a itemprop=\"sameAs\"
                           href=\"${twitterlink}\"
                           onClick=\"logGoogleAnalyticsEvent('Link click','Person','${twitterlink}');\"
                           onKeyUp=\"return vjsu.onKeyUp(event)\"
                           title=\"${document.socialmedias.twitteruser}\"
                           target=\"_blank\">@${twitteruser}</a>"/>
        <#assign metadata += [{
        "title": "Twitter",
        "value": twitterHashTags
        }] />

    </#if>

    <#assign heroOptions += {
        "metaData": metadata
    }/>
    <@hero heroOptions />


    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <#if renderNav>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4">
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

            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">

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
