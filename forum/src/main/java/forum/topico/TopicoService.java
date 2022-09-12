package forum.topico;

import forum.curso.Curso;
import forum.curso.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Objects;
import java.util.Optional;


@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public TopicoService(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    /**
     * method Get(All)
     * */
    public Page<TopicoDTO> getAll(Pageable pageable){
        Page<Topico> tpcs = topicoRepository.findAll(pageable);
        Page<TopicoDTO> dtos = tpcs.map(this::converterEntidadeParaDto);
        return dtos;
    }

    /**
     * method Get(ID)
     * */
    public Optional<TopicoDTO> verificarPessoaExiste(Long id) {
        TopicoDTO topicoDTO = buscarPorID(id);
        if (Objects.isNull(topicoDTO)) {
            System.out.println("Pessoa com id: [{}], nao foi encontrada!" + id);
        }
        return Optional.ofNullable(topicoDTO);
    }

    /**
     * method POST
     * */
    @Transactional
    public TopicoDTO salvar(TopicoDTO topicoDTO){
        Topico topico = new Topico();
        topico.setTitulo(topicoDTO.getTitulo());
        topico.setMensagem(topicoDTO.getMensagem());

        return new TopicoDTO(this.topicoRepository.save(topico));
    }

    /**
     * method PUT
     * */
    @Transactional
    public TopicoDTO editar(Long id, TopicoDTO pessoaDTO) {
        Topico entidade = this.topicoRepository.findById(id).orElse(null);
        if (Objects.nonNull(entidade)) {
            entidade.setTitulo(pessoaDTO.getTitulo());
            entidade.setMensagem(pessoaDTO.getMensagem());
            return converterEntidadeParaDto(this.topicoRepository.save(entidade));
        }
        return null;
    }

    /**
     * method DELETE
     * */
    @Transactional
    public void remover(Long id){
        this.topicoRepository.deleteById(id);
    }

    /**
     * entity para dto
     * */
    private TopicoDTO converterEntidadeParaDto(Topico topico) {
        return new TopicoDTO(topico);
    }

    /**
     * buscar por id
     * */
    @Transactional
    public TopicoDTO buscarPorID(Long id) {
        return converterEntidadeParaDto(topicoRepository.getReferenceById(id));
    }

}
