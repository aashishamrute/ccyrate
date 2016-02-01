package com.ccy.web;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ccy.domain.CcyRate;
import com.ccy.util.CcyRateUtils;

/**
 * Controller Servlet to handle Currency requests.
 * 
 * @author Aashish Amrute
 *
 */
// @RequestMapping("/ccy")
@Controller
public class CcyController {

	private static Logger LOG = Logger.getLogger(CcyController.class);

	/**
	 * Attribute for latest currency result.
	 */
	private static final String ATTR_LATEST_RESULT = "latest_currency_result";

	/**
	 * Initial request to generate home page.
	 * 
	 * @param uiModel
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String ccyRateForm(Model uiModel) {
		populateForm(uiModel, getUserName(),new CcyRate());
		uiModel.addAttribute(ATTR_LATEST_RESULT, "Please Select Currency.");
		return "ccy/ccyrate";
	}

	/**
	 * Servlet to find currency rate for user request.
	 * 
	 * @param ccyFrom
	 *            - Base Currency
	 * @param ccyTo
	 *            - Target Currency
	 * @param size
	 *            - No. of rows displayed from Hist.
	 * @param uiModel
	 *            - Model Object.
	 * @return
	 */
	@RequestMapping(value = "/ccy", params = "find=selectCcy", method = RequestMethod.GET)
	public String findCcyRate(@Valid @ModelAttribute(value = "ccyrate") CcyRate ccyrate, BindingResult bindingResult,
			Model uiModel) {

		String username = getUserName();
		

		if (bindingResult.hasErrors()) {
			populateForm(uiModel, username, ccyrate);
			return "ccy/ccyrate";
		}
		
		// keep at this place to avoid current search from Hist list.
		populateForm(uiModel, username, new CcyRate());

		ccyrate.setUsername(username);

		try {
			CcyRateUtils.setCurrencyRate(ccyrate).persist();
			uiModel.addAttribute(ATTR_LATEST_RESULT,
					"1 " + ccyrate.getCcyFrom() + " = " + ccyrate.getRate() + " " + ccyrate.getCcyTo() + " (Rate Date: "
							+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ccyrate.getRateDate()) + ")");
		} catch (Exception e) {
			LOG.error("Error while retriving Currency Rate: ", e);
			uiModel.addAttribute(ATTR_LATEST_RESULT,
					"Unable to retrive Currency Rate for " + ccyrate.getCcyFrom() + " to " + ccyrate.getCcyTo());
		}

		return "ccy/ccyrate";
	}

	/**
	 * Add Date Time format pattern to be displayed on screen.
	 * 
	 * @param uiModel
	 */
	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("ccyhist_ratedate_format",
				DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
		uiModel.addAttribute("ccy_hist_date_format",
				DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
	}

	/**
	 * add currency list to model. Base currency always USD, as currency
	 * external api offer free service only for USD, for other currency premium
	 * service required.
	 * 
	 * @param uiModel
	 */
	void addCurrencyList(Model uiModel) {
		uiModel.addAttribute("currency_from", CcyRateUtils.getBaseCurrencyList());
		uiModel.addAttribute("currency_to", CcyRateUtils.getCurrencyList());
	}

	/**
	 * Add search history record to model.
	 * 
	 * @param uiModel
	 * @param username
	 *            - username
	 * @param size
	 *            - max no. of record
	 */
	void addHistRecord(Model uiModel, String username, Integer size) {
		List<CcyRate> ccyRateHists = CcyRate.findCcyHistByUserName(username, size);
		uiModel.addAttribute("ccyRateHists", ccyRateHists);
	}

	/**
	 * get logged-in username from security context.
	 * 
	 * @return - user name
	 */
	String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	/**
	 * Populate form content.
	 * 
	 * @param uiModel
	 * @param ccyrate
	 */
	void populateForm(Model uiModel, String username, CcyRate ccyRate) {
		addHistRecord(uiModel, username, 10);
		addDateTimeFormatPatterns(uiModel);
		addCurrencyList(uiModel);
		uiModel.addAttribute("ccyrate", ccyRate);
	}
}
