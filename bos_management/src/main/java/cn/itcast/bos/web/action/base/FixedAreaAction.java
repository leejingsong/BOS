package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.base.common.BaseAction;

@ParentPackage("json-default")
@Scope("prototype")
@Namespace("/")
@Controller
public class FixedAreaAction extends BaseAction<FixedArea> {
	
	@Autowired
	private FixedAreaService fixedAreaService;

	//保存定区
	@Action(value="fixedArea_add",results={@Result(name="suceess",location="./pages/base/fixed_area.html",type="redirect")})
	public String addFixedArea(){
		fixedAreaService.add(model);
		
		return "success";
	}
	
	@Action(value="fixedArea_findAll", results={@Result(name="success",type="json")})
	public String fixedAreafindAll(){
		Pageable pageable=new PageRequest(page-1, rows);
		Specification<FixedArea> specification=new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list=new ArrayList<Predicate>();
				if(StringUtils.isNoneBlank(model.getId())){//根据定区编号查询
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCompany())){//根据公司查询:模糊查询
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}
				
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<FixedArea> pageData=fixedAreaService.findall(specification,pageable);
		pushPageDatatoValueStacke(pageData);
		
		return "success";
	}
	
}
