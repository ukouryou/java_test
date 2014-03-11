package search.test.company.vo;

import java.io.Serializable;

public class CompanySearchVO implements Serializable{

    private static final long serialVersionUID = 8400666550954456567L;

    private String companyId;
    private String companyName;

    public int start;
    public int rows;

    public String queryParameters;

    private SEARCHTYPE searchType;

    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public String getQueryParameters() {
        return queryParameters;
    }
    public void setQueryParameters(String queryParameters) {
        this.queryParameters = queryParameters;
    }
    public SEARCHTYPE getSearchType() {
        return searchType;
    }
    public void setSearchType(SEARCHTYPE searchType) {
        this.searchType = searchType;
    }



    public static enum SEARCHTYPE {
        AUTOCOMPLETE_SEARCH,COMMON_SEARCH;
    }

    @Override
    public String toString() {
        return "CompanySearchVO [companyId=" + companyId + ", companyName="
                + companyName + ", start=" + start + ", rows=" + rows
                + ", queryParameters=" + queryParameters + "]";
    }

}