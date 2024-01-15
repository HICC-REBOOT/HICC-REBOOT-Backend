package hiccreboot.backend.controller;

import hiccreboot.backend.common.dto.Appendix.AppendixResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppendixController {

    @GetMapping
    public Object getAppendices(@RequestParam("count") int count) {
        // 이 부분에 Appendix 생성 로직 작성

        List<String> appendices = new ArrayList<>();
        return new AppendixResponse(appendices);
    }
}
