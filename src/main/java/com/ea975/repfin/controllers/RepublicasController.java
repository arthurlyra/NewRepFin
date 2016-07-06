package com.ea975.repfin.controllers;

import com.ea975.repfin.components.Republicas;
import com.ea975.repfin.components.UserRoles;
import com.ea975.repfin.components.Users;
import com.ea975.repfin.daos.RepublicasDAO;
import com.ea975.repfin.daos.TransactionsDAO;
import com.ea975.repfin.daos.UserRolesDAO;
import com.ea975.repfin.daos.UsersDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RepublicasController {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final Integer NOT_IN_REPUBLICA = 1;
    private static final Integer HAS_REPUBLICA = 2;

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private UserRolesDAO userRolesDAO;
    @Autowired
    private RepublicasDAO republicasDAO;

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

        if(StringUtils.isBlank(repName) || rep2 != null) {
            return "redirect:/republica?edited=false";
        }

        republica.setName(repName);
        republicasDAO.save(republica);

        return "redirect:/republica?edited=true";
    }

    public void setUsersDAO(UsersDAO usersDAO){this.usersDAO = usersDAO;}
    public void setUserRolesDAO(UserRolesDAO userRolesDAO) {this.userRolesDAO = userRolesDAO;}
    public void setRepublicasDAO(RepublicasDAO republicasDAO){this.republicasDAO = republicasDAO;}
}
