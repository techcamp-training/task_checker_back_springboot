package in.tech_camp.task_checker_back.controller;

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

import in.tech_camp.task_checker_back.dto.TaskReportDTO;
import in.tech_camp.task_checker_back.dto.UpdateStatusDTO;
import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.service.TaskDuplicateService;
import in.tech_camp.task_checker_back.service.TaskService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

  private final TaskService taskService;
  private final TaskDuplicateService taskDuplicateService;

  @GetMapping("/")
  public List<TaskEntity> showIndex() {
    return taskService.findAll();
  }

  @PostMapping("/")
  public ResponseEntity<?> createTask(@RequestBody TaskEntity task) {
    try {
      List<TaskEntity> tasks = taskService.create(task);
      return ResponseEntity.ok(tasks);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("messages", List.of(e.getMessage())));
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
  }

  @PutMapping("/{taskId}/update")
  public ResponseEntity<?> updateTask(@PathVariable("taskId") Integer id, @RequestBody TaskEntity task) {
    try {
      List<TaskEntity> tasks = taskService.update(id, task);
      if (tasks == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("messages", List.of("Task not found")));
      }
      return ResponseEntity.ok(tasks);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("messages", List.of(e.getMessage())));
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
  }

  @DeleteMapping("/{taskId}/delete")
  public ResponseEntity<?> deleteGenre(@PathVariable("taskId") Integer taskId) {
    try {
      List<TaskEntity> tasks = taskService.delete(taskId);
      return ResponseEntity.ok(tasks);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));
    }
  }

  @PostMapping("/{taskId}/duplicate")
  public ResponseEntity<?> duplicateTask(@PathVariable("taskId") Integer taskId) {
    try {
      List<TaskEntity> tasks = taskDuplicateService.duplicate(taskId);
      if (tasks == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("messages", List.of("Task not found")));
      }
      return ResponseEntity.ok(tasks);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
  }

  @PostMapping("/{taskId}/update/status")
  public ResponseEntity<?> updateStatus(@PathVariable("taskId") Integer taskId, @RequestBody UpdateStatusDTO statusRequest) {
    try {
      List<TaskEntity> tasks = taskService.updateStatus(taskId, statusRequest.getStatus());
      if (tasks == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("messages", List.of("Task not found")));
      }
      return ResponseEntity.ok(tasks);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("messages", List.of("Internal Server Error")));
    }
  }

  @GetMapping("/report/")
  public ResponseEntity<?> getReport() {
    try {
      return ResponseEntity.ok(taskService.getReport());
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("messages", List.of("Internal Server Error")));
    }
  }
}
