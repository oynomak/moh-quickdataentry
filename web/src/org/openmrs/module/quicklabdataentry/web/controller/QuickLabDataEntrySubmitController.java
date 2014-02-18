package org.openmrs.module.quicklabdataentry.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

public class QuickLabDataEntrySubmitController  extends AbstractController {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		int noRows = 0;
		String displayDiv = "none";
		Patient patient = Context.getPatientService().getPatient(Integer.valueOf(request.getParameter("patientId")));
		ModelAndView mav = new ModelAndView(new RedirectView("../../patientDashboard.form?patientId=" + patient.getPatientId()));
		Location location = Context.getLocationService().getLocation(Integer.valueOf(request.getParameter("locationId")));;
		String msg = "Lab tests saved successfully!";
		Object error_msg = "Save failure! Verify that you entered all required date correctly.";
		
		// Setting the table rows number: corresponding to the number of Encounters
		if(request.getParameter("noRows")!=null && !request.getParameter("noRows").equals(""))
			if(request.getParameter("rowSelect") != null 
					&& (request.getParameter("rowSelect").equals("Confirm") 
							|| request.getParameter("rowSelect").equals("Confirmer"))){
				noRows = Integer.parseInt(request.getParameter("noRows"));
				
				if(noRows >= 1) {
					mav.addObject("numRows", noRows);
					mav.addObject("locationId", location.getLocationId());
					mav.addObject("rowSelect", request.getParameter("rowSelect"));
					
					return mav;
				}
		}
		
