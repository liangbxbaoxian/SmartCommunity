package ${PackageName};

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.common.config.NetConfig;
import com.common.net.ErrorProcess;
import com.common.net.ResultCode;
import ${PackageName}.bean.${DataName};
import ${PackageName}.task.${TaskName};
import com.common.task.TaskDoneListener;

public class ${ClassName} extends Activity {
	
	private ViewGroup loadLayout;
	private ViewGroup contentLayout;
	
	private ${TaskName} m${TaskName};
	private ${DataName} m${DataName};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.${LayoutName});
		
		getIntentData();
		initView();
		
		execute${TaskName}("请在这里填写请求的方法", get${TaskName}Params());
	}
	
	/**
	 * 获取传入的Intent数据
	 */
	private void getIntentData() {
		
	}
	
	/**
	 * 初始化视图
	 */
	private void initView() {
		loadLayout = (ViewGroup) findViewById(R.id.loadLayout);
		contentLayout = (ViewGroup) findViewById(R.id.contentLayout);
		contentLayout.setVisibility(View.GONE);		
	}
	
	private void execute${TaskName}(String methodUrl, Map<String, String> params) {
		if(m${TaskName} != null) {
			m${TaskName}.cancel(true);
		}
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL + methodUrl;
		m${TaskName} = new ${TaskName}(getApplicationContext(), url, new ${TaskName}Listener());		
		m${TaskName}.execute(params);
	}
	
	private Map<String, String> get${TaskName}Params() {
		Map<String, String> params = new HashMap<String, String>();
		
		//请在这里填写请求参数
		
		return params;
	}
	
	private class ${TaskName}Listener implements TaskDoneListener {
		
		public void taskDone(Integer resultCode, List<Object> list){
			if(resultCode == ResultCode.RESULT_SUCCESS) {
				m${DataName} = (${DataName})list.get(0);
				
				//请在这里处理UI更新
				
			} else {
				ErrorProcess.showError(getApplicationContext(), resultCode);
			}
			
			loadLayout.setVisibility(View.GONE);
			contentLayout.setVisibility(View.VISIBLE);
		}
	}
}
