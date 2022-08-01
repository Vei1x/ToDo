package com.nikita.todo.business.repository.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "todo_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoTaskDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
