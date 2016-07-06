package com.ea975.repfin.controllers;

import com.ea975.repfin.components.Republicas;
import com.ea975.repfin.components.Transactions;
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
import java.util.List;

@Controller
public class TransactionsController {

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private TransactionsDAO transactionsDAO;

    @RequestMapping(value = "/addTransaction", method = RequestMethod.POST)
    public String addTransaction(Model model, HttpServletRequest httpServletRequest,
                                 @RequestParam(value = "descricao", required = false) String descricao,
                                 @RequestParam(value = "valor", required = false) Float valor,
                                 @RequestParam(value = "id", required = false) Integer id) {
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());

        if(descricao == null || valor == null) {
            return "redirect:/home?noTransaction=true";
        }

        Republicas republica = user.getRepublica();
        List<Users> moradores = usersDAO.findByRepublicaId(republica.getRepublica_id());
        Float valorPorMorador = valor/moradores.size();
        Float balance = 0.0F;

        if(id == null) {
            transactionsDAO.save(new Transactions(descricao, valor, user, republica));

            for (Users m: moradores) {
                balance = Float.parseFloat(m.getBalance());
                balance = balance - valorPorMorador;
                m.setBalance(balance.toString());
                usersDAO.save(m);
            }
        } else {
            Transactions t = transactionsDAO.findOne(id);
            Float antigoValorPorMorador = t.getTotal_value()/moradores.size();

            for (Users m: moradores) {
                balance = Float.parseFloat(m.getBalance());
                balance = balance + antigoValorPorMorador - valorPorMorador;
                m.setBalance(balance.toString());
                usersDAO.save(m);
            }

            t.setDescription(descricao);
            t.setTotal_value(valor);
            transactionsDAO.save(t);
        }

        return "redirect:/home";
    }

    @RequestMapping(value = "/deleteTransaction", method = RequestMethod.GET)
    public String deleteTransaction(Model model, HttpServletRequest httpServletRequest,
                                    @RequestParam(value = "id", required = false) Integer id) {
        Users user = usersDAO.findByName(httpServletRequest.getRemoteUser());
        Republicas republica = user.getRepublica();
        List<Users> moradores = usersDAO.findByRepublicaId(republica.getRepublica_id());
        Transactions transaction = transactionsDAO.findOne(id);
        Float valorPorMorador = transaction.getTotal_value()/moradores.size();
        Float balance = 0.0F;

        for (Users m: moradores) {
            balance = Float.parseFloat(m.getBalance());
            balance = balance + valorPorMorador;
            m.setBalance(balance.toString());
            usersDAO.save(m);
        }

        transactionsDAO.delete(id);

        return "redirect:/home";
    }

    public void setUsersDAO(UsersDAO usersDAO){this.usersDAO = usersDAO;}
    public void setTransactionsDAO(TransactionsDAO transactionsDAO){this.transactionsDAO = transactionsDAO;}
}
