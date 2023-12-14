package com.todo.todolist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.todolist.dto.ResponseDTO;
import com.todo.todolist.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;

	// 연습
	@GetMapping("/test")
	public ResponseEntity<?> testTodo(){
		String str = service.testService();	//테스트 서비스 사용
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	
}
