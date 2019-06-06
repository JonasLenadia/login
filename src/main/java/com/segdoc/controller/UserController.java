package com.segdoc.controller;

import javax.validation.Valid;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.segdoc.model.User;
import com.segdoc.service.UserService;

@Controller
public class UserController {

	@Autowired(required=false)
	private UserService userService;
	
	@RequestMapping(value= {"/", "/login"}, method=RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("user/login");
		return model;
	}
	
	@RequestMapping(value = {"/signup"}, method=RequestMethod.GET)
	public ModelAndView signup() {
		ModelAndView model = new ModelAndView();
		User user = new User();
		model.addObject("user", user);
		model.setViewName("user/signup");
		
		return model;
	}
	
	@RequestMapping(value= {"/signup"}, method=RequestMethod.POST)
	public ModelAndView createUser(@Valid User user, BindingResult bindingResults) {
		ModelAndView model = new ModelAndView();
		User userExists = userService.findByEmail(user.getEmail());
		
		if(user != null) {
			bindingResults.rejectValue("email", "User has been registered successfully!");
		}if(bindingResults.hasErrors()) {
			model.setViewName("user/signup");
		}else {
			userService.saveUser(user);
			model.addObject("msg", "User has been registed successfully!");
			model.addObject("user", new User());
			model.setViewName("user/signup");
			
		}
		
		return model;
		
	}
	
	@RequestMapping(value= {"/home/home"}, method=RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView model = new ModelAndView();
		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		
		model.addObject("userName", user.getFirstname());
		model.setViewName("home/home");
		return model;
	}
	@RequestMapping(value= {"/access_denied"}, method=RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView model = new ModelAndView();
		model.setViewName("errors/access_denied");
		return model;
	}
	
}
