package com.softyorch.cursospring.app.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.softyorch.cursospring.app.util.Constants.UPLOADS_FOLDER;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    //private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathPhoto = getPath(filename);

        Resource resource = new UrlResource(pathPhoto.toUri());

        if (!resource.exists() && !resource.isReadable()) {
            throw new RuntimeException("Error: no se puede leer la imagen: " + pathPhoto);
        }

        return resource;
    }

    @Override
    public String copy(MultipartFile photo) throws IOException {
        String uniqueFilename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
        Path rootPath = getPath(uniqueFilename);

        Files.copy(photo.getInputStream(), rootPath);

        return uniqueFilename;
    }

    @Override
    public boolean delete(String filename) {
        Path rootPhoto = getPath(filename);
        File photo = rootPhoto.toFile();

        if (photo.exists() && photo.canRead())
            return photo.delete();
        return false;
    }

    public Path getPath(String filename) {
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();

    }
}
