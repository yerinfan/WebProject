package kr.ac.kopo.resource;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;

public class MultipartInputStreamFileResource extends InputStreamResource {
    private final String filename;

    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return -1; // length 가 불명확하므로 -1 반환
    }
}
