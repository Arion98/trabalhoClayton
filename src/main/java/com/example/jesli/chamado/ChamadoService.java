package com.example.jesli.chamado;

import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jesli.cliente.Cliente;
import com.example.jesli.cliente.ClienteService;
import com.example.jesli.enums.Prioridade;
import com.example.jesli.enums.Status;
import com.example.jesli.tecnico.Tecnico;
import com.example.jesli.tecnico.TecnicoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private TecnicoService tecnicoService;

    @Autowired
    private ClienteService clienteService;

    public Chamado findById(Integer id) {
        Optional<Chamado> obj = chamadoRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id, id));
    }

    public Chamado create(ChamadoDTO objDTO) {
        objDTO.setId(null);

        Chamado newObj = new Chamado(objDTO);

        return chamadoRepository.save(newObj);
    }

    public Chamado newChamado(ChamadoDTO objDTO) {
        Tecnico tecnico = tecnicoService.findById(objDTO.getTecnico());
        Cliente cliente = clienteService.findById(objDTO.getCliente());

        Chamado newObj = new Chamado(objDTO);

        if (objDTO.getId() != null) {
            newObj.setId(objDTO.getId());
        }

        if (objDTO.getStatus().equals(2)) {
            newObj.setDataFechamento(LocalDate.now());
        }

        newObj.setTecnico(tecnico);
        newObj.setCliente(cliente);
        newObj.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade()));
        newObj.setStatus(Status.toEnum(objDTO.getStatus()));
        newObj.setTitulo(objDTO.getTitulo());
        newObj.setObservacoes(objDTO.getObservacoes());

        return chamadoRepository.save(newObj);
    }

    public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
        objDTO.setId(id);

        Chamado oldObj = findById(id);

        oldObj = new Chamado(objDTO);

        return chamadoRepository.save(oldObj);
    }

    public List<Chamado> findAll() {
        return chamadoRepository.findAll();
    }

}
