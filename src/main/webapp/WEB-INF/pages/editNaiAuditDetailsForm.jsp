<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title><spring:message code="label.naiauditdetailsedit.title" text="Edit NAI Audit Details" /></title>
    <LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/appEdit.css">
</head>
<body>
	<div class="page-header">
    <img src="${pageContext.request.contextPath}/styles/icons/Black_Duck_Logo.png" alt="Black Duck Logo"  width="200" height="50" id=""/>       	
        <h1><spring:message code="label.naiauditdetailsedit.title" text="Edit NAI Audit Details" /></h1>
    </div>
    <div class="regular">


		<form:form method="post" action="editnaiauditdetails" commandName="app">
			<!-- CSRF token is inserted automatically by form:form tag -->
			<button type="submit" class="btn btn-primary" name="action"
				value="submit">
				<spring:message code="label.naiauditdetailsedit.submit" />
			</button>
		</form:form>
	 
</div> 
<p class="advice"><spring:message code="label.naiauditdetailsedit.advice"/></p>
<button type="button" class="btn btn-primary" value="cancel" onclick="window.close()"><spring:message code="label.naiauditdetailsedit.exit"/></button>
<br/>
<br/>

</body>
</html>