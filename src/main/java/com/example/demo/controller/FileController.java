package com.example.demo.controller;

import com.example.demo.domain.dto.ErrorMessage;
import com.example.demo.domain.dto.FileResult;
import com.example.demo.domain.model.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.demo.domain.model.File;
import java.util.UUID;
import com.example.demo.repository.FileRepository;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    //Obté una llista de tots els arxius

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(fileRepository.findBy());
    }

    //Descarrega una arxiu pel seu ID

    @GetMapping("/{id}")
    public ResponseEntity<?> getFile(@PathVariable UUID id) {
        File file = fileRepository.findById(id).orElse(null);

        if (file == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorMessage.message("File not found"));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.contenttype))
                .contentLength(file.data.length)
                .body(file.data);
    }

    //Carrega un arxiu

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile uploadedFile) {
        try {
            File file = new File();
            file.contenttype = uploadedFile.getContentType();
            file.data = uploadedFile.getBytes();

            File savedFile = fileRepository.save(file);

            FileResult fileResult = new FileResult(savedFile.fileid, savedFile.contenttype);

            return ResponseEntity.ok().body(fileResult);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    //Elimina un arxiu pel seu ID

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        File file = fileRepository.findById(id).orElse(null);
        if(file != null) {
            fileRepository.delete(file);
            return ResponseEntity.ok().body(ErrorMessage.message("S'ha eliminat l'arxiu amd id  '" + id + "'"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(ErrorMessage.message("No s'ha trobat l'arxiu amd id '" + id + "'"));
        }
    }

    //Elimina tots els arxius

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAll() {
        fileRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body(ErrorMessage.message("S'han eliminat TOTS"));
    }

}
