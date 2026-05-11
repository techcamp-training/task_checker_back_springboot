package in.tech_camp.task_checker_back.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import in.tech_camp.task_checker_back.dto.TaskReportDTO;
import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.repository.TaskRepository;

@SpringBootTest
@DisplayName("TaskService - レポート取得機能")
class TaskServiceReportTest {

    @Autowired
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    private TaskEntity taskWithStatus(int status) {
        TaskEntity task = new TaskEntity();
        task.setStatus(status);
        return task;
    }

    @Nested
    @DisplayName("totalCount: 全タスク数の集計")
    class TotalCount {

        @Test
        @DisplayName("タスクが0件の場合、totalCountは0であること")
        void shouldReturnZeroWhenNoTasks() {
            when(taskRepository.findAll()).thenReturn(List.of());

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getTotalCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("タスクが3件の場合、totalCountは3であること")
        void shouldReturnCorrectTotalCount() {
            when(taskRepository.findAll()).thenReturn(List.of(
                taskWithStatus(0), taskWithStatus(1), taskWithStatus(5)
            ));

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getTotalCount()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("completionRate: 完了率の計算")
    class CompletionRate {

        @Test
        @DisplayName("タスクが0件の場合、completionRateは0.0であること")
        void shouldReturnZeroWhenNoTasks() {
            when(taskRepository.findAll()).thenReturn(List.of());

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getCompletionRate()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("完了タスク(status=5)が1件、総数3件の場合、completionRateは33.3であること")
        void shouldReturnCorrectRateWhenOneCompleted() {
            when(taskRepository.findAll()).thenReturn(List.of(
                taskWithStatus(0), taskWithStatus(1), taskWithStatus(5)
            ));

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getCompletionRate()).isEqualTo(33.3);
        }

        @Test
        @DisplayName("全タスクが完了(status=5)の場合、completionRateは100.0であること")
        void shouldReturn100WhenAllCompleted() {
            when(taskRepository.findAll()).thenReturn(List.of(
                taskWithStatus(5), taskWithStatus(5)
            ));

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getCompletionRate()).isEqualTo(100.0);
        }

        @Test
        @DisplayName("完了タスクが0件の場合、completionRateは0.0であること")
        void shouldReturnZeroWhenNoCompleted() {
            when(taskRepository.findAll()).thenReturn(List.of(
                taskWithStatus(0), taskWithStatus(1)
            ));

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getCompletionRate()).isEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("countByStatus: ステータス別件数の集計")
    class CountByStatus {

        @Test
        @DisplayName("全ステータス(0〜5)のキーが含まれること")
        void shouldContainAllStatusKeys() {
            when(taskRepository.findAll()).thenReturn(List.of(taskWithStatus(0)));

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getCountByStatus()).containsKeys(0, 1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("タスクが0件の場合、全ステータスのカウントが0であること")
        void shouldReturnZeroCountsWhenNoTasks() {
            when(taskRepository.findAll()).thenReturn(List.of());

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getCountByStatus()).containsEntry(0, 0L)
                .containsEntry(1, 0L)
                .containsEntry(2, 0L)
                .containsEntry(3, 0L)
                .containsEntry(4, 0L)
                .containsEntry(5, 0L);
        }

        @Test
        @DisplayName("各ステータスのカウントが正しいこと")
        void shouldReturnCorrectCountsPerStatus() {
            when(taskRepository.findAll()).thenReturn(List.of(
                taskWithStatus(0), taskWithStatus(0), taskWithStatus(5)
            ));

            TaskReportDTO report = taskService.getReport();

            assertThat(report.getCountByStatus()).containsEntry(0, 2L)
                .containsEntry(5, 1L);
        }
    }
}
