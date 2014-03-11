package search.test.company.vo;

import java.io.Serializable;

public class Schema_CompanyVO implements Serializable {

    private static final long serialVersionUID = -5365055444283309626L;

    private int id;
    private String name;
    private String logoFileName;
    private int staffCount;
    private int followerCount;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogoFileName() {
        return logoFileName;
    }
    public void setLogoFileName(String logoFileName) {
        this.logoFileName = logoFileName;
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

}
