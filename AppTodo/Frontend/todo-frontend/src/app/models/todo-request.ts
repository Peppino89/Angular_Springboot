import { Priority, TodoStatus } from './todo';

export interface TodoRequest {

  titolo: string,
  descrizione?: string,
  status: TodoStatus,
  priority: Priority,
  dataScadenza?: string
}
