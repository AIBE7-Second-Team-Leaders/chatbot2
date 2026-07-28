package org.example.chatbot2.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/chat")
    public String chat(HttpSession session) {
        if (session.getAttribute("loginUserId") == null) {
            return "redirect:/";
        }

        return "chat";
    }
}
