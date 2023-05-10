package com.thegame.controller

import com.thegame.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class UserController(private  val userService: UserService) {

    @RequestMapping(value = ["users.html"])
    fun getUsers(model: Model): String {
        model.addAttribute("users", userService.getAllUsers())
        return "users"
    }
}