package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
 
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

	//模型驱动，数据封装，需要new出对象
	private Standard standard=new Standard();
	@Override
	public Standard getModel() {
		return standard;
	}
	
	//接收页面的数据：属性驱动
	private int page;
	private int rows;
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	//在action中需要调用service的方法：注入service
	@Autowired
	private StandardService standardService;
	
	
	@Action(value="standard_save",results= {@Result(name="success",type="redirect",location="./pages/base/standard.html")}) 
	public String save() {
		System.out.println("添加收派标准，模型驱动成功。。。。。。。");
		standardService.save(standard);
	return "success";	
	}

	//收派标准的分页方法
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String pageuqery(){
		//根据页面传来的数据，到服务器；里面进行查询，并将查询的json数据返回页面
		Pageable pageable=new PageRequest(page-1, rows);
		Page<Standard> pageData=standardService.findPageData(pageable);
		//将数据进行封装到Map集合
		Map<String, Object> result=new HashMap<String,Object>();
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		
		//将result放进值栈中，返回前端页面，返回过程中，会自动转为json数据，返回路径是前端页面请求的路径
		ActionContext.getContext().getValueStack().push(result);
		return "success";
	}
	
	//查询所有收派标准的方法
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String finaAll(){
		List<Standard> list=standardService.findAll();
		//将获取的list集合放进值栈中
		ActionContext.getContext().getValueStack().push(list);
		return "success";
	}
}
