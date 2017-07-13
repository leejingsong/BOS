package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaService {

	void add(FixedArea model);

	Page<FixedArea> findall(Specification<FixedArea> specification, Pageable pageable);

}
