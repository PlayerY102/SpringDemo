package hello;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hello.User;
import hello.UserRepository;

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
			, @RequestParam String email, @RequestParam String password) {
		User user=new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		userRepository.save(user);

		log.info(user.toString()+" saved to the repo");
		return "main";
	}

	@PostMapping(path = "/login")
	public String login(@RequestParam String email, @RequestParam String password, Model model) {
		List<User> users = userRepository.findByEmail(email);
		if (users == null) {
			log.info("attempting to log in with the non-existed account");
			return "该用户不存在";
		} else {
			User user = users.get(0);
			if (user.getPassword().equals(password)) {
				// 如果密码与邮箱配对成功:
				model.addAttribute("name", user.getName());
				log.info(user.toString()+ " logged in");
			} else {
				// 如果密码与邮箱不匹配:
				model.addAttribute("name", "logging failed");
				log.info(user.toString()+ " failed to log in");
			}
			return "index";
		}
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
