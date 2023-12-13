package com.todo.todolist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.todolist.dto.ResponseDTO;
import com.todo.todolist.dto.TestRequestBodyDTO;

/*
 * 	@RestController : http관련 코드 및 요청/응답 매핑을 스프링이 알아서 해준다.
 */

@RestController
@RequestMapping("test")	//'test'라는 URI경로 지정
public class TestController {
	
	@GetMapping
	public String testController1() {
		System.out.println("test로 진입");
		return "Hello World 1";
	}
	
	@GetMapping("/testGetMapping")
	public String testController2() {
		System.out.println("test/testGetMapping으로 진입");
		return "Hello World! testGetMapping!";
	}
	
	/*
	 * 	PathVariable을 이용한 매개변수 전달
	 * 	required : 매개변수가 필수인지 아닌지 설정
	 * 	http://localhost:8080/test/1234
	 */
	@GetMapping("/{id}")
	public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
		System.out.println("@PathVariable, id = " + id + "");
		return "Hello World ID " + id;
	}

	/*
	 * 	RequestParam을 이용한 매개변수 전달
	 * 	http://localhost:8080/test/testRequestParam?id=1234
	 */
	@GetMapping("/testRequestParam")
	public String testControllerRequestParam(@RequestParam(required = false) int id) {
		System.out.println("@RequestParam, id = " + id + "");
		return "Hello World ID " + id;
	}

	/*
	 * 	RequestBody를 이용한 매개변수 전달 - 반환하는 리소스가 복잡할 때 주로 사용
	 * 	@RequestBody TestRequestBodyDTO testRequestBodyDTO : RequestBody로 날아오는 JSON을 DTO오브젝트로 변환해서 가져오라는 뜻
	 * 	즉, 클라이언트는 요청 바디로 JSON형태의 문자열을 넘겨주고, JSON의 내부는 TestRequestBodyDTO와 같아야 한다.
	 * 	http://localhost:8080/test/testRequestBody
	 * 	body : { "id": 123, "message": "Hello ?" }
	 */
	@GetMapping("/testRequestBody")
	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
		return "Hello World ID" + testRequestBodyDTO.getId() 
			 + "Message : " + testRequestBodyDTO.getMessage();
	}	

	/*
	 * 	ResponseDTO를 반환하는 컨트롤러 메서드
	 */
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testControllerResponseBody() {
		List<String> list = new ArrayList<>();
		list.add("안녕? 나는 ResponseDTO야");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}

	/*
	 * 	ResponseEntity를 반환하는 컨트롤러 메서드
	 * 	- HTTP 응답 바디뿐만 아니라, 여러 다른 매개변수(status, header)를 조작할 때 사용
	 */
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity() {
		List<String> list = new ArrayList<>();
		list.add("안녕? 나는 ResponseEntity야. 그리고 넌 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		
		// http status를 200으로 설정
		// return ResponseEntity.ok().body(response);

		// http status를 400로 설정.
		return ResponseEntity.badRequest().body(response);
	}
}