package cn.itcast.bos.web.action.base.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	//模型驱动
	protected T model;
	@Override
	public T getModel() {
		return model;
	}
	
	//因为ModelDriven是接口，无法直接实例化，所有通过构造器，进行实例化ModelDriven
	//要想实例化modelDriven，必须获取子类对象的类型：通过反射技术，构造子类Action对象
	public BaseAction() {
		//子类获取父类的泛型类型
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		//获取第一个泛型参数
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		
		//生成构造实例
		try {
			model=modelClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//接收页面传输的page，rows
	protected int page;
	protected int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
 
	//将分页查询数据压入值栈的方法
	public void pushPageDatatoValueStacke(Page<T> pageData){
		Map<String, Object> result=new HashMap<String,Object>();
		result.put("total",pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);	
	}
	
}
