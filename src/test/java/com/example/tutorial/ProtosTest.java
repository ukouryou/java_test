/**
 *
 */
package com.example.tutorial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.example.tutorial.AddressBookProtos.Person;
import com.tianji.search.vo.UserProtos.User;

/**
 * @author andy
 *
 */
public class ProtosTest {

    Person.Builder john = Person.newBuilder().setId(0).setName("who am I").setEmail("xx.ss@tianji.com");

    String url = "http://search.tianji.com/user/searchConverter?name=alex1&page=22";

    String url2 = "http://search.tianji.com/psearch";

    @Test
    public void testMergeFrom() {
         try {
            Person.Builder builderPerson = john.mergeFrom(new FileInputStream("/home/andy/work/protoFile"));
            System.out.println(builderPerson.getEmail());
            System.out.println(builderPerson.getId());
            System.out.println(builderPerson.getName());
            builderPerson.setId(10);
            System.out.println(builderPerson.getId());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testWriterTo(){
        Person person = john.build();
        try {
            person.writeTo(new FileOutputStream("/home/andy/work/protoFile"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testRetrieveUsser() throws Exception{
       // User user = RestTemplateHelper.getRestTemplate().getForObject(url, User.class);

//        URL target = new URL(url);
//        HttpURLConnection conn = (HttpURLConnection) target.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-Type", "application/x-protobuf");
//        conn.setRequestProperty("Accept", "application/x-protobuf");
//        conn.connect();
//        // check response code
//        int code = conn.getResponseCode();
//        System.out.println("code:" + code);
//        System.out.println(conn.getContent());
//        boolean success = (code >= 200) && (code < 300);
//
//        InputStream in = success ? conn.getInputStream() : conn.getErrorStream();
//        User user = User.parseFrom(in);
//        in.close();
        //System.out.println(user.toString());

    }

    @Test
    public void testUpdateUsser() throws Exception{


        User john = User.newBuilder()
                .setName("John Doe")
                .setPage(22)
                .build();
                //byte[] content = john.toByteArray();

                URL targetUrl = new URL(url2);
                HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/x-protobuf");
                connection.setRequestProperty("Accept", "application/x-protobuf");
                connection.setRequestMethod("POST");
                //connection.setRequestProperty("Connect-Length", Integer.toString(content.length));
                //connection.setFixedLengthStreamingMode(content.length);
                OutputStream outputStream = connection.getOutputStream();
                john.writeTo(outputStream);
                //outputStream.write(content);
                outputStream.flush();
                String message = connection.getResponseMessage();
                System.out.println(message);
                outputStream.close();


    }

    @Test
    public void testUpdate() {
        User user = User.newBuilder().setName("alex").setPage(10).build();
       // RestTemplateHelper.getRestTemplate().postForEntity(url2, user, null);
    }

    @Test
    public void testGet() {
       // RestTemplateHelper.getRestTemplate().getForObject(url, User.class);
    }

}
