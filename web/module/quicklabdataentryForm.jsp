<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/style/stylesheet.css" />

<h2><spring:message code="quicklabdataentry.title"/></h2>

<br/>
<div class="" id="patientLink" style="display: ${displayDiv};">
	<a href="../../patientDashboard.form?patientId=${patient_id}">
	<spring:message code="quicklabdataentry.viewdashboard"/>
		${patient.personName}
	</a>
</div>

<form action="quicklabdataentryForm.form" method="get" id="form_select">
	
	<c:set value="${param.noRows}" var="numRows"/>
	<c:set value="${param.patientId}" var="patient"/>
	<c:set value="${param.locationId}" var="location"/>

	<br/>
	<b class="boxHeader"><spring:message code="quicklabdataentry.boxtitle" /></b>
	<div class="box">
		<table>
			<tr>
				<td><spring:message code="quicklabdataentry.patient"/></td>
				<td><openmrs_tag:patientField formFieldName="patientId" initialValue="${patient}"/></td>
			</tr>
			<tr>
				<td><spring:message code="quicklabdataentry.location"/></td>
				<td><openmrs_tag:locationField formFieldName="locationId" initialValue="${location}" /></td>
			</tr>
			<tr>
				<td><spring:message code="quicklabdataentry.numbercolumns"/></td>
				<td>
					<select name="noRows" id="noRows" onselect="setTableRows()">
						<c:forEach var="item" items="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20"varStatus="status">
							<option <c:if test="${item==noRows}" >selected="selected"</c:if>
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

<form action="quicklabdataentryForm.form" method="get" id="form_encounter">

	<c:set value="${num_rows}" var="numRows"/>
	<c:set value="${patient_id}" var="patient"/>
	<c:set value="${location_id}" var="location"/>

	<input type="hidden" name="numRows" value="${param.noRows}"/>
	<input type="hidden" name="patient" value="${param.patientId}"/>
	<input type="hidden" name="location" value="${param.locationId}"/>
	<br/>
	<b class="boxHeader" style="height: auto; display: ${hideDiv};"><spring:message code="quicklabdataentry.tabletitle"/></b>
	<div class="box" style="height: auto;">
		<table width="100%">
			  <tr>
				   <td class="dateField"><spring:message code="quicklabdataentry.testdate"/></td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.provider"/></td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.hb"/><br/>(g/dl)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.ht"/><br/>(%)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.gb"/><br/>(x109/l)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.neutro"/><br/>(x109/l)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.lympho"/><br/>(x109/l)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.plt"/><br/>(x109/l)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.sgot"/><br/>(iu/l)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.sgpt"/><br/>(iu/l)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.creat"/><br/>(μmol./l)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.glucose"/><br/>(mg/dl)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.cd4"/><br/>(cells/mm3)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.viralload"/><br/>(copies/ml)</td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.cd4percent"/></td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.hivpcr"/></td>
				   <td class="tableColumns"><spring:message code="quicklabdataentry.rpr"/></td>
				  </tr>
		<!-- </table>
	</div>
	<div>
		<table width="100%">-->
		  <c:forEach var="item" items="${rows}"varStatus="status">
		      <tr>
			        <td class="dateField"><input type="text" size="12" value="" name="testDate_${status.count}" onclick="showCalendar(this)" /></td>
			        <td class="tableColumns"><openmrs_tag:userField formFieldName="provider_${status.count}" initialValue="${param.providerId}" /></td>
			        <td class="tableColumns"><input size="5" type="text" name="hb_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="ht_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="gb_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="neutro_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="lympho_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="plt_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="sgot_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="sgpt_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="creat_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="glucose_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="cd4_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="viral_load_${status.count}"/></td>
			        <td class="tableColumns"><input size="5" type="text" name="cd4_perc_${status.count}"/></td>
			        
			        <td class="tableColumns">
				        <select name="hiv_pcr_${status.count}">
				        	<option value="0">--</option>
				        	<option value="703">Positive</option>
				        	<option value="664">Negative</option>
				        </select>
			        </td>
			        <td class="tableColumns">
				        <select name="rpr_${status.count}">
				        	<option value="0">--</option>
				        	<option value="1228">Positive</option>
				        	<option value="1229">Negative</option>
				        </select>
			        </td>
			        
		      </tr>
		  </c:forEach>
		</table>
		<hr/>
		<table>
			<tr>
				<td><input type="submit" name="labSave" value="<spring:message code="quicklabdataentry.save"/>" /></td>
			</tr>
		</table>
	</div>
	
</form>

<br/>

<%@ include file="/WEB-INF/template/footer.jsp"%>
