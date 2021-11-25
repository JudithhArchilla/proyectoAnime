package com.example.demo.controller;

import com.example.demo.domain.dto.ListResult;
import com.example.demo.domain.model.Anime;
import com.example.demo.domain.dto.ErrorMessage;
import com.example.demo.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/animes")
public class AnimeController {

    @Autowired
    private AnimeRepository animeRepository;

    //Obté la llista de tots els ànimes

    @GetMapping("/")
    public ResponseEntity<?> findAllAnime(Authentication authentication) {
        return ResponseEntity.ok().body(ListResult.list(animeRepository.findAll()));
    }

    //Obté la llista de tots els ànimes

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllAnimeById (@PathVariable UUID id){
        Anime anime = animeRepository.findById(id).orElse(null);
        if (anime!= null){
            return ResponseEntity.ok().body(anime);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.message("No s'ha trobat l'anime amd id '"+ id + "'"));
        }
    }

    //Afegeix un ànime

    @PostMapping("/")
    public ResponseEntity<?> createAnime(@RequestBody Anime anime) {
        if (animeRepository.findByName(anime.getName()) != null) {
            Anime animePost = animeRepository.save(anime);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.message("Ja existeix un anime amb el nom ' " + anime.getName() + " '"));
        } else {
            return ResponseEntity.ok().body(animeRepository.save(anime));
        }
    }

    //Elimina un ànime pel seu ID

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimeById(@PathVariable UUID id) {
        Anime anime = animeRepository.findById(id).orElse(null);
        if(anime != null) {
            animeRepository.delete(anime);
            return ResponseEntity.ok().body(ErrorMessage.message("S'ha eliminat l'anime amd id '" + id + "'"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(ErrorMessage.message("No s'ha trobat l'anime amd id '" + id + "'"));
        }
    }

}
