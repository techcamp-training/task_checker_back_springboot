package in.tech_camp.task_checker_back.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.task_checker_back.entity.TaskEntity;

@Mapper
public interface TaskRepository {
  @Select("SELECT * FROM tasks")
  List<TaskEntity> findAll();

  @Select("SELECT * FROM tasks WHERE id = #{id}")
  TaskEntity findById(Integer id);

  @Insert("INSERT INTO tasks (name, explanation, deadline_date, status, genre_id) VALUES (#{name}, #{explanation}, #{deadlineDate}, #{status}, #{genreId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(TaskEntity task);

  @Update("UPDATE tasks SET name = #{name}, explanation = #{explanation}, deadline_date = #{deadlineDate}, status = #{status}, genre_id = #{genreId}, updated_at = #{updatedAt} WHERE id = #{id}")
  void update(TaskEntity task);

  @Delete("DELETE FROM tasks WHERE id = #{id}")
  void deleteById(Integer id);
}
