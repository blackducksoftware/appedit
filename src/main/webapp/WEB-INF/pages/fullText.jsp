<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title><spring:message code="label.fulltext.title" text="Full Text" /></title>
    <LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/appEdit.css">
</head>
<body>
	<div class="page-header">
    <img src="${pageContext.request.contextPath}/styles/icons/Black_Duck_Logo.png" alt="Black Duck Logo"  width="200" height="50" id=""/>       	
        <h1><spring:message code="label.fulltext.remediationcomment.title" text="Full Remediation Comment" /></h1>
        <h4><b><spring:message code="label.fulltext.app.name" text="Application name" />:</b> ${fullTextViewData.applicationName}</h4>
        <h4><b><spring:message code="label.fulltext.app.version" text="Application name" />:</b> ${fullTextViewData.applicationVersion}</h4>
        <h4><b><spring:message code="label.fulltext.comp.name" text="Component name" />:</b> ${fullTextViewData.componentName}</h4>
        <h4><b><spring:message code="label.fulltext.comp.version" text="Component version" />:</b> ${fullTextViewData.componentVersion}</h4>
        <h4><b><spring:message code="label.fulltext.vuln.name" text="Vulnerability name" />:</b> ${fullTextViewData.vulnerabilityName}</h4>
        <p class="has-error">${message}</p>
        <br />
    </div>

    <div class="regular">
    <p><b><spring:message code="label.fulltext.remediationcomment" text="Remediation Comment" />:</b> ${fullTextViewData.fullText}</p>
	</div> 
	
<p class="advice"><spring:message code="label.result.advice"/></p>
<button type="button" class="btn btn-primary" value="cancel" onclick="window.close()"><spring:message code="label.result.exit"/></button>
<br/>
<br/>

</body>
</html>