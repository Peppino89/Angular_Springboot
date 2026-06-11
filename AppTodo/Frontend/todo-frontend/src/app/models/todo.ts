export type TodoStatus = 'TODO'| 'IN_PROGRESS' | 'DONE' ;
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface Todo {
  id: number,
  titolo: string,
  descrizione?: string,
  status: TodoStatus,
  priority: Priority,
  dataScadenza?: string,
  dataCreazione?: string,
  username: string,
}
