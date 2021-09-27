<#ftl output_format="HTML">

<#macro modal id='default-modal' options = {}>
    <div class="nhsd-m-modal ${options.open?then('nhsd-m-modal--open', '')}" id="${id}" ${options.mandatory?then('data-modal-mandatory', '')}>
        <div role="dialog" class="nhsd-m-modal__container" aria-labelledby="default_dialog_label" aria-modal="true">
            <div class="nhsd-a-box nhsd-!t-padding-6">
                <#nested />
            </div>
        </div>
    </div>
</#macro>
