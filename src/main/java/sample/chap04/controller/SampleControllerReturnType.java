package sample.chap04.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;
import sample.chap04.domain.SampleDTO;

@Controller
@Log4j
@RequestMapping(value = "/sample2/*")
public class SampleControllerReturnType {
	//Controller 메서드 리턴 타입: jsp 파일 호출하도록 명시
	
	// Return type: void
	@GetMapping("/basicOnlyGet")
	public void basicGet() {
		log.info("basicGet==========");
	}
	// http://localhost:8080/mypro01/sample2/basicOnlyGet
	
	//Return Type: String
	@GetMapping("/ex04")
	public String ex04(SampleDTO dto, Integer page) {
		log.info("dto: " + dto);
		log.info("page: " + page);
		
		return "/sample/ex04";
	} // http://localhost:8080/mypro01/sample2/ex04?name=홍길동&age=24&page=1
	
}
