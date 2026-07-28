package org.example.chatbot2.web;

import jakarta.servlet.http.HttpSession;
import org.example.chatbot2.chat.domain.AppUser;
import org.example.chatbot2.chat.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public String signUp(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String displayName,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authService.signUp(email, password, displayName);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "회원가입이 완료되었습니다."
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            AppUser user = authService.login(email, password);

            session.setAttribute("loginUserId", user.getUserId());
            session.setAttribute("loginUserName", user.getDisplayName());

            return "redirect:/chat";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
