package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRepository courierRepository;
	//保存快递员操作
	@Override
	public void save(Courier courier) {
		courierRepository.save(courier);
	}
	
	//查询所有快递员信息
	@Override
	public Page<Courier> findAll(Specification<Courier> specification,Pageable pageable){
		return courierRepository.findAll(specification,pageable);
	}

	
	//批量修改快递员状态：不可用——不是真正删除，而是修改
	@Override
	public void courier_delBatch(String[] idArray) {
		 for (String idStr : idArray) {
			int id = Integer.parseInt(idStr);
			//因为courierRrepository没有update方法，所以需要自己写方法
			courierRepository.updateDelTag(id);
		}
	}

	//批量修改快递员状态：可用——不是真正删除，而是修改
	@Override
	public void courier_undelBatch(String[] idArray) {
		 for (String idStr : idArray) {
				int id = Integer.parseInt(idStr);
				//因为courierRrepository没有update方法，所以需要自己写方法
				courierRepository.updateunDelTag(id);
			}
	}

	
}
