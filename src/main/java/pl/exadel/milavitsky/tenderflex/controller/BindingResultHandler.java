package pl.exadel.milavitsky.tenderflex.controller;

import org.springframework.validation.BindingResult;

public interface BindingResultHandler {

    default String bindingResultHandler(BindingResult bindingResult) {
        return bindingResult.getAllErrors().get(0).getDefaultMessage();
    }
}
