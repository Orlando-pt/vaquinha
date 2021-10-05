package com.orlando.vaquinha.web;

import java.sql.Timestamp;
import java.util.List;

import com.orlando.vaquinha.dto.PaymentDto;
import com.orlando.vaquinha.dto.UserDto;
import com.orlando.vaquinha.model.Payment;
import com.orlando.vaquinha.model.User;
import com.orlando.vaquinha.paypal.ApiRequest;
import com.orlando.vaquinha.repository.PaymentRepository;
import com.orlando.vaquinha.service.SecurityService;
import com.orlando.vaquinha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class FrontController {

    @Autowired
    private ApiRequest paypalApiRequest;

    @Autowired
    private PaymentRepository paymentRepository;

    // login staff
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
    
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("balance", paypalApiRequest.getBalance());
        model.addAttribute("payment", new PaymentDto());
        model.addAttribute("user", new UserDto());
        model.addAttribute("contributions", paymentRepository.findTop12ByOrderByTimestampDesc());
        return "index";
    }

    @PostMapping("/contribution")
    public String payment(@ModelAttribute PaymentDto payment, Model model) {
        model.addAttribute("amount", payment.getAmount());
        if (!verifyPaymentParams(model, payment)) {
            var contribution = new Payment();

            contribution.setName(payment.getName());
            contribution.setAmount(payment.getAmount());
            contribution.setTimestamp(new Timestamp(System.currentTimeMillis()));

            paymentRepository.save(contribution);
        }
        return "contribution";
    }

    @PostMapping("/contribution/list")
    public String listContributions(@ModelAttribute UserDto user, Model model) {
        var contributions = this.paymentRepository.findTop12ByName(
            user.getName(),
            Sort.by(Sort.Direction.DESC, "timestamp")
        );

        model.addAttribute("contributions", replaceNameWithMonth(contributions));
        model.addAttribute("name", user.getName());
        return "list_contributions";
    }

    /**
     * endpoints to make login and registration
     * @param contributions
     * @return
     */

    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new User());
        
        return "registration";
    }

    @PostMapping("/registration")
    public String makeRegistration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult,
    Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPassword());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (error != null)
            model.addAttribute("error", "O utilizador e palavra-passe são inválidos");

        if (logout != null)
            model.addAttribute("message", "Bye Bye ;)");

        return "/login";
    }

    private List<Payment> replaceNameWithMonth(List<Payment> contributions) {

        contributions.forEach(
            (contribution) -> contribution.setName(
                getMonthOfTimestamp(contribution.getTimestamp())
            )
        );

        return contributions;
    }

    private String getMonthOfTimestamp(Timestamp timestamp) {
        var year = " de " + timestamp.toString().substring(0, 4);
        switch (timestamp.toLocalDateTime().getMonth().getValue()) {
            case 1:
                return "Janeiro" + year;
            case 2:
                return "Fevereiro" + year;
            case 3:
                return "Março" + year;
            case 4:
                return "Abril" + year;
            case 5:
                return "Maio" + year;
            case 6:
                return "Junho" + year;
            case 7:
                return "Julho" + year;
            case 8:
                return "Agosto" + year;
            case 9:
                return "Setembro" + year;
            case 10:
                return "Outubro" + year;
            case 11:
                return "Novembro" + year;
            case 12:
                return "Dezembro" + year;
        
            default:
                return "Desconhecido";
        }
    }

    private Boolean verifyPaymentParams(Model model, PaymentDto payment) {
        if (payment.getAmount() == null || payment.getName() == null) {
            model.addAttribute("error_message", "Por favor, verifica os campos de submissão. Não são aceites campos vazios.");            
            System.out.println("Null values");
            return true;
        }
        if (payment.getName().isEmpty()) {
            model.addAttribute("error_message", "Por favor, verifica o nome da submissão.");
            System.out.println("Empty name");
            return true;
        }
        if (Double.compare(payment.getAmount(), Double.valueOf(0.0)) <= 0) {
            model.addAttribute("error_message", "O valor da contribuição deve ser superior a 0. Por favor, verifica novamente o campo.");
            System.out.println("Bad amount value");
            return true;
        }
        return false;
    }
}
