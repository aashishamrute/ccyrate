/**
 * 
 */
package com.ccy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;

import org.apache.log4j.Logger;

import com.ccy.domain.CcyRate;

/**
 * Utility to get Currency and Currency Rate.
 * 
 * @author Aashish Amrute
 * 
 */
public class CcyRateUtils {

	private static final Logger LOG = Logger.getLogger(CcyRateUtils.class);

	/**
	 * URL Prefix.
	 */
	private static final String URL_PREFIX = "https://openexchangerates.org/api";

	/**
	 * URL Suffix to get latest Currency.
	 */
	private static final String URL_LATEST_CCY_API = "/latest.json";
	
	/**
	 * APP_ID for url.
	 */
	private static final String URL_APP_ID = "?app_id=17011ea94a98446d8354b75e628d9eeb";
	
	/**
	 * Historical URL
	 */
	private static final String URL_HISTORY = "/historical/:date.json";
	
	/**
	 * base currency.
	 */
	private static final String URL_BASE_CCY = "&base=:base";
	
	/**
	 * target currency.
	 */
	private static final String URL_TARGET_CCY = "&symbols=:symbols";

	/**
	 * URL Suffix to get Currency List.
	 */
	private static final String URL_CCY_LIST_SUFFIX = "/currencies.json";

	/**
	 * Currency List.
	 */
	private static List<String> CCY_LIST = new ArrayList<String>();

	/**
	 * Get Currency Rate.
	 * 
	 * @param username
	 *            - User Name
	 * @param ccyBase
	 *            - Base Currency
	 * @param ccyTarget
	 *            - Target Currency
	 * @return - Currency Rate Entity.
	 */
	public static CcyRate setCurrencyRate(CcyRate ccyRate) 
			throws MalformedURLException,IOException
	{
		String url = URL_PREFIX + URL_LATEST_CCY_API + URL_APP_ID;
		
		if (ccyRate.getRateDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			url = URL_PREFIX + URL_HISTORY.replace(":date", sdf.format(ccyRate.getRateDate())) + URL_APP_ID;
		}else {
			ccyRate.setRateDate(new Date());
		}
		
		try (JsonReader reader = Json.createReader(new InputStreamReader(
				new URL(url).openStream(), Charset.forName("UTF-8")))) {
			JsonObject object = reader.readObject();
			//ccyRate.setRateDate(new Date(object.getInt("timestamp")));
			JsonObject rates = object.getJsonObject("rates");
			ccyRate.setRate(rates.getInt(ccyRate.getCcyTo()));

		} catch (MalformedURLException e) {
			LOG.error("Error while opening URL stream", e);
			throw e;
		} catch (IOException e) {
			LOG.error("Error while opening URL stream", e);
			throw e;
		}

		return ccyRate;

	}

	/**
	 * Return the list of currencies from openexchangerates.org. In case of
	 * exception return empty string.
	 * 
	 * @return List of Currencies.
	 */
	public static List<String> getCurrencyList() {

		if (CCY_LIST != null && !CCY_LIST.isEmpty()) {
			return CCY_LIST;
		}

		InputStream is = null;
		try {
			is = new URL(URL_PREFIX + URL_CCY_LIST_SUFFIX).openStream();
			JsonParser parser = Json.createParser(new InputStreamReader(is, Charset.forName("UTF-8")));
			StringBuilder strbuild = new StringBuilder();
			while (parser.hasNext()) {
				JsonParser.Event event = parser.next();
				switch (event) {
				case KEY_NAME:
					strbuild.append(parser.getString()).append(" - ");
					break;
				case VALUE_STRING:
				case VALUE_NUMBER:
					strbuild.append(parser.getString());
					CCY_LIST.add(strbuild.toString());
					strbuild = new StringBuilder();
					break;
				default:
					break;
				}
			}
		} catch (MalformedURLException e) {
			LOG.error("Error while opening URL stream", e);
		} catch (IOException e) {
			LOG.error("Error while opening URL stream", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("Error while closing input stream", e);
				}
			}

		}
		return CCY_LIST;

	}

	/**
	 * Return the list of Base Currencies from openexchangerates.org. Currently
	 * only USD - US Doller will be return as other currencies are premium
	 * service. In case of exception return empty string.
	 * 
	 * @return List of Currencies.
	 */
	public static List<String> getBaseCurrencyList() {
		List<String> ls = new ArrayList<String>();

		ls.add("USD - United States Doller");
		// in future list can be replace with CCY_LIST with premium service from
		// openexchangerates.org
		return ls;
	}

}
