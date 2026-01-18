package kr.co.ictb.ictb.controller.todo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ictb.ictb.dao.TodoDao;
import kr.co.ictb.ictb.vo.MemberVO;
import kr.co.ictb.ictb.vo.TodoVO;

@Service
public class TodoService {
	@Autowired
	private TodoDao todoDao;

	public List<TodoVO> todoInfo(String tid) {
		return todoDao.todoInfo(tid);
	}

	public void addTodo(TodoVO vo) {
		todoDao.addTodo(vo);
	}

	public void deleteTodo(TodoVO vo) {
		todoDao.deleteTodo(vo);
	}	
	public void editTodo(TodoVO vo) {
		todoDao.editTodo(vo);
	}
	public List<TodoVO> friendTodoInfo(List<String> mid){
		return todoDao.friendTodoInfo(mid);
	}
	public List<MemberVO> selectMyfriends(List<String> mid){
		return todoDao.selectMyfriends(mid);
	}
	public List<Map<String, Object>>hometodo(String tid){
		return todoDao.hometodo(tid);
	}
	
}
