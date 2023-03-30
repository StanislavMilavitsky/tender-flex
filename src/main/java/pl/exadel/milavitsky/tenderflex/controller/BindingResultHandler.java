package pl.exadel.milavitsky.tenderflex.controller;

import org.springframework.validation.BindingResult;


public interface BindingResultHandler {

    /**
     * Get default message by validate exception
     *
     * @param bindingResult exceptions of validate
     * @return string default message of exception
     */
    default String bindingResultHandler(BindingResult bindingResult) {
        return bindingResult.getAllErrors().get(0).getDefaultMessage();
    }
}
