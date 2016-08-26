package guru.tour.controller;


import guru.tour.entity.HotNewsEntity;

import guru.tour.entity.LocationEntity;

import guru.tour.exception.HomeException;

import guru.tour.service.HotNewsEntityManager;
import guru.tour.service.LocationEntityManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping(value = "/")
public class HomepageController {

	
	@Autowired
	HotNewsEntityManager hotnews;
	
	

	@Autowired
	LocationEntityManager locationEntityManager;


	private static final Logger logger = LoggerFactory
			.getLogger(HomepageController.class);

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String homepage() throws Exception {
		// returning list hotnew and event,place data load into homepage
		List<HotNewsEntity> list = new ArrayList<HotNewsEntity>();
		list = hotnews.getAllHotNews();
		if (list.isEmpty() == true) {
			throw new HomeException(1);
		}
		for (HotNewsEntity hotNewsEntity : list) {
			System.out.println(hotNewsEntity.getHotnewsId() + " " + hotNewsEntity.getName() + "- "+ hotNewsEntity.getDescription()+" "+hotNewsEntity.getImage());				
		}
		return "homePage";
	}
	@RequestMapping(value = "/home/{id}", method = RequestMethod.GET)
	public String getEmployee(@PathVariable("id") int id, Model model)
			throws Exception {
		// deliberately throwing different types of exception
		if (id == 1) {
			throw new HomeException(id);
		} else if (id == 2) {
			throw new SQLException("SQLException, id=" + id);
		} else if (id == 3) {
			throw new IOException("IOException, id=" + id);
		} else if (id == 10) {
			List<HotNewsEntity> list = new ArrayList<HotNewsEntity>();
			list = hotnews.getAllHotNews();
			if (list.isEmpty() == true) {
				throw new HomeException(1);
			}
			
			return "homePage";
		} else {
			throw new Exception("Generic Exception, id=" + id);
		}

	}

	@ExceptionHandler(HomeException.class)
	public ModelAndView handleEmployeeNotFoundException(
			HttpServletRequest request, Exception ex) {
		logger.error("Requested URL=" + request.getRequestURL());
		logger.error("Exception Raised=" + ex);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exception", ex);
		modelAndView.addObject("url", request.getRequestURL());

		modelAndView.setViewName("HomeError");
		return modelAndView;
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about() {

		return "about";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact() {

		return "contact";
	}
	
	
	
/*	searchLocation*/
	@RequestMapping(value = "/searchLocation", method = RequestMethod.GET)
	public String searchLocation(HttpServletRequest request,ModelMap model) {
							
		LocationEntity locationEntity;		
		locationEntity =locationEntityManager.findByLocation_name(request.getParameter("locationName"));
		
		if(locationEntity != null){
			
			model.addAttribute("placeName", locationEntity.getName());
			/*model.addAttribute("locationEntity", locationEntity);*/
			/*model.put("placeName", locationEntity.getName());*/
			System.out.println("aaaaaaaaaaa   "+ locationEntity.getName().toString());
			return "location";			
		}
		
		return "HomeError";
	}	
}
