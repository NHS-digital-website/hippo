<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Expander" -->

<#macro expander section>
    <div class="article-section-with-no-heading expander ${(section.audience == "Most people")?then("expander-most", "expander-some")}">
        <details>
            <summary>
                <span>
                    ${section.heading}
                </span>
            </summary>
            <div class="details-body">
                <@hst.html hippohtml=section.content />
            </div>
        </details>
    </div>
</#macro>
