package kr.co.ictb.ictb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ictb.ictb.vo.MemberVO;
import kr.co.ictb.ictb.vo.TodoVO;

@Mapper
public interface TodoDao {

	public List<TodoVO> todoInfo(String tid);
	public void addTodo(TodoVO vo);
	public void deleteTodo(TodoVO vo);
	public void editTodo(TodoVO vo);
	public List<TodoVO> friendTodoInfo(@Param("list") List<String> list);
	public List<MemberVO> selectMyfriends(@Param("list") List<String> list);
	
}
