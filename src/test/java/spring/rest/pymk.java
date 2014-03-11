/**
 *
 */
package spring.rest;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * Feb 12, 2014
 * @author andy
 */
public class pymk {

    /**
     * @throws java.lang.Exception
     */
    //@BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void test() {
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.getForObject("http://10.40.3.65:8000/suggestion/pymk/31477123/1,6?source=dashboard", PYMKResponse.class);

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
            System.out.println(i);
        }
        List<Integer> subList = list.subList(1, 4);
        for (Integer integer : subList) {
            System.out.println(integer);
        }
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize(include = Inclusion.NON_NULL)
    static class PYMKResponse{
        private int itemAmount;
        private List<ResponseData> items;
        public int getItemAmount() {
            return itemAmount;
        }
        public void setItemAmount(int itemAmount) {
            this.itemAmount = itemAmount;
        }
        public List<ResponseData> getItems() {
            return items;
        }
        public void setItems(List<ResponseData> items) {
            this.items = items;
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        static class ResponseData{
            private int userId;
            private String[] pymkTypes;
            public int getUserId() {
                return userId;
            }
            public void setUserId(int userId) {
                this.userId = userId;
            }
            public String[] getPymkTypes() {
                return pymkTypes;
            }
            public void setPymkTypes(String[] pymkTypes) {
                this.pymkTypes = pymkTypes;
            }
        }
    }

}
