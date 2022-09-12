package forum.topico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@CrossOrigin
public class TopicoController {
    @Autowired
    private TopicoService topicoService;

    /**
     * method GET(All)
     * */
    @GetMapping
    public ResponseEntity<Page<TopicoDTO>> listarTopicos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(topicoService.getAll(pageable));
    }

    /**
     * method GET(id)
     * */
    @GetMapping("/{id}")
    public ResponseEntity<TopicoDTO> detalharTopico(@PathVariable Long id){
        Optional<TopicoDTO> topicoDTO = topicoService.verificarPessoaExiste(id);
        if (topicoDTO.isPresent()){
//            return new ResponseEntity<>(topicoDTO, HttpStatus.OK);
            return ResponseEntity.status(HttpStatus.OK).body(topicoDTO.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * method POST
     * */
    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDTO> salvarTopico(@RequestBody @Valid TopicoDTO topicoDTO){
        TopicoDTO topicoDTO1 = topicoService.salvar(topicoDTO);

        return new ResponseEntity<>(topicoDTO1, HttpStatus.CREATED);
    }

    /**
     * method PUT
     * */
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> atualizarTopico(@PathVariable Long id, @RequestBody @Valid TopicoDTO topicoDTO ){
        Optional<TopicoDTO> topicoDTO1 = topicoService.verificarPessoaExiste(id);
        if (topicoDTO1 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TopicoDTO topicoDTO2 = topicoService.editar(id, topicoDTO);
        return new ResponseEntity<>(topicoDTO2, HttpStatus.OK);
    }

    /**
     * method DELETE
     * */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> removerTopico(@PathVariable Long id){
        Optional<TopicoDTO> topicoDTO = topicoService.verificarPessoaExiste(id);
        if (topicoDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        topicoService.remover(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
