package br.com.marcelosnows.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.marcelosnows.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

  @Autowired
  private IUserRepository userRepository;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

      var serverletPath = request.getServletPath();
      if(serverletPath.startsWith("/tasks/")){

          //pegando authentication (user/password)
            var authorization = request.getHeader("Authorization"); 
            var authEncoded = authorization.substring("Basic".length()).trim();
            
          // conversão do Basic64
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode);
          
          
          // [mneves, 123]
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];
          
        
          //validar user
            var user = this.userRepository.findByUsername(username);
            if(user == null) {
              response.sendError(401);
            } else {
          
          //validar password
              var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
              if(passwordVerify.verified) {

          //segue próxima etapa.
              request.setAttribute("idUser", user.getId());
              filterChain.doFilter(request, response);
             } else {
              response.sendError(401);
            };    
          };

        }else {
          filterChain.doFilter(request, response);
        };  
    };
  };

// como funciona esse doFilter?
/*
  * basicamente podemos barrar a requisição, por não permissão
  * ou então siga em frente com a requisição.

*/