package com.ea975.repfin.controllers;

import com.ea975.repfin.components.UserRoles;
import com.ea975.repfin.components.Users;
import com.ea975.repfin.daos.RepublicasDAO;
import com.ea975.repfin.daos.TransactionsDAO;
import com.ea975.repfin.daos.UserRolesDAO;
import com.ea975.repfin.daos.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class MainController {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private UserRolesDAO userRolesDAO;

    @RequestMapping(value = {"/login", "/"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model){
//        if(error !=null) {
//            model.put("error", true);
//        }
//        if(logout != null) {
//            model.put("logout", true);
//        }

        return "login";
    }

    @RequestMapping(value = {"/hello"})
    public String hello(Model model, HttpServletRequest httpServletRequest){
        httpSession = httpServletRequest.getSession();

        model.addAttribute("title", "Essa página é pra ROLE_USER");
        model.addAttribute("message", "Página hello.html");
        model.addAttribute("httpServletRequest", httpServletRequest);
//        model.addAttribute("userName", httpServletRequest.getRemoteUser());
        return "hello";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest httpServletRequest){
        model.addAttribute("title", "Esta pagina é pra ROLE_ADMIN");
        model.addAttribute("message", "Welcome ADMIN!");
        model.addAttribute("httpServletRequest", httpServletRequest);
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, @RequestParam(value = "username", required = true) String username,
                           @RequestParam(value = "password", required = true) String password,
                           @RequestParam(value = "email", required = true) String email,
                           HttpServletRequest httpServletRequest) {
        if(!(username.isEmpty() || password.isEmpty() || email.isEmpty())) {
            Users user = usersDAO.save(new Users(username, password, "0.00", 1));
            userRolesDAO.save(new UserRoles(user, ROLE_USER, username));

            model.addAttribute("created", true);
            model.addAttribute("userName", user.getName());
            return "login";
        } else {
            return  "login?error";
        }
    }

    public void setUsersDAO(UsersDAO usersDAO){this.usersDAO = usersDAO;}
    public void setUserRolesDAO(UserRolesDAO userRolesDAO) {this.userRolesDAO = userRolesDAO;}
}
