package ${PackageName}.parser;

import java.util.List;

import com.common.net.ResultCode;
import ${PackageName}.bean.${DataClassName};
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ${ClassName} {
	
	public int parse(String json, List<Object> list) {		
		int code = ResultCode.RESULT_SUCCESS;
		
		Gson gson = new Gson();
		${DataClassName} data = gson.fromJson(json, new TypeToken<${DataClassName}>(){}.getType());
		list.add(data);
		
		return code;
	}
}