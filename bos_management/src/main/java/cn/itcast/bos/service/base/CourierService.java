package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier courier);

	Page<Courier> findAll(Specification<Courier> specification,Pageable pageable);

	//批量作废
	void courier_delBatch(String[] idArray);

	void courier_undelBatch(String[] idArray);

}
