package com.example.demo.models;


import com.example.demo.others.ClassType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Make a extension of room, that is for group chat
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String name;
    @ManyToMany(mappedBy = "rooms")
    private List<UserModel> users;
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
    @OneToMany(mappedBy = "room")
    @JsonManagedReference
    private List<ChatMessage> messages;
    private ClassType classType;
}
