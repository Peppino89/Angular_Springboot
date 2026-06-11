package org.example.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo.dto.TodoRequest;
import org.example.todo.dto.TodoResponse;
import org.example.todo.enums.Priority;
import org.example.todo.enums.TodoStatus;
import org.example.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoRequest todoRequest,
                                               // con @AuthenticationPrincipal  stiamo dicendo Prendi l'utente loggato direttamente dal token JWT
                                               @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(todoService.create(todoRequest, user.getUsername()));

    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>>getAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(todoService.getAll(user.getUsername()));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse>update(@PathVariable Long id, @Valid @RequestBody TodoRequest todoRequest, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(todoService.update(id, todoRequest, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        todoService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TodoResponse>> getByStatus(@PathVariable TodoStatus status, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(todoService.getByStatus(user.getUsername(),status));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TodoResponse>> getByPriority(@PathVariable Priority priority, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(todoService.getByPriority(user.getUsername(),priority));
    }
}
