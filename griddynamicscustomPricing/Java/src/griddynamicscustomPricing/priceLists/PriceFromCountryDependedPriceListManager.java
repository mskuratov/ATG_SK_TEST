package com.griddynamics.customPricing.priceLists;

import atg.commerce.pricing.priceLists.PriceListManager;
import atg.repository.*;

public class PriceFromCountryDependedPriceListManager extends PriceListManager {
    private static final String COUNTRY           = "country";
    private static final String STATE             = "state";
    private static final String PRICE_LIST_DEP    = "Country_State_Price_List_Dep";
    private static final String PRICE_LIST        = "priceList";
    private static final String PRICE_LIST_ID     = "priceListId";
    private Repository csBasedPriceList;

    public void setCsBasedPriceList(Repository csBasedPriceList) {
        this.csBasedPriceList = csBasedPriceList;
    }

    public Repository getCsBasedPriceList() {
        return this.csBasedPriceList;
    }

    public String getCountryFromPriceDependedPriceListId(String country, String state) {
        RepositoryView priceListView = null;
        Repository csbpl = getCsBasedPriceList();
        try {
            priceListView = csbpl.getView(PRICE_LIST_DEP);
        } catch (RepositoryException e) {}
        if (priceListView != null) {
            QueryBuilder qb = priceListView.getQueryBuilder();
            try {
                Query[] queries = new Query[2];
                QueryExpression countryPropExp = qb.createPropertyQueryExpression(COUNTRY);
                QueryExpression countryConsExp = qb.createConstantQueryExpression(country);
                queries[0] = qb.createComparisonQuery(countryPropExp, countryConsExp, 4);
                QueryExpression statePropExp = qb.createPropertyQueryExpression(STATE);
                QueryExpression stateConsExp = qb.createConstantQueryExpression(state);
                queries[1] = qb.createComparisonQuery(statePropExp, stateConsExp, 4);
                Query query = qb.createAndQuery(queries);
                RepositoryItem[] csBasedPriceLists = priceListView.executeQuery(query);
                if ((csBasedPriceLists != null) && (csBasedPriceLists.length == 1))
                    return csBasedPriceLists[0].getPropertyValue(PRICE_LIST_ID).toString().replace("priceList:", "");
            } catch (RepositoryException e) {
                logError("Couldn't get price list id. Will search for price in user's profile prise list");
            }
        }
        return null;
    }

    public RepositoryItem getCountryDependedPriceListById(String id) {
        Repository plr = getPriceListRepository();
        try {
            RepositoryView priceListView = plr.getView(PRICE_LIST);
            QueryBuilder qb = priceListView.getQueryBuilder();
            QueryExpression plPropExp = qb.createPropertyQueryExpression("id");
            QueryExpression plConsExp = qb.createConstantQueryExpression(id);
            Query query = qb.createComparisonQuery(plPropExp, plConsExp, 4);
            RepositoryItem[] ri = priceListView.executeQuery(query);
            if ((ri != null) && (ri.length == 1)) {
                return ri[0];
            }
        } catch (RepositoryException e) {
            if (isLoggingDebug())
                logError("Couldn't get priceList by id. Will search for price in user's profile prise list");
        }
        return null;
    }
}