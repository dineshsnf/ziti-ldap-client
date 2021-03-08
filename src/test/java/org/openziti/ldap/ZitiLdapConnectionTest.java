package org.openziti.ldap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.ldaptive.LdapException;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResponse;
import org.openziti.ZitiApp;
import org.openziti.ZitiContext;
import org.slf4j.LoggerFactory;

/**
 * Sample for Ziti LDAP Connection
 */
public class ZitiLdapConnectionTest
{
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ZitiLdapConnectionTest.class);

    public void listUsers() throws LdapException {

        // should be a enrolled and valid ziti identity
        String credJson = "{\"ztAPI\":\"https://10.0.0.1:443\",\"id\":{\"key\":\"pem:-----BEGIN EC PRIVATE KEY-----\\nMIGvJiQISo=\\n-----END EC PRIVATE KEY-----\\n\",\"cert\":\"pem:-----BEGIN CERTIFICATE-----\\nMIID3jZEFQ==\\n-----END CERTIFICATE-----\\n\",\"ca\":\"pem:-----BEGIN CERTIFICATE-----\\nMIIFvNd+oI=\\n-----END CERTIFICATE-----\\n\"},\"configTypes\":null}";

        ZitiContext zitiContext = new ZitiApp.CredentialBuilder().fromJson(credJson).build().getContext();

        ZitiLdapConnectionConfig zitiLdapConnectionConfig = new ZitiLdapConnectionConfig.Builder().service("ad ldap tcp - ad.sandbox.internal").bindDn("sandbox\\xxxx").bindPass("xxxxx").build();

        ZitiLdapConnection zitiLdapConnection = new ZitiLdapConnection(zitiContext,zitiLdapConnectionConfig);

        zitiLdapConnection.open();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setBaseDn("OU=sandbox,DC=ad,DC=sandbox,DC=netfoundry,DC=io");
        searchRequest.setFilter("(&(objectClass=user))");
        searchRequest.setReturnAttributes("sn","givenName", "samAccountName");

        org.ldaptive.SearchOperationHandle searchOperationHandle = zitiLdapConnection.operation(searchRequest);

        SearchResponse searchResponse = searchOperationHandle.execute();

        log.info("Search response status : {}",searchResponse.isSuccess());

        searchResponse.getEntries().stream().forEach(ldapEntry -> ldapEntry.getAttributes().stream().forEach(ldapAttribute -> {
            log.info("Attribute Name : {}  Attributte Value : {}",ldapAttribute.getName(),ldapAttribute.getStringValue());
        }));

        zitiLdapConnection.close();

        zitiLdapConnection.open();

        searchOperationHandle = zitiLdapConnection.operation(searchRequest);

        searchResponse = searchOperationHandle.execute();

        log.info("Search response status : {}",searchResponse.isSuccess());

        Assertions.assertTrue(searchResponse.isSuccess());

        zitiLdapConnection.close();
    }
}
