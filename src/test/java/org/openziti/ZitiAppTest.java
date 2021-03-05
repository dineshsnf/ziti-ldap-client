package org.openziti;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Ziti App
 */
public class ZitiAppTest {

    @Test
    public void testGetContext() {
        String credJson = "{\"ztAPI\":\"https://10.0.0.1:443\",\"id\":{\"key\":\"pem:-----BEGIN EC PRIVATE KEY-----\\nMIGvJiQISo=\\n-----END EC PRIVATE KEY-----\\n\",\"cert\":\"pem:-----BEGIN CERTIFICATE-----\\nMIID3jZEFQ==\\n-----END CERTIFICATE-----\\n\",\"ca\":\"pem:-----BEGIN CERTIFICATE-----\\nMIIFvNd+oI=\\n-----END CERTIFICATE-----\\n\"},\"configTypes\":null}";
        try {
            ZitiApp zitiApp = new ZitiApp.CredentialBuilder().fromJson(credJson).build();
            Assertions.fail("Not expected to pass");
        }catch(Exception e){
            Assertions.assertTrue(e.getMessage().contains("unsupported format"));
        }
    }
}
