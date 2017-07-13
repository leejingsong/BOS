package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	//模型驱动
	private Courier courier=new Courier();
	@Override
	public Courier getModel() {
		// TODO Auto-generated method stub
		return courier;
	}
	
	//需要调用service，所以注入CourerSerice
	@Autowired
	private CourierService courierService;
	
	//属性驱动
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}


	@Action(value="courier_save",results={@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String saveCourier(){
		courierService.save(courier);
		return "success";
	}


	@Action(value="courier_finaAll",results={@Result(name="success",type="json")})
	public String courierFindAll(){
		Pageable pageable=new PageRequest(page-1, rows);
		//根据查询条件，构造一个specification
		Specification<Courier> specification=new Specification<Courier>() {
			/*
			 * toPredicate（）构件条件查询的方法，里面的三个参数：
			 * Root:获得条件表达式 ，如 name=？age=？
			 * CriteriaQuery:构造简单查询条件返回，提供where方法
			 * CriteriaBuilder:构造predicate对象，条件对象，构造复杂条件效果
			 * 方法返回null，代表无条件查询
			 */
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//通过List，将所有的条件进行保存
			 List<Predicate> list=new ArrayList<>();
				
				
				
				//当前查询的Root跟对象：courier
				//单表查询（查询当前对象，对应的数据表）:这里用的是spring-commons-lang3这个包
				//构造courierNum条件
				if(StringUtils.isNotBlank(courier.getCourierNum())){
					//说明快递员的工号不为空：进行快递员的工号查询(分为等值查询---模糊查询),返回结果就是一个predicate对象
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					
					list.add(p1);//保存条件
				}
				//构造company条件
				if(StringUtils.isNoneBlank(courier.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+courier.getCompany()+"%");
					
					list.add(p2);//保存条件
				}
				//构造快递员的type条件
				if(StringUtils.isNotBlank(courier.getType())){
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					
					list.add(p3);//保存条件
					
				}
				
				//多表查询：查询当前对象 关联的对象，以及对应的数据表
				//使用courier(就是root表)，表关联standard表:指定属性名，以及关联的类型
				//获得join对象就是关联目标表的root对象
				Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER );
				//判断：条件是否存在
				if(courier.getStandard()!=null && StringUtils.isNoneBlank(courier.getStandard().getName())){
					//进行收派标准的名称模糊查询
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
					
					list.add(p4);//保存条件
				}
				return cb.and(list.toArray(new Predicate[0]));//将集合转为数组，new出来的对象相当于泛型
			}
		};
		
		Page<Courier> pageData=courierService.findAll(specification,pageable);
		//将获取的值放进值栈，并以total 和 rows的形式
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
		return "success";
	}
	
	@Action(value="courier_delBatch",results={@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String courier_del(){
		//接收参数
		String ids = ServletActionContext.getRequest().getParameter("ids");
		//将ids进行拆分
		String[] idArray=ids.split(",");
		//调用业务层，实现批量的作废
		courierService.courier_delBatch(idArray);
		return "success";
	}
	
	@Action(value="courier_undelBatch",results={@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String courier_undel(){
		//接收参数
		String ids = ServletActionContext.getRequest().getParameter("ids");
		//将ids进行拆分
		String[] idArray=ids.split(",");
		//调用业务层，实现批量的作废
		courierService.courier_undelBatch(idArray);
		return "success";
	}
	
}
