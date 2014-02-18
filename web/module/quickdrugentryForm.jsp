<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/style/stylesheet.css" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery-1.3.2.js" />

<script type="text/javascript">
	var $qle= jQuery.noConflict();
</script>

<h2><spring:message code="quicklabdataentry.drugtitle"/></h2>

<br/>
<form action="quickdrugentryForm.form" method="get" id="form_select">

	<b class="boxHeader"><spring:message code="quicklabdataentry.boxtitle" /></b>
	<div class="box">
		<table>
			<tr>
				<td><spring:message code="quicklabdataentry.patient"/></td>
				<td><openmrs_tag:patientField formFieldName="patientId" initialValue="${patient}"/></td>
			</tr>
			<tr>
				<td><spring:message code="quicklabdataentry.location"/></td>
				<td><openmrs_tag:locationField formFieldName="locationId" initialValue="${model.location}" /></td>
			</tr>
			<tr>
				<td><spring:message code="quicklabdataentry.numbercolumns"/></td>
				<td>
					<select name="noRows" id="noRows" onselect="setTableRows()">
						<c:forEach var="item" items="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20"varStatus="status">
							<option <c:if test="${item==model.noRows}" >selected="selected"</c:if>
							value="${item}">${status.count}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="rowSelect" value="<spring:message code="quicklabdataentry.confirm"/>" /></td>
			</tr>
		</table>
	</div>
	
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
