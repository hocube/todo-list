package com.todo.todolist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.todo.todolist.model.TodoEntity;
import com.todo.todolist.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

// @Service : 이 클래스는 스프링 컴포넌트며 기능적으로 비즈니스 로직을 수행하는 서비스 레이어임을 알려주는 어노테이션

@Slf4j
@Service
public class TodoService {

	@Autowired
	private TodoRepository repository;

	public String testService() {
		// TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();

		// TodoEntity 저장
		repository.save(entity);

		// TodoEntity 검색
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		return savedEntity.getTitle();
	}

	// 리팩토링한 메서드
	private void validate(final TodoEntity entity) {
		if (entity == null) {
			log.info("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}

		if (entity.getUserId() == null) {
			log.info("Unknown user");
			throw new RuntimeException("Unknown user");
		}
	}

	// create 메서드
	public List<TodoEntity> create(final TodoEntity entity) {
		// validations
		validate(entity);

		// save(): JpaRepository가 제공하는 엔티티 저장 메서드
		repository.save(entity);

		log.info("Entity Id : {} is saved.", entity.getId());

		// userId로 select한 새Todo리스트 반환
		return repository.findByUserId(entity.getUserId());
	}

	// Retrieve (검색)
	public List<TodoEntity> retrieve(final String userId) {

		log.info("User Id : {} is saved.", userId);
		return repository.findByUserId(userId);
	}

	// Update
	public List<TodoEntity> update(final TodoEntity entity) {
		// (1) 저장할 엔티티가 유효한지 확인한다. 이 메서드는 2, 3, 1 Create Todo에서 구현했다.
		validate(entity);

		// (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트할 수 없기 때문이다.
		final Optional<TodoEntity> original = repository.findById(entity.getId());

//		Optional과 Lambda에 익숙하지 않다면 이거 쓰지 않아도 됨.
//		original.ifPresent(todo -> {
//			(3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
//			todo.setTitle(entity.getTitle());
//			todo.setDone(entitiy.isDone());
//			
//			(4) 데이터베이스에 새 값을 저장한다.
//			repositoty.save(todo);
//		});

		if (original.isPresent()) {
			// (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
			final TodoEntity todo = original.get();
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());

			// (4) 데이터베이스에 새 값을 저장한다.
			repository.save(todo);
		}

		// 2.3.2 Retrieve Todo에서 만든 메서드를 이용해 유저의 모든 Todo 리스트를 리턴한다.
		return retrieve(entity.getUserId());
	}
	
	// delete
	public List<TodoEntity> delete(final TodoEntity entity){
		// (1) 저장할 엔티티가 유효한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
		validate(entity);
		
		try {
			// (2) 엔티티를 삭제한다.
			repository.delete(entity);
		} catch (Exception e) {
			// (3) exception 발생 시 id와 exception을 로깅한다.
			log.error("error deleting entity ", entity.getId(), e);
			// (4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화하기 위해 e를 리턴하지 않고
			// 	   새 exception 오브젝트를 리턴한다.
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		// (5) 새 Todo 리스트를 가져와 리턴한다.
		return retrieve(entity.getUserId());
	}
}