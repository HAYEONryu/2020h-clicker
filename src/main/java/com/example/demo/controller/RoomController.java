package com.example.demo.controller;

import java.security.SecureRandom;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.example.demo.domain.MemberVO;
import com.example.demo.domain.RoomVO;
import com.example.demo.mappers.roomMapper;
import com.example.demo.service.MemberService;
import com.example.demo.service.RoomService;
import com.example.demo.domain.PtUserVO;

@Controller
@SessionAttributes("member")
public class RoomController {

	private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
	
	@Inject
	RoomService roomservice;
	MemberService service;
	
	@Resource(name="com.example.demo.mappers.roomMapper")
	roomMapper mRoomMapper;

	@RequestMapping(value="/main", method = RequestMethod.GET)
    public String list1( Model model, @RequestParam(value="id",required=false)String id) throws Exception{
		logger.info("list");
        model.addAttribute("list", roomservice.list(id));
        
//        PtUserVO pt = new PtUserVO();
//        pt = roomservice.plist(id);
//        model.addAttribute("plist", roomservice.pplist(pt.getPt_user()));
        model.addAttribute("plist", roomservice.plist(id));
        
        return "/main"; //생성할 jsp
	}
	
	@RequestMapping("/enterProc") //새로운 방 참여하기(방코드입력)
	private String roomEnterProc(Model model, HttpServletRequest request,RedirectAttributes rttr) throws Exception{
		
		PtUserVO pt = new PtUserVO();
		
		String code = request.getParameter("code");
		
		pt.setRoom_id(mRoomMapper.findId(code));
		pt.setRoom_name(mRoomMapper.findName(code));
		pt.setMd_user(mRoomMapper.findMdUser(code));
		pt.setPt_user(request.getParameter("pt_user"));
		
		roomservice.update(pt);
		rttr.addAttribute("id", request.getParameter("pt_user"));
		 
		return "redirect:/main";
	}
	
	@RequestMapping("/insertProc") //새로운 방 만들기(방제목입력)
	private String roomInsertProc(HttpServletRequest request,RedirectAttributes rttr) throws Exception{
	    RoomVO room = new RoomVO();

	    SecureRandom random = new SecureRandom();

	    String ENGLISH_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String ENGLISH_UPPER = ENGLISH_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        
        /** 랜덤을 생성할 대상 문자열 **/
        String DATA_FOR_RANDOM_STRING = ENGLISH_LOWER + ENGLISH_UPPER + NUMBER;
        
        /** 랜덤 문자열 길이 **/
        int random_string_length=6;
        
        StringBuilder sb = new StringBuilder(random_string_length);
        for (int i = 0; i < random_string_length; i++) {
            sb.append( DATA_FOR_RANDOM_STRING.charAt(
            		random.nextInt(DATA_FOR_RANDOM_STRING.length())
            		));
        } 
	    room.setRoom_name(request.getParameter("room_name"));
	    room.setCode(sb.toString());
	    room.setMd_user(request.getParameter("md_user"));
	    roomservice.roomInsert(room);
	    rttr.addAttribute("id", request.getParameter("md_user"));
	    return "redirect:/main";
	}
	
	@RequestMapping(value="/delete/{user_id}/{room_id}") //mroom 삭제
    private String roomDelete(@PathVariable int room_id,@PathVariable String user_id,RedirectAttributes rttr,HttpServletRequest request) throws Exception{
        roomservice.roomDelete1(room_id);
        roomservice.roomDelete2(room_id);
        roomservice.roomDelete3(room_id);
        
        rttr.addAttribute("id", user_id);
        return "redirect:/main"; //main?id=111이런식으로 가야됌
    }
	


}