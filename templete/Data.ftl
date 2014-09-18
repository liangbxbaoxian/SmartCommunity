package ${PackageName}.bean;

<#if import != "null">
import ${import}
</#if>

public class ${ClassName} extends BaseBean{

	<#list Parent? keys as parentKey>
		<#assign parentValues = Parent[parentKey]>		 
		<#if parentKey != "ChildClassList">
    public ${parentValues};
    	<#else>
    		<#list parentValues as childItem>
    		
    			<#list childItem? keys as childKey>
    				<#assign childValues = childItem[childKey]>
    				<#if childKey == "ChildClassName">    			
    public class ${childValues} { 
    				<#else>    					 
    					<#if childKey != "ClassEnd">    			    			   
    	public ${childValues};
    					<#else>
    }
    					</#if>
    				</#if>
    			</#list>    		
    		</#list>
    	</#if>
	</#list>
	
}
