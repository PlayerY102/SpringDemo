package hello;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hello.User;
import hello.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller    // This means that this class is a Controller

public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String showForm() {
		return "index";
	}

	@GetMapping("/register")
	public String showRigister(){
		return "register";
	}

	@PostMapping(path="/add")
	public String addNewUser (@RequestParam String name
			, @RequestParam String email, @RequestParam String password,HttpServletRequest request,Model model) {
		HttpSession session=request.getSession(true);
		User user=new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRemain(100);
		userRepository.save(user);
		session.setAttribute("currentUser",user);
		model.addAttribute("currentUser",user);
		return "main";
	}

	@PostMapping(path = "/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpServletResponse response, HttpServletRequest request,Model model)throws IOException {
		List<User> users = userRepository.findByEmail(email);
		HttpSession session=request.getSession(true);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		if (users == null||users.size()<=0) {
			out.print("<script>alert('该用户不存在')</script>");
			return "index";
		}
		else {
			User user = users.get(0);
			if (user.getPassword().equals(password)) {
				session.setAttribute("currentUser",user);
				model.addAttribute("currentUser",user);
				log.info(user.toString()+ " logged in");
				return "main";
			} else {
				out.print("<script>alert('密码错误')</script>");
				return "index";
			}
		}
	}
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
