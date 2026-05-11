package in.tech_camp.task_checker_back.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import in.tech_camp.task_checker_back.entity.TaskEntity;
import in.tech_camp.task_checker_back.service.TaskDuplicateService;
import in.tech_camp.task_checker_back.service.TaskService;

@WebMvcTest(TaskController.class)
@DisplayName("TaskControllerTest")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskDuplicateService taskDuplicateService;

    @Nested
    @DisplayName("タスク作成")
    class CreateTask {

        @Test
        @DisplayName("優先度「high」を指定してタスクを作成できること")
        void createTaskWithHighPriority() throws Exception {
            TaskEntity responseTask = new TaskEntity();
            responseTask.setId(1);
            responseTask.setName("テストタスク");
            responseTask.setPriority("high");
            when(taskService.create(any())).thenReturn(List.of(responseTask));

            mockMvc.perform(post("/api/tasks/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"テストタスク\", \"priority\": \"high\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("high"));

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskService).create(captor.capture());
            assertThat(captor.getValue().getPriority()).isEqualTo("high");
        }

        @Test
        @DisplayName("優先度「low」を指定してタスクを作成できること")
        void createTaskWithLowPriority() throws Exception {
            TaskEntity responseTask = new TaskEntity();
            responseTask.setId(1);
            responseTask.setName("テストタスク");
            responseTask.setPriority("low");
            when(taskService.create(any())).thenReturn(List.of(responseTask));

            mockMvc.perform(post("/api/tasks/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"テストタスク\", \"priority\": \"low\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("low"));

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskService).create(captor.capture());
            assertThat(captor.getValue().getPriority()).isEqualTo("low");
        }

        @Test
        @DisplayName("優先度を指定しない場合、デフォルト値「medium」が設定されること")
        void createTaskWithDefaultPriority() throws Exception {
            TaskEntity responseTask = new TaskEntity();
            responseTask.setId(1);
            responseTask.setName("テストタスク");
            responseTask.setPriority("medium");
            when(taskService.create(any())).thenReturn(List.of(responseTask));

            mockMvc.perform(post("/api/tasks/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"テストタスク\"}"))
                .andExpect(status().isOk());

            ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
            verify(taskService).create(captor.capture());
            assertThat(captor.getValue().getPriority()).isEqualTo("medium");
        }

        @Test
        @DisplayName("タスク作成後のレスポンスJSONにpriorityフィールドが含まれること")
        void createTaskResponseContainsPriorityField() throws Exception {
            TaskEntity responseTask = new TaskEntity();
            responseTask.setId(1);
            responseTask.setName("テストタスク");
            responseTask.setPriority("medium");
            when(taskService.create(any())).thenReturn(List.of(responseTask));

            mockMvc.perform(post("/api/tasks/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"テストタスク\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").exists());
        }
    }

    @Nested
    @DisplayName("タスク複製")
    class DuplicateTask {

        @Test
        @DisplayName("存在するIDを指定した場合、200 OKが返ること")
        void shouldReturn200WhenTaskExists() throws Exception {
            TaskEntity copiedTask = new TaskEntity();
            copiedTask.setId(2);
            copiedTask.setName("元タスク(コピー)");
            when(taskDuplicateService.duplicate(1)).thenReturn(List.of(copiedTask));

            mockMvc.perform(post("/api/tasks/1/duplicate"))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("レスポンスボディがJSON配列であること")
        void shouldReturnJsonArray() throws Exception {
            TaskEntity copiedTask = new TaskEntity();
            copiedTask.setId(2);
            when(taskDuplicateService.duplicate(1)).thenReturn(List.of(copiedTask));

            mockMvc.perform(post("/api/tasks/1/duplicate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("taskDuplicateService.duplicate(id)が正しいIDで呼ばれること")
        void shouldCallDuplicateWithCorrectId() throws Exception {
            TaskEntity copiedTask = new TaskEntity();
            when(taskDuplicateService.duplicate(1)).thenReturn(List.of(copiedTask));

            mockMvc.perform(post("/api/tasks/1/duplicate"));

            verify(taskDuplicateService).duplicate(1);
        }

        @Test
        @DisplayName("存在しないIDを指定した場合、404が返ること")
        void shouldReturn404WhenTaskNotFound() throws Exception {
            when(taskDuplicateService.duplicate(999)).thenReturn(null);

            mockMvc.perform(post("/api/tasks/999/duplicate"))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("存在しないIDを指定した場合、404レスポンスのbodyにmessagesフィールドが含まれること")
        void shouldReturnMessagesFieldWhenNotFound() throws Exception {
            when(taskDuplicateService.duplicate(999)).thenReturn(null);

            mockMvc.perform(post("/api/tasks/999/duplicate"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages").exists());
        }
    }
}
