package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
//收派标准的实现类

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	//service的实现类需要调用Dao，所以这里需要注入Dao
	@Autowired
	private StandardRepository standardRepository;
	 
	@Override
	public void save(Standard standard) {
		//调用Dao，执行sava方法
		standardRepository.save(standard);
	}

	//收派标准的分页查询
	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		 
		return standardRepository.findAll(pageable);
	}

	//查询所有的收派标准
	@Override
	public List<Standard> findAll() {
		
		return standardRepository.findAll();
	}

}
