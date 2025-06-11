package br.com.alura.screenmatchSpring.Service;

public interface IConverteDados {
   <T> T obterDados (String json, Class<T> classe);
}
