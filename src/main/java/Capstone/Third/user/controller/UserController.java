package Capstone.Third.user.controller;

import Capstone.Third.user.entity.User;
import Capstone.Third.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("userForm") UserSaveForm userForm) {
        log.info("signupForm");
        return "users/signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("userForm") UserSaveForm userForm, BindingResult result) {

        log.info("signup");

        //에러가 발생하면 userForm으로 이동
        if (result.hasErrors()) {
            return "users/signupForm";
        }

        User user = User.builder()
                .loginId(userForm.getLoginId())
                .password(userForm.getPassword())
                .userName(userForm.getUserName())
                .money(10000000L)   //시드 머니 1000만원으로 시작
                .build();

        user.setStockMoney(0L);
        userService.join(user);

        log.info("signup success");
        return "redirect:/";
    }


}
