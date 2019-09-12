package com.advanced.taracat.controllerview;

import com.advanced.taracat.service.CatService;
import com.advanced.taracat.service.TarakanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {
    @Autowired
    private CatService catService;
    @Autowired
    private TarakanService tarakanService;

    @GetMapping("/cat")
    public String catFight(){
        return "cat";
    }
    @GetMapping("/tar")
    public String runTarakan(){
        return "tar";
    }
}
