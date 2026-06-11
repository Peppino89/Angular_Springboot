import { Component, effect, inject, input, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { TodoStoreService } from '../../stores/todo-store.service';
import { Priority, Todo, TodoStatus } from '../../models/todo';
import { TodoRequest } from '../../models/todo-request';

@Component({
  selector: 'app-todo-form',
  imports: [ReactiveFormsModule],
  templateUrl: './todo-form.component.html',
  styleUrl: './todo-form.component.css',
})
export class TodoFormComponent {
  private fb = inject(FormBuilder);
  private todoStore = inject(TodoStoreService);

  showForm = signal<boolean>(false);

  todoToEdit = input<Todo | null>(null);
  formClosed = output<void>();

  statuses: TodoStatus[] = ['TODO', 'IN_PROGRESS', 'DONE'];
  priorities: Priority[] = ['LOW', 'MEDIUM', 'HIGH'];

  todoForm = this.fb.group({
    titolo: ['', [Validators.required]],
    descrizione: [''],
    status: ['TODO' as TodoStatus, [Validators.required]],
    priority: ['MEDIUM' as Priority, [Validators.required]],
    dataScadenza: [''],
  });

  constructor() {
    effect(() => {
      const todo = this.todoToEdit();

      if (todo) {
        this.todoForm.patchValue({
          titolo: todo.titolo,
          descrizione: todo.descrizione,
          status: todo.status,
          priority: todo.priority,
          dataScadenza: todo.dataScadenza ? todo.dataScadenza.slice(0, 16) : '',
        });
      }
    });
  }

  toggleForm(): void {
    this.showForm.update((value) => !value);
  }

  onSubmit(): void {
    if (this.todoForm.invalid) {
      this.todoForm.markAllAsTouched();
      return;
    }

    const raw = this.todoForm.getRawValue();

    const request: TodoRequest = {
      titolo: raw.titolo!,
      descrizione: raw.descrizione!,
      status: raw.status!,
      priority: raw.priority!,
      dataScadenza: raw.dataScadenza ? raw.dataScadenza : undefined,
    };

    const editing = this.todoToEdit();

    if (editing) {
      this.todoStore.updateTodo(editing.id, request);
    } else {
      this.todoStore.createTodo(request);
    }

    this.todoForm.reset({
      titolo: '',
      descrizione: '',
      status: 'TODO',
      priority: 'MEDIUM',
      dataScadenza: '',
    });
    this.resetForm();
    this.showForm.set(false);
    this.formClosed.emit();
  }

  cancelEdit(): void {
    this.resetForm();
    this.showForm.set(false);
    this.formClosed.emit();
  }


  private resetForm(): void {
    this.todoForm.reset({
      titolo: '',
      descrizione: '',
      status: 'TODO',
      priority: 'MEDIUM',
      dataScadenza: '',
    });
  }
}
