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
	private UserRepository userRepository;//用户表
	@Autowired
	private TransactionRepository transactionRepository;//历史交易表
	@Autowired
	private FriendRepository friendRepository;//好友表
	@Autowired
	private MessageRepository messageRepository;//交易申请表

	@GetMapping("/")//直接访问跳转到login.html
	public String showBegin() {
		return "login";
	}

	@GetMapping("/register")//访问register跳转到register.html
	public String showRegister(){
		return "register";
	}

	@PostMapping(path="/add")//注册用户的post申请
	@ResponseBody//返回的是一个状态给网页，让网页进行提示交互
	public Object addNewUser (@RequestParam String name
			, @RequestParam String email, @RequestParam String password,HttpServletRequest request,Model model) throws Exception{

		HttpSession session=request.getSession();

		List<User> list=userRepository.findByEmail(email);
		if(list!=null&&list.size()>0){
			return "1";//email已注册
		}
		User user=new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRemain(100);

		userRepository.save(user);		//添加到sql
		List<User> users=userRepository.findByEmail(email);
		if (users == null||users.size()<=0) {
			return "2";//数据库存储失败
		}
		user=users.get(0);
//		HFJavaExample.addUserToChain(user); //添加到区块链

		session.setAttribute("currentUser",user);
		return "0";//成功注册
	}


	@PostMapping(path = "/login")//登录表单的post
	@ResponseBody//返回的是一个状态，让网页进行处理
	public Object login(@RequestParam String email, @RequestParam String password, HttpServletResponse response, HttpServletRequest request,Model model)throws IOException {

		log.info(email+" "+password);
		HttpSession session=request.getSession();
		//response.setContentType("text/html;charset=utf-8");
		//PrintWriter out = response.getWriter();

		List<User> users = userRepository.findByEmail(email);

		if (users == null||users.size()<=0) {
			//out.print("<script>alert('该用户不存在')</script>");
			return "1";//用户不存在
		}
		User user = users.get(0);
//		try{
//			User userFromChain=HFJavaExample.getUserFromChain(user.getId());
//			if(userFromChain==null){
//				return "4";//区块链没查询到
//			}
//			if(!userFromChain.equals(user)){
//				return "3";//区块链数据与数据库不一致
//			}
//		}catch (Exception e){
//			return "4";//区块链查询错误
//		}
		if (!user.getPassword().equals(password)) {
			//out.print("<script>alert('密码错误')</script>");
			return "2";//密码错误
		}

		session.setAttribute("currentUser",user);//将当前用户存入session中
		return "0";//成功登录
	}

	@PostMapping(path="/addFriend")//添加好友申请
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


	@RequestMapping(path="/updateMain")//刷新界面的申请
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
		currentUser=userRepository.findById(currentUser.getId().intValue());
		model.addAttribute("currentUser",currentUser);//刷新用户
		List<Friend> friends=friendRepository.findByUserA(currentUser.getId());
		List<Integer> myFriendsInteger=friends.stream().map(Friend::getUserB).collect(Collectors.toList());
		List<User> myFriendsList=userRepository.findByIdIn(myFriendsInteger);
		model.addAttribute("myFriends",myFriendsList);//存入好友

		List<Transaction> transactionsFrom=transactionRepository.findByUserFrom(currentUser.getId());
		List<Transaction> transactionsTo=transactionRepository.findByUserTo(currentUser.getId());
		transactionsFrom.addAll(transactionsTo);
		Collections.sort(transactionsFrom);
		List<TransactionSend> transactionSends=new ArrayList<>();
		for(Transaction i:transactionsFrom){
			TransactionSend temp=new TransactionSend(i,userRepository);
			transactionSends.add(temp);
		}
		model.addAttribute("transactions",transactionSends);//存入历史交易

		List<Message> messages=messageRepository.findByUserTo(currentUser.getId());
		List<MessageSend> messageSends=new ArrayList<>();
		for(Message i:messages){
			MessageSend temp=new MessageSend(i,userRepository);
			messageSends.add(temp);
		}
		model.addAttribute("messages",messageSends);//存入未处理交易

		return "dashboard";
	}

	@PostMapping(path = "/addTransaction")//交易
	public String addTransaction(@RequestParam int messageId,HttpServletResponse response,HttpServletRequest request)throws Exception{

		HttpSession session=request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		Message message=messageRepository.findById(messageId);

		if(message==null){
			out.print("<script>alert('无记录')</script>");
			return "updateMain";
		}

		User userFrom=userRepository.findById(message.getUserFrom().intValue());

		User userTo=userRepository.findById(message.getUserTo().intValue());

		int amount=message.getAmount();
		try{
			userFrom.setRemain(userFrom.getRemain()-amount);
			userTo.setRemain(userTo.getRemain()+amount);
		}catch (Exception e){
			log.warn(e.toString());
			out.print("<script>alert('余额不够')</script>");
			return "updateMain";
		}

		Transaction transaction=new Transaction(message);
		transactionRepository.save(transaction);//存储这个交易

//		HFJavaExample.updateUser(userTo.getId(),userTo.getRemain());//更新区块链
//		HFJavaExample.updateUser(userFrom.getId(),userFrom.getRemain());
		userRepository.save(userTo);//更新数据库
		userRepository.save(userFrom);

		return "updateMain";
	}

	@PostMapping(path="/addMessage")//发送交易申请
	public String addMessage(@RequestParam String userToEmail,@RequestParam int amount,HttpServletResponse response,HttpServletRequest request)throws IOException{
		HttpSession session=request.getSession();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		User userFrom=(User)session.getAttribute("currentUser");

		if(userFrom== null){
			out.print("<script>alert('你还未登录')</script>");
			return "login";
		}
		List<User> users = userRepository.findByEmail(userToEmail);
		if (users == null||users.size()<=0) {
			out.print("<script>alert('该用户不存在')</script>");
			return "updateMain";
		}
		User userTo=users.get(0);
		if(amount>userFrom.getRemain()){
			out.print("<script>alert('超过你的余额')</script>");
			return "updateMain";
		}
		Message message=new Message();
		message.setAmount(amount);
		message.setTime(Calendar.getInstance());
		message.setUserFrom(userFrom.getId());
		message.setUserTo(userTo.getId());

		messageRepository.save(message);//存入数据库

		return "updateMain";
	}


	@PostMapping(path="/delMessage")//拒绝、删除交易申请
	@ResponseBody
	public Object delMessage(@RequestParam int messageId){
		Message message=messageRepository.findById(messageId);
		if(message==null){
			return "1";
		}
		messageRepository.deleteById(messageId);
		return "0";
	}

	@PostMapping(path = "/sendSuggest")//发送建议和意见
	public String sendSuggest(@RequestParam String suggest){
		log.info("用户发送："+suggest);//发入log
		return "updateMain";
	}

//	@GetMapping(path="/all")//用于查看所有用户的信息，开发时用于调试
//	public @ResponseBody Iterable<User> getAllUsers() {
//		return userRepository.findAll();
//	}
}
