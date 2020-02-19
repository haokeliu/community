package com.nowcoder.community.controller;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    //响应JSON数据 （异步请求）  eg：注册时判断 昵称是否可用
    //Java对象 => JSON字符串 (衔接的作用) => JS对象
    @GetMapping("/emp")
    @ResponseBody //返回字符串
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age",23);
        return emp;
    }

    @GetMapping("/emps")
    @ResponseBody //返回字符串
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age",23);
        Map<String, Object> emp2 = new HashMap<>();
        emp2.put("name", "李四");
        emp2.put("age",24);
        list.add(emp);
        list.add(emp2);
        return list;
    }

    //响应HTML数据
    @GetMapping("/teacher")
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age","30");
        modelAndView.setViewName("/demo/view");
        // 把modelandView编辑好之后直接返回给前端控制器
        return modelAndView;
    }

    @GetMapping("/school")
    public String getSchool(Model model) {
        //把model 从 前端控制器获取 然后结束后返回视图
        model.addAttribute("name", "peakingU");
        model.addAttribute("age","120");
        return "/demo/view";
    }

    //Post请求
    @PostMapping("/student")
    @ResponseBody
    public String saveStudent(String name, int age) {
        return "name : "+name +", age = "+age;
    }

    //Get请求
    // students?current=1&limit=20
    //@RequestMapping(path = "/students", method = RequestMethod.GET)
    @GetMapping(path = "/students")
    @ResponseBody
    public String getStudents(
            // @RequestParam表示请求中的参数 ， 参数名为current 可以不传  不传默认值为1  为变量 current赋值
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {

        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // student/123 路径参数
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        return ""+id;
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " " + value);
        }
        System.out.println(request.getParameter("code"));

        // response 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try ( //Java7 的新改动，可以少写finally
                PrintWriter writer = response.getWriter();
                ){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/data")
    @ResponseBody
    public String findData () {
        return alphaService.find();
    }

    // cookie示例

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置cookie生效的范围
        cookie.setPath("/community/alpha");
        // 设置cookie的生存时间
        cookie.setMaxAge(60 * 10);
        // 发送cookie
        response.addCookie(cookie);

        return "set cookie";
    }

    //ajax示例
    @PostMapping("/ajax")
    @ResponseBody
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJsonString(0,"操作成功");
    }
}
