package jy.zhongye.controller;

import jy.zhongye.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    @Autowired
    AccountService accountService;

    /***
     * @method index
     * @description
     * @author
     * @date
     * @param
     * @return
     */
    @RequestMapping({"/", "/index"})
    public ModelAndView index() {
        return new ModelAndView("index");
    }

}
