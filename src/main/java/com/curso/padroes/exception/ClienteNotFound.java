package com.curso.padroes.exception;

public class ClienteNotFound extends RuntimeException{
    public ClienteNotFound(long id) {
        super("Cliente não encontrado. ID: " + id);
    }
}
