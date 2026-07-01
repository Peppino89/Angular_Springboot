package com.example.barberbooking.storage;

import com.example.barberbooking.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadPath= Paths.get("uploads/services");
    private static final List<String> ALLOWED_EXTENSIONS =
            List.of(".png", ".jpg", ".jpeg", ".webp");

    @Override
    public String upload(MultipartFile file) {
        try{

            validateFile(file);

            String extension = extractExstension(file.getOriginalFilename());
            String fileName = generateFilename(extension);

            log.info("Nome file generato: {}", fileName);

            Path targetPath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Immagine salvata correttamente in {}", targetPath);

            return "/uploads/services/" + fileName;

        }catch (IOException e){
            log.error("Errore durante il caricamento dell'immagine", e);
            throw new FileStorageException("Errore durante il caricamento dell'immagine");
        }
    }

    @Override
    public void delete(String fileUrl) {

        if(fileUrl == null || fileUrl.isBlank()){
            return;
        }

        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);

            log.info("Eliminazione immagine: {}", fileUrl);

            Path targetPath = uploadPath.resolve(fileName);
            Files.deleteIfExists(targetPath);

            log.info("Immagine eliminata se presente: {}", targetPath);




        }catch (IOException e){
            log.error("Errore durante l'eliminazione dell'immagine", e);
            throw new FileStorageException("Errore durante l'eliminazione dell'Immagine");
        }
    }


    private void validateFile(MultipartFile file) {
        if(file.isEmpty()){
            throw  new FileStorageException("File vuoto");
        }

        String contentType = file.getContentType();

        if(contentType == null || !contentType.startsWith("image/")){
            throw  new FileStorageException("Puoi caricare solo immagini");
        }

        long maxSize = 2 * 1024 * 1024;

        if(file.getSize() > maxSize){
            throw new FileStorageException("Il file non può superare i 2 MB");
        }
    }

    private String extractExstension(String originalFilename) {
        String extension = "";

        if(originalFilename!=null && originalFilename.contains(".")){
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new FileStorageException("Formato immagine non supportato");
        }

        return extension;

    }


    private String generateFilename(String extension){
        return UUID.randomUUID().toString() + extension;
    }
}
