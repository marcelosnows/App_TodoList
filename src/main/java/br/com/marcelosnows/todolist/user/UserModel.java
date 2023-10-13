package br.com.marcelosnows.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="tb_users")


public class UserModel {
  
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;
  
  // Isso significa que no DB esse username será uma coluna com restrição de atributo único. Caso já exista, apresentará erro. 
  @Column(unique = true)
  private String username;
  private String name;
  private String password;

  @CreationTimestamp
  private LocalDateTime createdAt;
  
  /*  
  // método GETTER para inserir valor no atributo PRIVATE
  public void setUsername(String username) {
    this.username = username;
  };

  // método SETTER para recuperar valor no atributo PRIVATE
  public String getUsername() {
    return username;
  };

  public void setName(String name) {
    this.name = name;
  };

  public String getName() {
    return name;
  };

  public void setPassword(String password) {
    this.password = password;
  };

  public String getPassword() {
    return password;
  };
  */

};


