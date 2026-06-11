package org.example.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.todo.dto.TodoRequest;
import org.example.todo.dto.TodoResponse;
import org.example.todo.entity.Todo;
import org.example.todo.entity.User;
import org.example.todo.enums.Priority;
import org.example.todo.enums.TodoStatus;
import org.example.todo.exception.TodoNotFoundException;
import org.example.todo.exception.UnauthorizedException;
import org.example.todo.exception.UserNotFoundException;
import org.example.todo.repository.TodoRepository;
import org.example.todo.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final UtenteRepository utenteRepository;

    public TodoResponse create(TodoRequest request,String username) {
        User user = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        Todo todo = Todo.builder()
                .titolo(request.getTitolo())
                .descrizione(request.getDescrizione())
                .status(request.getStatus())
                .priority(request.getPriority())
                .dataScadenza(request.getDataScadenza())
                .dataCreazione(LocalDateTime.now())
                .user(user)
                .build();

        return  toResponse(todoRepository.save(todo));

    }


    public List<TodoResponse> getAll(String username) {
        User user = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        return todoRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());


    }

    public TodoResponse update(Long id, TodoRequest request,String username) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()->new TodoNotFoundException("Todo non trovato"));

        if(!todo.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Non sei autorizzato a modificare questo todo");
        }

        todo.setTitolo(request.getTitolo());
        todo.setDescrizione(request.getDescrizione());
        todo.setStatus(request.getStatus());
        todo.setPriority(request.getPriority());
        todo.setDataScadenza(request.getDataScadenza());

        return toResponse(todoRepository.save(todo));


    }

    public void delete(Long id,String username) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()->new TodoNotFoundException("Todo non trovato"));

        if(!todo.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Non sei autorizzato ad eliminare questo todo");
        }

        todoRepository.deleteById(id);
    }

    public List<TodoResponse> getByStatus (String username, TodoStatus status) {

        User user = utenteRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("Utente non trovato"));

        return todoRepository.findByUserIdAndStatus(user.getId(),status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TodoResponse> getByPriority(String username, Priority priority) {

        User user = utenteRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("Utente non trovato"));

        return todoRepository.findByUserIdAndPriority(user.getId(),priority)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

    }

    private TodoResponse toResponse(Todo todo){
        TodoResponse response = new TodoResponse();
                response.setId(todo.getId());
                response.setTitolo(todo.getTitolo());
                response.setDescrizione(todo.getDescrizione());
                response.setStatus(todo.getStatus());
                response.setPriority(todo.getPriority());
                response.setDataScadenza(todo.getDataScadenza());
                response.setUsername(todo.getUser().getUsername());
        return response;
    }




}
