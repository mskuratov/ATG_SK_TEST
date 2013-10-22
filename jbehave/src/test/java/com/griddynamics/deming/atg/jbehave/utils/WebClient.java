package com.griddynamics.deming.atg.jbehave.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebClient {

    public static String downloadContent(URL url) throws IOException {
        InputStream in = url.openStream();

        StringBuilder contentBuilder = new StringBuilder();

        byte[] buffer = new byte[256];

        while(true){
            int byteRead = in.read(buffer);

            if(byteRead == -1) {
                break;
            }

            for(int i = 0; i < byteRead; ++i){
                contentBuilder.append((char) buffer[i]);
            }
        }
        return contentBuilder.toString();
    }
}
