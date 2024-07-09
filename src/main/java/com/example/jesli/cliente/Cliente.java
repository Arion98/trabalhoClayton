package com.example.jesli.cliente;

import com.example.jesli.chamado.Chamado;
import com.example.jesli.enums.Perfil;
import com.example.jesli.pessoa.Pessoa;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Cliente extends Pessoa {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    List<Chamado> chamados = new ArrayList<>();

    public Cliente() {
        super();
        addPerfil(Perfil.CLIENTE);
    }

    public Cliente(ClienteDTO obj) {
        super(obj.getId(), obj.getNome(), obj.getCpf(), obj.getEmail(), obj.getSenha());
        addPerfil(Perfil.CLIENTE);
    }
}
