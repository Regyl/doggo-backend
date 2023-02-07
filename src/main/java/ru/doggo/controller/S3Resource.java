package ru.doggo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.doggo.exceptions.S3KeyDoesNotExistException;
import ru.doggo.service.S3Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping(S3Resource.BASE_URI)
public class S3Resource {

    static final String BASE_URI = "/";

    private S3Service service;

    @Autowired
    public S3Resource(S3Service service) {
        this.service = service;
    }

    @GetMapping("/{bucketName}/objects/{objectKey}")
    public ResponseEntity<Resource> downloadS3Object(@PathVariable String bucketName, @PathVariable String objectKey) {
        try {
            final Resource s3Object = service.getObject(bucketName, objectKey);
            return ResponseEntity.ok(s3Object);
        } catch (S3KeyDoesNotExistException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/{bucketName}/objects", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadS3Object(@PathVariable String bucketName, MultipartHttpServletRequest request)
            throws IOException, URISyntaxException {
        final Optional<MultipartFile> multipartFile = request.getMultiFileMap().toSingleValueMap().values().stream().findFirst();
        if (multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Resource resource = multipartFile.get().getResource();
        final String objectKey = service.putObject(bucketName, resource);
        System.out.println(objectKey);
        final URI location = new URI(String.format("%s/%s/objects/%s", S3Resource.BASE_URI, bucketName, objectKey));
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{bucketName}/objects/{objectKey}")
    public ResponseEntity<Void> removeObject(@PathVariable String bucketName, @PathVariable String objectKey) {
        try {
            service.removeObject(bucketName, objectKey);
            return ResponseEntity.noContent().build();
        } catch (S3KeyDoesNotExistException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
