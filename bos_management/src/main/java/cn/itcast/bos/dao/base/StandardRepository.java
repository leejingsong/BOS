package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer> {
	//根据收派标准进行查询：不需要编写实现类
	public List<Standard> findByName(String name); 
	public List<Standard> findById(Integer id); 
	
	@Query(value="from Standard where name=?",nativeQuery=false)	
	public List<Standard> queryName(String name);
	
	@Query
	public List<Standard> queryName2(String name);
	
	@Query
	public List<Standard> queryId(Integer id);
	
	
	
	@Query(value="update Standard set minLength=? where id=?")
	@Modifying
	public void change(Integer id,Integer miniLength);
	
	@Query(value="delete Standard where id=?")
	@Modifying
	public void delete(Integer id);
}
