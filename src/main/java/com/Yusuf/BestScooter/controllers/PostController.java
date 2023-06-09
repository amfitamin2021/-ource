package com.Yusuf.BestScooter.controllers;

import com.Yusuf.BestScooter.models.Post;
import com.Yusuf.BestScooter.models.Product;
import com.Yusuf.BestScooter.models.User;
import com.Yusuf.BestScooter.repo.ProductRepository;
import com.Yusuf.BestScooter.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.Yusuf.BestScooter.repo.PostRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/order")
    public String order(@RequestParam String name, Model model) {

        model.addAttribute("sname", name);
        return "order";
    }

    @GetMapping("/profil")
    public String profil(Principal principal, Model model){
        if(principal != null){
            String username = principal.getName();
            model.addAttribute("username", username);
            model.addAttribute("role", userRepository.findByUsername(username).getRole());
        }
        Iterable<Post> posts = postRepository.searchAllByUsername(principal.getName());
        model.addAttribute("posts", posts);
        return "profil";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        return "admin";
    }

    @PostMapping("/root")
    public String postsucess(@RequestParam long id, Model model){
        Post p = postRepository.searchById(id);
        if (!p.getStatus().equals("SUCCESSFULLY")){
            p.setStatus("SUCCESSFULLY");
        }
        else if (p.getStatus().equals("SUCCESSFULLY")){
            p.setStatus("PROCESSING");
        }
        postRepository.save(p);
        return "redirect:/root";
    }



    @PostMapping("/orderadd")
    public String orderadd(Principal principal, @RequestParam String name, @RequestParam String family, @RequestParam String email, @RequestParam String address, @RequestParam String payment, @RequestParam String sname, Model model){
        Post post = new Post(name,family,email,address,payment,sname, principal.getName(), "PROCESSING");
        postRepository.save(post);

//        postRepository.searchById(post.getId()).setStatus("SUCCESSFULLY");
        return "redirect:/profil";
    }


    @PostMapping("/root/addProduct")
    public String addProduct(@RequestParam String name, @RequestParam String sum, @RequestParam String img, Principal principal, Model model){
        Product product = new Product(name, sum, img);
        productRepository.save(product);


        return "redirect:/root";
    }

    @PostMapping("/root/deleteuser")
    public String deleteuser(Principal principal, @RequestParam long id){
        User user = userRepository.getOne(id);
        if (userRepository.findByUsername(principal.getName()).getId() != id) {
            userRepository.deleteById(id);
        }
        return "redirect:/root";

    }

    @PostMapping("/root/changerole")
    public String changerole(Principal principal, @RequestParam long id){
        User user = userRepository.getOne(id);
        if (userRepository.findByUsername(principal.getName()).getId() != id){
            if (user.getRole().equals("USER")){
                user.setRole("ADMIN");
            }
            else if(user.getRole().equals("ADMIN")){
                user.setRole("USER");
            }
            userRepository.save(user);
        }

        return "redirect:/root";

    }





    }
