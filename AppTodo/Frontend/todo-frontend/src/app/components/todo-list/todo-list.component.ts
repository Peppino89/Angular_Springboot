import { Component, inject, OnInit, signal } from '@angular/core';
import { TodoStoreService } from '../../stores/todo-store.service';
import { AuthStoreService } from '../../stores/auth-store.service';
import { Priority, Todo, TodoStatus } from '../../models/todo';
import { DatePipe } from '@angular/common';
import { TodoFormComponent } from '../todo-form/todo-form.component';

@Component({
  selector: 'app-todo-list',
  imports: [DatePipe, TodoFormComponent],
  templateUrl: './todo-list.component.html',
  styleUrl: './todo-list.component.css',
})
export class TodoListComponent implements OnInit {
  todoStore = inject(TodoStoreService);
  authStore = inject(AuthStoreService);

  statutes: (TodoStatus | 'ALL')[] = ['ALL', 'TODO', 'IN_PROGRESS', 'DONE'];
  priorities: (Priority | 'ALL')[] = ['ALL', 'LOW', 'MEDIUM', 'HIGH'];

  editingTodo = signal<Todo|null>(null);

  ngOnInit(): void {
    this.todoStore.loadTodos();
  }

  startEdit(todo:Todo): void {
    this.editingTodo.set(todo);
    window.scrollTo({top:0,behavior:'smooth'});
  }

  clearEditing(): void {
    this.editingTodo.set(null);
  }

  onSearch(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.todoStore.setSearchTerm(value);
  }

  onStatusChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as TodoStatus | 'ALL';
    this.todoStore.setStatusFilter(value);
  }

  onPriorityChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as Priority | 'ALL';
    this.todoStore.setPriorityFilter(value);
  }

  onSortFieldChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as
      | 'titolo'
      | 'descrizione'
      | 'priority'
      | 'status'
      | 'dataCreazione';

    this.todoStore.setSortField(value);
  }

  deleteTodo(id: number): void {
    const confirmed = confirm('Sei sicuro di voler eliminare questo todo ?');

    if (confirmed) {
      this.todoStore.deleteTodo(id);
    }
  }

  logout(): void {
    this.authStore.logout();
  }
}
