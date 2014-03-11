package search.test.company.vo;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchResponse<E> implements Serializable {

    private static final long serialVersionUID = -4582710141834984732L;

    private long totalItems;
    private List<E> items;

    public long getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
    public List<E> getItems() {
        return items;
    }
    public void setItems(List<E> items) {
        this.items = items;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class BasicItem {
        private String id;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class CompanyItem extends BasicItem{
        private String companyName;
        private int staffCount;
        private int followerCount;
        private String logoFileName;

        public String getCompanyName() {
            return companyName;
        }
        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
        public int getStaffCount() {
            return staffCount;
        }
        public void setStaffCount(int staffCount) {
            this.staffCount = staffCount;
        }
        public int getFollowerCount() {
            return followerCount;
        }
        public void setFollowerCount(int followerCount) {
            this.followerCount = followerCount;
        }
        public String getLogoFileName() {
            return logoFileName;
        }
        public void setLogoFileName(String logoFileName) {
            this.logoFileName = logoFileName;
        }
    }

}
