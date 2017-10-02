<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<html>
<head>
</head>
<body>
<h1 id="header">Publication:</h1>
<#if document??>
    <h1 id="title">${document.title?html}</h1>
    <h1 id="summary">${document.summary?html}</h1>
<#else>
      <h1>No publication</h1>
</#if>
</body>
</html>
