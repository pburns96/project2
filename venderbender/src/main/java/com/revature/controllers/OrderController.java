package com.revature.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revature.beans.Album;
import com.revature.beans.Concert;
import com.revature.beans.Customer;
import com.revature.beans.Order;
import com.revature.beans.OrderItem;
import com.revature.services.DataService;

@Controller
public class OrderController {

	@Autowired
	private DataService dataService;

	// This is used for creating a Order object for a service side cart for each
	// session
	
	private static final Logger log = Logger.getLogger(OrderController.class);
	

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	// Get all Orders and provide customer.
	// If you are not a manager than you only retrive orders from that provided
	// customer
	@RequestMapping(value = "/getAllOrders.do", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Order>> getAllOrders(HttpServletRequest request) {
		log.info("Getting orders");
		HttpSession session = request.getSession(false);
		if(session!=null){ //If no session, fail to fetch orders
			Customer customer = (Customer) session.getAttribute("customer");
			if(customer!=null){ //if no customer, fail to fetch orders
				if(customer.isManager()){ //if manager, fetch all orders.
					return new ResponseEntity<List<Order>>(this.dataService.getOrders(),HttpStatus.OK);
				}
				else{ //if not manager, fetch orders by customers
					return new ResponseEntity<List<Order>>(this.dataService.getOrders(this.dataService.getCustomer(customer.getUsername())),HttpStatus.OK);
				}
			}
		}
		//if it fails to fetch orders return status forbidden and an empty list
		return new ResponseEntity<List<Order>>(new LinkedList<Order>(),HttpStatus.FORBIDDEN);
	}

	@RequestMapping(value = "/getOrder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Order> getOrdersFromCustomer(@RequestParam int id) {
		// Get customer from session
		// If a customer isnt logged in the nredirect them to log in
		// If a customer request for a order that isnt theirs throw an error
		return new ResponseEntity<Order>(this.dataService.getOrder(id), HttpStatus.OK);
	}

	@RequestMapping(value = "/createOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Void> createOrder(@RequestBody Order order,  HttpServletRequest request) {
		// Get customer from session
		// If a customer isnt logged in the nredirect them to log in
		Customer customer = (Customer)request.getSession().getAttribute("customer");
		if (customer != null) {
		this.dataService.createOrder(order,customer);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
	}

	@RequestMapping(value = "/cart/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Order> cartAddItem(@RequestBody OrderItem item, HttpServletRequest request) {
		// If a customer isnt logged in the nredirect them to log in
		if (request.getSession().getAttribute("customer") != null) {
			//applicationContext = new FileSystemXmlApplicationContext("/src/main/webapp/WEB-INF/bender.xml");
			Order cart = getCart(request);
			//If that item is already added then update the quantity instead
			boolean found = false;
	
			for (OrderItem listItem : cart.getOrderItems()) {
				Album listAlbum = listItem.getAlbum();
				Album album = item.getAlbum();
				Concert listConcert =  listItem.getConcertTicket();
				Concert concert =  item.getConcertTicket();
				if((album != null && listAlbum!= null  && (album.equals(listAlbum))) ||(
					concert!= null && listConcert != null && (concert.getBand() == listConcert.getBand())))
				{
					found =true;
					listItem.setQuantity(listItem.getQuantity() + 1);
					break;
				}
			}
			
			if(!found)
			{
			cart.addOrderItem(item);
			}
			request.getSession().setAttribute("cart",cart);
			return new ResponseEntity<Order>(cart,HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Order>(HttpStatus.FORBIDDEN);
		}
	}
	
	@RequestMapping(value = "/cart/remove", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Order> cartRemoveItem(@RequestBody OrderItem item, HttpServletRequest request) {
		// If a customer isnt logged in the nredirect them to log in
		if (request.getSession().getAttribute("customer") != null) {
			//applicationContext = new FileSystemXmlApplicationContext("/src/main/webapp/WEB-INF/bender.xml");
			Order cart = getCart(request);
			
			cart.getOrderItems().remove(item);
			request.getSession().setAttribute("cart",cart);
			
			return new ResponseEntity<Order>(cart,HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Order>(HttpStatus.FORBIDDEN);
		}
	}
	
	@RequestMapping(value = "/cart/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Order> cartUpdateItemQuantity(@RequestBody OrderItem item, HttpServletRequest request) {
		// If a customer isnt logged in then redirect them to log in
		if (request.getSession().getAttribute("customer") != null) {
			//applicationContext = new FileSystemXmlApplicationContext("/src/main/webapp/WEB-INF/bender.xml");
			Order cart = getCart(request);
			for (OrderItem listItem : cart.getOrderItems()) {
				Album listAlbum = listItem.getAlbum();
				Album album = item.getAlbum();
				Concert listConcert =  listItem.getConcertTicket();
				Concert concert =  item.getConcertTicket();
				if((album != null && listAlbum!= null  && (album.equals(listAlbum))) ||(
					concert!= null && listConcert != null && (concert.getBand() == listConcert.getBand())))
				{
					listItem.setQuantity(item.getQuantity());
					break;
				}
			}
			
			request.getSession().setAttribute("cart",cart);
			
			return new ResponseEntity<Order>(cart,HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Order>(HttpStatus.FORBIDDEN);
		}
	}
	
	private Order getCart(HttpServletRequest request)
	{
		Order cart = (Order) request.getSession().getAttribute("cart");
		if(cart == null)
		{
			//cart = (Order)ApplicationContextService.getContext().getBean("order");
			cart = new Order();
			request.getSession().setAttribute("cart",cart);
		}
		return cart;
	}
	

}