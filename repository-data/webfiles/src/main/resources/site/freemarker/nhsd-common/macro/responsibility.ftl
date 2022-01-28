<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "./card.ftl">
<#include "personitem.ftl">

  <#macro responsibility resp idsuffix firstname manages="" managedby="">
    <#if
     resp?? && resp?has_content && (resp.responsible?has_content || resp.responsibleforservice?has_content) ||
     manages?has_content ||
     managedby?has_content
    >
      <div id="responsibility-${slugify(idsuffix)}">
        <h2 class="nhsd-t-heading-xl">Responsibilities</h2>

        <#if resp.responsible?has_content>
          <p class="nhsd-t-body" data-uipath="person.responsibilities">
              ${firstname} has responsibility for:
                <#list resp.responsible as responsibility>
                <#assign cardProperties = {
                  "background": "light-grey",
				  "content": responsibility,
				  "date": document.lastModified?date,
				  "bullets": true
                  }/>
                  <@card cardProperties/>
                  <br/>
                </#list>
          </p>
        </#if>

        <#if resp.responsibleforservice?has_content>
          <p class="nhsd-t-body" data-uipath="person.responsibleforservice">
              ${firstname} is responsible for:
                <#list resp.responsibleforservice as service>
                  <@hst.link hippobean=service var="link" />
	              <#assign cardProperties = {
	                  "background": "light-grey",
					  "content": service.title,
					  "link": link,
					  "date": service.lastModified?date
	              }/>
                  <@card cardProperties/>
                  <br/>
                </#list>
          </p>
        </#if>

        <#if manages?has_content || managedby?has_content>
            <div class="nhsd-a-box--border-blue" style="padding: 10px;">
        </#if>
        <#if manages?has_content>
          <p class="nhsd-t-body" data-uipath="person.manages">
              ${firstname} manages:
                <#list manages as managee>
                  <#assign pinfo = managee.personalinfos />
                  <#assign displayName = pinfo.preferredname?has_content?then(pinfo.preferredname, pinfo.firstname + " " + pinfo.lastname) />
                  <#assign personrole = ''>
                  <#if managee.roles?has_content && managee.roles.primaryroles?has_content>
                    <#assign personrole = managee.roles.firstprimaryrole />
                  </#if>
                  <@personitem managee/>
                </#list>
          </p>
        </#if>

        <#if managedby?has_content>
          <p class="nhsd-t-body" data-uipath="person.managedby">
              <#assign pinfo = managedby.personalinfos />
              <#assign displayName = pinfo.preferredname?has_content?then(pinfo.preferredname, pinfo.firstname + " " + pinfo.lastname) />
              <#assign personrole = ''>
              <#if managedby.roles?has_content && managedby.roles.primaryroles?has_content>
                <#assign personrole = ', ' + managedby.roles.firstprimaryrole />
              </#if>
              ${firstname} is managed by:
              <@personitem managedby/>
          </p>
         </#if>
         <#if manages?has_content || managedby?has_content>
            </div>
        </#if>
      </div>
    </#if>
  </#macro>
