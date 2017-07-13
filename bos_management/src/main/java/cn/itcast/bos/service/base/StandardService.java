package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

//收派标准的业务层接口
public interface StandardService {

	//保存收派标注方法
	public void save(Standard standard);

	public Page<Standard> findPageData(Pageable pageable);

	
	public List<Standard> findAll();
}
