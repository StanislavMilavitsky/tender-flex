package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.service.UserService;



/**
 * User RestController.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController{

    private final UserService userService;

    /**
     * Find all users use method from service layer
     *
     * @return list of users
     * @throws ServiceException if cant find users
     */
    @GetMapping
    public Page<User> findAll(@PageableDefault(page = 0, size = 20) Pageable pageable) throws ServiceException{
        return userService.findAll(pageable);
    }

}
