package controlador;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import modelo.Ikastetxeak;

public class IkastetxeakC {

	public List<Ikastetxeak> ikastetxeakLortuIDz(String idCentro) {
	    Gson gson = new Gson();
	    List<Ikastetxeak> ikastetxeak = null;

	    try (FileReader reader = new FileReader("src/ikastetxeak.json")) {
	        JsonObject rootObject = gson.fromJson(reader, JsonObject.class);
	        
	        JsonArray ikastetxeakArray = rootObject.getAsJsonArray("IKASTETXEAK");
	        
	        ikastetxeak = gson.fromJson(ikastetxeakArray, new TypeToken<List<Ikastetxeak>>() {}.getType());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    if (ikastetxeak != null) {
	        return ikastetxeak.stream()
	                      .filter(centro -> centro.getCCEN().equals(idCentro))
	                      .toList();
	    }
	    return null;
	}
	
	public List<Ikastetxeak> ikastetxeakLortu() {
		Gson gson = new Gson();
		List<Ikastetxeak> ikastetxeak = null;

		try (FileReader reader = new FileReader("src/ikastetxeak.json")) {
			JsonObject rootObject = gson.fromJson(reader, JsonObject.class);

			JsonArray ikastetxeakArray = rootObject.getAsJsonArray("IKASTETXEAK");

			ikastetxeak = gson.fromJson(ikastetxeakArray, new TypeToken<List<Ikastetxeak>>() {
			}.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ikastetxeak;
	}
	
	public static void main(String[] args) {
		IkastetxeakC ikastetxeakC = new IkastetxeakC();
		List<Ikastetxeak> ikastetxeak = ikastetxeakC.ikastetxeakLortu();
		for (Ikastetxeak centro : ikastetxeak) {
            System.out.println(centro.getNOM());
		}
	}

}