		try {
			
			// If and only if labSave=Save (i.e. Submit button is clicked)
			if(request.getParameter("labSave") != null 
					&& (request.getParameter("labSave").equals("Save") 
							|| request.getParameter("labSave").equals("Sauvegarder")))
				if(request.getParameter("numRows") != null 
						&& !request.getParameter("numRows").equals(""))
					if(request.getParameter("location")!=null && !request.getParameter("location").equals(""))
						if(request.getParameter("patient")!=null && !request.getParameter("patient").equals("")) {
							int numRows = Integer.parseInt(request.getParameter("numRows"));
							patient = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patient")));
							location = Context.getLocationService().getLocation(Integer.parseInt(request.getParameter("location")));
							
							if(numRows > 0) {
								handleLabQuickEntry(numRows , patient, location, request);
								displayDiv = "inline";
								mav.addObject("patient_id",patient.getPatientId());
								mav.addObject("location_id", location.getLocationId());
								mav.addObject("num_rows", numRows);
								request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
							}
						}
		} catch (Exception exception) {
			exception.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, error_msg);
		}

		mav.addObject("noRows", noRows);
		mav.addObject("displayDiv", displayDiv);
		mav.addObject(new RedirectView("../../patientDashboard.form?patientId=" + patient.getPatientId()));
		
		return mav;
	}

	/**
	 * Saves the list of encounters as they are entered multiple
	 * 
	 * @param numOfRows the number of the table's rows (corresponding to the number of encounters)
	 * @param patient the patient that encounter the provider (does the test)
	 * @param location the location where the encounter happens
	 * @param request HttpServletRequest object
	 * @throws ParseException
	 */
	private void handleLabQuickEntry(int numOfRows, Patient patient, Location location, HttpServletRequest request) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						
		if(numOfRows > 0 && patient != null && location != null && request != null)
			for(int i=1; i <= numOfRows; i++) {
	
				// create Encounter
				Encounter encounter = new Encounter();
				encounter.setPatient(patient);
				encounter.setLocation(location);
				
				// check for the date of test that will be considered as Encounter date.
				if(request.getParameter("testDate_"+i)!=null&&!request.getParameter("testDate_"+i).equals("")) {
					Date date = (Date) formatter.parse(request.getParameter("testDate_"+i));
					encounter.setEncounterDatetime(date);

					// It's better to get the encounter date over here!
					setEncounterType(encounter, patient, date);
					
					addObsToEncounter(patient, location, request, i, encounter,	date);
				}
				
				// check for the provider (lab officer) of test that will be considered as Encounter provider.
				if(request.getParameter("provider_"+i)!=null&&!request.getParameter("provider_"+i).equals("")) {
					Person provider = Context.getUserService().getUser(Integer.parseInt(request.getParameter("provider_"+i))).getPerson();
					encounter.setProvider(provider);
				}
				
				// persist Encounter
				Context.getEncounterService().saveEncounter(encounter);
				log.info("********QUICK LAB DATA ENTRY*****<<saved encounter>>***"+encounter.toString());
			}
	}
	
	/**
	 * The following tests correspond to the Obs of each encounter.
	 * TESTS: Hb (21), Ht (1015), GB (678), Neutro	(1022), Lympho (1021)
	 * Plt (729), SGOT (653), SGPT (654), Créat. (790), Glucose	(887)
	 * CD4 (5497), HIV VIRAL LOAD (856), CD4% (730), HIV PCR (1553), RPR (1478)
	 *
	 * @param patient the patient that encounter the provider (does the test)
	 * @param location the location where the encounter happens
	 * @param request HttpServletRequest object
	 * @param i the counter that is appended to the fields
	 * @param encounter the Encounter where these tests are done
	 * @param date the Encounter date
	 */
	private void addObsToEncounter(Patient patient, Location location,
			HttpServletRequest request, int i, Encounter encounter, Date date) {
		
		
		// check for the Hb (g/dl) test that will be considered as Encounter Obs
		if(request.getParameter("hb_"+i)!=null&&!request.getParameter("hb_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(21), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("hb_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("ht_"+i)!=null&&!request.getParameter("ht_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(1015), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("ht_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}
		
		// check for the Hb (g/dl) test that will be considered as Encounter Obs
		if(request.getParameter("gb_"+i)!=null&&!request.getParameter("gb_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(678), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("gb_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("neutro_"+i)!=null&&!request.getParameter("neutro_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(1022), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("neutro_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}
		
		// check for the Hb (g/dl) test that will be considered as Encounter Obs
		if(request.getParameter("lympho_"+i)!=null&&!request.getParameter("lympho_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(1021), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("lympho_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("plt_"+i)!=null&&!request.getParameter("plt_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(729), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("plt_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}
		
		// check for the Hb (g/dl) test that will be considered as Encounter Obs
		if(request.getParameter("sgot_"+i)!=null&&!request.getParameter("sgot_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(653), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("sgot_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("sgpt_"+i)!=null&&!request.getParameter("sgpt_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(654), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("sgpt_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}
		
		// check for the Hb (g/dl) test that will be considered as Encounter Obs
		if(request.getParameter("creat_"+i)!=null&&!request.getParameter("creat_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(790), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("creat_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("glucose_"+i)!=null&&!request.getParameter("glucose_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(887), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("glucose_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}
		
		// check for the Hb (g/dl) test that will be considered as Encounter Obs
		if(request.getParameter("cd4_"+i)!=null&&!request.getParameter("cd4_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(5497), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("cd4_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("viral_load_"+i)!=null&&!request.getParameter("viral_load_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(856), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("viral_load_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Hb (g/dl) test that will be considered as Encounter Obs
		if(request.getParameter("cd4_perc_"+i)!=null&&!request.getParameter("cd4_perc_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(730), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("cd4_perc_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("hiv_pcr_"+i)!=null&&!request.getParameter("hiv_pcr_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(1553), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("hiv_pcr_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}

		// check for the Ht (%) test that will be considered as Encounter Obs
		if(request.getParameter("rpr_"+i)!=null&&!request.getParameter("rpr_"+i).equals("")) {
			Obs obs = new Obs(patient, Context.getConceptService().getConcept(1478), date, location);
			obs.setEncounter(encounter);
			obs.setValueNumeric(Double.parseDouble(request.getParameter("rpr_"+i)));
			 
			// add the Obs to the Encounter
			encounter.addObs(obs);
		}
	}
	
	private void setEncounterType(Encounter encounter, Patient patient, Date date) {
		
		if(patient.getAge(date) >= 15) {//Adult >= 15 years old
			
			EncounterType adultReturn = Context.getEncounterService().getEncounterType(2);
			encounter.setEncounterType(adultReturn);
		}
		if(patient.getAge(date) < 15) {// Pediatric < 15 years old.
			
			EncounterType pedReturn = Context.getEncounterService().getEncounterType(4);
			encounter.setEncounterType(pedReturn);
		}
		
	}
	
}
