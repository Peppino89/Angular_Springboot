package org.example.todo.repository;

import org.example.todo.entity.Todo;
import org.example.todo.enums.Priority;
import org.example.todo.enums.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(Long userId);
    List<Todo> findByUserIdAndStatus(Long userId, TodoStatus status);
    List<Todo> findByUserIdAndPriority(Long userId, Priority priority);

}
