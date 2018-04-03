<#ftl output_format="HTML">
<#macro narrowArticleSections sections>
<#if sections?has_content>
<#list sections as section>
    <div class="grid-row">
        <div class="column column--reset">
            <div id="section-${section?index+1}" class="article-section">
                <div class="grid-row">
                    <div class="column column--two-thirds column--reset">
                        <h2>${section.title}</h2>
                        <@hst.html hippohtml=section.html/>  
                    </div>
                </div>
            </div>  
        </div>
    </div>
</#list>
</#if>
</#macro>