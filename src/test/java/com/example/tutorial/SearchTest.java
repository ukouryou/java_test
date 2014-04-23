/**
 *
 */
package com.example.tutorial;

import org.junit.Test;

/**
 * @author andy
 *
 */
public class SearchTest {

    String baseURL = "http://search.tianji.com/";


    @Test
    public void testPSearch() {
       /* final String psearchURL = baseURL + "psearch?keyword=中国&rows=10&userScope=ALL";
        PSearchResponse response = RestTemplateHelper.getRestTemplate().getForObject(psearchURL, PSearchResponse.class);
        System.out.println("-------------print psearch resposne ------------------");
        System.out.println("result Numfound:" + response.getNumFound());
        System.out.println("user List:");
        System.out.println("user List Size:" + response.getUserList().size());
        for (ResponseUserVO userVO: response.getUserList()) {
            System.out.println("id:" +userVO.getUserId());
            System.out.println("skills:" +userVO.getSkillList().toString());
        }

        System.out.println("facet response:");

        for (FacetResponse facetResponse : response.getFacetResponseList()) {
            System.out.println("facet.field:" + facetResponse.getField());
            for (KVPair pair : facetResponse.getFacetItemList()) {
                System.out.println(pair.getKey());
                System.out.println(pair.getValue());
            }
            System.out.println("****************************************");
        }*/

    }

}
