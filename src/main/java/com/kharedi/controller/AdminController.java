package com.kharedi.controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kharedi.dto.productDTO;
import com.kharedi.model.Category;
import com.kharedi.model.Product;
import com.kharedi.repository.OrderRepository;
import com.kharedi.service.CategoryService;
import com.kharedi.service.OrderDataService;
import com.kharedi.service.ProductService;
import com.kharedi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

	private static String dir = System.getProperty("user.dir") + "/src/main/resources/static/images/ProductImages";
	
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderDataService orderService;

    @GetMapping("/admin")
    public String admin(Model m){
    	m.addAttribute("users" , userService.getAllUsers());
    	m.addAttribute("orders" , orderService.getAllOrders());
        return "AdminPage";
    }


    @GetMapping("/admin/categories")
    public String category(Model m){
        m.addAttribute("categories", categoryService.findAllCategory());
        return "categories";
    }

    public int stocksForCategory(int category_Id){
        List<Product> products = productService.getAllProductByCategoryId(category_Id);
        int totalStock=0;
        for(Product product:products){
            totalStock += product.getStock();
        }
        return totalStock;
    }

    @ModelAttribute("currentURI")
    public String getCurrentURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/admin/categories/add")
    public String addCategory(Model m){
        m.addAttribute("category" , new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postAddCategory(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deletingCategoryId(@PathVariable int id){
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updatingCategoryId(@PathVariable int id , Model m){
        Optional<Category> category = categoryService.putCategoryById(id);
        if(category.isPresent()){
            m.addAttribute("category" , category.get());
            return "categoriesAdd";
        }else {
            return "404";
        }
    }

    @GetMapping("/admin/product")
    public String Products(Model m){
        m.addAttribute("products" , productService.getAllProducts());
        return "products";
    }

    @GetMapping("/admin/addProducts")
    public String addProducts(Model m){
        m.addAttribute("productDTO" , new productDTO());
        m.addAttribute("categories" , categoryService.findAllCategory());
        return "addProducts";
    }

    @PostMapping("/admin/addProducts")
    public String addProductPost(@ModelAttribute("productDTO") productDTO pDTO,
            @RequestParam("image") MultipartFile file,
            @RequestParam("featureProducts") String featureProductsStr,
            @RequestParam("imageName") String imageName,
            RedirectAttributes redirectAttributes,
            Model m) throws IOException {

        boolean featureProducts = Boolean.parseBoolean(featureProductsStr);

        Product product = new Product();
        
        String productId = UUID.randomUUID().toString();
        
        
        product.setId(pDTO.getId());
        product.setTitle(pDTO.getTitle());
        product.setCategory(categoryService.putCategoryById(pDTO.getCategoryId()).get());
        product.setDescription(pDTO.getDescription());
        product.setOff(pDTO.getOff());
        product.setListPrice(pDTO.getListPrice());
        product.setOurPrice(pDTO.getOurPrice());
        product.setStarRating(pDTO.getStarRating());
        product.setActive(pDTO.isActive());
        product.setStock(pDTO.getStock());

        try {
            byte[] imageByte = file.getBytes();
            String imageNameWithId = productId + ".jpg";
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(dir, imageNameWithId)));
            bos.write(imageByte);
            bos.close();
            
            product.setImageName(imageNameWithId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        productService.addProduct(product);
        
        redirectAttributes.addFlashAttribute("reloadPage", true);

        List<Product> featureProductsList = productService.getFeatureProducts(featureProducts);
        
        m.addAttribute("featureProducts", featureProductsList);
        m.addAttribute("reloadPage", true);

        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deletingProductId(@PathVariable long id){
        productService.productRemoveById(id);
        return "redirect:/admin/product";
    }

    
    @GetMapping("/admin/product/update/{id}")
    public String productUpdating(@PathVariable long id, Model m) {
        Optional<Product> optionalProduct = productService.productGetById(id);
        
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            productDTO productDTO = new productDTO();
    
            productDTO.setId(product.getId());
            productDTO.setTitle(product.getTitle());
            productDTO.setCategoryId(product.getCategory().getId());
            productDTO.setDescription(product.getDescription());
            productDTO.setOff(product.getOff());
            productDTO.setListPrice(product.getListPrice());
            productDTO.setOurPrice(product.getOurPrice());
            productDTO.setStarRating(product.getStarRating());
            productDTO.setActive(product.isActive());
            productDTO.setStock(product.getStock());
    
            productDTO.setImageName(product.getImageName());
            productDTO.setImages(product.getImages());
    
            m.addAttribute("categories", categoryService.findAllCategory());
            m.addAttribute("productDTO", productDTO);
    
            return "addProducts";
        } else {
            return "404";
        }
    }
    
    @GetMapping("/admin/user/delete/{id}")
    public String userRemoveByAdmin(@PathVariable long id) {
    	userService.userRemoveById(id);
    	return "redirect:/admin";
    }
    
    @GetMapping("/admin/order/delete/{id}")
    public String orderRemoveByAdmin(@PathVariable long id) {
    	orderService.deleteOrderById(id);
    	return "redirect:/admin";
    }
    
}