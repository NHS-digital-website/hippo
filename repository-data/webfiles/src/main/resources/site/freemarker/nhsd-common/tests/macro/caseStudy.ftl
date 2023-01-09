<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#include "../../macro/toolkit/organisms/caseStudy.ftl">

<div class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-6">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col">
             <div data-variant="default">
                <#assign options = {
                    "image": {
                        "bean": imageBean,
                        "alt": "Case study alt text"
                    }
                } />
                <@caseStudy options; imageSection>
                    <div class="nhsd-t-body-s nhsd-!t-font-weight-bold nhsd-!t-margin-bottom-2" data-test-text="intro">Case Stud</div>

                    <h1 class="nhsd-t-heading-xl nhsd-!t-margin-bottom-3" data-test-text="heading">Default Case Study</h1>

                    ${imageSection}

                    <p class="nhsd-t-body" data-test-text="summary">NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning. NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning. NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning. NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning.</p>

                    <a class="nhsd-a-button nhsd-!t-margin-bottom-0" href="https://digital.nhs.uk/blog/data-points-blog">
                        <span class="nhsd-a-button__label" data-test-text="button">Data blog posts</span>
                    </a>
                </@caseStudy>
            </div>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col">
            <div data-variant="mirrored">
                <#assign mirroredOptions = options />
                <#assign mirroredOptions += {
                    "mirrored": true
                } />
                <@caseStudy mirroredOptions; imageSection>
                    <div class="nhsd-t-body-s nhsd-!t-font-weight-bold nhsd-!t-margin-bottom-2" data-test-text="intro">Case Study</div>

                    <h1 class="nhsd-t-heading-xl nhsd-!t-margin-bottom-3" data-test-text="heading">Mirrored Case Study</h1>

                    ${imageSection}

                    <p class="nhsd-t-body" data-test-text="summary">NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning. NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning. NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning. NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre on the systems that allow members of the public to set preferences about the use of their data for research and planning.</p>

                    <a class="nhsd-a-button nhsd-!t-margin-bottom-0" href="https://digital.nhs.uk/blog/data-points-blog">
                        <span class="nhsd-a-button__label" data-test-text="button">Data blog posts</span>
                    </a>
                </@caseStudy>
            </div>
        </div>
    </div>
</div>