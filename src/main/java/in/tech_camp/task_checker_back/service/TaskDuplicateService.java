package in.tech_camp.task_checker_back.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.repository.TaskRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskDuplicateService {

    private final TaskRepository taskRepository;

    public List<TaskEntity> duplicate(Integer id) {
        TaskEntity original = taskRepository.findById(id);
        if (original == null) return null;

        taskRepository.insert(buildCopy(original));
        return taskRepository.findAll();
    }

    private TaskEntity buildCopy(TaskEntity original) {
        TaskEntity copy = new TaskEntity();
        String baseName = original.getName() != null ? original.getName() : "";
        copy.setName(baseName + "(コピー)");
        copy.setExplanation(original.getExplanation());
        copy.setDeadlineDate(original.getDeadlineDate());
        copy.setGenreId(original.getGenreId());
        copy.setPriority(original.getPriority());
        copy.setStatus(0);
        return copy;
    }
}
