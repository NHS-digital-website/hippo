<#ftl output_format="HTML">

<#macro skipLink anchorTo="main-content" label="main content">
            <a href="#${anchorTo}" class="nhsd-a-skip-link">
            <div class="nhsd-t-grid nhsd-t-grid--full-width">
                <div class="nhsd-t-row">
                <div class="nhsd-t-col nhsd-t!t-no-gutters">
                    <div class="nhsd-t-grid">
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col">
                        <span class="nhsd-a-skip-link__label">Skip to ${label}</span>
                        </div>
                    </div>
                    </div>
                </div>
                </div>
            </div>
        </a>
</#macro>
