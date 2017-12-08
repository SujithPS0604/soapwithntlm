# SOAP with NTLM
Sample Java application to use NTLM authentication with SOAP

# NTLM

NTLM credentials are based on data obtained during the interactive logon process and consist of a domain name, a user name, and a one-way hash of the user's password. 

NTLM uses an encrypted challenge/response protocol to authenticate a user without sending the user's password over the wire. Instead, the system requesting authentication must perform a calculation that proves it has access to the secured NTLM credentials.

Interactive NTLM authentication over a network typically involves two systems:
   - a client system, where the user is requesting authentication, 
   - a domain controller, where information related to the user's password is kept. 

Noninteractive authentication, which may be required to permit an already logged-on user to access a resource such as a server application, typically involves three systems: 
	- a client, 
	- a server, 
	- a domain controller that does the authentication calculations on behalf of the server.

# Working of NTLM

The following steps present an outline of NTLM non-interactive authentication. 

The first step provides the user's NTLM credentials and occurs only as part of the interactive authentication (logon) process.

1. (Interactive authentication only) A user accesses a client computer and provides a domain name, user name, and password. The client computes a cryptographic hash of the password and discards the actual password.

2. The client sends the user name to the server (in plaintext).

3. The server generates a 16-byte random number, called a challenge or nonce, and sends it to the client.

4. The client encrypts this challenge with the hash of the user's password and returns the result to the server. This is called the response.

5. The server sends the following three items to the domain controller:

	-	User name

	-	Challenge sent to the client

	-	Response received from the client

6. The domain controller uses the user name to retrieve the hash of the user's password from the Security Account Manager database. It uses this password hash to encrypt the challenge.

7. The domain controller compares the encrypted challenge it computed (in step 6) to the response computed by the client (in step 4). If they are identical, authentication is successful.

Your application should not access the NTLM security package directly; instead, it should use the Negotiate security package. Negotiate allows your application to take advantage of more advanced security protocols if they are supported by the systems involved in the authentication. Currently, the Negotiate security package selects between Kerberos and NTLM. Negotiate selects Kerberos unless it cannot be used by one of the systems involved in the authentication.


# A Sample Java Client

```java

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

            for (Header header : post.getAllHeaders()) {
                System.out.println(header.getName() + " : " + header.getValue());
            }
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


```