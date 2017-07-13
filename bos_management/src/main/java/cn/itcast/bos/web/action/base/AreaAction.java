package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.base.common.BaseAction;

@ParentPackage("json-default")
@Scope("prototype")
@Namespace("/")
@Controller
public class AreaAction extends BaseAction<Area> {

	 
	
	 

	//注入areaService
	@Autowired
	private AreaService areaService;
	
	// 接收上传文件
	private File file;//获得文件
	private String fileFileName;//获得文件名（包括后缀名）

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	//接收页面传输的page，rows
	private int page;
	private int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value = "area_batchImport", results = {@Result(name = "success", location = "./pages/base/area.html", type = "redirect") })
	public String areaImport() throws Exception {
		//定义一个集合，接收每次遍历行数据的对象
		List<Area> list=new ArrayList<Area>();
		
		// 解析Excel文件：先判断文件后缀名,再用响应的解析文件的方法
		int pos = fileFileName.lastIndexOf(".");
		String extensionStr = fileFileName.substring(pos);
		System.out.println("文件格式：============"+extensionStr);
		if(extensionStr.equalsIgnoreCase(".xls")){
			//第一步：加载读取Excel文件的对象，.xls由HSSF解析
			HSSFWorkbook hssfWorkbook=new HSSFWorkbook(new FileInputStream(file));
			//第二步：读取sheet:获取第一个工作簿sheet
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			//第三步：读取表格中的每一行→通过遍历sheet，获取每一行的数据,而每一行就代表一个Area对象的 数据
			for (Row row : sheet) {
				if(row.getRowNum()==0){//因为表头没有数据，第一行直接跳过
					continue;
				}
				if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())){//如果行的第一个单元格空或者没有内容，无效格子，跳过
					continue;
				}
				Area area=new Area();//实例化一个area对象，封装数据，从第二行开始，每一行对应一个数据
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());
				
				
				//基于pinyin4J生成城市的编码和简码
				String province=area.getProvince();//拿到省
					province=province.substring(0,province.length()-1);//切掉最后一个字
				String city=area.getCity();//拿到市
					city=city.substring(0,city.length()-1);
				String district=area.getDistrict();//拿到区
					district=district.substring(0,district.length()-1);
				//获得简码
				String[] headers=PinYin4jUtils.getHeadByString(province+city+district);//生成拼音首字母
				StringBuffer buffer=new StringBuffer();
				for (String head : headers) {
					buffer.append(head);
				}
				String shortCode=buffer.toString();//获得简码数据
				area.setShortcode(shortCode);
				
				//获得城市编码
				String cityCode=PinYin4jUtils.hanziToPinyin(city, "");
				area.setCitycode(cityCode);
				
				list.add(area);//将遍历封装的数据装入list集合
			}
			//调用service业务层，进行数据的存入
			areaService.addBatch(list);
			
			return "success";
			
			
		}
		if(extensionStr.equalsIgnoreCase(".xlsx")){
			
			//第一步：加载读取Excel文件的对象，.xlsx由XSSF解析
			XSSFWorkbook xssfWorkbook=new XSSFWorkbook(new FileInputStream(file));
			//第二步：读取sheet:获取第一个工作簿sheet
			XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
			//第三步：读取表格中的每一行→通过遍历sheet，获取每一行的数据,而每一行就代表一个Area对象的 数据
			for (Row row : sheet) {
				if(row.getRowNum()==0){//因为表头没有数据，第一行直接跳过
					continue;
				}
				if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())){//如果行的第一个单元格空或者没有内容，无效格子，跳过
					continue;
				}
				Area area=new Area();//实例化一个area对象，封装数据，从第二行开始，每一行对应一个数据
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());
				
				//基于pinyin4J生成城市的编码和简码
				String province=area.getProvince();//拿到省
					province=province.substring(0,province.length()-1);//切掉最后一个字
				String city=area.getCity();//拿到市
					city=city.substring(0,city.length()-1);
				String district=area.getDistrict();//拿到区
					district=district.substring(0,district.length()-1);
				//获得简码
				String[] headers=PinYin4jUtils.getHeadByString(province+city+district);//生成拼音首字母
				StringBuffer buffer=new StringBuffer();
				for (String head : headers) {
					buffer.append(head);
				}
				String shortCode=buffer.toString();//获得简码数据
				area.setShortcode(shortCode);
				
				//获得城市编码
				String cityCode=PinYin4jUtils.hanziToPinyin(city, "");
				area.setCitycode(cityCode);
				
				
				list.add(area);//将遍历封装的数据装入list集合
			}
			//调用service业务层，进行数据的存入
			areaService.addBatch(list);
			
			return "success";
			
		}
		return null;
	}
	
	@Action(value="area_findAll",results={@Result(name="success",type="json")})
	public String areaFindAll(){
		
		Pageable pageable=new PageRequest(page-1, rows);//构造无条件分页查询
		Specification<Area> specification=new Specification<Area>() {//构造带条件的分页查询
			
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list=new ArrayList<Predicate>();//创建保存“条件”的集合
				//添加条件
				if(StringUtils.isNoneBlank(model.getProvince())){
					//根据省份查询：模糊查询
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+model.getProvince()+"%");
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCity())){
					//根据城市查询
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(model.getDistrict())){
					//根据区域查询
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+model.getDistrict()+"%");
					list.add(p3);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<Area> pageData=areaService.findAll(specification,pageable);
		 pushPageDatatoValueStacke(pageData);
		
		return "success";
	}
}
