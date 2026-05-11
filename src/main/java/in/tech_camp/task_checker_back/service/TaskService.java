package in.tech_camp.task_checker_back.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.repository.TaskRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskService {

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
}
