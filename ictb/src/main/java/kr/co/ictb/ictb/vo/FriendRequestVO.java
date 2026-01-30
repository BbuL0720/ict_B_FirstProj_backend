package kr.co.ictb.ictb.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Alias("frvo")
public class FriendRequestVO {

	private int frid;
	private String req_id;
	private String receiver_id;
	private String status;
	private String frdate;
}
/*
CREATE TABLE friend_req(
frid NUMBER NOT null PRIMARY key,
req_id VARCHAR2(50) NOT null,
receiver_id VARCHAR2(50) NOT null,
status VARCHAR2(20) DEFAULT 'pending',
frdate DATE DEFAULT sysdate,
CONSTRAINT requester_fk FOREIGN KEY (req_id) REFERENCES MEMBER(mid),
CONSTRAINT receiver_fk FOREIGN KEY (receiver_id) REFERENCES MEMBER(mid),
CONSTRAINT friend_uq UNIQUE (req_id,receiver_id)
); 
 */
 