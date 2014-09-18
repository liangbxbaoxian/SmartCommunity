package ${PackageName}.parser;

<#if isList == "true">
import java.util.List;
</#if>

import ${PackageName}.bean.${DataClassName};
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ${ClassName} {
	<#if isList == "false">
	public ${DataClassName} parse(String resultStr) {		
		
		Gson gson = new Gson();
		${DataClassName} data = gson.fromJson(resultStr, new TypeToken<${DataClassName}>(){}.getType());
		
		return data;
	}
	<#else>
	public List<${DataClassName}> parse(String resultStr) {		
		
		Gson gson = new Gson();
		List<${DataClassName}> dataList = gson.fromJson(resultStr, new TypeToken<List<${DataClassName}>>(){}.getType());
		
		return dataList;
	}
	</#if>
}