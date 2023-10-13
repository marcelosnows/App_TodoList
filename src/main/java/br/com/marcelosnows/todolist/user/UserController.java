package br.com.marcelosnows.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/*
 * Modificadores de Classes (Tipos de acessos)
 * public
 * private
 * protected 
 */

 @RestController
 @RequestMapping("/users")

  public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
      var user = this.userRepository.findByUsername(userModel.getUsername());

      if(user != null) {
         // Mensagem de erro
        // Status Code
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuário já existe!");
      };
      
      // Gerando senha criptografada do usuário.

      var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
      userModel.setPassword(passwordHashred);

      var userCreated = this.userRepository.save(userModel);
      return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    };
  }; 


/*
  * String (texto)
  * Integer (int) numeros inteiros
  * Double (double) Números 0.0000
  * Float (float) Números 0.000
  * char (A C)
  * Date (date)
  * void - sem retorno 
*/

