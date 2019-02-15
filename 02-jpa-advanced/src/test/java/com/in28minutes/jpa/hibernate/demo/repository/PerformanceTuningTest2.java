package com.in28minutes.jpa.hibernate.demo.repository;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.jpa.hibernate.demo.DemoApplication;
import com.in28minutes.jpa.hibernate.demo.entity.Course;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=DemoApplication.class)
public class PerformanceTuningTest2 {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	EntityManager em;
	
	@Transactional
//	@Test
	public void createOnePlusProblem() {
		TypedQuery<Course> createQuery = em.createQuery("Select c from Course c",Course.class);
		List<Course> resultList = createQuery.getResultList();
		for(Course course:resultList) {
			logger.info("course -> {} students -> {}", course, course.getStudents());
		}
	}
	
	@Transactional
	@Test
	public void solvingOnePlusProblem() {
		EntityGraph<Course> entityGraph = em.createEntityGraph(Course.class);
		Subgraph<Object> subgraph = entityGraph.addSubgraph("students");
		TypedQuery<Course> createQuery = em.createQuery("Select c from Course c",Course.class);
		createQuery.setHint("javax.persistence.loadgraph", entityGraph);
		
		List<Course> resultList = createQuery.getResultList();
		for(Course course:resultList) {
			logger.info("course -> {} students -> {}", course, course.getStudents());
		}
	}

}
