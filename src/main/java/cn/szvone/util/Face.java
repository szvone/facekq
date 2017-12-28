package cn.szvone.util;

import net.sf.json.JSONObject;

public class Face {
	
	public static String detect(String imageUrl){
		String Cookies[] = {""};
		String heard = "";
		HttpRequestUtil h = new HttpRequestUtil();
		String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
		String param = "api_key=4CoCybd7Cn0YqW9XOVSjx_O9ho4dkwpY&api_secret=bDV0cHcGHcj7E1hEGTPbWvbOC0vwGYXc&image_url="+imageUrl;
		String jsonString = h.sendPost(url, param, Cookies, "UTF8", heard, false);
		String faces_token = readJson(jsonString, "0");
		return faces_token;
	}
	
	
	
	public static String compared(String first_token, String imageUrl){
		String Cookies[] = {""};
		String heard = "";
		String url = "https://api-cn.faceplusplus.com/facepp/v3/compare";
		String param = "api_key=4CoCybd7Cn0YqW9XOVSjx_O9ho4dkwpY&api_secret=bDV0cHcGHcj7E1hEGTPbWvbOC0vwGYXc&face_token1="+first_token+"&image_url2="+imageUrl;
		HttpRequestUtil h = new HttpRequestUtil();
		String jsonString = h.sendPost(url, param, Cookies, "UTF8", heard, false);
		String similar = readJson(jsonString, "1");
		return similar;
	}
	
	
	
	public static String readJson(String jsonString, String code){
		String faces = "";
		try{
			JSONObject json = JSONObject.fromObject(jsonString);
			if(code.equals("0")){
				faces  = json.getString("faces");
				faces = (faces.split("\\[")[1]).split("\\]")[0];
				JSONObject json1 = JSONObject.fromObject(faces); 
				faces = json1.getString("face_token");
			}else{
				faces = json.getString("confidence");
			}
		}catch (Exception e) {
			return "";
		}
		return faces;
	}
	
		
	
	
}
