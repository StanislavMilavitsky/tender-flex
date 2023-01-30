package pl.exadel.milavitsky.tenderflex.mapper;

public interface Mapper<T, K> {

    T toDTO(K k);

    K fromDTO(T t);
}