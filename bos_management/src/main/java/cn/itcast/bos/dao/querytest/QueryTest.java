package cn.itcast.bos.dao.querytest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class QueryTest {

	@Autowired
	private StandardRepository standardRepository;
	
	@Test
	public void test1(){
		System.out.println("aaaaaaaaaa"+standardRepository.findByName("22"));
	}
	@Test
	public void test22(){
		System.out.println("aaaaaaaaaa"+standardRepository.findById(1));
	}
	@Test
	public void test2(){
		System.out.println("bbbbbbbbb"+standardRepository.queryName("22"));
	}
	@Test
	public void test3(){
		System.out.println("bbbbbbbbb"+standardRepository.queryName2("22"));
	}
	
	@Test
	public void test33(){
		System.out.println("bbbbbbbbb"+standardRepository.queryId(2));
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void test4(){
		standardRepository.change(8888, 1);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void test5(){
		standardRepository.delete(1);
	}
	
}
