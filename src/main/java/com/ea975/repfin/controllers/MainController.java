package com.ea975.repfin.controllers;

import com.ea975.repfin.components.Republicas;
import com.ea975.repfin.components.Transactions;
import com.ea975.repfin.components.UserRoles;
import com.ea975.repfin.components.Users;
import com.ea975.repfin.daos.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    @RequestMapping(value = {"/login", "/"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        return "login";
    }
}