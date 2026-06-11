import { computed, inject, Injectable, signal } from '@angular/core';
import { Priority, Todo, TodoStatus } from '../models/todo';
import { TodoService } from '../services/todo.service';
import { TodoRequest } from '../models/todo-request';

type StatusFilter = TodoStatus|'ALL';
type PriorityFilter = Priority|'ALL';

type SortField = 'titolo'| 'descrizione'| 'priority'| 'status' | 'dataCreazione';
type SortDirection = 'asc' | 'desc';


@Injectable({
  providedIn: 'root',
})
export class TodoStoreService {
  private todoService = inject(TodoService);

  todos = signal<Todo[]>([]);
  loading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

  searchTerm = signal<string>('');
  selectedStatus = signal<StatusFilter>('ALL');
  selectedPriority = signal<PriorityFilter>('ALL');

  sortField = signal<SortField>('dataCreazione');
  sortDirection = signal<SortDirection>('desc');

  filteredAndSortedTodos = computed(() => {
    let result = [...this.todos()];

    const search = this.searchTerm().trim().toLowerCase();

    if (search) {
      result = result.filter(
        (todo) =>
          todo.titolo.toLowerCase().includes(search) ||
          (todo.descrizione ?? '').toLowerCase().includes(search),
      );
    }

    if (this.selectedStatus() !== 'ALL') {
      result = result.filter((todo) => todo.status === this.selectedStatus());
    }

    if (this.selectedPriority() !== 'ALL') {
      result = result.filter((todo) => todo.priority === this.selectedPriority());
    }

    const field = this.sortField();
    const direction = this.sortDirection() === 'asc' ? 1 : -1;

    result.sort((a, b) => {
      const valueA = a[field] ?? '';
      const valueB = b[field] ?? '';

      if (field === 'dataCreazione') {
        return (new Date(valueA).getTime() - new Date(valueB).getTime()) * direction;
      }
      return String(valueA).localeCompare(valueB) * direction;
    });

    return result;
  });

  setSearchTerm(value: string): void {
    this.searchTerm.set(value);
  }

  setStatusFilter(statusFilter: StatusFilter): void {
    this.selectedStatus.set(statusFilter);
  }

  setPriorityFilter(priority: PriorityFilter): void {
    this.selectedPriority.set(priority);
  }

  setSortField(field: SortField): void {
    this.sortField.set(field);
  }

  toggleSortDirection(): void {
    this.sortDirection.update((direction) => (direction === 'asc' ? 'desc' : 'asc'));
  }

  resetFilters(): void {
    this.searchTerm.set('');
    this.selectedStatus.set('ALL');
    this.selectedPriority.set('ALL');
    this.sortField.set('dataCreazione');
    this.sortDirection.set('desc');
  }

  totalTodos = computed(() => this.todos().length);

  completedTodos = computed(() => this.todos().filter((todo) => todo.status === 'DONE').length);

  pendingTodos = computed(() => this.todos().filter((todo) => todo.status !== 'DONE').length);

  loadTodos():void{
    this.loading.set(true);
    this.errorMessage.set(null);

    this.todoService.getAll().subscribe({
      next: todos => {
        this.todos.set(todos);
      },
      error: () => {
        this.errorMessage.set("Errore durante il caricamento dei todo");
        this.loading.set(false);
      },
      complete:() => {
        this.loading.set(false);
      }
    });

  }

  createTodo(request:TodoRequest):void{
    this.loading.set(true);
    this.errorMessage.set(null);

    this.todoService.create(request).subscribe({
      next:(cretedTodo)=>{
        this.todos.update(todos=>[cretedTodo,...todos])
      },
      error:() => {
        this.errorMessage.set("Errore durante la creazione del todo");
        this.loading.set(false);
      },
      complete:() => {
        this.loading.set(false);
      }
    });
  }

  updateTodo(id:number,request:TodoRequest):void{
    this.loading.set(true);
    this.errorMessage.set(null);

    this.todoService.update(id,request).subscribe({
      next:(updatedTodo)=>{
        this.todos.update(todos=> todos.map(
          todo=>
            todo.id === id ? updatedTodo: todo
        ));
      },
      error:() => {
        this.errorMessage.set("Errore durante la modifica del todo");
        this.loading.set(false);
      },
      complete:() => {
        this.loading.set(false);
      }
    });
  }

  deleteTodo(id:number):void{
    this.loading.set(true);
    this.errorMessage.set(null);

    this.todoService.delete(id).subscribe({
      next:()=>{
        this.todos.update(todos=>
          todos.filter(todo=> todo.id !== id)
        );
      },
      error:() => {
       this.errorMessage.set("Errore durante l'eliminazione del todo");
       this.loading.set(false);
      },
      complete:() => {
        this.loading.set(false);
      }
    });
  }

}
