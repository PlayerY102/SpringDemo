package hello;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	@Autowired
	private MessageRepository messageRepository;

	@GetMapping("/")
	public String showBegin() {
		return "login";
	}

	@GetMapping("/register")
	public String showRegister(){
		return "register";
	}

	@PostMapping(path="/add")
	@ResponseBody
	public Object addNewUser (@RequestParam String name
			, @RequestParam String email, @RequestParam String password,HttpServletRequest request,Model model) {

		HttpSession session=request.getSession();

		List<User> list=userRepository.findByEmail(email);
		if(list!=null&&list.size()>0){
			return "1";
		}
		User user=new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRemain(100);

		userRepository.save(user);

		session.setAttribute("currentUser",user);
		return "0";
	}

	@PostMapping(path = "/login")
	@ResponseBody
	public Object login(@RequestParam String email, @RequestParam String password, HttpServletResponse response, HttpServletRequest request,Model model)throws IOException {

		log.info(email+" "+password);
		HttpSession session=request.getSession();
		//response.setContentType("text/html;charset=utf-8");
		//PrintWriter out = response.getWriter();

		List<User> users = userRepository.findByEmail(email);

		if (users == null||users.size()<=0) {
			//out.print("<script>alert('该用户不存在')</script>");
			return "1";
		}
		User user = users.get(0);
		if (!user.getPassword().equals(password)) {
			//out.print("<script>alert('密码错误')</script>");
			return "2";
		}

		session.setAttribute("currentUser",user);
		return "0";
	}

	@PostMapping(path="/addFriend")
	public String addFriend(@RequestParam String friendEmail,Model model,HttpServletResponse response,HttpServletRequest request)throws IOException{
		HttpSession session=request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		User userA=(User)session.getAttribute("currentUser");
		if(userA== null){
			out.print("<script>alert('你还未登录')</script>");
			return "login";
		}
		List<User> users = userRepository.findByEmail(friendEmail);
		if (users == null||users.size()<=0) {
			out.print("<script>alert('该用户不存在')</script>");
			return "updateMain";
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
			return "login";
		}
		List<User> users = userRepository.findByEmail(friendEmail);
		if (users == null||users.size()<=0) {
			out.print("<script>alert('该用户不存在')</script>");
			return "updateMain";
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
			return "login";
		}
		model.addAttribute("currentUser",currentUser);
		List<Friend> friends=friendRepository.findByUserA(currentUser.getId());
		List<Integer> myFriendsInteger=friends.stream().map(Friend::getUserB).collect(Collectors.toList());
		List<User> myFriendsList=userRepository.findByIdIn(myFriendsInteger);
		model.addAttribute("myFriends",myFriendsList);

		List<Transaction> transactionsFrom=transactionRepository.findByUserFrom(currentUser.getId());
		List<Transaction> transactionsTo=transactionRepository.findByUserTo(currentUser.getId());
		transactionsFrom.addAll(transactionsTo);
		Collections.sort(transactionsFrom);
		List<TransactionSend> transactionSends=new ArrayList<>();
		for(Transaction i:transactionsFrom){
			TransactionSend temp=new TransactionSend(i,userRepository);
			transactionSends.add(temp);
		}
		model.addAttribute("transactions",transactionSends);

		List<Message> messages=messageRepository.findByUserTo(currentUser.getId());
		List<MessageSend> messageSends=new ArrayList<>();
		for(Message i:messages){
			MessageSend temp=new MessageSend(i,userRepository);
			messageSends.add(temp);
		}
		model.addAttribute("messages",messageSends);

		return "dashboard";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
