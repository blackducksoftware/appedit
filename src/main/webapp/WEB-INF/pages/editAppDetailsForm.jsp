<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.io.*,java.util.*, javax.servlet.*" %>

<html>
<head>
    <title><spring:message code="label.appdetailsedit.title" text="Edit Application Details" /></title>
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
        <h1><spring:message code="label.appdetailsedit.title" text="Edit Application Details" /></h1>
    </div>
    <div class="regular">
		<form:form method="post" action="editappdetails" commandName="app">
			<!-- CSRF token is inserted automatically by form:form tag -->

			<table class="results-table">
				<tr>
					<td><label class=field-label for="app-appName"><spring:message code="label.appdetailsedit.appname" />:</label></td>
					<td>${app.appName}</td>
				</tr>
				
				<c:forEach items="${app.attrNames}" var="attrName" varStatus="x"> 
					<tr>
						<td><label class="field-label">${attrName}:</label></td>
						<td><form:input path="attrValues[${x.index}].value" cssClass="form-control"/></td> 
					</tr>
				</c:forEach>
				
			</table>
			<br/>
			<button type="submit" class="btn btn-primary" name="action"
				value="submit">
				<spring:message code="label.appdetailsedit.submit" />
			</button>
			<button type="button" class="btn btn-primary" value="cancel"
				onclick="doExit()">
				<spring:message code="label.appdetailsedit.cancel" />
			</button>
		</form:form>
	</div>
</body>
</html>