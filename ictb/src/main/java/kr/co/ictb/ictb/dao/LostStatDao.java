package kr.co.ictb.ictb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LostStatDao {
	List<Map<String, Object>> lostlocstat(String searchValue);
	List<Map<String, Object>> lostcatstat(String searchValue);
	List<Map<String, Object>> losttimestat(Map<String, String> map);
}
