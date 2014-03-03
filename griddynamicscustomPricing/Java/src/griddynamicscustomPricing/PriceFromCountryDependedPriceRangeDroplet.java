package com.griddynamics.customPricing;

import atg.adapter.gsa.GSAItem;
import atg.commerce.pricing.PriceRangeDroplet;
import atg.commerce.pricing.priceLists.PriceListException;
import atg.commerce.pricing.priceLists.PriceListManager;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.servlet.ServletUtil;
import com.griddynamics.customPricing.priceLists.PriceFromCountryDependedPriceListManager;

import javax.servlet.ServletException;
import java.io.IOException;

public class PriceFromCountryDependedPriceRangeDroplet extends PriceRangeDroplet {
    private static final String SHIPPING_ADDRESS  = "shippingAddress";
    private static final String COUNTRY           = "Country";
    private static final String STATE             = "State";
    private boolean priceDependsOnCountry = true;
    private PriceFromCountryDependedPriceListManager mPriceListManager;

    public PriceFromCountryDependedPriceListManager getPriceListManager() {
        return this.mPriceListManager;
    }

    public void setPriceListManager(PriceListManager pPriceListManager) {
        this.mPriceListManager = (PriceFromCountryDependedPriceListManager)pPriceListManager;
    }

    public boolean isPriceDependsOnCountry() {
        return this.priceDependsOnCountry;
    }

    public void setPriceDependsOnCountry(boolean priceDependsOnCountry) {
        this.priceDependsOnCountry = priceDependsOnCountry;
    }

    public void service(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
            throws ServletException, IOException
    {
        Object productId = pRequest.getObjectParameter(PRODUCT_ID);
        RepositoryItem profile = ServletUtil.getCurrentUserProfile();
        GSAItem address = (GSAItem)profile.getPropertyValue(SHIPPING_ADDRESS);
        RepositoryItem priceList = null;
        String csBasedPriceListId = null;
        double highestPrice = 0.0D;
        double lowestPrice = 0.0D;
        if ((isPriceDependsOnCountry()) && (address != null) && (productId != null)) {
            String country = (String)address.getPropertyValue(COUNTRY);
            String state = (String)address.getPropertyValue(STATE);
            logDebug("DISPOSABLE!Country = " + country);
            logDebug("DISPOSABLE!State = " + state);
            try {
                PriceFromCountryDependedPriceListManager plManager = getPriceListManager();
                csBasedPriceListId = plManager.getCountryFromPriceDependedPriceListId(country, state); //Getting SCBased priceListId for user
                if (csBasedPriceListId != null) {
                    priceList = plManager.getCountryDependedPriceListById(csBasedPriceListId); //Getting SCBased priceList for user
                }
                if (productId != null) {  //Business logic from parent class, using CSBased price list as a stab for salePriceList
                    RepositoryItem product = getCatalogRepository().getItem(productId.toString(), getProductItemType());
                    if (product != null) {
                        if (isUsingPriceLists()) {
                            highestPrice = getPricingTools().retrieveHighestPriceListPrice(product, priceList, priceList);
                            lowestPrice = getPricingTools().retrieveLowestPriceListPrice(product, priceList, priceList);
                            setHighLowPriceOutputParams(pRequest, highestPrice, lowestPrice);
                        }
                        else {
                            highestPrice = getPricingTools().retrieveHighestChildSKUPrice(product);
                            lowestPrice = getPricingTools().retrieveLowestChildSKUPrice(product);
                            setHighLowPriceOutputParams(pRequest, highestPrice, lowestPrice);
                        }
                    }
                }
            } catch (RepositoryException e) {
                if (isLoggingDebug()) {
                    logDebug("Couldn't get price list for product/sku from repository. Will try with user regular price list");
                }
            } catch (PriceListException e) {
                if (isLoggingDebug()) {
                    logDebug("Couldn't get price for product/sku from Country/State based price list. Will try from user regular price list");
                }
            } finally {
                if ((lowestPrice != 0.0D) && (highestPrice != 0.0D)) {
                    pRequest.serviceLocalParameter(OUTPUT, pRequest, pResponse);
                } else {
                    super.service(pRequest, pResponse);
                }
            }
        } else {
            super.service(pRequest, pResponse);
        }
    } //service
}