package interfaces;

public interface IParsing<T> {
    T parse(String input) throws Exception;
}