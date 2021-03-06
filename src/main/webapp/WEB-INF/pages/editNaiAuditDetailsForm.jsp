<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title><spring:message code="label.naiauditdetailsedit.title" text="Edit NAI Audit Details" /></title>
    <LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/appEdit.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/u/dt/jq-2.2.3,dt-1.10.12,b-1.2.1,b-colvis-1.2.1,cr-1.3.2,se-1.2.0/datatables.min.css"/>
    <script type="text/javascript" src="https://cdn.datatables.net/u/dt/jq-2.2.3,dt-1.10.12,b-1.2.1,b-colvis-1.2.1,cr-1.3.2,se-1.2.0/datatables.min.js"></script>
	    
	<!-- jquery ui, for tooltips -->
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  	<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	<script>
  		$(function() {
    		$( document ).tooltip();
  		});
  	</script>
  
  	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	
	<script type="text/javascript" class="init">
	
	var documentCheckboxes; // This is no good after initial page load
	var clonedCheckboxes = [];
	var tableGlobal;
	var defaultStatusValue;
	
	$(document).ready(function() {
		console.log("==================================================");
		console.log("Document ready")
		console.log("---------------------------------");

		documentCheckboxes = document.getElementsByClassName("rowCheckbox");
		
		for (var i=0; i < documentCheckboxes.length; i++) {
			var rowCheckbox = documentCheckboxes.item(i); 
			var cboxClone = rowCheckbox.cloneNode(true);
			clonedCheckboxes[i] = cboxClone;
		}

		unCheckAllRows();
		
	    // Setup - add a text input to each header cell
	    var colIndex=0;
	    $('#table_id tfoot th').each( function () {
			console.log("Column sort field initialization")
	        var title = $(this).text();
	        if (title.length > 0) {
	        	$(this).html( '<input type="text" placeholder="Search '+title+'" />' );
	        }
	        
	    } );
	    
	    var displayedRowCount = <c:out value="${selectedVulnerabilities.displayedRowCount}"/>
	    var firstRowIndex = <c:out value="${selectedVulnerabilities.firstRowIndex}"/>
	 
	    // DataTable
	    tableGlobal = $('#table_id').DataTable( {
	    	select: true,
	    	dom: '<"filter"f><"pagination"p>t<"info"i>r<"pageLen"l>',
	    	"lengthMenu": [ 5, 10, 25, 50, 75, 100 ],
	    	"displayStart": firstRowIndex,
	    	"pageLength": displayedRowCount
	    } );

	    // Apply the search
	    tableGlobal.columns().every( function () {
			console.log("Column sort keyup initialization")
	        var that = this;
	        if (this.index() == 0) {
	        	return;
	        }
	 
	        $( 'input', this.footer() ).on( 'keyup change', function () {
	            if ( that.search() !== this.value ) {
					console.log("Column sort keyup activated")
	                that
	                    .search( this.value )
	                    .draw();
	                unCheckAllRows();
	            }
	        } );
	    } );
	    
	    $("input").keyup(function(){
			console.log("Input keyup activated")
	        formChanged();
	    });
	    
	    
	    $('#table_id').on( 'page.dt', function () {
	        console.log( 'Page change event' );
	        
	        var oFormObject = document.forms['theForm'];
			
	        console.log("Page start row index: " + tableGlobal.page.info().start);
			console.log("Page end row index: " + tableGlobal.page.info().end);
			 
			oFormObject.elements["firstRowIndex"].value = tableGlobal.page.info().start;
			oFormObject.elements["displayedRowCount"].value = tableGlobal.page.info().end - tableGlobal.page.info().start;
			
			unCheckAllRows();
	    } );
	    
	    $('#table_id').on( 'search.dt', function () {
	        console.log( 'Search table event' );
	        
	        unCheckAllRows();
	    } );
	    
	    $('#table_id').on( 'order.dt', function () {
	        console.log( 'Sort by column event' );
	        
	        var order = tableGlobal.order();
	        
	        var colNum = order[0][0];
	        console.log("Col #: " + colNum);
	        if (colNum == 0) {
	        	console.log("This is column 0, which is really the select all box");
	        	return;
	        }
	        console.log('Ordering on column '+order[0][0]+' ('+order[0][1]+')');
	        
	        var oFormObject = document.forms['theForm'];
			
	        console.log("Page start row index: " + tableGlobal.page.info().start);
			console.log("Page end row index: " + tableGlobal.page.info().end);
			 
			oFormObject.elements["firstRowIndex"].value = tableGlobal.page.info().start;
			oFormObject.elements["displayedRowCount"].value = tableGlobal.page.info().end - tableGlobal.page.info().start;
			
			unCheckAllRows();
	    } );
	    
	    disableSaveButton();
	    
	    defaultStatusValue = $("#status").val();
	} );
	
	function unCheckAllRows() {
		console.log("unCheckAllRows() called: Clearing " + clonedCheckboxes.length + " checkboxes");
		
		for (var i=0; i < clonedCheckboxes.length; i++) {
			var rowCheckbox = clonedCheckboxes[i]; 
			console.log("Attempting to set this checkbox value to false: " + rowCheckbox.id);
			var actualCheckbox = document.getElementById(rowCheckbox.id);
			if (actualCheckbox == null) {
				console.log("Unable to set the actual checkbox to false because it is null");
			} else {
				console.log("Actually setting this checkbox value to false: " + actualCheckbox.id);
				console.log("    value was: " + actualCheckbox.checked);
				actualCheckbox.checked = false;
			}
		}
	}
	
	function selectAllButtonClicked() {
		console.log("selectAllButtonClicked() called");
		 setAllVisibleRowCheckboxes(true);
		 formChanged();
	}
	
	function deSelectAllButtonClicked() {
		console.log("deSelectAllButtonClicked() called");
		setAllVisibleRowCheckboxes(false);
		formChanged();
	}
	
	function setAllVisibleRowCheckboxes(newValue) {
		 // Identify visible rows
		 globalTable = new $.fn.dataTable.Api( '#table_id' );
		 console.log("Page start row index: " + tableGlobal.page.info().start);
		 console.log("Page end row index: " + tableGlobal.page.info().end);		 
		 var visibleRows = tableGlobal.rows({"page":"current", "filter":"applied", "search":"applied"});
		 console.log("visibleRows found " + visibleRows.length + " rows");
		 
		 visibleRows.every( function ( rowIdx, tableLoop, rowLoop ) {
			 	var row = tableGlobal.row( rowIdx );
			 	var htmlTableRowElement = row.node(); // HTMLTableRowElement
			    var data = row.data();
			    var htmlCollectionCells = htmlTableRowElement.cells;
			    var htmlTableCellElementCheckbox = htmlCollectionCells.item(0);
			    var cboxElementCollection = htmlTableCellElementCheckbox.getElementsByClassName("rowCheckbox");
			    var cbox = cboxElementCollection.item(0);
			    console.log("cbox.checked: " + cbox.checked);
			    cbox.checked = newValue;
			} );
	}
	

	function formChanged() {
		console.log("formChanged() called");
		var userCheckedARow = false;
		var userEnteredSomething = false;
		console.log("Examining " + documentCheckboxes.length + " checkboxes");
		for (var i=0; i < documentCheckboxes.length; i++) {
			var rowCheckbox = documentCheckboxes.item(i); 
			console.log("Examining checkbox with this ID: " + rowCheckbox.id);
			if (rowCheckbox.checked == true) {
				console.log("formChanged() row " + i + ": user checked this row");
				userCheckedARow = true;
				break;
			}
		}
		
		if (userCheckedARow) {
			console.log("formChanged() row " + i + ": user checked a row");
			var commentValue = $('#comment_field').val();
			var statusValue = $('#status').val();
			if ((statusValue.length > 0) && (commentValue.length > 0)) {
				console.log("formChanged() row " + i + ": user entered something (status and comment)");
				userEnteredSomething = true;
			}
		}

		if (userEnteredSomething) {
			console.log("formChanged(): enabling save button");
			enableSaveButton();
		} else {
			console.log("formChanged(): disabling save button");
			disableSaveButton();
		}
	}
	
	function doExit() {
		console.log("doExit()");
		var logoutUrl = "${pageContext.request.contextPath}/logout";
		console.log("logoutUrl: " + logoutUrl);
		$.ajax({
			type : "GET",
			url : logoutUrl,
			success : function(data, text) {
				console.log("logout succeeded: text: " + text);
				window.close();
			},
			error : function(request, status, error) {
				console.log("logout error: responseText: " + request.responseText);
				console.log("logout error: status: " + status);
				console.log("logout error: error: " + error);
			}
		});
		
	}
	
	function resetStatusAndCommentFields() {
		$('#status').val(defaultStatusValue);
		$('#comment_field').val("");
	}
	
	function getRowMessageElement(htmlCollectionCells) {
		var htmlTableCellElementVulnName = htmlCollectionCells.item(1);
		var rowMessageElement = htmlTableCellElementVulnName.getElementsByTagName("div")[0];
		return rowMessageElement;
	}
	
	function setRowMessage(rowMessageElement, message) {
		var messageNode = document.createTextNode(message);
		rowMessageElement.appendChild(messageNode);
	}
	
	function setAdvice(msg) {
		console.log("Setting advice to: " + msg);
		clearAdvice();
		var adviceElement = document.getElementsByClassName("advice")[0];
		var messageNode = document.createTextNode(msg);
		adviceElement.appendChild(messageNode); 
	}
	
	function clearAdvice() {
		console.log("Clearing advice");
		var adviceElement = document.getElementsByClassName("advice")[0];
	    if (adviceElement.childNodes.length > 0) {
	    	console.log("Removing old advice: " + adviceElement.childNodes[0].textContent);
	    	adviceElement.removeChild(adviceElement.childNodes[0]);
	    }
	}
	
	function resetAdvice() {
	    setAdvice("Select 1 or more rows, enter values, and click Save");
	}
	
	function disableSaveButton() {
		console.log("Disabling Save button");
		document.getElementById('saveButton').disabled=true;
	}
	
	function enableSaveButton() {
		console.log("Enabling Save button");
		document.getElementById('saveButton').disabled=false;
	}
	
	function doSave() {
		console.log("doSave()");
		disableSaveButton();
		setAdvice("Saving...");
		
		 // Identify visible rows
		 globalTable = new $.fn.dataTable.Api( '#table_id' );
		 console.log("Page start row index: " + tableGlobal.page.info().start);
		 console.log("Page end row index: " + tableGlobal.page.info().end);
		 		 
		 var visibleRows = tableGlobal.rows({"page":"current", "filter":"applied", "search":"applied"});
		 console.log("visibleRows found " + visibleRows.length + " rows");
		 
		 visibleRows.every( function ( rowIdx, tableLoop, rowLoop ) {
			    console.log("about to get row at " + rowIdx);
			 	var row = tableGlobal.row( rowIdx );
			 	var htmlTableRowElement = row.node(); // HTMLTableRowElement
			    var data = row.data();
			    var htmlCollectionCells = htmlTableRowElement.cells;

			    // Remove any row message resulting from stuff that happened last Save
			    var rowMessageElement = getRowMessageElement(htmlCollectionCells);
			    if (rowMessageElement.childNodes.length > 0) {
			    	console.log("Removing message text from this row: " + rowMessageElement.childNodes[0].textContent);
			    	rowMessageElement.removeChild(rowMessageElement.childNodes[0]);
			    }
			    
			 	// Get checkbox value
			    var htmlTableCellElementCheckbox = htmlCollectionCells.item(0);
			    var cboxElementCollection = htmlTableCellElementCheckbox.getElementsByClassName("rowCheckbox");
			    var cbox = cboxElementCollection.item(0);
			    if (cbox.checked) {
			    	console.log("This row is checked");
			    	
			    	// Send: selected vuln key
					var data = {};
					data["key"] = cbox.value;

					// Send status and comment values
					var newStatusValue = $('#status').val();
					var newCommentValue = $('#comment_field').val();
					data["status"] = newStatusValue;
					data["comment"] = newCommentValue;
					
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					$(document).ajaxSend(function(e, xhr, options) {
						console.log("ajaxSend()");
						xhr.setRequestHeader(header, token);
					});
					
					console.log("Will send: " + JSON.stringify(data));
					
					$.post("editnaiauditdetails", data,
						    function(response, status){
								resetAdvice();
								if (response.status == 'SUCCEEDED') {
									console.log("Row update succeeded on server; new row data: " + response.newRowData + "; Updating row display");
									updateRowDisplay(cbox, htmlCollectionCells, response, newStatusValue, newCommentValue);
									cbox.checked = false;
									resetStatusAndCommentFields();
								} else if (response.status == 'UNCHANGED') {
									console.log("Row was unchanged");
									setRowMessage(rowMessageElement, "Warning: This row was not changed (because status was unchanged)");
									cbox.checked = false;
									resetStatusAndCommentFields();
								} else if (response.status == 'FAILED') {
						        	console.log("Row update FAILED: " + response.message);
						        	setRowMessage(rowMessageElement, "Row update FAILED: " + response.message);
						        	cbox.checked = false;
						        	resetStatusAndCommentFields();
								} else {
									console.log("Row update returned an unknown status: " + response.status);
									setRowMessage(rowMessageElement, "Row update returned an unknown status value: " + response.message);
									cbox.checked = false;
									resetStatusAndCommentFields();
								}
						    })
						    .fail(function(response) {
						    	console.log("*** Post failed");
						    	console.log("Response: " + response.status);
						    	if (response.status == "403") {
						    		console.log("The session timed out");
						        	setAdvice("The session has timed out. Please refresh or exit this browser window.");
						        	cbox.checked = false;
						        	resetStatusAndCommentFields();
						    	} else {
						    		console.log("Error code: " + response.status);
						    		setAdvice("Error code: " + response.status + ". Please refresh or exit this browser window.");
						        	cbox.checked = false;
						        	resetStatusAndCommentFields();
						    	}
						    });
			    }
			} );
		
		
	}
	
	function updateRowDisplay(cbox, htmlCollectionCells, response, newStatusValue, newCommentValue) {
		
		console.log("Getting NAI status in row (to update it)");
		var htmlTableCellElementNaiStatus = htmlCollectionCells.item(11);
	    var returnedStatus = response.newRowData.auditPart.vulnerabilityNaiAuditStatus;
	    htmlTableCellElementNaiStatus.innerText = newStatusValue;
	    
	    console.log("Getting Comment in row (to update it)");
		var htmlTableCellElementNaiComment = htmlCollectionCells.item(12);
	    var returnedComment = response.newRowData.auditPart.vulnerabilityNaiAuditComment;
	    htmlTableCellElementNaiComment.innerText = newCommentValue;

	    console.log("Getting Rem Comment in row (to update it)");
		var htmlTableCellElementRemComment = htmlCollectionCells.item(10);
	    var returnedRemCommentShort = response.newRowData.ccPart.vulnerabilityRemediationCommentsShort;
	    console.log("Changing " + htmlTableCellElementRemComment.innerHtml + " to " + returnedRemCommentShort);
	    htmlTableCellElementRemComment.innerText = returnedRemCommentShort;
	    if (response.newRowData.ccPart.vulnerabilityRemediationComments.length > response.newRowData.ccPart.vulnerabilityRemediationCommentsShort.length) {
			var moreLink = '<br/><a target="_blank" href="showfulltext?itemType=REMEDIATION_COMMENTS&itemKey=' + response.newRowData.key.asString + '" >more...</a>';
			console.log("moreLink: " + moreLink);
			var breakElem = document.createElement("br");
			var moreLinkElem = document.createElement("a");
			var linkText = document.createTextNode("more...");
			moreLinkElem.title = "See full text in separate window";
			moreLinkElem.href = "showfulltext?itemType=REMEDIATION_COMMENTS&itemKey=" + response.newRowData.key.asString;
			moreLinkElem.target = "_blank";
			moreLinkElem.appendChild(linkText);
			htmlTableCellElementRemComment.appendChild(breakElem);
			htmlTableCellElementRemComment.appendChild(moreLinkElem);
	    }
	    
	    var returnedRemCommentPopupText = response.newRowData.ccPart.vulnerabilityRemediationCommentsPopUpText;
	    htmlTableCellElementRemComment.title = returnedRemCommentPopupText;

	    console.log("Getting Rem Status in row (to update it)");
		var htmlTableCellElementRemStatus = htmlCollectionCells.item(9);
	    var returnedRemStatus = response.newRowData.ccPart.vulnerabilityRemediationStatus;
	    console.log("Changing " + htmlTableCellElementRemStatus.innerText + " to " + returnedRemStatus);
	    htmlTableCellElementRemStatus.innerText = returnedRemStatus;
	    

	    console.log("Getting Actual Rem Date in row (to update it)");
		var htmlTableCellElementRemDateActual = htmlCollectionCells.item(8);
	    var returnedRemDateActual = response.newRowData.ccPart.vulnerabilityActualRemediationDate;
	    if (returnedRemDateActual == undefined) {
	    		returnedRemDateActual = "";
	    }
	    console.log("Changing " + htmlTableCellElementRemDateActual.innerText + " to " + returnedRemDateActual);
	    htmlTableCellElementRemDateActual.innerText = returnedRemDateActual;
	    
	    console.log("Getting Target Rem Date in row (to update it)");
	    var htmlTableCellElementRemDateTarget = htmlCollectionCells.item(7);
	    var returnedRemDateTarget = response.newRowData.ccPart.vulnerabilityTargetRemediationDate;
	    if (returnedRemDateTarget == undefined) {
	    		returnedRemDateTarget = "";
	    }
	    console.log("Changing " + htmlTableCellElementRemDateTarget.innerText + " to " + returnedRemDateTarget);
	    htmlTableCellElementRemDateTarget.innerText = returnedRemDateTarget;
	}
	
