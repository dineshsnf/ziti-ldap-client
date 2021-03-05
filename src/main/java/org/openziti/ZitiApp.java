package org.openziti;


import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ZitiApp {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(ZitiApp.class);

    private static final String TEMP_FILE_PREFIX = "token";

    private static final String TEMP_FILE_SUFFIX = "jwt";

    private static final char[] NO_PASSWORD = {};

    private ZitiContext context;

    private ZitiApp(ZitiContext context) {
        this.context = context;
    }

    public static class CredentialBuilder {
        private File jwt;

        public CredentialBuilder fromJson(String jsonInput) {
            InputStream jwtInStream = null;
            OutputStream jwtOutStream = null;
            try {
                this.jwt = File.createTempFile(TEMP_FILE_PREFIX,TEMP_FILE_SUFFIX);
                jwtInStream = new ByteArrayInputStream(jsonInput.getBytes());
                jwtOutStream = new FileOutputStream(this.jwt);
                IOUtils.copy(jwtInStream, jwtOutStream);
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format("Failed to read credentials - cause : %s",e));
            }finally {
                try {
                    if(jwtOutStream!=null)
                        jwtOutStream.close();

                    if(jwtInStream!=null)
                        jwtInStream.close();

                } catch (IOException e) {
                    throw new IllegalArgumentException(String.format("Failed to read credentials - cause : %s",e));
                }
            }
            return this;
        }

        public ZitiApp build(){
            return new ZitiApp(Ziti.newContext(jwt,NO_PASSWORD));
        }
    }

    public ZitiContext getContext() {
        return this.context;
    }
}
