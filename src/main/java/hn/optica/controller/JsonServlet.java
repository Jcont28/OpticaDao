package hn.optica.controller;
import jakarta.servlet.http.*; 
import jakarta.servlet.*; import java.io.*; 
import com.google.gson.*;
public abstract class JsonServlet extends HttpServlet {
 protected final Gson gson=new Gson();
 protected JsonObject body(HttpServletRequest r)
         throws IOException{return JsonParser.parseReader(r.getReader()).getAsJsonObject();}
 protected void json(HttpServletResponse r,Object o)
         throws IOException{r.setContentType("application/json;charset=UTF-8");r.getWriter().write(gson.toJson(o));}
 protected int num(JsonObject o,String k){
     return o.get(k).getAsInt();}
 protected String str(JsonObject o,String k){
     return o.has(k)&&!o.get(k).isJsonNull()?o.get(k).getAsString():null;}
 protected boolean vendedor(HttpServletRequest r){HttpSession s=r.getSession(false);
 return s!=null&&"VENDEDOR".equals(s.getAttribute("Nombre_Usuario"));}
 protected boolean cliente(HttpServletRequest r){HttpSession s=r.getSession(false);
 return s!=null&&"CLIENTE".equals(s.getAttribute("Nombre_Usuario"));}
}
