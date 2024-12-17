//package Capstone.Third.service;
//
//import Capstone.Third.user.domain.User;
//import Capstone.Third.user.service.UserService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class MemberServiceTest {
//
//    @Autowired
//    private UserService memberService;
//
//
//    @Test
//    public void 조인() {
//        String loginId = "kim";
//        String password = "kim";
//        String name = "김";
//
//        //given
//        User member = User.builder()
//                .name(name)
//                .loginId(loginId)
//                .password(password)
//                .build();
//        //when
//        Long id = memberService.findUser(member);
//
//        //then
//        Assertions.assertEquals(id, memberService.findUser(id));
////        Assertions.assertEquals(member.getName(), "김");
////        Assertions.assertEquals(member.getPassword(), "kim");
////        Assertions.assertEquals(member.getLoginId(), "kim");
//
//
//    }
//
//}