</script>
</head>
<body>
	<div class="page-header">
    <img src="${pageContext.request.contextPath}/styles/icons/Black_Duck_Logo.png" alt="Black Duck Logo"  width="200" height="50" id=""/>       	
        <h1><spring:message code="label.naiauditdetailsedit.title" text="Edit NAI Audit Details" /></h1>
        <h4><b><spring:message code="label.naiauditdetailsedit.app.name" text="Application name" />:</b> ${selectedVulnerabilities.applicationName}</h4>
        <h4><b><spring:message code="label.naiauditdetailsedit.app.version" text="Application name" />:</b> ${selectedVulnerabilities.applicationVersion}</h4>
        <p class="has-error">${message}</p>
        <br />
    </div>
    <div class="regular">
    
    <button onclick="selectAllButtonClicked()" class="btn btn-primary">Select All Visible Rows</button>
	<button onclick="deSelectAllButtonClicked()" class="btn btn-primary">Clear Selection</button>

	<form:form id="theForm" method="post" modelAttribute="selectedVulnerabilities"  action="editnaiauditdetails">
	<!-- CSRF token is inserted automatically by form:form tag -->
		

	
		
	<table id="table_id" class="display">
    <thead>
        <tr>
        	<th style="background:none;"></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.name" text="Vulnerability Name" /></th>
            <th><spring:message code="label.naiauditdetailsedit.comp.name" text="Component Name" /></th>
            <th><spring:message code="label.naiauditdetailsedit.comp.version" text="Component Version" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.severity" text="Severity" /></th>
            
            <th><spring:message code="label.naiauditdetailsedit.vuln.date.published" text="Date Published" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.description" text="Description" /></th>
            
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.date.target" text="Target Remediation Date" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.date.actual" text="Actual Remediation Date" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.status" text="Remediation Status" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.comment" text="Remediation Comment" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.nai.audit.status" text="NAI Audit Status" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.nai.audit.comment" text="NAI Audit Comment" /></th>
        </tr>
    </thead>
    <tfoot>
        <tr>
        	<th></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.name" text="Vulnerability Name" /></th>
            <th><spring:message code="label.naiauditdetailsedit.comp.name" text="Component Name" /></th>
            <th><spring:message code="label.naiauditdetailsedit.comp.version" text="Component Version" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.severity" text="Severity" /></th>
            
            <th><spring:message code="label.naiauditdetailsedit.vuln.date.published" text="Date Published" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.description" text="Description" /></th>
            
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.date.target" text="Target Remediation Date" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.date.actual" text="Actual Remediation Date" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.status" text="Remediation Status" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.remediation.comment" text="Remediation Comment" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.nai.audit.status" text="NAI Audit Status" /></th>
            <th><spring:message code="label.naiauditdetailsedit.vuln.nai.audit.comment" text="NAI Audit Comment" /></th>
        </tr>
    </tfoot>
    <tbody>
    	<c:forEach var="vulnerability" items="${vulnNaiAuditDetailsList}" varStatus="rowCount">
        	<tr>
        		<td><form:checkbox class="rowCheckbox" onchange="javascript:formChanged();" id="checkbox${rowCount.index}" path="itemList" value="${vulnerability.key.asString}" /></td>
            	<td>
            		<a href="https://web.nvd.nist.gov/view/vuln/detail?vulnId=${vulnerability.ccPart.vulnerabilityName}" target="_blank">${vulnerability.ccPart.vulnerabilityName}</a>
            		<br/>
            		<font color="red"><div id="rowMessage"></div></font>
            	</td>
            	<td>${vulnerability.ccPart.componentName}</td>
            	<td>${vulnerability.ccPart.componentVersion}</td>
            	<td>${vulnerability.ccPart.vulnerabilitySeverityString}</td>
            	
            	<td><fmt:formatDate type="date" 
            		value="${vulnerability.ccPart.vulnerabilityDatePublished}" /></td>
            	<td title="${vulnerability.ccPart.vulnerabilityDescription}">${vulnerability.ccPart.vulnerabilityDescriptionShort}</td>
            	
            	<td><fmt:formatDate type="date" 
            		value="${vulnerability.ccPart.vulnerabilityTargetRemediationDate}" /></td>
            	<td><fmt:formatDate type="date" 
            		value="${vulnerability.ccPart.vulnerabilityActualRemediationDate}" /></td>
            		
            	<td>${vulnerability.ccPart.vulnerabilityRemediationStatus}</td>
            	<td title="${vulnerability.ccPart.vulnerabilityRemediationCommentsPopUpText}"><span style="white-space: pre-wrap">${vulnerability.ccPart.vulnerabilityRemediationCommentsShort}<c:if test="${fn:length(vulnerability.ccPart.vulnerabilityRemediationComments) > fn:length(vulnerability.ccPart.vulnerabilityRemediationCommentsShort)}"><c:out value='<br/><a target="_blank" title="See full text in separate window" href="${pageContext.request.contextPath}/showfulltext?itemType=REMEDIATION_COMMENTS&itemKey=${vulnerability.key.asString}" >more...</a>' escapeXml="false"/></c:if></span></td>
            	<td>${vulnerability.auditPart.vulnerabilityNaiAuditStatus}</td>
            	<td title="${vulnerability.auditPart.vulnerabilityNaiAuditComment}">${vulnerability.auditPart.vulnerabilityNaiAuditCommentShort}</td>
        	</tr>
        </c:forEach>
    </tbody>
	</table>
	<br/>
	<br/>
	<label for="naiauditstatus_field"><spring:message code="label.naiauditdetailsedit.vuln.nai.audit.status" text="NAI Audit Status" />: </label>
	
	<form:select onchange="javascript:formChanged();" id="status" path="vulnerabilityNaiAuditStatus">
	<form:options items="${vulnerabilityNaiAuditStatusOptions}"  />		
	</form:select>
	<br />
	<form:errors path="vulnerabilityNaiAuditStatus" cssClass="error" />
			
	<br/>
	<label for="comment_field"><spring:message code="label.naiauditdetailsedit.vuln.nai.audit.comment" text="NAI Audit Comment" />: </label>
	<form:input path="comment" size="20" id="comment_field" /><br/>
	<form:errors path="comment" cssClass="error" /> 
	<br/>
	
	<!-- Pass application details through to next controller via model attribute "selectedVulnerabilities" -->
	<form:hidden path="applicationId" />
	<form:hidden path="applicationName" />
	<form:hidden path="applicationVersion" />
	
	<form:hidden path="firstRowIndex" />
	<form:hidden path="displayedRowCount" />
	
	</form:form>
		
	<button id="saveButton" onclick="doSave()" class="btn btn-primary">
			<spring:message code="label.naiauditdetailsedit.submit" />
	</button>
	
	 
</div> 
<p class="advice"><spring:message code="label.naiauditdetailsedit.advice"/></p>
<button type="button" class="btn btn-primary" value="cancel" onclick="doExit()"><spring:message code="label.naiauditdetailsedit.exit"/></button>
<br/>
<br/>

</body>
</html>