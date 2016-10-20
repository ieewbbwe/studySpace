package cn.android_mobile.core.net.http;


/**
 * 统一服务管理者
 * */
public class ServiceManager {

	/*public static ServiceResponse getServiceResponse(ServiceRequest request,String serviceName)
	{
		try {
			@SuppressWarnings("unchecked")
			Class<Service> serviceClass = (Class<Service>) Class.forName("cn.changesoft.service."+serviceName);
			Service service = serviceClass.newInstance();
			return service.execute(request);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	public static ServiceResponse getServiceResponse(ServiceRequest request,Class<? extends Service> class1)
	{
		try {
			Service _service = class1.newInstance();
			return _service.execute(request);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ServiceResponse getServiceResponse(ServiceRequest request,Service s)
	{
		 return s.execute(request);
	}
}
