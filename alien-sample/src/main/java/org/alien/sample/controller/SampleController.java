package org.alien.sample.controller;

import org.alien.framework.ioc.annotation.Inject;
import org.alien.framework.mvc.annotation.Action;
import org.alien.framework.mvc.annotation.Controller;
import org.alien.framework.mvc.bean.Data;
import org.alien.framework.mvc.bean.View;
import org.alien.sample.Service.MyService;

@Controller
public class SampleController {

    @Inject
    public MyService service;

    @Action("get:/testJson")
    public Data testJson(){
        Data data = new Data();
        data.setModel(service.getPerson());
        return data;
    }


    @Action("get:/testJsp")
    public View testJsp(){
        View view = new View("index.jsp");
        return view;
    }
}
