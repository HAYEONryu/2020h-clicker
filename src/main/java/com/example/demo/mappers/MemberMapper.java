package com.example.demo.mappers;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.MemberVO;

@Mapper
public interface MemberMapper {

	public int registerInsert(MemberVO member) throws Exception;
	public MemberVO login (MemberVO member) throws Exception;
	public int idChk(String user_id) throws Exception;
	public int confirm(MemberVO member) throws Exception;
}
