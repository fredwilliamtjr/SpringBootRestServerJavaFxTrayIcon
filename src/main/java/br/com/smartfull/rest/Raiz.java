package br.com.smartfull.rest;

public class Raiz {

    private final long id;
    private final String conteudo;

    public Raiz(long id, String conteudo) {
        this.id = id;
        this.conteudo = conteudo;
    }

    public long getId() {
        return id;
    }

    public String getConteudo() {
        return conteudo;
    }
}
