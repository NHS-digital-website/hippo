<#ftl output_format="HTML" encoding="UTF-8">

<#--
    //########################################################################
    //  HEADER CONTRIBUTIONS
    //########################################################################
-->

<@hst.headContribution keyHint="formCss">
    <link rel="stylesheet" href="<@hst.webfile path="/css/eforms.css"/>" type="text/css" />
</@hst.headContribution>

<@hst.headContribution keyHint="jquery-datepicker">
    <script src="<@hst.webfile path="/js/eforms/jquery-ui-1.12.1.custom.min.js"/>"></script>
</@hst.headContribution>

<@hst.headContribution keyHint="formJsValidation">
    <script src="<@hst.webfile path="/js/eforms/jquery-validate-1.1.2.min.js"/>"></script>
</@hst.headContribution>

<@hst.headContribution keyHint="formJsValidationHippo">
    <script src="<@hst.webfile path="/js/eforms/jquery-hippo-validate.js"/>"></script>
</@hst.headContribution>
<@hst.headContribution keyHint="formJsReCaptcha">
    <script src='https://www.google.com/recaptcha/api.js'></script>
</@hst.headContribution>

