package com.blackducksoftware.tools.appedit.appdetails.service;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;

/**
 * Converts AppDetails bean to/from ViewAppBean
 * 
 * @author sbillings
 *
 */
public interface AppDetailsBeanConverter {

    /**
     * Create a View-friendly application bean from a Code Center-centric
     * application bean.
     *
     * @param appDetails
     * @return
     */
    public abstract ViewAppBean createViewAppBean(AppDetails appDetails);

    /**
     * Create a Code Center-centric application bean from a View-friendly
     * application bean.
     *
     * @param viewAppBean
     * @return
     */
    public abstract AppDetails createAppDetails(ViewAppBean viewAppBean);

}