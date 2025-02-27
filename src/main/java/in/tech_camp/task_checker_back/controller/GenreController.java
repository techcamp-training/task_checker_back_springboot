package in.tech_camp.task_checker_back.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.task_checker_back.entity.GenreEntity;
import in.tech_camp.task_checker_back.repository.GenreRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/genres")
@AllArgsConstructor
public class GenreController {
  private final GenreRepository genreRepository;

  @GetMapping("/")
  public List<GenreEntity> showIndex() {
    List<GenreEntity> genres = genreRepository.findAll();
    return genres;
  }

  @PostMapping("/")
  public ResponseEntity<?> createGenre(@RequestBody GenreEntity genre) {
    try {
      genreRepository.insert(genre);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
    List<GenreEntity> genres = genreRepository.findAll();
    return ResponseEntity.ok().body(genres);
  }

  @DeleteMapping("/{genreId}")
  public ResponseEntity<?> deleteGenre(@PathVariable("genreId") Integer id) {
    try {
      genreRepository.deleteById(id);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
    List<GenreEntity> genres = genreRepository.findAll();
    return ResponseEntity.ok().body(genres);
  }
}
