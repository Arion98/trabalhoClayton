package com.example.joao.Tecnico;

import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.joao.Pessoa.Pessoa;
import com.example.joao.Pessoa.PessoaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public Tecnico findById(Integer id) {
        Optional<Tecnico> obj = tecnicoRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id, id));
    }

    public List<Tecnico> findAll() {
        return tecnicoRepository.findAll();
    }

    public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
        objDTO.setId(id);

        Tecnico oldObj = findById(id);

        if (!objDTO.getSenha().equals(oldObj.getSenha())) {
            objDTO.setSenha(objDTO.getSenha());
        }

        validaPorCpfEEmail(objDTO);

        oldObj = new Tecnico(objDTO);
        return tecnicoRepository.save(oldObj);
    }

    public Tecnico create(TecnicoDTO objDTO) {
        objDTO.setId(null);

        validaPorCpfEEmail(objDTO);

        Tecnico newObj = new Tecnico(objDTO);

        return tecnicoRepository.save(newObj);
    }

    private void validaPorCpfEEmail(TecnicoDTO objDTO) {
        Optional<Pessoa> objCpf = pessoaRepository.findByCpf(objDTO.getCpf());

        if (objCpf.isPresent() && objCpf.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("CPF ja existe");
        }

        Optional<Pessoa> objEmail = pessoaRepository.findByEmail(objDTO.getEmail());

        if (objEmail.isPresent() && objEmail.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("Email ja existe");
        }
    }

    public void delete(Integer id) {
        Tecnico obj = findById(id);

        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
        }

        tecnicoRepository.deleteById(id);
    }
}