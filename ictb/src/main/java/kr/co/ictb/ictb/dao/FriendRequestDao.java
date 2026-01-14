package kr.co.ictb.ictb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ictb.ictb.vo.FriendRequestVO;
import kr.co.ictb.ictb.vo.MemberVO;

@Mapper
public interface FriendRequestDao {
	List<MemberVO> selectAllExceptMe(@Param("mid") String mid);
	
	void sendRequest(@Param("req_id") String reqId , @Param("receiver_id") String receiverId);
	
	List<FriendRequestVO> getPending(@Param("mid") String mid);
	
	void updateStatus(@Param("frid") Long frid , @Param("status") String status);
	
	List<MemberVO> getFriends(@Param("mid") String mid);
	
	List<FriendRequestVO> getSendRequests(@Param("mid") String mid);
	
	int checkRequestExists(@Param("req_id") String reqId , @Param("receiver_id") String receiverId);
	
	void resendRequest(@Param("req_id") String reqId,@Param("receiver_id") String receiverId);
}
