import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        invoke();
    }

    public static String invoke() {
        String bodyAsString = ""; //Provide Input SOAP Message

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new NTCredentials("UserName", "Password", "Host", "Domain"));

        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credsProvider).build();


        HttpPost post = new HttpPost("URL"); //Provide Request URL


        try {

            StringEntity input = new StringEntity(bodyAsString);
            input.setContentType("text/xml; charset=utf-8");
            post.setEntity(input);

            post.setHeader("Content-type", "text/xml; charset=utf-8");
            post.setHeader("SOAPAction", ""); //Provide Soap action

            org.apache.http.HttpResponse response = client.execute(post);

            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                return EntityUtils.toString(responseEntity);

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
