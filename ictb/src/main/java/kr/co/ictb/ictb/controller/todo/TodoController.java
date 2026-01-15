package kr.co.ictb.ictb.controller.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ictb.ictb.vo.MemberVO;
import kr.co.ictb.ictb.vo.TodoVO;

@RestController
@RequestMapping("/todo")
public class TodoController {

	@Autowired
	private TodoService todoService;
	
	
	@PostMapping("/todoInfo")
	public List<TodoVO> todoInfo(@RequestBody TodoVO vo){
		System.out.println(vo.getTid());
		return todoService.todoInfo(vo.getTid());
	}
	
	@PostMapping("/addTodo")
	public void addTodo(@RequestBody TodoVO vo){
		
		System.out.println(vo.getTid() + vo.getTdate() + vo.getTnum() + vo.getDescription());
		todoService.addTodo(vo);
	}
	
	@PostMapping("/deleteTodo")
	public void deleteTodo(@RequestBody TodoVO vo){
		System.out.println(vo.getTnum());
		todoService.deleteTodo(vo);
	}
	@PostMapping("/editTodo")
	public void editTodo(@RequestBody TodoVO vo){
		System.out.println(vo.getTnum());
		System.out.println(vo.getDescription());
		todoService.editTodo(vo);
	}
	@PostMapping("/FriendTodo")
	public List<TodoVO> friendTodoInfo(@RequestBody List<String> mid){
		
		return todoService.friendTodoInfo(mid);
	}
	@PostMapping("/selectMyfriends")
	public List<MemberVO> selectMyfriends(@RequestBody List<String> mid ){
		System.out.println("시작");
		for (String e : mid) {
			System.out.println( " mid = " + e);
		}
		return todoService.selectMyfriends(mid);
	}
	
}
