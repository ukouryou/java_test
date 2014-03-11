/**
 *
 */
package search.test;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import search.test.company.vo.CompanySearchVO;
import search.test.company.vo.SearchResponse;
import search.test.company.vo.SearchResponse.BasicItem;

/**
 * @author andy
 *
 */
public class CompanySearchClientTest {

    private RestTemplate restTemplate = new RestTemplate();

    private final String serchHost = "http://search.tianji.com/";

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCompanySearch () {

        CompanySearchVO companySearchVO = new CompanySearchVO();
        companySearchVO.setCompanyName("四川");
        companySearchVO.setRows(10);

        Object object = restTemplate.postForObject(this.serchHost + "company/search", companySearchVO, Object.class);
        System.out.println(object.getClass());

        SearchResponse<BasicItem> searchResponse = mapper.convertValue(object, new TypeReference<SearchResponse<BasicItem>>() {});

//        ResponseEntity<SearchResponse> response = restTemplate.postForEntity(this.serchHost + "company/search", companySearchVO, SearchResponse.class);
//
//        SearchResponse<BasicItem> searchResponse = response.getBody();
        System.out.println(searchResponse.getTotalItems());
        System.out.println(searchResponse.getItems().size());
        if (null != searchResponse && null != searchResponse.getItems() && 0 < searchResponse.getItems().size()) {
            for (BasicItem item : searchResponse.getItems()) {
                System.out.println(item.getId());
            }
        }

    }
    @Test
    public void testNickname() {
       // String responseData = restTemplate.getForObject("http://www.tianji.com/rest/v2/profile/info/nickname/57984648", String.class);
         //   System.out.println(responseData);
        NickNameResponseData responseData = restTemplate.getForObject("http://www.tianji.com/rest/v2/profile/info/nickname/57984648", NickNameResponseData.class);
        System.out.println(responseData.getStatus());
        System.out.println(responseData.getData().getNickname());
        if (null != responseData && 1 == responseData.getStatus()) {
            System.out.println(responseData.getData().getNickname());
        }

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize(include = Inclusion.NON_NULL)
    static class NickNameResponseData{
        private int status;
        private ResponseData data;
        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        public ResponseData getData() {
            return data;
        }
        public void setData(ResponseData data) {
            this.data = data;
        }

        static class ResponseData{
            private String nickname;
            public String getNickname() {
                return nickname;
            }
            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
