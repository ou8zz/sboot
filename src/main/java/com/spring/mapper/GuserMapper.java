package com.spring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Guser;

@Mapper
@Transactional(propagation=Propagation.REQUIRED)
public interface GuserMapper {

	@SelectKey(before=true, keyProperty="id", resultType = Integer.class, statement = { "SELECT guser_id.nextval FROM DUAL" })
	@Insert("INSERT INTO guser (id, ename, cname, pwd, email, gender) VALUES (#{id}, #{ename}, #{cname}, #{pwd}, #{email}, #{gender})")
	public void addGuser(Guser u);


}
