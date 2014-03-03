package com.griddynamics.customPricing.priceLists;

import atg.adapter.gsa.GSAItem;
import atg.commerce.order.CommerceItem;
import atg.commerce.pricing.ItemPriceInfo;
import atg.commerce.pricing.PricingException;
import atg.commerce.pricing.priceLists.ItemPriceCalculator;
import atg.commerce.pricing.priceLists.ItemSchemePriceCalculator;
import atg.commerce.pricing.priceLists.PriceListException;
import atg.commerce.pricing.priceLists.PriceListManager;
import atg.repository.RepositoryItem;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class PriceFromCountryDependedItemPriceCalculator extends ItemPriceCalculator {
    private static final String SHIPPING_ADDRESS  = "shippingAddress";
    private static final String COUNTRY           = "Country";
    private static final String STATE             = "State";
    private boolean priceDependsOnCountry = true;
    private PriceFromCountryDependedPriceListManager mPriceListManager;

    public boolean isPriceDependsOnCountry() {
        return priceDependsOnCountry;
    }

    public void setPriceDependsOnCountry(boolean priceDependsOnCountry) {
        this.priceDependsOnCountry = priceDependsOnCountry;
    }

    public PriceFromCountryDependedPriceListManager getPriceListManager() {
        return mPriceListManager;
    }

    public void setPriceListManager(PriceListManager pPriceListManager) {
        this.mPriceListManager = (PriceFromCountryDependedPriceListManager)pPriceListManager;
    }

    @Override
    public void priceItem(ItemPriceInfo pPriceQuote, CommerceItem pItem, RepositoryItem pPricingModel, Locale pLocale,
                            RepositoryItem pProfile, Map pExtraParameters) throws PricingException
    {
        boolean success = false;
        RepositoryItem price = null;
        String country = null;
        String state = null;
        GSAItem address = (GSAItem) pProfile.getPropertyValue(SHIPPING_ADDRESS);
        if (address != null) {
            country = (String)address.getPropertyValue(COUNTRY);
            state = (String)address.getPropertyValue(STATE);
        }
        String sku = pItem.getCatalogRefId();
        String product = pItem.getAuxiliaryData().getProductId();
        RepositoryItem priceList = null;
        String csBasedPriceListId = null;
        try {
            PriceFromCountryDependedPriceListManager plManager = getPriceListManager();
            if (priceDependsOnCountry && plManager != null && country != null && state != null &&
                    (sku != null || product != null)) {
                csBasedPriceListId = plManager.getCountryFromPriceDependedPriceListId(country, state); //Getting SCBased priceListId for user
            }
            if (csBasedPriceListId != null) {
                priceList = plManager.getCountryDependedPriceListById(csBasedPriceListId);  //Getting SCBased priceList for user
            }
            if (priceList != null) {
                price = plManager.getPrice(priceList, product, sku);
            }
        } catch (PriceListException e) {
            if (isLoggingDebug()) {
                logDebug("Couldn't get price for product/sku from Country/State based price list. Will try from user regular price list");
            }
        }

        if (price != null) { //Business logic from parent class
            Properties pricingSchemeNames = getPricingSchemeNames();
            String pricingSchemeName;
            pricingSchemeName = getPricingScheme(price);
            if (pricingSchemeNames != null) {
                String calculatorName = pricingSchemeNames.getProperty(pricingSchemeName);
                ItemSchemePriceCalculator schemeCalculator = (ItemSchemePriceCalculator)resolveName(calculatorName, true);
                if (schemeCalculator != null) {
                    schemeCalculator.priceItem(price, pPriceQuote, pItem, pPricingModel, pLocale, pProfile, pExtraParameters);
                    pPriceQuote.setPriceList(priceList);
                    success = true;
                }
            }
        }
        if (!success) {
             super.priceItem(pPriceQuote, pItem, pPricingModel, pLocale, pProfile, pExtraParameters);
        }
    }
}

