/**
 * 
 */
package org.openmrs.module.quicklabdataentry.extension.html;

import org.openmrs.module.web.extension.PatientDashboardTabExt;

/**
 * @author Yves GAKUBA
 * 
 */
public class QuickDataEntryDashboardTab extends PatientDashboardTabExt {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openmrs.module.web.extension.PatientDashboardTabExt#getPortletUrl()
	 */
	@Override
	public String getPortletUrl() {
		return "quicklabdataentry";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openmrs.module.web.extension.PatientDashboardTabExt#getRequiredPrivilege
	 * ()
	 */
	@Override
	public String getRequiredPrivilege() {
		return "Add Quick Lab data";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabId()
	 */
	@Override
	public String getTabId() {
		return "quicklabdataentry";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabName()
	 */
	@Override
	public String getTabName() {
		return "quicklabdataentry.title";
	}

}
