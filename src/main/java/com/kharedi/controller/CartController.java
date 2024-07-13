package com.kharedi.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kharedi.Global.Global;
import com.kharedi.Utility.MailConstructor;
import com.kharedi.model.OrderedData;
import com.kharedi.model.Product;
import com.kharedi.model.User;
import com.kharedi.model.UserDetail;
import com.kharedi.service.OrderDataService;
import com.kharedi.service.ProductService;
import com.kharedi.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private ProductService productService;
	@Autowired
	private OrderDataService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private MailConstructor mailConstructor;

	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable int id) {
		Global.cart.add(productService.productGetById(id).get());
		return "redirect:/product";
	}

	@GetMapping("/cart")
	public String cartView(Model m) {
		m.addAttribute("cartCount", Global.cart.size());
		m.addAttribute("total", Global.cart.stream().mapToDouble(Product::getOurPrice).sum());
		m.addAttribute("cart", Global.cart);
		return "cart";
	}

	@GetMapping("/cart/removeItem/{index}")
	public String removeCartItem(@PathVariable int index) {
		Global.cart.remove(index);
		return "redirect:/cart";
	}

	@GetMapping("/checkout")
	public String checkout(Model m, Authentication authentication, HttpSession session) {
	    Object principal = authentication.getPrincipal();
	    String firstName = null;
	    String lastName = null;
	    String address = null; // Add this line

	    if (principal instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) principal;
	        String username = userDetails.getUsername();
	        // Assuming you have a service method to fetch user details by username
	        User user = userService.getUserByUsername(username);
	        if (user != null) {
	            firstName = user.getFirstName();
	            lastName = user.getLastName();
	            address = user.getAddress(); // Assuming address is a field in the User entity
	        }
	    }

	    m.addAttribute("firstName", firstName != null ? firstName : "");
	    m.addAttribute("lastName", lastName != null ? lastName : "");
	    m.addAttribute("address", address != null ? address : ""); // Add this line

	    Long orderId = (Long) session.getAttribute("orderId");
	    m.addAttribute("orderId", orderId);
	    m.addAttribute("total", Global.cart.stream().mapToDouble(Product::getOurPrice).sum());
	    return "checkout";
	}

	@PostMapping("/updateUserDetails")
	public String updateUserDetails(
	        @RequestParam("userId") Long userId,
	        @RequestParam("address") String address,
	        @RequestParam("postcode") String postcode,
	        @RequestParam("city") String city,
	        Model model
	) {
	    // Update user details in the database
	    userService.updateUserDetails(userId, address, postcode, city);
	    
	    // Pass the userId to the checkout page
	    model.addAttribute("userId", userId);
	    
	    return "redirect:/checkout";        
	}


	@GetMapping("/payNow")
	public String getPayNow() {
	    // You can handle GET requests for payNow here, if needed
	    // This method might return a view for displaying payment information or form
	    // Alternatively, it could redirect to a different page
	    return "redirect:/checkout"; // Example redirect to a payment form page
	}


	@PostMapping("/payNow")
	public String payNow(Authentication authentication, Model model, HttpSession session) {
	    Object principal = authentication.getPrincipal();
	    if (principal instanceof DefaultOidcUser) {
	        // If the user is logged in with Google
	        DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
	        String firstName = oidcUser.getGivenName();
	        String lastName = oidcUser.getFamilyName();
	        String email = oidcUser.getEmail();
	        
	        // Create the order
	        OrderedData order = new OrderedData();
	        order.setFirstName(firstName);
	        order.setLastName(lastName);
	        order.setUsername(email);
	        LocalDate confirmDate = LocalDate.now().plusDays(15);
	        order.setConfirmOrderDate(Date.from(confirmDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	        orderService.saveOrder(order);
	        
	        session.setAttribute("orderId", order.getId());
	        return "redirect:/confirmation?orderId=" + order.getId();
	    } else if (principal instanceof UserDetail) {
	        // If the user is a registered user
	        UserDetail userDetails = (UserDetail) principal;
	        long id = userDetails.getId();
	        String firstName = userDetails.getFirstName();
	        String lastName = userDetails.getLastName();
	        String email = userDetails.getEmail();
	        String phone = userDetails.getPhone();
	        String address = userDetails.getAddress();
	        String username = userDetails.getUsername();
	        double totalPrice = Global.cart.stream().mapToDouble(Product::getOurPrice).sum();
	        
	        // Create the order
	        OrderedData order = new OrderedData();
	        order.setFirstName(firstName);
	        order.setLastName(lastName);
	        order.setUsername(username);
	        order.setAddress(address);
	        order.setPhoneNumber(phone);
	        order.setEmail(email);
	        order.setOrderDate(new Date());
	        order.setTotalPrice(totalPrice);
	        
	        List<String> productNames = new ArrayList<>();
	        for(Product product: Global.cart) {
	        	productNames.add(product.getTitle());
	        }
	        
	        order.setProductName(productNames);
	        
	        User user = new User();
	        user.setId(id);
	        user.setEmail(email);
	        order.setUser(user);
	        
	        LocalDate confirmDate = LocalDate.now().plusDays(15);
	        order.setConfirmOrderDate(Date.from(confirmDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	        orderService.saveOrder(order);
	        
	        session.setAttribute("orderId", order.getId());
	        
	        //Email Handling
	        mailConstructor.sendHtmlEmail(user, order);

	        return "redirect:/confirmation?orderId=" + order.getId();
	    } else {
	        return "redirect:/404";
	    }
	}




	@GetMapping("/confirmation")
	public String confirm(Model m, @RequestParam("orderId") Long orderId) {
		OrderedData order = orderService.findById(orderId);
		m.addAttribute("orderId", orderId);
		m.addAttribute("order", order);
		Global.cart.clear();
		return "confirmation";
	}

}
