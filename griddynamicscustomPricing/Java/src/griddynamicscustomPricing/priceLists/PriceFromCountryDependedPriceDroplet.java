package com.griddynamics.customPricing.priceLists;

import atg.adapter.gsa.GSAItem;
import atg.commerce.pricing.priceLists.PriceDroplet;
import atg.commerce.pricing.priceLists.PriceListException;
import atg.commerce.pricing.priceLists.PriceListManager;
import atg.nucleus.naming.ParameterName;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;

import javax.servlet.ServletException;
import java.io.IOException;

public class PriceFromCountryDependedPriceDroplet extends PriceDroplet {
    private static final ParameterName OUTPUT     = ParameterName.getParameterName("output");
    private static final ParameterName PRODUCT    = ParameterName.getParameterName("product");
    private static final ParameterName SKU        = ParameterName.getParameterName("sku");
    private static final ParameterName PARENT_SKU = ParameterName.getParameterName("parentSku");
    private static final ParameterName EMPTY      = ParameterName.getParameterName("empty");
    private static final ParameterName PRICE_LIST = ParameterName.getParameterName("priceList");
    private static final String SHIPPING_ADDRESS  = "shippingAddress";
    private static final String SALE_PRICE_LIST   = "salePriceList";
    private static final String COUNTRY           = "Country";
    private static final String STATE             = "State";
    private static final String PRICE             = "price";
    private PriceFromCountryDependedPriceListManager mPriceListManager;
    private boolean priceDependsOnCountry = true;

    public boolean isPriceDependsOnCountry()
    {
        return this.priceDependsOnCountry;
    }

    public void setPriceDependsOnCountry(boolean priceDependsOnCountry) {
        this.priceDependsOnCountry = priceDependsOnCountry;
    }

    public PriceFromCountryDependedPriceListManager getPriceListManager()
    {
        return this.mPriceListManager;
    }

    public void setPriceListManager(PriceListManager pPriceListManager)
    {
        this.mPriceListManager = (PriceFromCountryDependedPriceListManager) pPriceListManager;
    }

    public void service(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
            throws ServletException, IOException
    {
        Object product = pRequest.getObjectParameter(PRODUCT);
        Object sku = pRequest.getObjectParameter(SKU);
        Object parentSku = pRequest.getObjectParameter(PARENT_SKU);
        Object price = null;
        RepositoryItem profile = ServletUtil.getCurrentUserProfile();
        GSAItem address = (GSAItem)profile.getPropertyValue(SHIPPING_ADDRESS);
        Object jspPriceList = pRequest.getObjectParameter(PRICE_LIST);
        GSAItem salePriceList = (GSAItem)profile.getPropertyValue(SALE_PRICE_LIST);
        RepositoryItem priceList = null;
        String csBasedPriceListId;
        if ((isPriceDependsOnCountry()) && (address != null) && ((product != null) || (sku != null))) {
            String country = (String)address.getPropertyValue(COUNTRY);
            String state = (String)address.getPropertyValue(STATE);
            logDebug("DISPOSABLE!Country = " + country);
            logDebug("DISPOSABLE!State = " + state);
            try {
                PriceFromCountryDependedPriceListManager plManager = getPriceListManager();
                csBasedPriceListId = plManager.getCountryFromPriceDependedPriceListId(country, state); //Getting SCBased priceListId for user
                if (csBasedPriceListId != null) {
                    priceList = plManager.getCountryDependedPriceListById(csBasedPriceListId);  //Getting SCBased priceList for user
                }
                if (priceList != null) {
                    if (parentSku != null) { //Business logic from parent class
                        if ((product instanceof RepositoryItem)) {
                            price = plManager.getPrice(priceList, (RepositoryItem)product, (RepositoryItem)sku, (RepositoryItem)parentSku);
                        }
                        else if ((product instanceof String)) {
                            price = plManager.getPrice(priceList, (String)product, (String)sku, (String)parentSku);
                        }
                        else if ((sku instanceof RepositoryItem)) {
                            price = plManager.getPrice(priceList, (RepositoryItem)product, (RepositoryItem)sku, (RepositoryItem)parentSku);
                        }
                        else if ((sku instanceof String)) {
                            price = plManager.getPrice(priceList, (String)product, (String)sku, (String)parentSku);
                        }

                    }
                    else if ((product instanceof RepositoryItem)) {
                        price = plManager.getPrice(priceList, (RepositoryItem)product, (RepositoryItem)sku);
                    }
                    else if ((product instanceof String))
                        price = plManager.getPrice(priceList, (String)product, (String)sku);
                    else if ((sku instanceof RepositoryItem)) {
                        price = plManager.getPrice(priceList, (RepositoryItem)product, (RepositoryItem)sku);
                    }
                    else if ((sku instanceof String))
                        price = plManager.getPrice(priceList, (String)product, (String)sku);
                }
            }
            catch (PriceListException e) {
                if (isLoggingDebug())
                    logDebug("Couldn't get price for product/sku from Country/State based price list. Will try from user regular price list");
            } finally {
                if (price != null) {
                    if (jspPriceList != null && salePriceList != null && jspPriceList.equals(salePriceList)) {
                        pRequest.serviceLocalParameter(EMPTY, pRequest, pResponse); //Setting empty sale price as a stab
                    } else {
                        pRequest.setParameter(PRICE, price);
                        pRequest.serviceLocalParameter(OUTPUT, pRequest, pResponse);
                    }
                } else {
                    super.service(pRequest, pResponse);
                }
            }
        } else {
            super.service(pRequest, pResponse);
        }
    } //service
}