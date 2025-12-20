<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.TargetedBanner" -->

<#assign bgColour = '#a0daf2' />
<#assign textColor = '#000000' />


<#if banner??>
    <div  id="banner-selected" class="nhsd-!t-bg nhsd-!t-colour" style="--bg-colour: ${bgColour}; --content-colour: ${textColor}; --link-colour: ${textColor}">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col nhsd-!t-padding-top-6 nhsd-!t-padding-bottom-6"><@hst.html hippohtml=banner.contentBanner contentRewriter=gaContentRewriter/></div>
            </div>
        </div>
    </div>
</#if>