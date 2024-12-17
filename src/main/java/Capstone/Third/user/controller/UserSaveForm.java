package Capstone.Third.user.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserSaveForm {

    @NotBlank(message = "아이디를 입력하세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "이름을 입력하세요")
    private String userName;

}
