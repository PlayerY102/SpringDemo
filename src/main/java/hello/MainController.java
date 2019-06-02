package hello;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hello.User;
import hello.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller    // This means that this class is a Controller

public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private FriendRepository friendRepository;


	@GetMapping("/")
	public String showBegin() {
		return "index";
	}

	@GetMapping("/register")
	public String showRegister(){
		return "register";
	}

	@PostMapping(path="/add")
	public String addNewUser (@RequestParam String name
			, @RequestParam String email, @RequestParam String password,HttpServletRequest request,Model model) {

		HttpSession session=request.getSession();

		User user=new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRemain(100);
		userRepository.save(user);

		session.setAttribute("currentUser",user);
		return "updateMain";
	}

	@PostMapping(path = "/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpServletResponse response, HttpServletRequest request,Model model)throws IOException {

		HttpSession session=request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		List<User> users = userRepository.findByEmail(email);

		if (users == null||users.size()<=0) {
			out.print("<script>alert('该用户不存在')</script>");
			return "index";
		}
		User user = users.get(0);
		if (!user.getPassword().equals(password)) {
			out.print("<script>alert('密码错误')</script>");
			return "index";
		}

		session.setAttribute("currentUser",user);
		return "updateMain";
	}

	@PostMapping(path="/addFriend")
	public String addFriend(@RequestParam String friendEmail,Model model,HttpServletResponse response,HttpServletRequest request)throws IOException{
		HttpSession session=request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		User userA=(User)session.getAttribute("currentUser");
		if(userA== null){
			out.print("<script>alert('你还未登录')</script>");
			return "index";
		}
		List<User> users = userRepository.findByEmail(friendEmail);
		if (users == null||users.size()<=0) {
			out.print("<script>alert('该用户不存在')</script>");
			return "main";
		}
		User userB = users.get(0);
		Friend friend=new Friend();
		friend.setUserA(userA.getId());
		friend.setUserB(userB.getId());
		friendRepository.save(friend);

		return "updateMain";
	}

	@PostMapping(path = "addTransaction")
	public String addTransaction(@RequestParam String friendEmail,@RequestParam int amount, Model model,HttpServletResponse response,HttpServletRequest request)throws IOException{

		HttpSession session=request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		User userFrom=(User)session.getAttribute("currentUser");
		if(userFrom== null){
			out.print("<script>alert('你还未登录')</script>");
			return "index";
		}
		List<User> users = userRepository.findByEmail(friendEmail);
		if (users == null||users.size()<=0) {
			out.print("<script>alert('该用户不存在')</script>");
			return "main";
		}
		User userTo=users.get(0);
		Transaction transaction=new Transaction();
		transaction.setAmount(amount);
		transaction.setUserFrom(userFrom.getId());
		transaction.setUserTo(userTo.getId());
		transaction.setTime(Calendar.getInstance());
		transactionRepository.save(transaction);
		userFrom.setRemain(userFrom.getRemain()-amount);
		userTo.setRemain(userTo.getRemain()+amount);
		userRepository.save(userTo);
		userRepository.save(userFrom);

		return "updateMain";
	}

	@RequestMapping(path="updateMain")
	public String updateMain( Model model,HttpServletResponse response,HttpServletRequest request)throws IOException{

		log.info("into updateMain");
		HttpSession session=request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		User currentUser=(User)session.getAttribute("currentUser");
		if(currentUser==null){
			out.print("<script>alert('未登录')</script>");
			return "index";
		}
		model.addAttribute("currentUser",currentUser);
		List<Friend> myFriends=friendRepository.findByUserA(currentUser.getId());
		model.addAttribute("myFriends",myFriends);

		List<Transaction> transactionsFrom=transactionRepository.findByUserFrom(currentUser.getId());
		List<Transaction> transactionsTo=transactionRepository.findByUserTo(currentUser.getId());
		model.addAttribute("transactionsFrom",transactionsFrom);
		model.addAttribute("transactionsTo",transactionsTo);

		return "main";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
