<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Person" -->

<#include "../include/imports.ftl">
<#include "macro/personimage.ftl">
<#include "macro/personalinfo.ftl">
<#include "macro/role.ftl">
<#include "macro/socialmedia.ftl">
<#include "macro/biography.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/award.ftl">
<#-- BLOGS -->
<#include "macro/relatedarticles.ftl">
<#-- NEWS -->
<#include "macro/latestblogs.ftl">
<#--  MANAGES -->
<#--  MANAGED BY -->
<#include "macro/responsibility.ftl">

<#include "macro/component/downloadBlock.ftl">
<#include "macro/contentPixel.ftl">

<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">

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

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>
   
<article class="article article--person" itemscope itemtype="http://schema.org/Person">
    <#assign heroOptions = getHeroOptions(document) />
    <@hero heroOptions />
    <br/>
    
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

    <div class="nhsd-t-grid">
       <div class="nhsd-t-row">
        	<div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-t-off-s-2 nhsd-t-coll-m-8 nhs-t-off-m-2 nhsd-t-col-l-8 nhsd-t-off-l-2 nhsd-t-col-xl-6 nhsd-t-off-xl-3">
         		<#if notSuppress>
              		<#if document.personimages?has_content>
                		<@personimage document.personimages idsuffix></@personimage>
                  	</#if>

					<#if document.roles?has_content && document.roles.primaryroles?has_content>
						<#list document.roles.primaryroles as role>
							${role}<br/>
						</#list>
					</#if>

					<#if document.socialmedias?has_content>
						<@socialMediaBar 1/>
					</#if>

                  	<#if document.biographies?has_content>
                    	<@biography document.biographies idsuffix/>
                  	</#if>

                  	<#if document.sections?has_content>
                  		<@sections document.sections/>
                  	</#if>

                  	<#if document.awards?has_content>
                  		<@award document.awards "dd" document.personalinfos.firstname/>
                  	</#if>
         		</#if>
         		<br/>
	            <@latestblogs document.relatedBlogs 'Person' 'blogs-' + idsuffix 'Blogs' />
	            <@latestblogs document.relatedNews 'Person' 'news-' + idsuffix 'News articles' />

	            <#if document.responsibilities?has_content >
	            	<@responsibility document.responsibilities idsuffix personName document.manages document.managedby ></@responsibility>
	            </#if>
        	</div>
    	</div>
	</div>
</article>
