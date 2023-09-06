package com.kzh.imageuploadapp.filestore;

import com.amazonaws.AmazonServiceException;
//import com.amazonaws.services.lightsail.model.PutAlarmRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetadata,
                     InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();

        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });

        try {
            System.out.println("path: "+ path);
            System.out.println("fileName: "+ fileName);
            System.out.println("inputStream: "+ inputStream);
            PutObjectRequest putRequest = new PutObjectRequest(path, fileName, inputStream, metadata);
            putRequest.getRequestClientOptions().setReadLimit(10000000);
            s3.putObject(putRequest);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }


//    public static void uploadFile(String fileName, InputStream inputStream) {
//        S3Client client = S3Client.builder().build();
//
//        System.out.println("fileName: "+ fileName);
//        System.out.println("inputStream: "+ inputStream);
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(BUCKET)
//                .key(fileName)
//                .build();
//
//        client.putObject(request,
//                RequestBody.fromInputStream(inputStream, inputStream.available()));
//    }

    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download file to s3", e);
        }
    }
}