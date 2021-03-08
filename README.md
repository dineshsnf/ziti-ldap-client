# ziti-ldap-client
LDAP client to connect and operate on directory servers protected by a ziti network

# 1. Add dependency in your application
*********************************************************************
```html
<dependency>
    <groupId>org.openziti</groupId>
    <artifactId>ziti-ldap-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

# 2. Ziti Context Initialization 
*********************************************************************
a) Use the JWT token and ziti tunneler to enroll  and create ziti credentials json file    
```html
ziti-tunnel enroll --jwt ZitifiedAD.jwt
```

b) Store the generated credentials json in your application's secret vault and load the same into your applicaiton's environment
```html
String credJson = "{\"ztAPI\":\"https://10.0.0.1:443\",\"id\":{\"key\":\"pem:-----BEGIN EC PRIVATE KEY-----\\nMIGvJiQISo=\\n-----END EC PRIVATE KEY-----\\n\",\"cert\":\"pem:-----BEGIN CERTIFICATE-----\\nMIID3jZEFQ==\\n-----END CERTIFICATE-----\\n\",\"ca\":\"pem:-----BEGIN CERTIFICATE-----\\nMIIFvNd+oI=\\n-----END CERTIFICATE-----\\n\"},\"configTypes\":null}";
```

c) Create Ziti Context
```html
ZitiContext zitiContext = new ZitiApp.CredentialBuilder().fromJson(credJson).build().getContext();
```

# 3. Initialize and open LDAP connection
*********************************************************************
```html
ZitiLdapConnectionConfig zitiLdapConnectionConfig = new ZitiLdapConnectionConfig.Builder().service("ad ldap tcp - ad.sandbox.internal").bindDn("sandbox\\xxxx").bindPass("xxxxx").build();

ZitiLdapConnection zitiLdapConnection = new ZitiLdapConnection(zitiContext,zitiLdapConnectionConfig);

zitiLdapConnection.open();
```

# 4. Search and filter LDAP Users
*********************************************************************
```html
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

```

# 5. Close LDAP connection
*********************************************************************
```html
zitiLdapConnection.close();
```