package in.tech_camp.task_checker_back.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.task_checker_back.dto.UpdateStatusDTO;
import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.repository.TaskRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

  private final TaskRepository taskRepository;

  @GetMapping("/")
  public List<TaskEntity> showIndex() {
    List<TaskEntity> tasks = taskRepository.findAll();
    return tasks;
  }

  @PostMapping("/")
  public ResponseEntity<?> createTask(@RequestBody TaskEntity task) {
    try {
      taskRepository.insert(task);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
    List<TaskEntity> tasks = taskRepository.findAll();
    return ResponseEntity.ok().body(tasks);
  }

  @PutMapping("/{taskId}/update")
  public ResponseEntity<?> updateTask(@PathVariable("taskId") Integer id, @RequestBody TaskEntity task) {

    TaskEntity existingTask = taskRepository.findById(task.getId());
    if(existingTask == null){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("messages", List.of("Task not found")));
    }

    existingTask.setName(task.getName());
    existingTask.setExplanation(task.getExplanation());
    existingTask.setDeadlineDate(task.getDeadlineDate());
    existingTask.setStatus(task.getStatus());
    existingTask.setGenreId(task.getGenreId());
    existingTask.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    try {
      taskRepository.update(existingTask);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }

    List<TaskEntity> tasks = taskRepository.findAll();

    return ResponseEntity.ok().body(tasks);
  }

  @DeleteMapping("/{taskId}/delete")
  public ResponseEntity<?> deleteGenre(@PathVariable("taskId") Integer taskId) {
    try {
      taskRepository.deleteById(taskId);
      List<TaskEntity> tasks = taskRepository.findAll();
      return ResponseEntity.ok().body(tasks);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));
    }
  }

  @PostMapping("/{taskId}/update/status")
  public ResponseEntity<?> updateStatus(@PathVariable("taskId") Integer taskId, @RequestBody UpdateStatusDTO statusRequest) {

    TaskEntity task = taskRepository.findById(taskId);
    if(task == null){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("messages", List.of("Task not found")));
    }

    task.setStatus(statusRequest.getStatus());
    task.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    try {
      taskRepository.update(task);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
    List<TaskEntity> tasks = taskRepository.findAll();

    return ResponseEntity.ok().body(tasks);
  }
}
