package in.tech_camp.task_checker_back.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.task_checker_back.dto.TaskReportDTO;
import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.repository.TaskRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskService {

  private static final int STATUS_MIN = 0;
  private static final int STATUS_MAX = 5;
  private static final int STATUS_COMPLETED = 5;

    private final TaskRepository taskRepository;

    private static final List<String> VALID_PRIORITIES = List.of("low", "medium", "high");

    private void validatePriority(String priority) {
        if (priority != null && !VALID_PRIORITIES.contains(priority)) {
            throw new IllegalArgumentException("優先度はlow、medium、highのいずれかを指定してください");
        }
    }

    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    public List<TaskEntity> create(TaskEntity task) {
        validatePriority(task.getPriority());
        taskRepository.insert(task);
        return taskRepository.findAll();
    }

    public List<TaskEntity> update(Integer id, TaskEntity task) {
        validatePriority(task.getPriority());
        TaskEntity existingTask = taskRepository.findById(id);
        if (existingTask == null) return null;
        existingTask.setName(task.getName());
        existingTask.setExplanation(task.getExplanation());
        existingTask.setDeadlineDate(task.getDeadlineDate());
        existingTask.setStatus(task.getStatus());
        existingTask.setGenreId(task.getGenreId());
        existingTask.setPriority(task.getPriority());
        existingTask.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        taskRepository.update(existingTask);
        return taskRepository.findAll();
    }

    public List<TaskEntity> delete(Integer taskId) {
        taskRepository.deleteById(taskId);
        return taskRepository.findAll();
    }

    public List<TaskEntity> updateStatus(Integer taskId, Integer status) {
        TaskEntity task = taskRepository.findById(taskId);
        if (task == null) return null;
        task.setStatus(status);
        task.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        taskRepository.update(task);
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TaskReportDTO getReport() {
        List<TaskEntity> tasks = taskRepository.findAll();
        int total = tasks.size();

        Map<Integer, Long> countByStatus = new HashMap<>();
        for (int i = STATUS_MIN; i <= STATUS_MAX; i++) {
            countByStatus.put(i, 0L);
        }
        for (TaskEntity task : tasks) {
            countByStatus.merge(task.getStatus(), 1L, Long::sum);
        }

        double completionRate = 0.0;
        if (total > 0) {
            long completed = countByStatus.getOrDefault(STATUS_COMPLETED, 0L);
            completionRate = Math.round((double) completed / total * 1000.0) / 10.0;
        }

        TaskReportDTO report = new TaskReportDTO();
        report.setTotalCount(total);
        report.setCountByStatus(countByStatus);
        report.setCompletionRate(completionRate);
        return report;
    }
}
