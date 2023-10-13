package br.com.marcelosnows.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.marcelosnows.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")

public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    System.out.println("Chegou no controller");
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);

    // validação de data - StartAt()/EndAt() não poder ser superior a data atual.
    var currentDate = LocalDateTime.now();
    if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body("A data de início/término deve ser superior à data atual.");    
  };

    // a StartAt() não pode ser superior a EndAt()
    if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body("A data/hora de início deve(m) ser inferior(es) à data de término.");    
  };

    var task = this.taskRepository.save(taskModel);
      return ResponseEntity.status(HttpStatus.OK).body(task);
  };

  @GetMapping("/") // retornar tudo que for relacionado ao user
  public List<TaskModel> list(HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID) idUser);
    return tasks;
  };

  // http://localhost:8080/tasks/id

  @PutMapping("/{id}") // passando o ID da task a ser alterada
  public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
    // var idUser = request.getAttribute("idUser");
    
    var task = this.taskRepository.findById(id).orElse(null);

    if(task == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body("Tarefa não encontrada!");
    };
    
    var idUser = request.getAttribute("idUser");

    if(!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body("Você não tem permissão para efetuar esta operação!");
    };
    
    Utils.copyNonNullProperties(taskModel, task); //taskModel como source, task como target
    
    // com o código acima, essas linhas não são necessárias.
    // taskModel.setIdUser((UUID) idUser);
    // taskModel.setId(id);
    var taskUpdated = this.taskRepository.save(task);
    return ResponseEntity.ok().body(taskUpdated); // o orElse resolve o problema do save. 
  };

};
