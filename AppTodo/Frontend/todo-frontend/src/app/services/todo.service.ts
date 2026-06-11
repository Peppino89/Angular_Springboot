import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Priority, Todo, TodoStatus } from '../models/todo';
import { TodoRequest } from '../models/todo-request';

@Injectable({
  providedIn: 'root',
})
export class TodoService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/todos';

  getAll():Observable<Todo[]>{
    return this.http.get<Todo[]>(this.apiUrl);
  }

  create(request:TodoRequest):Observable<Todo>{
    return this.http.post<Todo>(this.apiUrl, request);
  }

  update(id:number,request:TodoRequest):Observable<Todo>{
    return this.http.put<Todo>(`${this.apiUrl}/${id}`, request);
  }

  delete(id:number):Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByStatus(status: TodoStatus):Observable<Todo[]>{
    return this.http.get<Todo[]>(`${this.apiUrl}/status/${status}`);
  }

  getByPriority(priority: Priority):Observable<Todo[]>{
    return this.http.get<Todo[]>(`${this.apiUrl}/priority/${priority}`);
  }
  
}
