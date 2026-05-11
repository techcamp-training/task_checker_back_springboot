package in.tech_camp.task_checker_back.dto;

import java.util.Map;

import lombok.Data;

@Data
public class TaskReportDTO {
    private int totalCount;
    private Map<Integer, Long> countByStatus;
    private double completionRate;
}
