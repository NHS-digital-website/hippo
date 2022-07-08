<#ftl output_format="HTML">

<#include "../../macro/card.ftl">

<div class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-6">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6">
            <div id="BasicCard">
                <#assign cardProperties = {
                    "background": "light-grey",
                    "title": "Basic card",
                    "shortsummary": "Our teams design, develop and operate the national IT and data services that support clinicians at work, help patients get the best care, and use data to improve health and care.",
                    "link": "http://digital.nhs.uk/"
                }/>
                <@card cardProperties />
            </div>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6">
            <div id="ButtonCard">
                <#assign cardProperties = {
                    "background": "light-grey",
                    "title": "Button card",
                    "shortsummary": "Our teams design, develop and operate the national IT and data services that support clinicians at work, help patients get the best care, and use data to improve health and care.",
                    "link": "http://digital.nhs.uk/",
                    "button": "Button Text"
                }/>
                <@card cardProperties />
            </div>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6">
            <div id="ImageCard">
                <#assign cardProperties = {
                    "background": "light-grey",
                    "title": "Image card",
                    "link": "http://digital.nhs.uk/",
                    "image": imageBean
                } />
                <@card cardProperties />
            </div>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6">
            <div id="AuthorCard">
                <#assign cardProperties = {
                    "background": "light-grey",
                    "title": "Author card",
                    "link": "http://digital.nhs.uk/",
                    "image": imageBean,
                    "authorsInfo": [{
                        "name": "Lee Jacobson",
                        "image": imageBean,
                        "role": "Developer",
                        "org": "NHS Digital"
                    }]
                } />
                <@card cardProperties />
            </div>
        </div>
    </div>
</div>
