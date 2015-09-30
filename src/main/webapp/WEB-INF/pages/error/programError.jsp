<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
    <title>Error</title>
    <LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/appEdit.css">
</head>
<body>
<div class="page-header">
    <img src="${pageContext.request.contextPath}/styles/icons/Black_Duck_Logo.png" alt="Black Duck Logo"  width="200" height="50" id=""/>       	
        <h1><spring:message code="label.error.program.title" text="Error" /></h1>
    </div>
<p class="advice">${message}</p>
<button type="button" class="btn btn-primary" value="cancel" onclick="window.close()"><spring:message code="label.appdetailsedit.exit"/></button>	
</body>
</html>