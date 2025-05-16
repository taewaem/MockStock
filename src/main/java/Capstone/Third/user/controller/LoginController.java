package Capstone.Third.user.controller;

import Capstone.Third.user.entity.User;
import Capstone.Third.user.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") UserLoginForm loginForm) {
        return "users/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") UserLoginForm loginForm,
                        BindingResult result,
                        HttpServletRequest request,
                        @RequestParam(name = "user", defaultValue = "/") String redirectURL) {

        if (result.hasErrors()) {
            return "users/loginForm";
        }

        User loginUser = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginUser == null) {
            log.info("login fail");
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/loginForm";
        }

        log.info("loginId = {}, password = {}", loginUser.getLoginId(), loginUser.getPassword());
        log.info("redirectURL = {}", redirectURL);

        //로그인 성공
        //세션이 있으면 세션 반환, 세션이 없는 경우 신규 생성 default 값은 true
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
//        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
        session.setAttribute("loginUser", loginUser);


        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        //false로 지정하면 세션이 존재하지 않을 경우 새로운 세션을 생성하지 않는다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            //세션 무효화
            session.invalidate();
        }

        return "redirect:/";
    }


}
