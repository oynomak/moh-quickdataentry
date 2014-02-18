/**
 * 
 */
package org.openmrs.module.quicklabdataentry.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Kamonyo Mugabo
 *
 */
public class QuickDrugEntryFormController extends ParameterizableViewController {

		/** (non-Jsdoc)
		 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
		@Override
		protected ModelAndView handleRequestInternal(HttpServletRequest request,
				HttpServletResponse response) {
			
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName(getViewName());
		
		return mav;

	}
}
