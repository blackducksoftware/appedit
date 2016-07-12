<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title><spring:message code="label.result.title" text="Updated Application Details" /></title>
    <LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/appEdit.css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script type="text/javascript">
    function doExit() {
		console.log("doExit()");
		
		$.ajax({
			type : "GET",
			url : "logout"
		});
		window.close();
	}
    </script>
</head>
<body>
	<div class="page-header">
    <img src="${pageContext.request.contextPath}/styles/icons/Black_Duck_Logo.png" alt="Black Duck Logo"  width="200" height="50" id=""/>       	
        <h1><spring:message code="label.result.title" text="Updated Application Details" /></h1>
    </div>
    <div class="regular">
   <table class="results-table">
    <tr>
        <td><label class=field-label><spring:message code="label.result.applicationName" text="Application Name" />:</label></td>
        <td>${app.appName}</td>
    </tr>
    
    <c:forEach items="${app.attrNames}" var="attrName" varStatus="x"> 
		<tr>
			<td><label class="field-label">${attrName}:</label></td>
			<td>${app.attrValues[x.index].value}</td> 
		</tr>
	</c:forEach>
				
</table> 
</div> 
<p class="advice"><spring:message code="label.result.advice"/></p>
<button type="button" class="btn btn-primary" value="cancel" onclick="doExit()"><spring:message code="label.result.exit"/></button>
<br/>
<br/>

</body>
</html>