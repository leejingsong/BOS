package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	
	//注入Dao
	@Autowired
	private AreaRepository areaRepository;

	//批量保存的方法
	@Override
	public void addBatch(List<Area> list) {
		
		areaRepository.save(list);
	}

	//分页查询所有area,并带有条件
	@Override
	public Page<Area> findAll(Specification<Area> specification, Pageable pageable) {
		return areaRepository.findAll(specification, pageable);
	}
}
