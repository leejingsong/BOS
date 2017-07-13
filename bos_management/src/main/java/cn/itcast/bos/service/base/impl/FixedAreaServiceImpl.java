package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	
	@Autowired
	private FixedAreaRepository fixedAreaServiceImpl;

	//保存fixedArea
	@Override
	public void add(FixedArea model) {
		fixedAreaServiceImpl.save(model);
	}

	@Override
	public Page<FixedArea> findall(Specification<FixedArea> specification, Pageable pageable) {
		 
		return fixedAreaServiceImpl.findAll(specification, pageable);
	}

}
