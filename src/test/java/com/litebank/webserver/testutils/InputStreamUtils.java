package com.litebank.webserver.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class InputStreamUtils {
    public static <T> ServletInputStream getServletInputStream(ObjectMapper objectMapper, T value) throws JsonProcessingException {
        var byteArrayInputStream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(value));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
