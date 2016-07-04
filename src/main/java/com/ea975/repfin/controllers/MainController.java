package com.ea975.repfin.controllers;

import com.ea975.repfin.components.Republicas;
import com.ea975.repfin.components.UserRoles;
import com.ea975.repfin.components.Users;
import com.ea975.repfin.daos.RepublicasDAO;
import com.ea975.repfin.daos.TransactionsDAO;
import com.ea975.repfin.daos.UserRolesDAO;
import com.ea975.repfin.daos.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final Integer NOT_IN_REPUBLICA = 1;
    private static final Integer HAS_REPUBLICA = 2;

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private UserRolesDAO userRolesDAO;
    @Autowired
    private RepublicasDAO republicasDAO;
    @Autowired
    private TransactionsDAO transactionsDAO;

    @RequestMapping(value = {"/login", "/"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model){
        return "login";
    }

//    @RequestMapping(value = {"/hello"})
//    public String hello(Model model, HttpServletRequest httpServletRequest){
//        httpSession = httpServletRequest.getSession();
//
//        model.addAttribute("title", "Essa página é pra ROLE_USER");
//        model.addAttribute("message", "Página hello.html");
//        model.addAttribute("httpServletRequest", httpServletRequest);
//        return "hello";
//    }

//    @RequestMapping(value = "/home", method = RequestMethod.GET)
//    public String home(Model model, HttpServletRequest httpServletRequest){
//        model.addAttribute("title", "Esta pagina é pra ROLE_ADMIN");
//        model.addAttribute("message", "Welcome ADMIN!");
//        model.addAttribute("httpServletRequest", httpServletRequest);
//        return "home";
//    }

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
            return  "redirect:/login?error";
        }
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest httpServletRequest) {
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());
        if(user.getStatus() == NOT_IN_REPUBLICA) {
            return "redirect:/welcome";
        } else {
            model.addAttribute("httpServletRequest", httpServletRequest);
            return "home";
        }
    }

    @RequestMapping(value = "/welcome")
    public String welcome(Model model, HttpServletRequest httpServletRequest,
                        @RequestParam(value = "emptyField", required = false) Boolean emptyField,
                        @RequestParam(value = "askedToEnter", required = false) String askedToEnter,
                          @RequestParam(value = "repExists", required = false) Boolean repExists){

        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());

        if(user.getStatus() == NOT_IN_REPUBLICA) {

            if(user.getRepublica() != null){
                model.addAttribute("repName", user.getRepublica().getName());
            } else {
                if (emptyField != null && emptyField) {
                    model.addAttribute("emptyField", emptyField);
                } else if (askedToEnter != null && !askedToEnter.isEmpty()) {
                    model.addAttribute("askedToEnter", askedToEnter);
                }
                if (repExists != null && repExists) {
                    model.addAttribute("repExists", repExists);
                }
            }

            model.addAttribute("httpServletRequest", httpServletRequest);

            return "welcome";

        } else {
            return "redirect:/home";
        }
    }

    @RequestMapping(value = "/enterRep", method = RequestMethod.POST)
    public String enterRep(Model model, HttpServletRequest httpServletRequest,
                           @RequestParam(value = "repToEnter", required = false) String repToEnter) {

        model.addAttribute("httpServletRequest", httpServletRequest);
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());
        Republicas republica = republicasDAO.findByName(repToEnter);

        if (!repToEnter.isEmpty()) {
            if(republica != null){
                user.setRepublica(republica);
                usersDAO.save(user);
                return "redirect:/welcome?askedToEnter=true";
            } else {
                return "redirect:/welcome?askedToEnter=false";
            }
        } else {
            return "redirect:/welcome?emptyField=true";
        }
    }

    @RequestMapping(value = "/createRep", method = RequestMethod.POST)
    public String createRep(Model model, HttpServletRequest httpServletRequest,
                           @RequestParam(value = "repToCreate", required = false) String repToCreate) {
        model.addAttribute("httpServletRequest", httpServletRequest);
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());

        if(repToCreate!= null && repToCreate.isEmpty()) {
           return "redirect:/welcome?emptyField=true";
        }

        Republicas republica = republicasDAO.findByName(repToCreate);
        UserRoles userRole = userRolesDAO.findByUserId(user.getUser_id());

        if(republica == null) {
            republica = republicasDAO.save(new Republicas(repToCreate));

            user.setRepublica(republica);
            user.setStatus(HAS_REPUBLICA);
            usersDAO.save(user);

            userRole.setRole(ROLE_ADMIN);
            userRolesDAO.save(userRole);

            return "redirect:/home";
        } else {
            return "redirect:/welcome?repExists=true";
        }
    }

    public void setUsersDAO(UsersDAO usersDAO){this.usersDAO = usersDAO;}
    public void setUserRolesDAO(UserRolesDAO userRolesDAO) {this.userRolesDAO = userRolesDAO;}
    public void setRepublicasDAO(RepublicasDAO republicasDAO){this.republicasDAO = republicasDAO;}
    public void setTransactionsDAO(TransactionsDAO transactionsDAO){this.transactionsDAO = transactionsDAO;}
}