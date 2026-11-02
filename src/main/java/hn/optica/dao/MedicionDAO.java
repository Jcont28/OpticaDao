package hn.optica.dao;
import java.util.List; import java.util.Map;
public interface MedicionDAO { List<Map<String,Object>> porCliente(int clienteId) throws Exception; }
