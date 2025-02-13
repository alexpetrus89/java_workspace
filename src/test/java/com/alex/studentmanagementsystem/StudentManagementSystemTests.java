package com.alex.studentmanagementsystem;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
class StudentManagementSystemTests {

	@Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BeanFactory beanFactory;

	@Test
	void contextLoads() {
		assertNotNull(applicationContext);
		assertNotNull(beanFactory);
    	// altre verifiche di base
	}

}
