package com.example.kacper.inzynierka;

import com.example.kacper.inzynierka.finalresult.FinalResultController;
import com.example.kacper.inzynierka.questions.Question;
import com.example.kacper.inzynierka.questions.QuestionController;
import com.example.kacper.inzynierka.results.TestResultController;
import com.example.kacper.inzynierka.security.controllers.AuthController;
import com.example.kacper.inzynierka.test.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InzynierkaApplicationTests {

	@Autowired
	private AuthController authController;
	@Autowired
	private TestController testController;
	@Autowired
	private QuestionController questionController;
	@Autowired
	private TestResultController testResultController;
	@Autowired
	private FinalResultController finalResultController;


	@Test
	void contextLoads() {
			assertThat(authController).isNotNull();
	}

}
