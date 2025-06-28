package kr.ac.kopo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import kr.ac.kopo.service.NaverSearchService;

@RestController
@RequestMapping("/api/naver")
public class NaverSearchController {

    private final NaverSearchService service;
    public NaverSearchController(NaverSearchService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public List<Map<String, String>> search(@RequestParam("query") String query) {
    	  return service.search(query);
    }
}
