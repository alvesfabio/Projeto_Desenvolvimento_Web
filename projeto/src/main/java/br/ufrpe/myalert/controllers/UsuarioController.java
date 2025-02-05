package br.ufrpe.myalert.controllers;

import br.ufrpe.myalert.dao.LoginDAO;
import br.ufrpe.myalert.models.Login;
import br.ufrpe.myalert.models.Usuario;
import br.ufrpe.myalert.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin("*")
@RestController
@RequestMapping("/usuarios")
class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private LoginDAO loginDAO;

    @GetMapping("/{cpf}")
    public ResponseEntity<?> getByCpf(@PathVariable("cpf") String cpf) {
        Usuario usuario = usuarioService.getByCpf(cpf);
        if(usuario == null) {
            return new ResponseEntity<>("Usuario não existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        Usuario usuarioAux = usuarioService.create(usuario);
        Login login = loginDAO.save(new Login(usuario.getUsername(), usuario.getPassword()));
        if(usuarioAux == null) {
            return new ResponseEntity<>("Usuário já cadastrado", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(usuarioAux, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Usuario usuario) {
        Usuario usuarioAux = usuarioService.update(usuario);
        if(usuarioAux == null) {
            return new ResponseEntity<>("Usuário não está cadastrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioAux, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Usuario usuario) {
        boolean deletado = usuarioService.delete(usuario);
        if(!deletado) {
            return new ResponseEntity<>("Usuario não existe para ser deletado", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(deletado, HttpStatus.OK);
    }
}
