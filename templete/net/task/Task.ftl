package ${PackageName}.task;

import java.util.Map;

import android.content.Context;

import com.common.config.DebugConfig;
import com.common.net.HttpHelper;
import com.common.net.HttpHelper.ProcesserJson;
import com.common.task.BaseTask;
import com.common.task.TaskDoneListener;
import ${PackageName}.parser.${ParserName};

public class ${ClassName} extends BaseTask {
		
	public ${ClassName}(Context context, String url, TaskDoneListener doneListenter) {
		super(context, url, doneListenter);
	}
	
	@Override
	public int fetchContent(Map<String, String> params) {

		return HttpHelper.${ReqType}(url, params, new ProcesserJson() {
			
			@Override
			public int execute(String json) {
				DebugConfig.ShowLog("json", json);
				${ParserName} parser = new ${ParserName}();
				return parser.parse(json, mList);
			}
		});
	}
}