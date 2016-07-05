package com.ea975.repfin.controllers;

import com.ea975.repfin.components.Republicas;
import com.ea975.repfin.components.Transactions;
import com.ea975.repfin.components.UserRoles;
import com.ea975.repfin.components.Users;
import com.ea975.repfin.daos.*;

import com.sun.javafx.binding.StringFormatter;
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
    @Autowired
    private SubTransactionsDAO subTransactionsDAO;

    @RequestMapping(value = {"/login", "/"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model){
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, @RequestParam(value = "username", required = true) String username,
                           @RequestParam(value = "password", required = true) String password,
                           @RequestParam(value = "email", required = true) String email,
                           HttpServletRequest httpServletRequest) {
        Users user2 = usersDAO.findByName(username);

        if(!(username.isEmpty() || password.isEmpty() || email.isEmpty() || user2 != null)) {
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
    public String home(Model model, HttpServletRequest httpServletRequest,
                       @RequestParam(value = "id", required = false) Integer transactionId) {

        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());
        Republicas republica;
        List<Transactions> transactionsList;
        Transactions editTransaction;
        Float totalTransactions = 0.0F;

        if(user.getStatus() == NOT_IN_REPUBLICA) {
            return "redirect:/welcome";
        } else {
            republica = user.getRepublica();
            transactionsList = transactionsDAO.findByRepublicaId(republica.getRepublica_id());

            for (Transactions transaction: transactionsList) {
                totalTransactions += transaction.getTotal_value();
            }

            if(transactionId != null){
                StringBuilder url = new StringBuilder();
                editTransaction = transactionsDAO.findOne(transactionId);

                url.append("editTransaction?id=").append(editTransaction.getTransaction_id());
                String uri = url.toString();
                
                model.addAttribute("descricao", editTransaction.getDescription());
                model.addAttribute("valor", editTransaction.getTotal_value());
                model.addAttribute("newOrEdit", "Editar transação:");
                model.addAttribute("url", uri);
            } else {
                model.addAttribute("newOrEdit","Adicionar nova transação:");
            }

            model.addAttribute("user", user);
            model.addAttribute("transactions", transactionsList);
            model.addAttribute("totalTransactions", totalTransactions);
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

    @RequestMapping(value = "/user")
    public String showUser(Model model, HttpServletRequest httpServletRequest,
                           @RequestParam(value = "edited", required = false) Boolean edited) {
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());

        if(user.getStatus() == NOT_IN_REPUBLICA) {
            return "redirect:/welcome";
        }

        if(edited != null) {
            model.addAttribute("edited", edited);
        }
        model.addAttribute("user", user);
        return "user";
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public String editUser(Model model, HttpServletRequest httpServletRequest,
                           @RequestParam(value = "password", required = false) String password) {
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());

        if(password == null) {
            return "redirect:/user?edited=false";
        }

        user.setPassword(password);

        usersDAO.save(user);

        return "redirect:/user?edited=true";
    }

    @RequestMapping(value = "/republica", method = RequestMethod.GET)
    public String showRepublica(Model model, HttpServletRequest httpServletRequest,
                                @RequestParam(value = "edited", required = false) Boolean edited,
                                @RequestParam(value = "accepted", required = false) Boolean accepted,
                                @RequestParam(value = "rejected", required = false) Boolean rejected,
                                @RequestParam(value = "removed", required = false) Boolean removed) {
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());

        if(user.getStatus() == NOT_IN_REPUBLICA) {
            return "redirect:/welcome";
        }

        List<Users> pendingUsers = usersDAO.findPendingUsersOfRepublica(user.getRepublica().getRepublica_id());
        List<Users> moradores = usersDAO.findByRepublicaId(user.getRepublica().getRepublica_id());

        if(edited != null) {
            model.addAttribute("edited", edited);
        }
        if(accepted != null) {
            model.addAttribute("accepted", accepted);
        }
        if(rejected != null) {
            model.addAttribute("rejected", rejected);
        }
        if(removed != null) {
            model.addAttribute("removed", removed);
        }
        model.addAttribute("users", pendingUsers);
        model.addAttribute("user", user);
        model.addAttribute("republica", user.getRepublica());
        model.addAttribute("moradores", moradores);
        return "republica";
    }

    @RequestMapping(value = "/editRepublica", method = RequestMethod.POST)
    public String editRepublica(Model model, HttpServletRequest httpServletRequest,
                                @RequestParam(value = "repName", required = false) String repName) {
        Republicas republica = usersDAO.findByName(httpServletRequest.getRemoteUser()).getRepublica();
        Republicas rep2 = republicasDAO.findByName(repName);

        if(repName == null || rep2 != null) {
            return "redirect:/republica?edited=false";
        }

        republica.setName(repName);
        republicasDAO.save(republica);

        return "redirect:/republica?edited=true";
    }

    @RequestMapping(value = "/acceptUser", method = RequestMethod.GET)
    public String acceptUser(Model model, HttpServletRequest httpServletRequest,
                             @RequestParam(value = "id", required = true) Integer id) {
        Users user = usersDAO.findOne(id);
        UserRoles userRole = userRolesDAO.findByUserId(id);

        user.setStatus(HAS_REPUBLICA);
        usersDAO.save(user);

        userRole.setRole(ROLE_ADMIN);
        userRolesDAO.save(userRole);

        return "redirect:/republica?accepted=true";
    }

    @RequestMapping(value = "/rejectUser", method = RequestMethod.GET)
    public String rejectUser(Model model, HttpServletRequest httpServletRequest,
                             @RequestParam(value = "id", required = true) Integer id) {
        Users user = usersDAO.findOne(id);
        UserRoles userRole = userRolesDAO.findByUserId(id);

        user.setRepublica(null);
        usersDAO.save(user);

        return "redirect:/republica?rejected=true";
    }

    @RequestMapping(value = "/removeUser", method = RequestMethod.GET)
    public String removeUser(Model model, HttpServletRequest httpServletRequest,
                             @RequestParam(value = "id", required = true) Integer id) {
        Users user = usersDAO.findOne(id);
        UserRoles userRole = userRolesDAO.findByUserId(id);

        user.setRepublica(null);
        user.setBalance("0.00");
        user.setStatus(NOT_IN_REPUBLICA);
        usersDAO.save(user);

        userRole.setRole(ROLE_USER);
        userRolesDAO.save(userRole);

        return "redirect:/republica?removed=true";
    }

    public void setUsersDAO(UsersDAO usersDAO){this.usersDAO = usersDAO;}
    public void setUserRolesDAO(UserRolesDAO userRolesDAO) {this.userRolesDAO = userRolesDAO;}
    public void setRepublicasDAO(RepublicasDAO republicasDAO){this.republicasDAO = republicasDAO;}
    public void setTransactionsDAO(TransactionsDAO transactionsDAO){this.transactionsDAO = transactionsDAO;}
    public void setSubTransactionsDAO(SubTransactionsDAO subTransactionsDAO){this.subTransactionsDAO = subTransactionsDAO;}
}