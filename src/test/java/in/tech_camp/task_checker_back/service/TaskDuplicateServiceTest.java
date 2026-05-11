package in.tech_camp.task_checker_back.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.repository.TaskRepository;

@SpringBootTest
@DisplayName("TaskDuplicateServiceTest")
class TaskDuplicateServiceTest {

    @Autowired
    private TaskDuplicateService taskDuplicateService;

    @MockitoBean
    private TaskRepository taskRepository;

    private TaskEntity originalTask;

    @BeforeEach
    void setUp() {
        originalTask = new TaskEntity();
        originalTask.setId(1);
        originalTask.setName("元タスク");
        originalTask.setExplanation("説明文");
        originalTask.setStatus(3);
        originalTask.setGenreId(2);
        originalTask.setPriority("high");
        when(taskRepository.findById(1)).thenReturn(originalTask);
        when(taskRepository.findAll()).thenReturn(List.of(originalTask));
    }

    @Nested
    @DisplayName("nameの加工")
    class NameProcessing {

        @Test
        @DisplayName("複製されたタスクのnameに「(コピー)」が末尾に追加されること")
        void shouldAppendCopySuffixToName() {
            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("元タスク(コピー)");
        }

        @Test
        @DisplayName("元のタスクのnameがnullの場合、「(コピー)」のみになること")
        void shouldResultInCopyOnlyWhenOriginalNameIsNull() {
            originalTask.setName(null);
            when(taskRepository.findById(1)).thenReturn(originalTask);

            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("(コピー)");
        }
    }

    @Nested
    @DisplayName("statusの初期化")
    class StatusInitialization {

        @Test
        @DisplayName("元のstatusが0の場合、複製後のstatusも0になること")
        void shouldSetStatusToZeroWhenOriginalIsZero() {
            originalTask.setStatus(0);

            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(0);
        }

        @Test
        @DisplayName("元のstatusが0以外（例: 5）の場合でも、複製後のstatusが必ず0になること")
        void shouldSetStatusToZeroRegardlessOfOriginalStatus() {
            originalTask.setStatus(5);

            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("引き継ぎ属性")
    class InheritedAttributes {

        @Test
        @DisplayName("元のタスクのexplanationが引き継がれること")
        void shouldInheritExplanation() {
            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getExplanation()).isEqualTo("説明文");
        }

        @Test
        @DisplayName("元のタスクのgenreIdが引き継がれること")
        void shouldInheritGenreId() {
            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getGenreId()).isEqualTo(2);
        }

        @Test
        @DisplayName("元のタスクのdeadlineDateが引き継がれること")
        void shouldInheritDeadlineDate() {
            Timestamp deadline = Timestamp.valueOf("2025-12-31 00:00:00");
            originalTask.setDeadlineDate(deadline);

            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getDeadlineDate()).isEqualTo(deadline);
        }

        @ParameterizedTest(name = "priority = {0}")
        @ValueSource(strings = {"low", "medium", "high"})
        @DisplayName("元のpriorityがそのまま引き継がれること（low/medium/high それぞれ）")
        void shouldInheritPriority(String priority) {
            originalTask.setPriority(priority);

            taskDuplicateService.duplicate(1);

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskRepository).insert(captor.capture());
            assertThat(captor.getValue().getPriority()).isEqualTo(priority);
        }
    }

    @Nested
    @DisplayName("元タスクの不変性")
    class OriginalTaskImmutability {

        @Test
        @DisplayName("複製処理によって元のタスクのstatusが変更されないこと")
        void shouldNotModifyOriginalTaskStatus() {
            taskDuplicateService.duplicate(1);

            assertThat(originalTask.getStatus()).isEqualTo(3);
        }

        @Test
        @DisplayName("複製処理によって元のタスクのnameが変更されないこと")
        void shouldNotModifyOriginalTaskName() {
            taskDuplicateService.duplicate(1);

            assertThat(originalTask.getName()).isEqualTo("元タスク");
        }
    }

    @Nested
    @DisplayName("DB操作・戻り値")
    class DatabaseOperationsAndReturnValue {

        @Test
        @DisplayName("複製後にtaskRepository.insert()が1回呼ばれること")
        void shouldCallInsertOnce() {
            taskDuplicateService.duplicate(1);

            verify(taskRepository, times(1)).insert(any());
        }

        @Test
        @DisplayName("複製後にtaskRepository.findAll()の結果が返されること")
        void shouldReturnFindAllResult() {
            List<TaskEntity> result = taskDuplicateService.duplicate(1);

            assertThat(result).isEqualTo(List.of(originalTask));
        }
    }

    @Nested
    @DisplayName("異常系")
    class ErrorCases {

        @Test
        @DisplayName("存在しないタスクIDを指定した場合、nullが返ること")
        void shouldReturnNullWhenTaskNotFound() {
            when(taskRepository.findById(999)).thenReturn(null);

            List<TaskEntity> result = taskDuplicateService.duplicate(999);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("存在しないIDの場合、taskRepository.insert()が呼ばれないこと")
        void shouldNotCallInsertWhenTaskNotFound() {
            when(taskRepository.findById(999)).thenReturn(null);

            taskDuplicateService.duplicate(999);

            verify(taskRepository, never()).insert(any());
        }
    }
}
